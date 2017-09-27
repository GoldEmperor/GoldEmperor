package com.goldemperor.PgdActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.goldemperor.MainActivity.ListViewDecoration;
import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.Utils;
import com.goldemperor.MainActivity.define;

import com.goldemperor.R;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
import com.google.gson.Gson;
import com.tapadoo.alerter.Alerter;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by Nova on 2017/7/25.
 */

public class GxpgActivity extends AppCompatActivity implements ScrollListenerHorizontalScrollView.OnScrollListener {


    private Context mContext;
    private Activity act;
    private SharedPreferences dataPref;
    private SharedPreferences.Editor dataEditor;
    private SwipeMenuRecyclerView mMenuRecyclerView;
    private GxpgAdapter mMenuAdapter;

    private TextView tv_group;
    private TextView tv_number;
    private TextView tv_planbill;
    private TextView tv_style;

    private TextView tv_tip;

    private FancyButton btn_submit;

    private FancyButton btn_list;

    private FancyButton btn_usePlan;
    //存储获取过来的工序
    List<ProcessWorkCardPlanEntry> processWorkCardPlanEntryList;

    //存储要传递的工序
    List<Sc_ProcessWorkCardEntry> sc_ProcessWorkCardEntryList;

    private String[][] nameList;

    public ScrollListenerHorizontalScrollView ScrollView;

    private int finterid;

    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态啦
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gxpg);
        //隐藏标题栏
        getSupportActionBar().hide();
        mContext = this;
        act = this;
        dataPref = this.getSharedPreferences(define.SharedName, 0);
        dataEditor = dataPref.edit();

        ScrollView = (ScrollListenerHorizontalScrollView) findViewById(R.id.ScrollView);
        ScrollView.setOnScrollListener(this);

        tv_tip = (TextView) findViewById(R.id.tv_tip);

        tv_group = (TextView) findViewById(R.id.tv_group);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_planbill = (TextView) findViewById(R.id.tv_planbill);
        tv_style = (TextView) findViewById(R.id.tv_style);


        //取出姓名和员工工号数据

        nameList = new String[PgdActivity.nameListResult.getData().size()][3];

        for (int i = 0; i < PgdActivity.nameListResult.getData().size(); i++) {
            nameList[i][0] = PgdActivity.nameListResult.getData().get(i).getEmp_Name();
            nameList[i][1] = PgdActivity.nameListResult.getData().get(i).getEmp_Code();
            nameList[i][2] = PgdActivity.nameListResult.getData().get(i).getEmp_ID();
        }


        //初始化数据
        processWorkCardPlanEntryList = new ArrayList<ProcessWorkCardPlanEntry>();

        sc_ProcessWorkCardEntryList = new ArrayList<Sc_ProcessWorkCardEntry>();


        Intent intent = getIntent();
        finterid = intent.getIntExtra("finterid", 0);
        getData(finterid);
        Log.e("finterid", "finterid:" + finterid);


        mMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mMenuRecyclerView.addItemDecoration(new ListViewDecoration(this));// 添加分割线。

        // 设置菜单创建器。
        mMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);

        mMenuAdapter = new GxpgAdapter(processWorkCardPlanEntryList, nameList, this);

        mMenuAdapter.setOnItemClickListener(onItemClickListener);
        mMenuRecyclerView.setAdapter(mMenuAdapter);

        //初始化数据库
        dbManager = initDb();

        btn_submit = (FancyButton) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(act);
                normalDialog.setTitle("提示");
                normalDialog.setMessage("你确定要提交?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                submitData();
                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                normalDialog.show();
            }
        });

        btn_list = (FancyButton) findViewById(R.id.btn_list);
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NameListActivity.class);
                mContext.startActivity(intent);
            }
        });
        btn_usePlan = (FancyButton) findViewById(R.id.btn_usePlan);
        btn_usePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useGxpgPlan();
                //计划只可应用一次
                btn_usePlan.setEnabled(false);
            }
        });
    }

    public void getData(int finterid) {
        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setText("数据载入中...");
        processWorkCardPlanEntryList.clear();
        sc_ProcessWorkCardEntryList.clear();
        RequestParams params = new RequestParams(define.GetProcessPlanEntry);
        params.setReadTimeout(60000);
        params.addQueryStringParameter("FInterID", String.valueOf(finterid));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                Gson g = new Gson();
                GxpgResult gxpgs = g.fromJson(result, GxpgResult.class);
                if (gxpgs.getData() != null) {

                    tv_group.setText("组别:" + gxpgs.getData().get(0).getFdeptmentname());
                    tv_number.setText("派工单号:" + gxpgs.getData().get(0).getProcessbillnumber());
                    tv_planbill.setText("计划跟踪号:" + gxpgs.getData().get(0).getPlanbill());
                    tv_style.setText("工厂型体:" + gxpgs.getData().get(0).getPlantbody());

                    for (int i = 0; i < gxpgs.getData().size(); i++) {
                        processWorkCardPlanEntryList.add(gxpgs.getData().get(i));
                        addSc_ProcessWorkCardEntry(gxpgs.getData().get(i));
                    }
                    tv_tip.setVisibility(View.GONE);

                } else {
                    tv_tip.setText("暂无数据");
                }
                mMenuAdapter.notifyDataSetChanged();
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

    /**
     * Item点击监听。
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {

        }
    };

    @Override
    public void onScroll(int scrollX) {
        for (int i = 0; i < mMenuAdapter.ScrollViewList.size(); i++) {
            mMenuAdapter.ScrollViewList.get(i).scrollTo(scrollX, ScrollView.getScrollY());
        }
    }

    public void addSc_ProcessWorkCardEntry(ProcessWorkCardPlanEntry processWorkCardPlanEntry) {
        Sc_ProcessWorkCardEntry sc_processWorkCardEntry = new Sc_ProcessWorkCardEntry();
        sc_processWorkCardEntry.setFinterid(processWorkCardPlanEntry.getFinterid());
        sc_processWorkCardEntry.setFentryid(processWorkCardPlanEntry.getFentryid());
        sc_processWorkCardEntry.setFmtono(processWorkCardPlanEntry.getFmtono());
        sc_processWorkCardEntry.setFpartsid(processWorkCardPlanEntry.getFpartsid());
        sc_processWorkCardEntry.setFsrcicmointerid(processWorkCardPlanEntry.getFsrcicmointerid());
        sc_processWorkCardEntry.setFprdmoid(processWorkCardPlanEntry.getFprdmoid());
        sc_processWorkCardEntry.setFmoid(null);
        sc_processWorkCardEntry.setFoperplanningid(processWorkCardPlanEntry.getFoperplanningid());
        sc_processWorkCardEntry.setFproductid(processWorkCardPlanEntry.getFproductid());
        sc_processWorkCardEntry.setFdeptmentid(processWorkCardPlanEntry.getFdeptmentid());
        sc_processWorkCardEntry.setFitemid(processWorkCardPlanEntry.getFitemid());
        sc_processWorkCardEntry.setFprocessid(null);
        sc_processWorkCardEntry.setFprocessnumber(processWorkCardPlanEntry.getProcesscode());
        sc_processWorkCardEntry.setFprocessname(processWorkCardPlanEntry.getProcessname());
        sc_processWorkCardEntry.setFsize(null);
        sc_processWorkCardEntry.setFmochinecode(null);
        sc_processWorkCardEntry.setFmochinecode(null);
        sc_processWorkCardEntry.setFmustqty(processWorkCardPlanEntry.getHavedispatchingnumber());
        sc_processWorkCardEntry.setFplanstartdate(null);
        sc_processWorkCardEntry.setFplanfinishdate(null);
        sc_processWorkCardEntry.setFfinishqty(processWorkCardPlanEntry.getReportednumber());
        sc_processWorkCardEntry.setFlastfinishdate(null);
        sc_processWorkCardEntry.setFprice(processWorkCardPlanEntry.getPrice());
        sc_processWorkCardEntry.setFamount(processWorkCardPlanEntry.getFmoney());
        sc_processWorkCardEntry.setFprocesserid(0);
        sc_processWorkCardEntry.setFprocessdate(null);
        sc_processWorkCardEntry.setFcardno(processWorkCardPlanEntry.getBarcode());
        sc_processWorkCardEntry.setFnotes(null);
        sc_processWorkCardEntry.setFversion("0.1");
        sc_processWorkCardEntry.setFsourceinterid(processWorkCardPlanEntry.getFsourceinterid());
        sc_processWorkCardEntry.setFsourceentryid(processWorkCardPlanEntry.getFsourceentryid());
        sc_processWorkCardEntry.setFprocessoutputid(processWorkCardPlanEntry.getFprocessoutputid());
        sc_processWorkCardEntry.setFbatchno(processWorkCardPlanEntry.getBatch());
        sc_processWorkCardEntry.setFgroupprintno(null);
        sc_processWorkCardEntry.setFmodelid(processWorkCardPlanEntry.getFmodelid());
        sc_processWorkCardEntry.setFroutingid(processWorkCardPlanEntry.getFroutingid());
        if (processWorkCardPlanEntry.getUndispatchingnumber().intValue() == 0) {
            sc_processWorkCardEntry.setFqty(processWorkCardPlanEntry.getDispatchingnumber());
        } else {
            sc_processWorkCardEntry.setFqty(processWorkCardPlanEntry.getUndispatchingnumber());
        }
        if (processWorkCardPlanEntry.getFempid() == 0) {
            sc_processWorkCardEntry.setName(nameList[0][0]);
            sc_processWorkCardEntry.setJobNumber(nameList[0][1]);
            sc_processWorkCardEntry.setFempid(Integer.valueOf(nameList[0][2]));
        } else {
            sc_processWorkCardEntry.setFempid(processWorkCardPlanEntry.getFempid());
            String emp = processWorkCardPlanEntry.getFemp();
            String name = emp.substring(emp.indexOf("(") + 1, emp.length() - 1);
            sc_processWorkCardEntry.setName(name);
            String jobNumber = emp.substring(0, emp.indexOf("("));
            sc_processWorkCardEntry.setJobNumber(jobNumber);
        }
        sc_ProcessWorkCardEntryList.add(sc_processWorkCardEntry);
    }

    public void useGxpgPlan() {
        try {
            for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
                List<GxpgPlan> gxpgPlanList = dbManager.selector(GxpgPlan.class).where("processname", " = ", sc_ProcessWorkCardEntryList.get(i).getFprocessname()).findAll();
                for (int j = 0; j < gxpgPlanList.size(); j++) {
                    //如果一道工序多个派工则添加数据
                    if (j > 0) {
                        ProcessWorkCardPlanEntry processWorkCardPlanEntry = (ProcessWorkCardPlanEntry) processWorkCardPlanEntryList.get(i).clone();
                        processWorkCardPlanEntryList.add(i + j, processWorkCardPlanEntry);

                        Sc_ProcessWorkCardEntry sc_processWorkCardEntry = (Sc_ProcessWorkCardEntry) sc_ProcessWorkCardEntryList.get(i).clone();
                        sc_ProcessWorkCardEntryList.add(i + j, sc_processWorkCardEntry);
                    }
                    sc_ProcessWorkCardEntryList.get(i + j).setFempid(gxpgPlanList.get(j).getEmpid());
                    sc_ProcessWorkCardEntryList.get(i + j).setJobNumber(gxpgPlanList.get(j).getUsernumber());
                    sc_ProcessWorkCardEntryList.get(i + j).setName(gxpgPlanList.get(j).getUsername());
                    int qty = (int) (processWorkCardPlanEntryList.get(i + j).getHavedispatchingnumber().floatValue() * gxpgPlanList.get(j).getPer());
                    sc_ProcessWorkCardEntryList.get(i + j).setFqty(new BigDecimal(qty));
                    Log.e("jindi", gxpgPlanList.get(j).getProcessname() + ",name:" + gxpgPlanList.get(j).getUsername() + ",qty:" + qty);
                }
                //赋值i，跳过多个派工
                if (gxpgPlanList.size() > 1) {
                    i = i + gxpgPlanList.size() - 1;
                }
            }
            mMenuAdapter.notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
            Log.e("jindi", e.toString());
        }
    }

    public void submitData() {
        //保存最近工序计划
        saveGxpgPlan();

        Gson g = new Gson();
        RequestParams params = new RequestParams(define.InsertProcessPlanEntry);
        params.setAsJsonContent(true);
        params.setBodyContent(g.toJson(sc_ProcessWorkCardEntryList));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                //重新设置数据
                Log.e("jindi", result);
                if (result.contains("OK")) {
                    Alerter.create(act)
                            .setTitle("提示")
                            .setText("提交成功")
                            .setBackgroundColorRes(R.color.colorAlert)
                            .show();
                    getData(finterid);
                } else {
                    Alerter.create(act)
                            .setTitle("提示")
                            .setText("提交失败")
                            .setBackgroundColorRes(R.color.colorAlert)
                            .show();

                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("jindi", ex.getMessage());
                tv_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("提交失败:" + ex.toString());
                Alerter.create(act)
                        .setTitle("提示")
                        .setText("提交失败:" + ex.getMessage())
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

    //保存工序计划
    public void saveGxpgPlan() {
        ArrayList<GxpgPlan> gxpgPlanList = new ArrayList<>();
        for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
            //先删除原先保留的工序计划
            try {
                WhereBuilder b = WhereBuilder.b();
                b.and("processname","=",sc_ProcessWorkCardEntryList.get(i).getFprocessname()); //构造修改的条件
                dbManager.delete(GxpgPlan.class, b);
            } catch (DbException e) {
                e.printStackTrace();
            }

            GxpgPlan gxpgPlan = new GxpgPlan();
            gxpgPlan.setProcessname(sc_ProcessWorkCardEntryList.get(i).getFprocessname());
            gxpgPlan.setUsername(sc_ProcessWorkCardEntryList.get(i).getName());
            gxpgPlan.setUsernumber(sc_ProcessWorkCardEntryList.get(i).getJobNumber());
            gxpgPlan.setEmpid(sc_ProcessWorkCardEntryList.get(i).getFempid());
            gxpgPlan.setPer(sc_ProcessWorkCardEntryList.get(i).getFqty().floatValue() / processWorkCardPlanEntryList.get(i).getHavedispatchingnumber().floatValue());
            Log.e("jindi", "processname:" + sc_ProcessWorkCardEntryList.get(i).getFprocessname() + ",processname:" + sc_ProcessWorkCardEntryList.get(i).getName() +
                    ",Qty:" + sc_ProcessWorkCardEntryList.get(i).getFqty());
            gxpgPlanList.add(gxpgPlan);
        }
        try {
            dbManager.save(gxpgPlanList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = mContext.getResources().getDimensionPixelSize(R.dimen.item_height);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_green)
                        .setImage(R.mipmap.ic_action_add)
                        .setText("复制")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。

                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

            }

        }
    };

    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView
         *                        #RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                //Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                //Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }

            // TODO 如果是删除：推荐调用Adapter.notifyItemRemoved(position)，不推荐Adapter.notifyDataSetChanged();
            if (menuPosition == 0) {// 处理按钮被点击。
                ProcessWorkCardPlanEntry processWorkCardPlanEntry = (ProcessWorkCardPlanEntry) processWorkCardPlanEntryList.get(adapterPosition).clone();
                processWorkCardPlanEntryList.add(adapterPosition + 1, processWorkCardPlanEntry);

                Sc_ProcessWorkCardEntry sc_processWorkCardEntry = (Sc_ProcessWorkCardEntry) sc_ProcessWorkCardEntryList.get(adapterPosition).clone();
                sc_processWorkCardEntry.setFentryid(sc_processWorkCardEntry.getFentryid()+1);
                sc_ProcessWorkCardEntryList.add(adapterPosition + 1, sc_processWorkCardEntry);
                //插入工序以后的所有entryid都要加1
                for(int i=adapterPosition + 2;i<sc_ProcessWorkCardEntryList.size();i++){
                    sc_ProcessWorkCardEntryList.get(i).setFentryid(sc_ProcessWorkCardEntryList.get(i).getFentryid()+1);
                }
                mMenuAdapter.notifyItemInserted(adapterPosition + 1);
            } else if (menuPosition == 1) {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(act);
                normalDialog.setTitle("提示");
                normalDialog.setMessage("你确定要删除这条数据");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int gxCount = 0;
                                for (int i = 0; i < processWorkCardPlanEntryList.size(); i++) {
                                    if (processWorkCardPlanEntryList.get(i).getProcesscode().equals(processWorkCardPlanEntryList.get(adapterPosition).getProcesscode())) {
                                        gxCount++;
                                    }
                                }
                                if (gxCount <= 1) {
                                    final AlertDialog.Builder normalDialog =
                                            new AlertDialog.Builder(act);
                                    normalDialog.setTitle("提示");
                                    normalDialog.setMessage("本道工序仅有此一条数据,不允许删除");
                                    normalDialog.setNegativeButton("取消",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    normalDialog.show();
                                } else {
                                    processWorkCardPlanEntryList.remove(adapterPosition);
                                    sc_ProcessWorkCardEntryList.remove(adapterPosition);
                                    mMenuAdapter.notifyItemRemoved(adapterPosition);
                                }
                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                normalDialog.show();
            }
            closeable.smoothCloseMenu();// 关闭被点击的菜单。
        }
    };

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
}
