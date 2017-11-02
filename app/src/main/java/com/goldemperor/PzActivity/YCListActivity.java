package com.goldemperor.PzActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.goldemperor.GxReport.GxReport;
import com.goldemperor.LoginActivity.LoginActivity;
import com.goldemperor.MainActivity.GsonFactory;
import com.goldemperor.MainActivity.Utils;
import com.goldemperor.MainActivity.define;
import com.goldemperor.PgdActivity.*;
import com.goldemperor.PgdActivity.PgdAdapter;
import com.goldemperor.R;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.goldemperor.PzActivity.PgdActivity.selectWorkCardPlan;
import static com.goldemperor.PzActivity.PgdActivity.showWorkCardPlan;

/**
 * Created by Nova on 2017/10/28.
 */

public class YCListActivity extends AppCompatActivity implements ScrollListenerHorizontalScrollView.OnScrollListener {

    List<Sc_WorkCardAbnormity> Sc_WorkCardAbnormityList = new ArrayList<Sc_WorkCardAbnormity>();
    private TwinklingRefreshLayout refreshLayout;
    private SwipeMenuRecyclerView mMenuRecyclerView;
    private FancyButton btn_zj;
    private Context mContext;
    private Activity act;
    private TextView tv_tip;
    public ScrollListenerHorizontalScrollView ScrollView;
    public static YCListAdapter mMenuAdapter;
    private SharedPreferences dataPref;
    private SharedPreferences.Editor dataEditor;

    private List<String[]> nameList = new ArrayList<String[]>();
    public static int YcCount=0;

    public static List<AbnormityModel> abnormityModel = new ArrayList<AbnormityModel>();
    public static List<String[]> selectAbnormity = new ArrayList<String[]>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态啦
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_yclist);
        //隐藏标题栏
        getSupportActionBar().hide();
        dataPref = this.getSharedPreferences(define.SharedName, 0);
        dataEditor = dataPref.edit();
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        ScrollView = (ScrollListenerHorizontalScrollView) findViewById(R.id.ScrollView);
        ScrollView.setOnScrollListener(this);
        act = this;
        mContext = this;
        btn_zj = (FancyButton) findViewById(R.id.btn_zj);
        btn_zj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, com.goldemperor.PzActivity.ZjActivity.class);
                startActivityForResult(i, 0);
            }
        });

        //初始化下拉刷新
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout);

        SinaRefreshView headerView = new SinaRefreshView(this);
        headerView.setArrowResource(R.drawable.arrow);
        headerView.setTextColor(0xff745D5C);
//        TextHeaderView headerView = (TextHeaderView) View.inflate(this,R.layout.header_tv,null);
        refreshLayout.setHeaderView(headerView);

        refreshLayout.setEnableLoadmore(false);

        refreshLayout.setOverScrollRefreshShow(true);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                //获取数据
                tv_tip.setVisibility(View.GONE);
                getData();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {

            }
        });

        getData();
        getAbnormityData();
        mMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。

        mMenuAdapter = new YCListAdapter(Sc_WorkCardAbnormityList, this);
        mMenuRecyclerView.setAdapter(mMenuAdapter);
    }

    @Override
    public void onScroll(int scrollX) {
        for (int i = 0; i < mMenuAdapter.ScrollViewList.size(); i++) {
            mMenuAdapter.ScrollViewList.get(i).scrollTo(scrollX, ScrollView.getScrollY());
        }
    }

    private void getData() {
        tv_tip.setText("数据载入中...");
        tv_tip.setVisibility(View.VISIBLE);
        Sc_WorkCardAbnormityList.clear();
        nameList.clear();
        RequestParams params = new RequestParams(define.IP8341 + define.GetWorkCardAbnormity);
        params.addQueryStringParameter("FInterID", String.valueOf(selectWorkCardPlan.getFinterid()));
        Log.e("jindi", params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("jindi", result);

                Gson g = new Gson();
                YcResult ycResult = g.fromJson(result, YcResult.class);
                if (ycResult.getData() != null) {
                    YcCount=Integer.valueOf(ycResult.getCount());
                    for (int i = 0; i < ycResult.getData().size(); i++) {
                        Sc_WorkCardAbnormityList.add(ycResult.getData().get(i));
                        boolean hasSame = false;


                        if (dataPref.getString(define.SharedEmpId, "").equals(String.valueOf(Sc_WorkCardAbnormityList.get(i).getFempID()))) {
                            Sc_WorkCardAbnormityList.get(i).setFname(dataPref.getString(define.SharedUser, ""));
                            Sc_WorkCardAbnormityList.get(i).setFdeptmentName(dataPref.getString(define.SharedDeptmentName, ""));
                            hasSame = true;
                            if (i == ycResult.getData().size() - 1) {
                                mMenuAdapter.notifyDataSetChanged();
                            }
                        } else {
                            for (int j = 0; j < nameList.size(); j++) {
                                if (nameList.get(j)[0].equals(String.valueOf(Sc_WorkCardAbnormityList.get(i).getFempID()))) {
                                    Sc_WorkCardAbnormityList.get(i).setFname(nameList.get(j)[1]);
                                    Sc_WorkCardAbnormityList.get(i).setFdeptmentName(nameList.get(j)[2]);
                                    hasSame = true;
                                    if (i == ycResult.getData().size() - 1) {
                                        mMenuAdapter.notifyDataSetChanged();
                                    }
                                }

                            }
                        }
                        if (!hasSame) {
                            GetUserID(i);
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    tv_tip.setVisibility(View.GONE);
                } else {
                    tv_tip.setText("暂无数据");
                }

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

    public void GetUserID(final int position) {
        RequestParams params = new RequestParams(define.IP8012 + define.GetUserID);
        params.addQueryStringParameter("FEmpID", String.valueOf(Sc_WorkCardAbnormityList.get(position).getFempID()));
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("jindi", result);
                if (result.contains("FUserID")) {
                    String Fname = result.substring(result.indexOf("<Fname>"), result.indexOf("</Fname>"));
                    Fname = Fname.replaceAll("<Fname>", "").replaceAll("</Fname>", "");
                    Sc_WorkCardAbnormityList.get(position).setFname(Fname);

                    String FDeptmentName = result.substring(result.indexOf("<FDeptmentName>"), result.indexOf("</FDeptmentName>"));
                    FDeptmentName = FDeptmentName.replaceAll("<FDeptmentName>", "").replaceAll("</FDeptmentName>", "");
                    Sc_WorkCardAbnormityList.get(position).setFdeptmentName(FDeptmentName);
                    String[] name = new String[3];
                    name[0] = String.valueOf(Sc_WorkCardAbnormityList.get(position).getFempID());
                    name[1] = Fname;
                    name[2] = FDeptmentName;
                    nameList.add(name);
                    mMenuAdapter.notifyDataSetChanged();
                }

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
    private void getAbnormityData() {
        abnormityModel.clear();
        String Fname="";
        if(selectWorkCardPlan.getFgroup().contains("针车")){
            Fname="针车";
        }else{
            Fname="成型";
        }
        RequestParams params = new RequestParams(define.IP8341 + define.GetAbnormityByName);
        params.addQueryStringParameter("FName", Fname);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson g = new Gson();
                ReasonResult reasonResult = g.fromJson(result, ReasonResult.class);
                if (reasonResult.getData() != null) {
                    for (int i = 0; i < reasonResult.getData().size(); i++) {
                        abnormityModel.add(reasonResult.getData().get(i));
                        String[] temp = new String[4];
                        temp[0] = reasonResult.getData().get(i).getFname();
                        temp[1] = String.valueOf(reasonResult.getData().get(i).getFitemID());
                        temp[2] = "无";
                        temp[3] = "未选";
                        selectAbnormity.add(temp);
                    }
                }
            }


            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mMenuAdapter != null) {
            getData();
        }

    }
}
