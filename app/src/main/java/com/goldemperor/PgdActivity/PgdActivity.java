package com.goldemperor.PgdActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.goldemperor.GxReport.GxReportScan;
import com.goldemperor.MainActivity.ListViewDecoration;
import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.Utils;
import com.goldemperor.MainActivity.define;


import com.goldemperor.R;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
import com.goldemperor.Widget.SublimePickerFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by Nova on 2017/7/25.
 */

public class PgdActivity extends AppCompatActivity implements ScrollListenerHorizontalScrollView.OnScrollListener{


    private Context mContext;

    private SharedPreferences dataPref;
    private SharedPreferences.Editor dataEditor;
    private TwinklingRefreshLayout refreshLayout;
    private SwipeMenuRecyclerView mMenuRecyclerView;
    private PgdAdapter mMenuAdapter;
    private List<WorkCardPlan> pgdWorkCardPlan;
    private List<WorkCardPlan> showWorkCardPlan;
    private TextView tv_showDate;
    private TextView tv_tip;
    private FancyButton btn_today;
    private FancyButton btn_yestoday;
    private FancyButton btn_week;
    private FancyButton btn_twoDay;
    private FancyButton btn_calendar;
    private String StartTime;
    private String EndTime;

    private int loadmoreLimit = 40;
    private int currentPosition = 0;

    public ScrollListenerHorizontalScrollView ScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态啦
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pgd);
        //隐藏标题栏
        getSupportActionBar().hide();
        mContext = this;
        dataPref = this.getSharedPreferences(define.SharedName, 0);
        dataEditor = dataPref.edit();

        ScrollView= (ScrollListenerHorizontalScrollView)findViewById(R.id.ScrollView);
        ScrollView.setOnScrollListener(this);

        StartTime = Utils.getCurrentTime();
        EndTime = Utils.getCurrentTime();

        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_showDate = (TextView) findViewById(R.id.tv_showDate);

        //初始化下拉刷新
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout);

        SinaRefreshView headerView = new SinaRefreshView(this);
        headerView.setArrowResource(R.drawable.arrow);
        headerView.setTextColor(0xff745D5C);
//        TextHeaderView headerView = (TextHeaderView) View.inflate(this,R.layout.header_tv,null);
        refreshLayout.setHeaderView(headerView);

        LoadingView loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);

        refreshLayout.setOverScrollRefreshShow(true);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                //获取数据
                getData(StartTime, EndTime);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                LoadMore();
            }
        });
        pgdWorkCardPlan = new ArrayList<WorkCardPlan>();
        showWorkCardPlan = new ArrayList<WorkCardPlan>();

        getData(StartTime, EndTime);

        mMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mMenuRecyclerView.addItemDecoration(new ListViewDecoration(this));// 添加分割线。


        mMenuAdapter = new PgdAdapter(showWorkCardPlan,this);

        mMenuAdapter.setOnItemClickListener(onItemClickListener);
        mMenuRecyclerView.setAdapter(mMenuAdapter);

        btn_today = (FancyButton) findViewById(R.id.btn_today);
        btn_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_tip.setVisibility(View.VISIBLE);
                StartTime = Utils.getCurrentTime();
                EndTime = Utils.getCurrentTime();
                getData(StartTime, EndTime);

            }
        });

        btn_yestoday = (FancyButton) findViewById(R.id.btn_yestoday);
        btn_yestoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_tip.setVisibility(View.VISIBLE);
                StartTime = Utils.getDateStr(Utils.getCurrentTime(), 1, false);
                EndTime = Utils.getDateStr(Utils.getCurrentTime(), 1, false);
                getData(StartTime, EndTime);

            }
        });

        btn_week = (FancyButton) findViewById(R.id.btn_week);
        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_tip.setVisibility(View.VISIBLE);
                StartTime = Utils.getBeginDayOfWeek().toString();
                EndTime = Utils.getCurrentTime();
                getData(StartTime, EndTime);

            }
        });

        btn_twoDay = (FancyButton) findViewById(R.id.btn_twoDay);
        btn_twoDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_tip.setVisibility(View.VISIBLE);
                StartTime = Utils.getDateStr(Utils.getCurrentTime(), 1, false);
                EndTime = Utils.getCurrentTime();
                getData(StartTime, EndTime);

            }
        });

        //日历回调函数
        final SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
            @Override
            public void onCancelled() {

            }

            @Override
            public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                                int hourOfDay, int minute,
                                                SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                                String recurrenceRule) {

            }
        };

        btn_calendar = (FancyButton) findViewById(R.id.btn_calendar);
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);
                Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                if (!optionsPair.first) { // If options are not valid
                    Toast.makeText(mContext, "No pickers activated",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Valid options
                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getFragmentManager(), "SUBLIME_PICKER");
            }
        });

        RequestParams params = new RequestParams(define.GetPlanEntry);
        params.setReadTimeout(60000);
        params.addQueryStringParameter("FInterID", "14174");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                Log.e("jindi", "result:" + result);
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("jindi", ex.toString());

            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });

    }

    //日历设置方法
    // Validates & returns SublimePicker options
    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;


        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;


        options.setDisplayOptions(displayOptions);

        // Enable/disable the date range selection feature
        options.setCanPickDateRange(true);

        // Example for setting date range:
        // Note that you can pass a date range as the initial date params
        // even if you have date-range selection disabled. In this case,
        // the user WILL be able to change date-range using the header
        // TextViews, but not using long-press.

        /*Calendar startCal = Calendar.getInstance();
        startCal.set(2016, 2, 4);
        Calendar endCal = Calendar.getInstance();
        endCal.set(2016, 2, 17);

        options.setDateParams(startCal, endCal);*/

        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    public void getData(final String StartTime, final String EndTime) {
        tv_tip.setText("数据载入中...");
        tv_showDate.setText("显示日期:" + StartTime + "到" + EndTime);
        RequestParams params = new RequestParams(define.GetPlanbyTime);
        params.setReadTimeout(60000);
        params.addQueryStringParameter("FStartTime", StartTime);
        params.addQueryStringParameter("EndTime", EndTime);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                Log.e("jindi", "size:" + result);
                pgdWorkCardPlan.clear();
                showWorkCardPlan.clear();
                currentPosition = 0;
                refreshLayout.setEnableLoadmore(true);
                List<String> filter = new ArrayList<String>();
                Gson g = new Gson();
                PgdResult pgds = g.fromJson(result, PgdResult.class);
                if (pgds.getData() != null) {
                    for (int i = 0; i < pgds.getData().size(); i++) {
                        if (!filter.contains(pgds.getData().get(i).getOrderbill()) && pgds.getData().get(i).getOrderbill().indexOf("J") != 0) {
                            filter.add(pgds.getData().get(i).getOrderbill());
                            //设置尺码
                            for (int j = 0; j < pgds.getData().size(); j++) {
                                if(pgds.getData().get(j).getOrderbill().equals(pgds.getData().get(i).getOrderbill())){
                                    if(pgds.getData().get(j).getFsize().equals("35")){
                                        pgds.getData().get(i).setFsize35(String.valueOf(pgds.getData().get(j).getDispatchingnumber().intValue()));
                                    }
                                    else if(pgds.getData().get(j).getFsize().equals("36")){
                                        pgds.getData().get(i).setFsize36(String.valueOf(pgds.getData().get(j).getDispatchingnumber().intValue()));
                                    }
                                    else if(pgds.getData().get(j).getFsize().equals("37")){
                                        pgds.getData().get(i).setFsize37(String.valueOf(pgds.getData().get(j).getDispatchingnumber().intValue()));
                                    }
                                    else if(pgds.getData().get(j).getFsize().equals("38")){
                                        pgds.getData().get(i).setFsize38(String.valueOf(pgds.getData().get(j).getDispatchingnumber().intValue()));
                                    }
                                    else if(pgds.getData().get(j).getFsize().equals("39")){
                                        pgds.getData().get(i).setFsize39(String.valueOf(pgds.getData().get(j).getDispatchingnumber().intValue()));
                                    }
                                    else if(pgds.getData().get(j).getFsize().equals("40")){
                                        pgds.getData().get(i).setFsize40(String.valueOf(pgds.getData().get(j).getDispatchingnumber().intValue()));
                                    }
                                }
                            }
                            pgdWorkCardPlan.add(pgds.getData().get(i));

                        }
                    }
                    Log.e("jindi", "size:" + pgds.getData().size());
                    Log.e("jindi", "data:" + g.toJson(pgds.getData().get(0)));
                    tv_tip.setVisibility(View.GONE);
                } else {
                    tv_tip.setText("暂无数据");
                }
                LoadMore();
                mMenuAdapter.notifyDataSetChanged();
                refreshLayout.finishRefreshing();
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("jindi", ex.toString());
                tv_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("数据载入失败:" + ex.toString());
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });

    }

    private void LoadMore() {
        int currentPositionTemp = currentPosition;
        for (; currentPosition < pgdWorkCardPlan.size() && currentPosition < currentPositionTemp + loadmoreLimit; currentPosition++) {
            showWorkCardPlan.add(pgdWorkCardPlan.get(currentPosition));
            Log.e("huayifu", "current:" + currentPositionTemp);
        }
        if (currentPosition >= pgdWorkCardPlan.size()) {
            refreshLayout.setEnableLoadmore(false);
        }
        refreshLayout.finishLoadmore();
    }

    /**
     * Item点击监听。
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Toast.makeText(mContext, "position"+position,
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onScroll(int scrollX) {
        for(int i=0;i<mMenuAdapter.ScrollViewList.size();i++){
            mMenuAdapter.ScrollViewList.get(i).scrollTo(scrollX,ScrollView.getScrollY());
        }
    }
}
