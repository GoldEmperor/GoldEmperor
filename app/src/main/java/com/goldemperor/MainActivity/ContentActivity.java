package com.goldemperor.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.flyco.banner.anim.select.ZoomInEnter;
import com.goldemperor.Banner.DataProvider;
import com.goldemperor.Banner.SimpleImageBanner;
import com.goldemperor.Banner.SimpleTextBanner;
import com.goldemperor.Banner.ViewFindUtils;
import com.goldemperor.CxStockIn.android.NetworkHelper;
import com.goldemperor.GxReport.GxReport;
import com.goldemperor.LoginActivity.LoginActivity;
import com.goldemperor.PgdActivity.PgdActivity;
import com.goldemperor.Public.SystemUtil;
import com.goldemperor.SetActivity.SetActivity;
import com.goldemperor.R;
import com.goldemperor.StockCheck.StockCheckActivity;
import com.goldemperor.Update.CheckVersionTask;
import com.tapadoo.alerter.Alerter;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.goldemperor.CxStockIn.CxStockInActivity.IsNeedCheckVersion;


/**
 * Created by Nova on 2017/7/25.
 */

public class ContentActivity extends AppCompatActivity {

    private FancyButton chenckBtn;

    private FancyButton orderBtn;
    private FancyButton processBtn;
    private FancyButton produceBtn;


    private FancyButton btn_cxstockin;
    private FancyButton btn_pgstockin;
    private FancyButton btn_cgstockin;

    private FancyButton btn_process_sc;

    private FancyButton btn_cxscanbarcode;

    private FancyButton setBtn;

    private Context mContext;
    private Activity act;
    private SharedPreferences dataPref;
    private SharedPreferences.Editor dataEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态啦
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_content);
        //隐藏标题栏
        getSupportActionBar().hide();
        mContext = this;
        act = this;
        dataPref = this.getSharedPreferences(define.SharedName, 0);
        dataEditor = dataPref.edit();

        SimpleImageBanner sib = ViewFindUtils.find(getWindow().getDecorView(), R.id.sib);
        sib
                .setSelectAnimClass(ZoomInEnter.class)
                .setSource(DataProvider.getList())
                .startScroll();
        sib.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(mContext, "positon:" + position, Toast.LENGTH_LONG).show();
            }
        });

        SimpleTextBanner stb = ViewFindUtils.find(getWindow().getDecorView(), R.id.stb);

        ArrayList<String> titles = new ArrayList<>();
        for (String title : DataProvider.text) {
            titles.add(title);
        }
        stb
                .setSource(titles)
                .startScroll();

        stb.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(mContext, "positon:" + position, Toast.LENGTH_LONG).show();
            }
        });

        chenckBtn = (FancyButton) findViewById(R.id.btn_check);
        chenckBtn.setIconResource(R.drawable.btn_material);
        chenckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, StockCheckActivity.class);
                mContext.startActivity(i);
            }
        });

        processBtn = (FancyButton) findViewById(R.id.btn_process);
        processBtn.setIconResource(R.drawable.btn_process);

        produceBtn = (FancyButton) findViewById(R.id.btn_produce);
        produceBtn.setIconResource(R.drawable.btn_produce);

        orderBtn = (FancyButton) findViewById(R.id.btn_order);
        orderBtn.setIconResource(R.drawable.btn_order);

        btn_process_sc = (FancyButton) findViewById(R.id.btn_process_sc);
        btn_process_sc.setIconResource(R.drawable.btn_query);

        btn_cxstockin = (FancyButton) findViewById(R.id.btn_cxstockin);
        btn_cxstockin.setIconResource(R.drawable.btn_saoyisao);

        btn_pgstockin = (FancyButton) findViewById(R.id.btn_pgstockin);
        btn_pgstockin.setIconResource(R.drawable.btn_set);

        btn_cgstockin = (FancyButton) findViewById(R.id.btn_cgstockin);
        btn_cgstockin.setIconResource(R.drawable.btn_set);

        btn_cgstockin = (FancyButton) findViewById(R.id.btn_cxscanbarcode);
        btn_cgstockin.setIconResource(R.drawable.btn_set);

        setBtn = (FancyButton) findViewById(R.id.btn_set);

        setBtn.setIconResource(R.drawable.btn_set);
        processBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataPref.getString(define.SharedPassword, define.NONE).equals(define.NONE)) {
                    Intent i = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(i);
                } else {
                    Intent i = new Intent(mContext, GxReport.class);
                    mContext.startActivity(i);
                }
            }
        });


        btn_process_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SystemModel = SystemUtil.getSystemModel();
                Log.e("jindi", "手机型号：" + SystemModel);
                if (SystemModel.equals("MT65")) {
                    Intent i = new Intent(mContext, com.goldemperor.ScReport.ScReportActivity.class);
                    mContext.startActivity(i);
                } else {
                    getControl("1050501");
                }
            }
        });


        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataPref.getString(define.SharedPassword, define.NONE).equals(define.NONE)) {
                    Intent i = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(i);
                } else {
                    Intent i = new Intent(mContext, PgdActivity.class);
                    mContext.startActivity(i);
                }
            }
        });

        btn_cxstockin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SystemModel = SystemUtil.getSystemModel();
                Log.e("jindi", "手机型号：" + SystemModel);
                if (SystemModel.equals("MT65")) {
                    Intent i = new Intent(mContext, com.goldemperor.CxStockIn.CxStockInActivity.class);
                    mContext.startActivity(i);
                } else {
                    getControl("1050101");
                }
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataPref.getString(define.SharedPassword, define.NONE).equals(define.NONE)) {
                    Intent i = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(i);
                } else {
                    Intent i = new Intent(mContext, SetActivity.class);
                    mContext.startActivity(i);
                }

            }
        });
        //如果有网络的情况下，apk更新
        if (IsNeedCheckVersion && NetworkHelper.isNetworkAvailable(this)) {

            new Thread() {
                @Override
                public void run() {
                    define.isWaiNet = !Utils.ping("192.168.99.79");
                    Log.e("jindi", "isWaiNet:" + define.isWaiNet);
                    CheckVersionTask myTask = new CheckVersionTask(act);
                    myTask.run();
                }
            }.start();
        }
    }

    private void getControl(final String controlID) {
        RequestParams params = new RequestParams(define.IsHaveControl);
        params.addQueryStringParameter("OrganizeID", "1");
        params.addQueryStringParameter("empID", dataPref.getString(define.SharedEmpId, define.NONE));
        params.addQueryStringParameter("controlID", controlID);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                if (result.contains("false")) {
                    Alerter.create(act)
                            .setTitle("提示")
                            .setText("你没有权限,请联系管理员开通权限")
                            .setBackgroundColorRes(R.color.colorAlert)
                            .show();
                } else if (result.contains("true")) {
                    if (controlID.equals("1050101")) {
                        Intent i = new Intent(mContext, com.goldemperor.CxStockIn.CxStockInActivity.class);
                        mContext.startActivity(i);
                    }else if(controlID.equals("1050501")){
                        Intent i = new Intent(mContext, com.goldemperor.ScReport.ScReportActivity.class);
                        mContext.startActivity(i);
                    }
                } else {
                    Alerter.create(act)
                            .setTitle("提示")
                            .setText("服务器返回失败")
                            .setBackgroundColorRes(R.color.colorAlert)
                            .show();

                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("jindi", ex.toString());
                Alerter.create(act)
                        .setTitle("提示")
                        .setText("网络错误")
                        .setBackgroundColorRes(R.color.colorAlert)
                        .show();

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


