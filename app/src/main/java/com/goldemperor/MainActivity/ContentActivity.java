package com.goldemperor.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.flyco.banner.anim.select.ZoomInEnter;
import com.goldemperor.Banner.DataProvider;
import com.goldemperor.Banner.SimpleImageBanner;
import com.goldemperor.Banner.SimpleTextBanner;
import com.goldemperor.Banner.ViewFindUtils;
import com.goldemperor.CxStockIn.CxStockInActivity;
import com.goldemperor.GxReport.GxReport;
import com.goldemperor.LoginActivity.LoginActivity;
import com.goldemperor.SetActivity.SetActivity;
import com.goldemperor.R;
import com.goldemperor.StockCheck.StockCheckActivity;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;


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

    private FancyButton btn_cxscanbarcode;

    private FancyButton setBtn;

    private Context mContext;

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
        chenckBtn.setIconResource(R.drawable.material);
        chenckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, StockCheckActivity.class);
                mContext.startActivity(i);
            }
        });

        processBtn = (FancyButton) findViewById(R.id.btn_process);
        processBtn.setIconResource(R.drawable.process);

        produceBtn = (FancyButton) findViewById(R.id.btn_produce);
        produceBtn.setIconResource(R.drawable.produce);

        orderBtn = (FancyButton) findViewById(R.id.btn_order);
        orderBtn.setIconResource(R.drawable.order);

        btn_cxstockin = (FancyButton) findViewById(R.id.btn_cxstockin);
        btn_cxstockin.setIconResource(R.drawable.query);

        btn_pgstockin = (FancyButton) findViewById(R.id.btn_pgstockin);
        btn_pgstockin.setIconResource(R.drawable.set);

        btn_cgstockin = (FancyButton) findViewById(R.id.btn_cgstockin);
        btn_cgstockin.setIconResource(R.drawable.set);

        btn_cgstockin =(FancyButton) findViewById(R.id.btn_cxscanbarcode);
        btn_cgstockin.setIconResource(R.drawable.set);

        setBtn = (FancyButton) findViewById(R.id.btn_set);

        setBtn.setIconResource(R.drawable.set);
        processBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, GxReport.class);
                mContext.startActivity(i);

            }
        });

        btn_cxstockin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(mContext, CxStockInActivity.class);
                    mContext.startActivity(i);

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

    }

}
