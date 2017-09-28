package com.goldemperor.PgdActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.WindowManager;

import com.goldemperor.MainActivity.ListViewDecoration;
import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.define;
import com.goldemperor.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.goldemperor.PgdActivity.PgdActivity.selectWorkCardPlan;


/**
 * Created by Nova on 2017/7/25.
 */

public class AssignActivity extends AppCompatActivity {


    private Context mContext;
    private Activity act;
    private SharedPreferences dataPref;
    private SharedPreferences.Editor dataEditor;
    private SwipeMenuRecyclerView mMenuRecyclerView;
    private AssignAdapter mMenuAdapter;

    private DbManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态啦
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gxpg_assign);
        //隐藏标题栏
        getSupportActionBar().hide();

        mContext = this;
        act=this;
        dataPref = this.getSharedPreferences(define.SharedName, 0);
        dataEditor = dataPref.edit();
        dbManager = initDb();

        mMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mMenuRecyclerView.addItemDecoration(new ListViewDecoration(this));// 添加分割线。

        try {
            Intent intent = getIntent();
            String planBody = intent.getStringExtra("planBody");
            List<GxpgPlan> gxpgPlanList = dbManager.selector(GxpgPlan.class).where("style", " = ", planBody).findAll();
            //Log.e("jindi","size:"+gxpgPlanList.size());
            mMenuAdapter = new AssignAdapter(gxpgPlanList);
            mMenuAdapter.setOnItemClickListener(onItemClickListener);
            mMenuRecyclerView.setAdapter(mMenuAdapter);
        } catch (DbException e) {
            e.printStackTrace();
        }


    }
    public DbManager initDb() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                // 数据库的名字
                .setDbName("jindi")
                // 保存到指定路径
                // .setDbDir(new
                // File(Environment.getExternalStorageDirectory().getAbsolutePath()))
                // 数据库的版本号
                .setDbVersion(1)
                //设置数据库打开的监听
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        //开启数据库支持多线程操作，提升性能，对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                //设置表创建的监听
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        Log.i("jindi", "onTableCreated：" + table.getName());
                    }
                })
                // 数据库版本更新监听
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager arg0, int arg1, int arg2) {
                        LogUtil.e("数据库版本更新了！");
                    }
                });
        return x.getDb(daoConfig);
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
