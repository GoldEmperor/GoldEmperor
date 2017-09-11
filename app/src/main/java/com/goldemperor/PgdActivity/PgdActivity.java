package com.goldemperor.PgdActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.goldemperor.MainActivity.ListViewDecoration;
import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.Utils;
import com.goldemperor.MainActivity.define;


import com.goldemperor.R;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
import com.goldemperor.Widget.SublimePickerFragment;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;

import com.tapadoo.alerter.Alerter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by Nova on 2017/7/25.
 */

public class PgdActivity extends AppCompatActivity implements ScrollListenerHorizontalScrollView.OnScrollListener {


    private Context mContext;
    private Activity act;
    private SharedPreferences dataPref;
    private SharedPreferences.Editor dataEditor;
    private TwinklingRefreshLayout refreshLayout;
    private SwipeMenuRecyclerView mMenuRecyclerView;
    private PgdAdapter mMenuAdapter;
    private List<WorkCardPlan> pgdWorkCardPlan;
    public static List<WorkCardPlan> showWorkCardPlan;
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

    public static NameListResult nameListResult;
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
        act = this;
        dataPref = this.getSharedPreferences(define.SharedName, 0);
        dataEditor = dataPref.edit();

        ScrollView = (ScrollListenerHorizontalScrollView) findViewById(R.id.ScrollView);
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


        mMenuAdapter = new PgdAdapter(showWorkCardPlan, this);

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

                if (selectedDate != null) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    if (selectedDate.getType() == SelectedDate.Type.SINGLE) {
                        StartTime=String.valueOf(format.format(selectedDate.getStartDate().getTime()));
                        EndTime=String.valueOf(format.format(selectedDate.getStartDate().getTime()));
                    } else if (selectedDate.getType() == SelectedDate.Type.RANGE) {
                        StartTime=String.valueOf(format.format(selectedDate.getStartDate().getTime()));
                        EndTime=String.valueOf(format.format(selectedDate.getEndDate().getTime()));
                    }
                    tv_tip.setVisibility(View.VISIBLE);
                    getData(StartTime, EndTime);
                }

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

        getFDeptmentData();

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
                pgdWorkCardPlan.clear();
                showWorkCardPlan.clear();
                currentPosition = 0;
                refreshLayout.setEnableLoadmore(true);
                List<String> filter = new ArrayList<String>();
                Gson g = new Gson();
                PgdResult pgds = g.fromJson(result, PgdResult.class);
                if (pgds.getData() != null) {
                    for (int i = 0; i < pgds.getData().size(); i++) {
                        if (!filter.contains(pgds.getData().get(i).getPlanbill()) && pgds.getData().get(i).getOrderbill().indexOf("J") != 0) {
                            filter.add(pgds.getData().get(i).getPlanbill());
                            //重新遍历,设置尺码
                            for (int j = 0; j < pgds.getData().size(); j++) {
                                if (pgds.getData().get(j).getPlanbill().equals(pgds.getData().get(i).getPlanbill())) {
                                    String[][] s=new String[1][2];
                                    s[0][0]=pgds.getData().get(j).getFsize();
                                    s[0][1]=String.valueOf(pgds.getData().get(j).getDispatchingnumber().intValue());
                                    pgds.getData().get(i).addSize(s);
                                }
                            }
                            pgdWorkCardPlan.add(pgds.getData().get(i));

                        }
                    }
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
                tv_tip.setText("数据载入失败,请检查网络:" + ex.toString());
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
            SCWorkCard2SCProcessWorkCard(position);
        }
    };

    @Override
    public void onScroll(int scrollX) {
        for (int i = 0; i < mMenuAdapter.ScrollViewList.size(); i++) {
            mMenuAdapter.ScrollViewList.get(i).scrollTo(scrollX, ScrollView.getScrollY());
        }
    }

    public void getFDeptmentData() {
        RequestParams params = new RequestParams(define.GetEmpByDeptID);
        params.addQueryStringParameter("FDeptmentID", dataPref.getString(define.SharedFDeptmentid,""));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                Gson g = new Gson();
                nameListResult = g.fromJson(result, NameListResult.class);
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("jindi", ex.toString());
                tv_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("数据载入失败,请检查网络:" + ex.toString());
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
    public void SCWorkCard2SCProcessWorkCard(int position){
        Gson g=new Gson();
        List<PushJsonCondition> pushJsonConditionList=new ArrayList<PushJsonCondition>();
        for (int i=0;i<showWorkCardPlan.get(position).getSizeList().size();i++) {
            PushJsonCondition pushJsonCondition = new PushJsonCondition();
            pushJsonCondition.setFEntryID(String.valueOf(showWorkCardPlan.get(position).getFentryid()));
            pushJsonCondition.setFInterID(String.valueOf(showWorkCardPlan.get(position).getFinterid()));
            pushJsonCondition.setFSize(String.valueOf(showWorkCardPlan.get(position).getSizeList().get(i)[0][0]));
            pushJsonConditionList.add(pushJsonCondition);
        }
        RequestParams params = new RequestParams(define.SCWorkCard2SCProcessWorkCard);
        params.addQueryStringParameter("PushJsonCondition", g.toJson(pushJsonConditionList));
        params.addQueryStringParameter("OrganizeID", dataPref.getString(define.SharedFOrganizeid,"1"));
        params.addQueryStringParameter("BillTypeID", "3");
        params.addQueryStringParameter("UserID", dataPref.getString(define.SharedJobNumber,"0"));
        params.addQueryStringParameter("DeptmentID", dataPref.getString(define.SharedFDeptmentid,"0"));

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result= URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(result.contains("success")) {

                    int finterid=Integer.valueOf(result.substring(result.indexOf("ReturnMsg"),result.indexOf(",")).replace("ReturnMsg\":",""));

                    Bundle bundle = new Bundle();
                    bundle.putInt("finterid", finterid);
                    Intent intent = new Intent(act,
                            GxpgActivity.class);
                    intent.putExtras(bundle);
                    // 启动另一个Activity。
                    startActivity(intent);

                }else{
                    Alerter.create(act)
                            .setTitle("提示")
                            .setText("派工单下推失败")
                            .setBackgroundColorRes(R.color.colorAlert)
                            .show();
                }
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

}
