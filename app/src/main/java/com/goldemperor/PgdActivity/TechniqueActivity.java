package com.goldemperor.PgdActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;


import com.goldemperor.MainActivity.ListViewDecoration;
import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.define;
import com.goldemperor.R;
import com.google.gson.Gson;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.goldemperor.PgdActivity.PgdActivity.selectWorkCardPlan;


/**
 * Created by Nova on 2017/7/25.
 */

public class TechniqueActivity extends AppCompatActivity {


    private Context mContext;
    private Activity act;
    private SharedPreferences dataPref;
    private SharedPreferences.Editor dataEditor;
    private SwipeMenuRecyclerView mMenuRecyclerView;
    private TechniqueAdapter mMenuAdapter;
    private int finterid;
    List<String[]> processList=new ArrayList<String[]>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态啦
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gxpg_technique);
        //隐藏标题栏
        getSupportActionBar().hide();

        mContext = this;
        act=this;
        dataPref = this.getSharedPreferences(define.SharedName, 0);
        dataEditor = dataPref.edit();


        mMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mMenuRecyclerView.setLayoutManager(new GridLayoutManager(this,3));// 布局管理器。
        mMenuRecyclerView.addItemDecoration(new ListViewDecoration(this));// 添加分割线。


        mMenuAdapter = new TechniqueAdapter(processList);

        mMenuAdapter.setOnItemClickListener(onItemClickListener);
        mMenuRecyclerView.setAdapter(mMenuAdapter);

        Intent intent = getIntent();
        finterid = intent.getIntExtra("finterid", 0);
        getData(finterid);
    }
    public void getData(int finterid) {

        RequestParams params = new RequestParams(define.GetRouteEntryBody);
        params.setReadTimeout(60000);
        params.addQueryStringParameter("FInterID", String.valueOf(finterid));
        Log.e("jindi",params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                Gson g = new Gson();
                TechniqueResult techniqueResult = g.fromJson(result, TechniqueResult.class);
                if (techniqueResult.getData() != null) {
                    for(int i=0;i<techniqueResult.getData().size();i++) {
                        String[] process = {techniqueResult.getData().get(i).getFprocessnumber(), techniqueResult.getData().get(i).getFprocessname()};
                        processList.add(process);
                    }
                }
                mMenuAdapter.notifyDataSetChanged();
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


    /**
     * Item点击监听。
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {

        }
    };

}
