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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.goldemperor.PgdActivity.GxpgAdapter.gxpgActivity;
import static com.goldemperor.PgdActivity.PgdActivity.selectWorkCardPlan;
import static com.goldemperor.R.id.position;


/**
 * Created by Nova on 2017/7/25.
 */

public class GxpgActivity extends AppCompatActivity implements ScrollListenerHorizontalScrollView.OnScrollListener {


    private Context mContext;
    private Activity act;
    private SharedPreferences dataPref;
    private SharedPreferences.Editor dataEditor;
    private SwipeMenuRecyclerView mMenuRecyclerView;
    public static GxpgAdapter mMenuAdapter;

    private TextView tv_group;
    private TextView tv_number;
    private TextView tv_planbill;
    private TextView tv_style;
    private TextView top_havedispatchingnumber;

    private TextView tv_tip;

    TextView tv_reportednumber;

    TextView top_noReportednumber;

    private TextView top_record;

    private TextView top_norecord;

    public static int norecord;

    private FancyButton btn_submit;

    private FancyButton btn_namelist;

    private FancyButton btn_instructor;

    private FancyButton btn_dispatchingnumberPublish;

    private FancyButton btn_reportPublish;

    private FancyButton btn_report;

    private FancyButton btn_PlanMaterial;

    private FancyButton btn_assign;

    private FancyButton btn_operation;
    //private FancyButton btn_useGxpgPlan;
    //存储获取过来的工序
    List<ProcessWorkCardPlanEntry> processWorkCardPlanEntryList;

    //存储要传递的工序
    List<Sc_ProcessWorkCardEntry> sc_ProcessWorkCardEntryList;

    private String[][] nameList;

    public ScrollListenerHorizontalScrollView ScrollView;

    private int finterid;

    private DbManager dbManager;

    private ProcessOutPut processOutPut;

    private int reportCount = 0;

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
        //初始化数据库
        dbManager = initDb();

        ScrollView = (ScrollListenerHorizontalScrollView) findViewById(R.id.ScrollView);
        ScrollView.setOnScrollListener(this);

        tv_tip = (TextView) findViewById(R.id.tv_tip);

        tv_group = (TextView) findViewById(R.id.tv_group);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_planbill = (TextView) findViewById(R.id.tv_planbill);
        tv_style = (TextView) findViewById(R.id.tv_style);
        top_havedispatchingnumber = (TextView) findViewById(R.id.top_havedispatchingnumber);

        tv_reportednumber = (TextView) findViewById(R.id.tv_reportednumber);
        top_noReportednumber = (TextView) findViewById(R.id.top_noReportednumber);
        top_noReportednumber.setText("汇报欠数:" + String.valueOf(selectWorkCardPlan.getNotreportnumber().intValue()));
        top_record = (TextView) findViewById(R.id.top_record);

        tv_reportednumber.setText(String.valueOf("已汇报数:" + selectWorkCardPlan.getReportednumber().intValue()));

        top_record.setText(String.valueOf("累计计工数:" + selectWorkCardPlan.getCumulativenumber().intValue()));
        reportCount = selectWorkCardPlan.getCumulativenumber().intValue();
        Log.e("jindi","reportCount:"+reportCount);
        //取出姓名和员工工号数据
        top_norecord = (TextView) findViewById(R.id.top_norecord);


        nameList = new String[PgdActivity.nameListResult.getData().size()][4];

        for (int i = 0; i < PgdActivity.nameListResult.getData().size(); i++) {
            nameList[i][0] = PgdActivity.nameListResult.getData().get(i).getEmp_Name();
            nameList[i][1] = PgdActivity.nameListResult.getData().get(i).getEmp_Code();
            nameList[i][2] = PgdActivity.nameListResult.getData().get(i).getEmp_ID();
            nameList[i][3] = PgdActivity.nameListResult.getData().get(i).getEmp_Departid();
        }


        //初始化数据
        processWorkCardPlanEntryList = new ArrayList<ProcessWorkCardPlanEntry>();

        sc_ProcessWorkCardEntryList = new ArrayList<Sc_ProcessWorkCardEntry>();


        Intent intent = getIntent();
        finterid = intent.getIntExtra("finterid", 0);
        getData(finterid);


        mMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mMenuRecyclerView.addItemDecoration(new ListViewDecoration(this));// 添加分割线。

        // 设置菜单创建器。
        mMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);

        mMenuAdapter = new GxpgAdapter(processWorkCardPlanEntryList, nameList, this, dbManager);

        mMenuAdapter.setOnItemClickListener(onItemClickListener);
        mMenuRecyclerView.setAdapter(mMenuAdapter);


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

        btn_namelist = (FancyButton) findViewById(R.id.btn_namelist);
        btn_namelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NameListActivity.class);
                mContext.startActivity(intent);
            }
        });
        btn_instructor = (FancyButton) findViewById(R.id.btn_instructor);

        btn_instructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InstructorActivity.class);
                mContext.startActivity(intent);
            }
        });
        btn_dispatchingnumberPublish = (FancyButton) findViewById(R.id.btn_dispatchingnumberPublish);

        btn_dispatchingnumberPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(act);
                normalDialog.setTitle("提示");
                normalDialog.setMessage("你确定要发布预排信息?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    List<GxpgPlanStatus> gxpgPlanStatusesList = dbManager.selector(GxpgPlanStatus.class).where("planbill", " = ", selectWorkCardPlan.getPlanbill()).and("orderbill", "=", selectWorkCardPlan.getOrderbill()).findAll();

                                    if (gxpgPlanStatusesList != null && gxpgPlanStatusesList.size() >= 1) {
                                        for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
                                            dispatchingnumberPublish(i);
                                        }
                                        Toast.makeText(mContext, "预排发布成功", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(mContext, "请先保存预排", Toast.LENGTH_LONG).show();
                                    }
                                } catch (DbException e) {
                                    e.printStackTrace();
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
        });

        btn_reportPublish = (FancyButton) findViewById(R.id.btn_reportPublish);

        btn_reportPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(act);
                normalDialog.setTitle("提示");
                normalDialog.setMessage("你确定要发布今日汇报信息?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
                                    reportPublish(i);
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
        });

        btn_report = (FancyButton) findViewById(R.id.btn_report);

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCProcessWorkCard2SCProcessOutPutBysuitID();
            }
        });

        btn_PlanMaterial = (FancyButton) findViewById(R.id.btn_PlanMaterial);

        btn_PlanMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MaterialActivity.class);
                mContext.startActivity(intent);
            }
        });

        btn_assign = (FancyButton) findViewById(R.id.btn_assign);

        btn_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("planBody", processWorkCardPlanEntryList.get(0).getPlantbody());
                Intent intent = new Intent(act,
                        AssignActivity.class);
                intent.putExtras(bundle);
                // 启动另一个Activity。
                startActivityForResult(intent, 0);
            }
        });
        btn_operation = (FancyButton) findViewById(R.id.btn_operation);

        btn_operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<GxpgPlanStatus> gxpgPlanStatusesList = dbManager.selector(GxpgPlanStatus.class).where("planbill", " = ", selectWorkCardPlan.getPlanbill()).and("orderbill", "=", selectWorkCardPlan.getOrderbill()).findAll();
                    if (norecord <= 0) {
                        Toast.makeText(mContext, "已汇报未计工数为零", Toast.LENGTH_LONG).show();
                    } else if (gxpgPlanStatusesList == null && gxpgPlanStatusesList.size() < 1) {
                        Toast.makeText(mContext, "工序未保存", Toast.LENGTH_LONG).show();
                    } else {
                        for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {

                            GxpgPlan gxpgPlan = dbManager.selector(GxpgPlan.class).where("style", " = ", selectWorkCardPlan.getPlantbody()).and("processname", "=", gxpgActivity.processWorkCardPlanEntryList.get(i).getProcessname()).and("username", "=", gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getName()).findFirst();
                            if (gxpgPlan != null) {
                                sc_ProcessWorkCardEntryList.get(i).setReportNumber(norecord * gxpgPlan.getPer());
                                mMenuAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }


            }
        });
        /*
        btn_useGxpgPlan = (FancyButton) findViewById(R.id.btn_useGxpgPlan);

        btn_useGxpgPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useGxpgPlan();
            }
        });
        */
        Log.e("jindi", selectWorkCardPlan.getFinterid().toString());
    }

    public void getData(int finterid) {
        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setText("数据载入中...");
        processWorkCardPlanEntryList.clear();
        sc_ProcessWorkCardEntryList.clear();
        RequestParams params = new RequestParams(define.GetProcessPlanEntry);
        params.setReadTimeout(60000);
        params.addQueryStringParameter("FInterID", String.valueOf(finterid));
        //Log.e("jindi", params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                Gson g = new Gson();
                GxpgResult gxpgs = g.fromJson(result, GxpgResult.class);
                if (gxpgs != null && gxpgs.getData() != null) {

                    tv_group.setText("组别:" + gxpgs.getData().get(0).getFdeptmentname());
                    tv_number.setText("派工单号:" + gxpgs.getData().get(0).getProcessbillnumber());
                    tv_planbill.setText("计划跟踪号:" + gxpgs.getData().get(0).getPlanbill());
                    tv_style.setText("工厂型体:" + gxpgs.getData().get(0).getPlantbody());
                    top_havedispatchingnumber.setText("应派工数:" + (gxpgs.getData().get(0).getHavedispatchingnumber().intValue()));
                    for (int i = 0; i < gxpgs.getData().size(); i++) {
                        processWorkCardPlanEntryList.add(gxpgs.getData().get(i));
                        addSc_ProcessWorkCardEntry(gxpgs.getData().get(i));
                    }

                    refreshData();
                    //自动应用计划工序
                    try {
                        List<GxpgPlanStatus> gxpgPlanStatusList = dbManager.selector(GxpgPlanStatus.class).where("planbill", " = ", selectWorkCardPlan.getPlanbill()).and("orderbill", "=", selectWorkCardPlan.getOrderbill()).findAll();

                        if (gxpgPlanStatusList == null || gxpgPlanStatusList.size() <= 0) {
                            useGxpgPlan();
                            Log.e("jindi", "应用计划工序");
                        } else {
                            Log.e("jindi", "工序已提交过");
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
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

    private void refreshData() {

        top_record.setText("累计计工数:" + String.valueOf(reportCount));
        //top_noReportednumber.setText("汇报欠数:" + String.valueOf(processWorkCardPlanEntryList.get(0).getHavedispatchingnumber().intValue() - reportCount));
        norecord = selectWorkCardPlan.getReportednumber().intValue() - reportCount;
        top_norecord.setText("已汇报未计工数:" + String.valueOf(norecord));
        //Log.e("jindi","reportCount:"+reportCount+" norecord"+norecord);
        mMenuAdapter.notifyDataSetChanged();
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
        sc_processWorkCardEntry.setFqty(processWorkCardPlanEntry.getDispatchingnumber());

        try {
            List<GxpgPlanStatus> gxpgPlanStatusesList = dbManager.selector(GxpgPlanStatus.class).where("planbill", " = ",selectWorkCardPlan.getPlanbill()).and("orderbill","=",selectWorkCardPlan.getOrderbill()).findAll();
            if ((sc_processWorkCardEntry.getFqty() == null || sc_processWorkCardEntry.getFqty().intValue() == 0)&&gxpgPlanStatusesList.size()<1) {
                sc_processWorkCardEntry.setFqty(new BigDecimal(processWorkCardPlanEntry.getHavedispatchingnumber().floatValue()));
            }
        } catch (DbException e) {
            e.printStackTrace();
        }



        if (processWorkCardPlanEntry.getFempid() == 0) {
            sc_processWorkCardEntry.setName(nameList[0][0]);
            sc_processWorkCardEntry.setJobNumber(nameList[0][1]);
            sc_processWorkCardEntry.setFempid(Integer.valueOf(nameList[0][2]));
            sc_processWorkCardEntry.setFdeptmentid(Integer.valueOf(nameList[0][3]));
        } else {
            sc_processWorkCardEntry.setFempid(processWorkCardPlanEntry.getFempid());
            String emp = processWorkCardPlanEntry.getFemp();
            String name = emp.substring(emp.indexOf("(") + 1, emp.length() - 1);
            sc_processWorkCardEntry.setName(name);
            String jobNumber = emp.substring(0, emp.indexOf("("));
            sc_processWorkCardEntry.setJobNumber(jobNumber);
            sc_processWorkCardEntry.setFdeptmentid(processWorkCardPlanEntry.getFdeptmentid());
        }

        try {
            GxpgPlan gxpgPlan = dbManager.selector(GxpgPlan.class).where("style", " = ", processWorkCardPlanEntry.getPlantbody()).and("processname", "=", processWorkCardPlanEntry.getProcessname()).and("username", "=", sc_processWorkCardEntry.getName()).findFirst();
            if (gxpgPlan != null) {
                DecimalFormat df = new DecimalFormat("#.0");
                //sc_processWorkCardEntry.setFqty(new BigDecimal(df.format(processWorkCardPlanEntry.getHavedispatchingnumber().floatValue() - selectWorkCardPlan.getReportednumber().floatValue() * gxpgPlan.getPer())));
                //Log.e("jindi","per:"+gxpgPlan.getPer());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        sc_ProcessWorkCardEntryList.add(sc_processWorkCardEntry);
    }

    public void useGxpgPlan() {
        try {
            for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
                List<GxpgPlan> gxpgPlanList = dbManager.selector(GxpgPlan.class).where("style", " = ", processWorkCardPlanEntryList.get(i).getPlantbody()).and("processname", "=", sc_ProcessWorkCardEntryList.get(i).getFprocessname()).findAll();
                if (gxpgPlanList != null) {
                    //先检查一遍此道工序是否有多道工序，如果是多道工序说明已应用过最近分配
                    int checkProcess = 0;
                    for (int c = 0; c < sc_ProcessWorkCardEntryList.size(); c++) {
                        if (sc_ProcessWorkCardEntryList.get(c).getFprocessname().equals(sc_ProcessWorkCardEntryList.get(i).getFprocessname())) {
                            checkProcess++;
                        }
                    }
                    for (int j = 0; j < gxpgPlanList.size(); j++) {
                        //如果一道工序多个派工,且不是第一个派工记录则添加数据
                        if (j > 0 && checkProcess <= 1) {
                            ProcessWorkCardPlanEntry processWorkCardPlanEntry = (ProcessWorkCardPlanEntry) processWorkCardPlanEntryList.get(i).clone();
                            processWorkCardPlanEntry.setFentryid(processWorkCardPlanEntryList.get(i).getFentryid() + 1);
                            //插入工序以后的所有entryid都要加1
                            for (int k = i + j; k < processWorkCardPlanEntryList.size(); k++) {
                                processWorkCardPlanEntryList.get(k).setFentryid(processWorkCardPlanEntryList.get(k).getFentryid() + 1);
                            }

                            processWorkCardPlanEntryList.add(i + j, processWorkCardPlanEntry);

                            Sc_ProcessWorkCardEntry sc_processWorkCardEntry = (Sc_ProcessWorkCardEntry) sc_ProcessWorkCardEntryList.get(i).clone();

                            sc_processWorkCardEntry.setFentryid(sc_ProcessWorkCardEntryList.get(i).getFentryid() + 1);
                            //插入工序以后的所有entryid都要加1
                            for (int k = i + j; k < sc_ProcessWorkCardEntryList.size(); k++) {
                                sc_ProcessWorkCardEntryList.get(k).setFentryid(sc_ProcessWorkCardEntryList.get(k).getFentryid() + 1);
                            }

                            sc_ProcessWorkCardEntryList.add(i + j, sc_processWorkCardEntry);


                        }
                        sc_ProcessWorkCardEntryList.get(i + j).setFempid(gxpgPlanList.get(j).getEmpid());
                        sc_ProcessWorkCardEntryList.get(i + j).setJobNumber(gxpgPlanList.get(j).getUsernumber());
                        sc_ProcessWorkCardEntryList.get(i + j).setName(gxpgPlanList.get(j).getUsername());
                        double qty = processWorkCardPlanEntryList.get(i + j).getHavedispatchingnumber().floatValue() * gxpgPlanList.get(j).getPer();
                        DecimalFormat df = new DecimalFormat("#.0");
                        sc_ProcessWorkCardEntryList.get(i + j).setFqty(new BigDecimal(df.format(qty)));
                        Log.e("jindi", gxpgPlanList.get(j).getProcessname() + ",name:" + gxpgPlanList.get(j).getUsername() + ",qty:" + qty);
                    }
                    //赋值i，跳过重复派工
                    if (gxpgPlanList.size() > 1) {
                        i = i + gxpgPlanList.size() - 1;
                    }
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
                if (result.contains("OK")) {
                    Alerter.create(act)
                            .setTitle("提示")
                            .setText("提交成功")
                            .setBackgroundColorRes(R.color.colorAlert)
                            .show();
                    //提交成功后反写
                    ReWrite();
                    //提交成功后刷新
                    getData(finterid);
                    selectWorkCardPlan.setPlanStatus("已排");
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

    public void ReWrite() {
        List<PushJsonCondition> pushJsonConditionList = new ArrayList<PushJsonCondition>();
        for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
            PushJsonCondition pushJsonCondition = new PushJsonCondition();
            pushJsonCondition.setFEntryID(String.valueOf(sc_ProcessWorkCardEntryList.get(i).getFentryid()));
            pushJsonCondition.setFInterID(String.valueOf(sc_ProcessWorkCardEntryList.get(i).getFinterid()));
            pushJsonCondition.setFSize(String.valueOf(sc_ProcessWorkCardEntryList.get(i).getFsize()));
            pushJsonConditionList.add(pushJsonCondition);
        }

        Gson g = new Gson();
        RequestParams params = new RequestParams(define.SCProcessWorkCardReWriteBysuitID);
        params.addBodyParameter("PushJsonCondition", g.toJson(pushJsonConditionList));
        params.addBodyParameter("suitID", "32");
        //Log.e("jindi",params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                //重新设置数据
                Log.e("jindi", result);
                if (result.contains("OK")) {

                } else {

                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("jindi", ex.getMessage());
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

        //存储派工单已排状态
        GxpgPlanStatus gxpgPlanStatus = new GxpgPlanStatus();
        gxpgPlanStatus.setOrderbill(selectWorkCardPlan.getOrderbill());
        gxpgPlanStatus.setPlanbill(selectWorkCardPlan.getPlanbill());
        gxpgPlanStatus.setStatus("已排");

        //存储工序计划
        ArrayList<GxpgPlan> gxpgPlanList = new ArrayList<>();
        for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
            //先删除原先保留的工序计划
            try {
                WhereBuilder b = WhereBuilder.b();
                b.and("style", "=", processWorkCardPlanEntryList.get(i).getPlantbody());
                b.and("processname", "=", sc_ProcessWorkCardEntryList.get(i).getFprocessname());
                dbManager.delete(GxpgPlan.class, b);
            } catch (DbException e) {
                e.printStackTrace();
            }

            GxpgPlan gxpgPlan = new GxpgPlan();
            gxpgPlan.setStyle(processWorkCardPlanEntryList.get(i).getPlantbody());
            gxpgPlan.setProcessname(sc_ProcessWorkCardEntryList.get(i).getFprocessname());
            gxpgPlan.setUsername(sc_ProcessWorkCardEntryList.get(i).getName());
            gxpgPlan.setUsernumber(sc_ProcessWorkCardEntryList.get(i).getJobNumber());
            gxpgPlan.setEmpid(sc_ProcessWorkCardEntryList.get(i).getFempid());
            //gxpgPlan.setPer(1.0f);
            gxpgPlan.setPer(sc_ProcessWorkCardEntryList.get(i).getFqty().floatValue() / (processWorkCardPlanEntryList.get(i).getHavedispatchingnumber().floatValue()));
            Log.e("jindi", String.valueOf(processWorkCardPlanEntryList.get(i).getHavedispatchingnumber().floatValue() - selectWorkCardPlan.getReportednumber().floatValue()));
            //Log.e("jindi", "getFqty:"+sc_ProcessWorkCardEntryList.get(i).getFqty().floatValue()+" getHavedispatchingnumber()"+processWorkCardPlanEntryList.get(i).getHavedispatchingnumber().floatValue());

            //Log.e("jindi", "stype:" + processWorkCardPlanEntryList.get(i).getPlantbody() + "processname:" + sc_ProcessWorkCardEntryList.get(i).getFprocessname() + ",processname:" + sc_ProcessWorkCardEntryList.get(i).getName() +
            // ",Qty:" + sc_ProcessWorkCardEntryList.get(i).getFqty());
            gxpgPlanList.add(gxpgPlan);
        }
        try {
            dbManager.save(gxpgPlanList);
            dbManager.save(gxpgPlanStatus);
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
                sc_processWorkCardEntry.setFentryid(sc_processWorkCardEntry.getFentryid() + 1);
                //插入工序以后的所有entryid都要加1
                for (int i = adapterPosition + 1; i < sc_ProcessWorkCardEntryList.size(); i++) {
                    sc_ProcessWorkCardEntryList.get(i).setFentryid(sc_ProcessWorkCardEntryList.get(i).getFentryid() + 1);
                }
                sc_processWorkCardEntry.setFqty(new BigDecimal(0));
                sc_processWorkCardEntry.setFfinishqty(new BigDecimal(0));

                sc_ProcessWorkCardEntryList.add(adapterPosition + 1, sc_processWorkCardEntry);

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

    public void SCProcessWorkCard2SCProcessOutPutBysuitID() {
        Gson g = new Gson();
        List<PushJsonCondition> pushJsonConditionList = new ArrayList<PushJsonCondition>();
        for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
            PushJsonCondition pushJsonCondition = new PushJsonCondition();
            pushJsonCondition.setFEntryID(String.valueOf(sc_ProcessWorkCardEntryList.get(i).getFentryid()));
            pushJsonCondition.setFInterID(String.valueOf(sc_ProcessWorkCardEntryList.get(i).getFinterid()));
            pushJsonCondition.setFSize(sc_ProcessWorkCardEntryList.get(i).getFsize());
            pushJsonConditionList.add(pushJsonCondition);

        }
        //Log.e("jindi", "interid:" + sc_ProcessWorkCardEntryList.get(0).getFinterid());
        RequestParams params = new RequestParams(define.SCProcessWorkCard2SCProcessOutPutBysuitID);
        params.addParameter("PushJsonCondition", g.toJson(pushJsonConditionList));
        params.addParameter("UserID", dataPref.getString(define.SharedUserId, "0"));
        params.addParameter("suitID", "32");
        //Log.e("jindi",params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1).replaceAll("\\\\", "").replaceAll("\"\\[", "\\[").replaceAll("]\"", "]");
                Log.e("jindi", result);
                if (result.contains("success")) {
                    Gson g = new Gson();
                    processOutPut = g.fromJson(result, ProcessOutPut.class);

                    for (int i = 0; i < processOutPut.getReturnMsg().size(); i++) {
                        processOutPut.getReturnMsg().get(i).setFcheckerid(processOutPut.getReturnMsg().get(i).getFbillerid());
                        processOutPut.getReturnMsg().get(i).setFemp(sc_ProcessWorkCardEntryList.get(i).getName());
                        processOutPut.getReturnMsg().get(i).setFempid(sc_ProcessWorkCardEntryList.get(i).getFempid());
                        processOutPut.getReturnMsg().get(i).setFdeptnumber(String.valueOf(sc_ProcessWorkCardEntryList.get(i).getFdeptmentid()));
                    }

                    //TextView tv_processname = (TextView) layout.findViewById(R.id.tv_processname);
                    //tv_processname.setText("工序名称:" + processOutPut.getReturnMsg().get(0).getFprocessname());

                    //TextView tv_emp = (TextView) layout.findViewById(R.id.tv_emp);
                    //tv_emp.setText("员工姓名:" + processOutPut.getReturnMsg().get(0).getFemp());

                    //final BigDecimal price = processOutPut.getReturnMsg().get(0).getFprice();
                    //TextView tv_fprice = (TextView) layout.findViewById(R.id.tv_fprice);
                    //tv_fprice.setText("单价:" + price);

                    //final TextView tv_famount = (TextView) layout.findViewById(R.id.tv_famount);
                    //tv_famount.setText("总价:" + processOutPut.getReturnMsg().get(0).getFamount());

                    float maxCount = 0;
                    for (int i = 0; i < processOutPut.getReturnMsg().size(); i++) {
                        //float per = sc_ProcessWorkCardEntryList.get(i).getFqty().floatValue() / processWorkCardPlanEntryList.get(i).getHavedispatchingnumber().floatValue();

                        //GxpgPlan gxpgPlan = dbManager.selector(GxpgPlan.class).where("style", " = ", selectWorkCardPlan.getPlantbody()).and("processname", "=", gxpgActivity.processWorkCardPlanEntryList.get(i).getProcessname()).and("username", "=", gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getName()).findFirst();
                        float count = 0;
                        for (int j = 0; j < gxpgActivity.sc_ProcessWorkCardEntryList.size(); j++) {
                            if (gxpgActivity.processWorkCardPlanEntryList.get(j).getProcessname().equals(gxpgActivity.processWorkCardPlanEntryList.get(i).getProcessname())) {
                                count += sc_ProcessWorkCardEntryList.get(j).getReportNumber();
                            }
                        }

                        Log.e("jindi", "maxCount：" + maxCount);
                        if (count > maxCount) {
                            maxCount = count;
                        }
                        if (maxCount > Float.valueOf(norecord)+0.01f) {
                            break;
                        } else {
                            float qty = sc_ProcessWorkCardEntryList.get(i).getReportNumber();
                            DecimalFormat df = new DecimalFormat("#.0");
                            processOutPut.getReturnMsg().get(i).setFqty(new BigDecimal(df.format(qty)));
                            Log.e("jindi","qty:"+df.format(qty));
                        }

                    }
                    Log.e("jindi","norecord:"+norecord);
                    if (maxCount > Float.valueOf(norecord)+0.01f) {
                        Toast.makeText(mContext, "计工数超过汇报数", Toast.LENGTH_LONG).show();
                    } else if (maxCount <= 0) {
                        Toast.makeText(mContext, "计工数不能小于零", Toast.LENGTH_LONG).show();
                    } else {
                        reportCount = (int) maxCount;
                        Log.e("jindi","reportCount:"+reportCount);
                        submitProcessOutPut();
                    }

                } else

                {
                    Alerter.create(act)
                            .setTitle("提示")
                            .setText("汇报单生成失败")
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

    private void submitProcessOutPut() {
        Gson g = new Gson();
        RequestParams params = new RequestParams(define.SCProcessOutPutSaveBysuitID);
        params.addParameter("PushJsonCondition", g.toJson(processOutPut.getReturnMsg()));
        params.addParameter("Type", "insert");
        params.addParameter("suitID", "32");
        //String log=params.toString();
        //Log.e("jindi",log.substring(0,log.length()/2));
        //Log.e("jindi",log.substring(log.length()/2));
        //Log.e("jindi", params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("jindi", result);
                if (result.contains("success")) {
                    Toast.makeText(mContext, "工序汇报录入中...", Toast.LENGTH_LONG).show();
                    SCProcessOutPutReWriteBysuitID();
                } else {
                    Toast.makeText(mContext, "工序汇报超额", Toast.LENGTH_LONG).show();
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

    private void SCProcessOutPutReWriteBysuitID() {
        List<PushJsonCondition> pushJsonConditionList = new ArrayList<PushJsonCondition>();
        for (int i = 0; i < processOutPut.getReturnMsg().size(); i++) {
            PushJsonCondition pushJsonCondition = new PushJsonCondition();
            pushJsonCondition.setFEntryID(String.valueOf(sc_ProcessWorkCardEntryList.get(i).getFentryid()));
            pushJsonCondition.setFInterID(String.valueOf(processOutPut.getReturnMsg().get(i).getFinterid()));
            pushJsonCondition.setFSize(String.valueOf(processOutPut.getReturnMsg().get(i).getFsize()));
            pushJsonConditionList.add(pushJsonCondition);
        }
        Gson g = new Gson();
        RequestParams params = new RequestParams(define.SCProcessOutPutReWriteBysuitID);
        params.addParameter("PushJsonCondition", g.toJson(pushJsonConditionList));
        params.addParameter("suitID", "32");
        //Log.e("jindi", params.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("jindi", result);
                if (result.contains("success")) {
                    //UpdateProcessPassQty();
                    Toast.makeText(mContext, "工序汇报录入成功", Toast.LENGTH_LONG).show();
                    GetWorkCardProcessQty();
                } else {
                    Toast.makeText(mContext, "工序汇报录入失败", Toast.LENGTH_LONG).show();
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

    private void UpdateProcessPassQty() {
        RequestParams params = new RequestParams(define.UpdateProcessPassQty);
        params.addQueryStringParameter("FInterID", String.valueOf(selectWorkCardPlan.getFinterid()));
        params.addQueryStringParameter("FQty", String.valueOf(reportCount));
        //Log.e("jindi", params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("jindi", result);
                if (result.contains("成功")) {
                    Toast.makeText(mContext, "工序汇报录入成功", Toast.LENGTH_LONG).show();
                    GetWorkCardProcessQty();
                } else {

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

    private void GetWorkCardProcessQty() {
        RequestParams params = new RequestParams(define.GetWorkCardProcessQty);
        params.addQueryStringParameter("FInterID", String.valueOf(selectWorkCardPlan.getFinterid()));
        //Log.e("jindi",params.toString());
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
                ProcessPassQty pq = g.fromJson(result, ProcessPassQty.class);

                //String reportStr = result.substring(result.indexOf("ReturnMsg"), result.indexOf(",")).replace("ReturnMsg\":", "").replace("\"", "");
                reportCount = pq.getData().get(0).getCumulativenumber().intValue();
                Log.e("jindi","reportCount:"+reportCount);
                refreshData();
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

    private void dispatchingnumberPublish(int position) {
        RequestParams params = new RequestParams(define.WechatPostByFEmpID);
        params.addHeader("AppKey", define.appKey);
        params.addHeader("Nonce", define.nonce);
        String curTime = String.valueOf((new Date()).getTime());
        params.addHeader("CurTime", curTime);
        String checkSum = Utils.getCheckSum(define.appSecret, define.nonce, curTime);
        params.addHeader("CheckSum", checkSum);


        WechatPostByFEmpIDJson wechatPostByFEmpIDJson = new WechatPostByFEmpIDJson();
        wechatPostByFEmpIDJson.setFEmpID(String.valueOf(sc_ProcessWorkCardEntryList.get(position).getFempid()));
        wechatPostByFEmpIDJson.setTemplate_id(define.template_id);


        WechatPostByFEmpIDData wechatPostByFEmpIDData = new WechatPostByFEmpIDData();
        wechatPostByFEmpIDData.setFirst("您有一条新的派工单");
        wechatPostByFEmpIDData.setKeyword1("工厂型体:" + processWorkCardPlanEntryList.get(position).getPlantbody() + " " + processWorkCardPlanEntryList.get(position).getProcessname());
        wechatPostByFEmpIDData.setKeyword2("预排目标:" + sc_ProcessWorkCardEntryList.get(position).getFqty());

        //SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        //String dd = dff.format(new Date());
        try {
            wechatPostByFEmpIDData.setRemark("计划开工时间:" + Utils.dateConvert(selectWorkCardPlan.getPlanstarttime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        wechatPostByFEmpIDJson.getData().add(wechatPostByFEmpIDData);

        PulishData pulishData = new PulishData();
        pulishData.setWechatPostByFEmpIDData(wechatPostByFEmpIDJson);
        Gson g = new Gson();
        params.setAsJsonContent(true);
        params.setBodyContent(g.toJson(pulishData));
        //Log.e("jindi",g.toJson(pulishData));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("jindi", result);
                if (result.contains("200")) {

                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("jindi", ex.getMessage());
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

    private void reportPublish(int position) {
        RequestParams params = new RequestParams(define.WechatPostByFEmpID);
        params.addHeader("AppKey", define.appKey);
        params.addHeader("Nonce", define.nonce);
        String curTime = String.valueOf((new Date()).getTime());
        params.addHeader("CurTime", curTime);
        String checkSum = Utils.getCheckSum(define.appSecret, define.nonce, curTime);
        params.addHeader("CheckSum", checkSum);


        WechatPostByFEmpIDJson wechatPostByFEmpIDJson = new WechatPostByFEmpIDJson();
        wechatPostByFEmpIDJson.setFEmpID(String.valueOf(sc_ProcessWorkCardEntryList.get(position).getFempid()));
        wechatPostByFEmpIDJson.setTemplate_id(define.template_id);


        WechatPostByFEmpIDData wechatPostByFEmpIDData = new WechatPostByFEmpIDData();
        wechatPostByFEmpIDData.setFirst("您有一条新的工单汇报信息");
        wechatPostByFEmpIDData.setKeyword1("工厂型体:" + processWorkCardPlanEntryList.get(position).getPlantbody() + " " + processWorkCardPlanEntryList.get(0).getProcessname());
        wechatPostByFEmpIDData.setKeyword2("汇报数量:" + sc_ProcessWorkCardEntryList.get(position).getReportNumber());

        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String dd = dff.format(new Date());

        wechatPostByFEmpIDData.setRemark("汇报时间:" + dd);
        wechatPostByFEmpIDJson.getData().add(wechatPostByFEmpIDData);

        PulishData pulishData = new PulishData();
        pulishData.setWechatPostByFEmpIDData(wechatPostByFEmpIDJson);
        Gson g = new Gson();
        params.setAsJsonContent(true);
        params.setBodyContent(g.toJson(pulishData));
        //Log.e("jindi",g.toJson(pulishData));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("jindi", result);
                if (result.contains("200")) {

                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("jindi", ex.getMessage());
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
