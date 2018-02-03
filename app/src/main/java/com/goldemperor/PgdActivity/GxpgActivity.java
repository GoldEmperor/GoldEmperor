package com.goldemperor.PgdActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.goldemperor.MainActivity.GsonFactory;
import com.goldemperor.MainActivity.ListViewDecoration;
import com.goldemperor.MainActivity.LogToFile;
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
import org.xutils.db.converter.FloatColumnConverter;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

    private TextView tv_todayTarget;

    TextView tv_reportednumber;

    TextView top_noReportednumber;

    private TextView top_record;

    private TextView top_norecord;

    private TextView tv_weiwai;

    public static int norecord;

    private FancyButton btn_submit;

    private FancyButton btn_namelist;

    private FancyButton btn_instructor;

    private FancyButton btn_dispatchingnumberPublish;

    private FancyButton btn_reportPublish;

    private FancyButton btn_reportCheck;

    private FancyButton btn_report;

    private FancyButton btn_again;

    private FancyButton btn_assign;

    private FancyButton btn_operation;

    private FancyButton btn_PlanMaterial;

    public FancyButton btn_saveOperation;
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

    private int recordCount = 0;//计工数

    private int thisRecordCount = 0;//本次计工数

    private int reportCount = 0;//汇报数

    private int Havedispatchingnumber = 0;
    private List<Float> reportNumberList = new ArrayList<Float>();

    public static int mScrollX = 0;


    //已计工数
    //public static HashMap<String, Float> readyRecordCount = new HashMap<>();
    //List<cj_processoutputentry> OutputList;

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
        tv_todayTarget = (TextView) findViewById(R.id.tv_todayTarget);

        top_record = (TextView) findViewById(R.id.top_record);

        tv_weiwai = (TextView) findViewById(R.id.tv_weiwai);

        if (selectWorkCardPlan.getCumulativenumber() != null) {
            top_record.setText(String.valueOf("累计计工数:" + selectWorkCardPlan.getCumulativenumber().intValue()));
            recordCount = selectWorkCardPlan.getCumulativenumber().intValue();
        }


        Log.e("jindi", "recordCount:" + recordCount);
        //取出姓名和员工工号数据
        top_norecord = (TextView) findViewById(R.id.top_norecord);

        if (selectWorkCardPlan.getFcanreportbynostockin()) {
            tv_weiwai.setText("是否委外:是");
            top_record.setVisibility(View.GONE);
            top_norecord.setVisibility(View.GONE);
        } else {
            tv_weiwai.setVisibility(View.GONE);
        }

        nameList = new String[PgdActivity.nameListResult.getData().size()][4];

        for (int i = 0; i < PgdActivity.nameListResult.getData().size(); i++) {
            nameList[i][0] = PgdActivity.nameListResult.getData().get(i).getEmp_Name();
            nameList[i][1] = PgdActivity.nameListResult.getData().get(i).getEmp_Code();
            nameList[i][2] = PgdActivity.nameListResult.getData().get(i).getEmp_ID();
            nameList[i][3] = PgdActivity.nameListResult.getData().get(i).getEmp_Departid();
        }

        for (int i = 0; i < 200; i++) {
            reportNumberList.add(0f);
        }
        //初始化数据
        processWorkCardPlanEntryList = new ArrayList<ProcessWorkCardPlanEntry>();

        sc_ProcessWorkCardEntryList = new ArrayList<Sc_ProcessWorkCardEntry>();


        Intent intent = getIntent();
        finterid = intent.getIntExtra("finterid", 0);

        getData(finterid,true);


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
                btn_instructor.setEnabled(false);
                GetInstructor();
            }
        });
        btn_dispatchingnumberPublish = (FancyButton) findViewById(R.id.btn_dispatchingnumberPublish);

        btn_dispatchingnumberPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<GxpgPlanStatus> gxpgPlanStatusesList = dbManager.selector(GxpgPlanStatus.class).where("planbill", " = ", selectWorkCardPlan.getPlanbill()).and("orderbill", "=", selectWorkCardPlan.getOrderbill()).findAll();

                    // if (gxpgPlanStatusesList == null && gxpgPlanStatusesList.size() < 1) {
                    // Toast.makeText(mContext, "请先保存预排", Toast.LENGTH_LONG).show();
                    // } else {
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

                                                if (sc_ProcessWorkCardEntryList.get(i).getFqty().intValue() > 0) {
                                                    dispatchingnumberPublish(i);
                                                }

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
                    // }
                } catch (DbException e) {
                    e.printStackTrace();
                }


            }
        });

        btn_reportPublish = (FancyButton) findViewById(R.id.btn_reportPublish);

        btn_reportPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(act);
                normalDialog.setTitle("提示");
                normalDialog.setMessage("你确定要发布员工校对信息?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
                                    if (sc_ProcessWorkCardEntryList.get(i).getReportNumber() > 0 && sc_ProcessWorkCardEntryList.get(i).getIsOpen()) {
                                        reportPublish(i);
                                    }
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
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(act);
                normalDialog.setTitle("提示");
                normalDialog.setMessage("你确定要对工序计工?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                btn_report.setEnabled(false);
                                saveData();
                                //SCProcessWorkCard2SCProcessOutPutBysuitID();
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

        btn_again = (FancyButton) findViewById(R.id.btn_again);

        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "工序汇报校正中...", Toast.LENGTH_LONG).show();
                againSCProcessOutPutReWriteBysuitID();
            }
        });

        btn_PlanMaterial = (FancyButton) findViewById(R.id.btn_PlanMaterial);

        btn_PlanMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MaterialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("finterid", selectWorkCardPlan.getFinterid());
                intent.putExtras(bundle);
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

        btn_reportCheck = (FancyButton) findViewById(R.id.btn_reportCheck);
        btn_reportCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                for (int i = 0; i < gxpgActivity.sc_ProcessWorkCardEntryList.size(); i++) {
                    float count = 0;
                    int OutputCount = 0;
                    for (int j = 0; j < gxpgActivity.sc_ProcessWorkCardEntryList.size(); j++) {
                        if (gxpgActivity.processWorkCardPlanEntryList.get(i).getProcessname().equals(gxpgActivity.processWorkCardPlanEntryList.get(j).getProcessname())) {
                            count += gxpgActivity.sc_ProcessWorkCardEntryList.get(j).getReportNumber();
                            OutputCount += (int) gxpgActivity.sc_ProcessWorkCardEntryList.get(j).getReportedqty();
                        }
                    }

                    /*
                    int OutputCount = 0;
                    if (OutputList != null) {
                        for (int j = 0; j < OutputList.size(); j++) {
                            if (gxpgActivity.processWorkCardPlanEntryList.get(i).getProcesscode().equals(OutputList.get(j).getFprocessnumber())) {
                                OutputCount += OutputList.get(j).getFqty().intValue();
                            }
                        }
                    }
                   */

                    if (!selectWorkCardPlan.getFcanreportbynostockin()) {
                        if (count > gxpgActivity.norecord && !message.contains(gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessname())) {
                            message += "【" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessnumber() + "】" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessname() + ",超过汇报数:" + (gxpgActivity.norecord - count) + "\n";
                        } else if (count < gxpgActivity.norecord && !message.contains(gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessname())) {
                            message += "【" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessnumber() + "】" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessname() + ",少于汇报数:" + (gxpgActivity.norecord - count) + "\n";
                        }
                        //else if (count + OutputCount > Havedispatchingnumber && !message.contains(gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessname())) {
                           // message += "【" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessnumber() + "】" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessname() + ",超过总数:" + (Havedispatchingnumber - count - OutputCount) + "\n";
                        //}
                    } else {
                        if (count + OutputCount > Havedispatchingnumber && !message.contains(gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessname())) {
                            message += "【" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessnumber() + "】" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessname() + ",超过总数:" + (Havedispatchingnumber - count - OutputCount) + "\n";
                        }
                    }
                }


                if (message.length() > 2) {
                    new AlertDialog.Builder(act)
                            .setTitle("计工数不符")
                            .setMessage(message)
                            .setPositiveButton("确定", null)
                            .setNegativeButton("取消", null)
                            .show();
                } else {
                    new AlertDialog.Builder(act)
                            .setTitle("计工数正常")
                            .setPositiveButton("确定", null)
                            .setNegativeButton("取消", null)
                            .show();
                }
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
                    } else if (gxpgPlanStatusesList == null || gxpgPlanStatusesList.size() < 1) {
                        Toast.makeText(mContext, "请先保存预排", Toast.LENGTH_LONG).show();
                    } else {
                        for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
                            GxpgPlan gxpgPlan = dbManager.selector(GxpgPlan.class).where("style", " = ", selectWorkCardPlan.getPlantbody()).and("processname", "=", gxpgActivity.processWorkCardPlanEntryList.get(i).getProcessname()).and("username", "=", gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getName()).findFirst();
                            if (gxpgPlan != null) {
                                sc_ProcessWorkCardEntryList.get(i).setReportNumber((float) Math.floor(norecord * gxpgPlan.getPer()));
                                Log.e("jindi", "" + sc_ProcessWorkCardEntryList.get(i).getReportNumber());
                                mMenuAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }


            }
        });
        btn_saveOperation = (FancyButton) findViewById(R.id.btn_saveOperation);

        btn_saveOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGxpgPlanReportNumber(true);
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
        GetDayWorkCardPlanQtyBysuitID();
        Log.e("jindi", selectWorkCardPlan.getFinterid().toString());
    }

    public void getData(int finterid,final boolean useGxpgPlanReportNumberSave) {
        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setText("数据载入中...");
        processWorkCardPlanEntryList.clear();
        sc_ProcessWorkCardEntryList.clear();
        RequestParams params = new RequestParams(define.Net2 + define.GetProcessWorkCardInfo);
        params.setReadTimeout(60000);
        params.addQueryStringParameter("FInterID", String.valueOf(finterid));
        Log.e("jindi", params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //Utils.e("jindi", "getData"+result);
                result = "{\"data\":" + result.substring(result.indexOf("[{"), result.indexOf("}]")) + "}]}";
                Utils.e("jindi", "getData" + result);

                Gson g = new Gson();
                GxpgResult gxpgs = g.fromJson(result, GxpgResult.class);
                if (gxpgs != null && gxpgs.getData() != null) {

                    tv_group.setText("组别:" + selectWorkCardPlan.getFgroup());
                    tv_number.setText("派工单号:" + gxpgs.getData().get(0).getProcessbillnumber().toUpperCase());
                    tv_planbill.setText("计划跟踪号:" + gxpgs.getData().get(0).getPlanbill().toUpperCase());
                    tv_style.setText("工厂型体:" + gxpgs.getData().get(0).getPlantbody().toUpperCase());
                    top_havedispatchingnumber.setText("应派工数:" + (gxpgs.getData().get(0).getHavedispatchingnumber().intValue()));
                    Havedispatchingnumber = gxpgs.getData().get(0).getHavedispatchingnumber().intValue();
                    for (int i = 0; i < gxpgs.getData().size(); i++) {
                        processWorkCardPlanEntryList.add(gxpgs.getData().get(i));
                        addSc_ProcessWorkCardEntry(gxpgs.getData().get(i));
                    }
                    /*
                    for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
                        sc_ProcessWorkCardEntryList.get(i).setReportNumber(reportNumberList.get(i));
                    }
                    */
                    //自动应用计划工序
                    try {
                        List<GxpgPlanStatus> gxpgPlanStatusList = dbManager.selector(GxpgPlanStatus.class).where("planbill", " = ", selectWorkCardPlan.getPlanbill()).and("orderbill", "=", selectWorkCardPlan.getOrderbill()).findAll();

                        if (gxpgPlanStatusList == null || gxpgPlanStatusList.size() <= 0) {
                            useGxpgPlan();
                            Log.e("jindi", "应用计划工序");
                        } else {
                            Log.e("jindi", "工序已提交过");
                        }
                        //应用暂存数据
                        if (useGxpgPlanReportNumberSave) {
                            useGxpgPlanReportNumberSave();
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                    tv_tip.setVisibility(View.GONE);
                    btn_report.setEnabled(true);
                    GetSCProcessOutPutQtyBysuitID();
                    //getCjProcessOutputEntry();
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
                tv_tip.setText("数据载入失败1:" + ex.toString());
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

    //计工前再保存一次
    public void saveGetData(int finterid) {
        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setText("数据载入中...");
        processWorkCardPlanEntryList.clear();
        sc_ProcessWorkCardEntryList.clear();
        RequestParams params = new RequestParams(define.Net2 + define.GetProcessWorkCardInfo);
        params.setReadTimeout(60000);
        params.addQueryStringParameter("FInterID", String.valueOf(finterid));
        Log.e("jindi", params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                result = "{\"data\":" + result.substring(result.indexOf("[{"), result.indexOf("}]")) + "}]}";

                //Utils.e("jindi", result);
                Gson g = new Gson();
                GxpgResult gxpgs = g.fromJson(result, GxpgResult.class);
                if (gxpgs != null && gxpgs.getData() != null) {

                    tv_group.setText("组别:" + selectWorkCardPlan.getFgroup());
                    tv_number.setText("派工单号:" + gxpgs.getData().get(0).getProcessbillnumber().toUpperCase());
                    tv_planbill.setText("计划跟踪号:" + gxpgs.getData().get(0).getPlanbill().toUpperCase());
                    tv_style.setText("工厂型体:" + gxpgs.getData().get(0).getPlantbody().toUpperCase());
                    top_havedispatchingnumber.setText("应派工数:" + (gxpgs.getData().get(0).getHavedispatchingnumber().intValue()));


                    Havedispatchingnumber = gxpgs.getData().get(0).getHavedispatchingnumber().intValue();

                    for (int i = 0; i < gxpgs.getData().size(); i++) {
                        processWorkCardPlanEntryList.add(gxpgs.getData().get(i));
                        addSc_ProcessWorkCardEntry(gxpgs.getData().get(i));
                    }
                    for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
                        sc_ProcessWorkCardEntryList.get(i).setReportNumber(reportNumberList.get(i));
                    }
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
                    SCProcessWorkCard2SCProcessOutPutBysuitID();
                    tv_tip.setVisibility(View.GONE);

                    GetSCProcessOutPutQtyBysuitID();
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
                tv_tip.setText("数据载入失败2:" + ex.toString());
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


    public void DeleteProcessWorkCardEntryByFID(long fid) {
        RequestParams params = new RequestParams(define.Net2 + define.DeleteProcessWorkCardEntryByFID);
        params.addQueryStringParameter("FID", String.valueOf(fid));
        Log.e("jindi","DeleteProcessWorkCardEntryByFID:"+params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("jindi",result);
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                btn_instructor.setEnabled(true);
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

        top_record.setText("累计计工数:" + String.valueOf(recordCount));
        top_noReportednumber.setText("汇报欠数:" + (Havedispatchingnumber - reportCount));
        //top_noReportednumber.setText("汇报欠数:" + String.valueOf(processWorkCardPlanEntryList.get(0).getHavedispatchingnumber().intValue() - reportCount));
        norecord = reportCount - recordCount;
        top_norecord.setText("已汇报未计工数:" + String.valueOf(norecord));
        //Log.e("jindi","reportCount:"+reportCount+" norecord"+norecord);
        //mMenuAdapter.notifyDataSetChanged();
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
        mScrollX = scrollX;
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
        sc_processWorkCardEntry.setHaveSave(true);
        sc_processWorkCardEntry.setReportedqty(processWorkCardPlanEntry.getReportedqty());

        sc_processWorkCardEntry.setFprocessflowid(processWorkCardPlanEntry.getFprocessflowid());

        sc_processWorkCardEntry.setFprocessingmethod(processWorkCardPlanEntry.getFprocessingmethod());

        sc_processWorkCardEntry.setFoperplanninginterid(processWorkCardPlanEntry.getFoperplanninginterid());
        //Log.e("jindi","Foperplanninginterid:"+processWorkCardPlanEntry.getFoperplanninginterid());
        sc_processWorkCardEntry.setFoperplanningentryid(processWorkCardPlanEntry.getFoperplanningentryid());

        //Log.e("jindi","Foperplanninginterid:"+processWorkCardPlanEntry.getFoperplanninginterid());
        sc_processWorkCardEntry.setFsourceentryfid(processWorkCardPlanEntry.getFsourceentryfid());
        //Log.e("jindi","sourceentryfid:"+processWorkCardPlanEntry.getFsourceentryfid());
        sc_processWorkCardEntry.setFrouteentryfid(processWorkCardPlanEntry.getFrouteentryfid());

        //Log.e("jindi","Frouteentryfid:"+processWorkCardPlanEntry.getFrouteentryfid());
        sc_processWorkCardEntry.setFoperplanningentryfid(processWorkCardPlanEntry.getFoperplanningentryfid());
        //Log.e("jindi","Foperplanningentryfid:"+processWorkCardPlanEntry.getFoperplanningentryfid());

        sc_processWorkCardEntry.setFid(processWorkCardPlanEntry.getFid());
        try {
            List<GxpgPlanStatus> gxpgPlanStatusesList = dbManager.selector(GxpgPlanStatus.class).where("planbill", " = ", selectWorkCardPlan.getPlanbill()).and("orderbill", "=", selectWorkCardPlan.getOrderbill()).findAll();
            if ((sc_processWorkCardEntry.getFqty() == null || sc_processWorkCardEntry.getFqty().intValue() == 0) && (gxpgPlanStatusesList == null || gxpgPlanStatusesList.size() < 1)) {
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

        try {
            GxpgSwitch gxpgSwitch = dbManager.selector(GxpgSwitch.class).where("style", " = ", processWorkCardPlanEntry.getPlantbody()).and("processname", "=", processWorkCardPlanEntry.getProcessname()).findFirst();
            if (gxpgSwitch != null && !gxpgSwitch.getOpen()) {
                sc_processWorkCardEntry.setIsOpen(false);
                //sc_processWorkCardEntry.setFqty(new BigDecimal(df.format(processWorkCardPlanEntry.getHavedispatchingnumber().floatValue() - selectWorkCardPlan.getReportednumber().floatValue() * gxpgPlan.getPer())));
                //Log.e("jindi","per:"+gxpgPlan.getPer());
            } else if (gxpgSwitch != null && gxpgSwitch.getOpen()) {
                sc_processWorkCardEntry.setIsOpen(true);
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

    public void useGxpgPlanReportNumberSave() {
        for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
            try {
                GxpgPlanReportNumberSave gxpgPlanReportNumberSave = dbManager.selector(GxpgPlanReportNumberSave.class).where("planbill", " = ", selectWorkCardPlan.getPlanbill()).and("orderbill", "=", selectWorkCardPlan.getOrderbill()).and("usernumber", "=", sc_ProcessWorkCardEntryList.get(i).getJobNumber()).and("processname", "=", sc_ProcessWorkCardEntryList.get(i).getFprocessname()).findFirst();
                if (gxpgPlanReportNumberSave != null) {
                    sc_ProcessWorkCardEntryList.get(i).setReportNumber(gxpgPlanReportNumberSave.getRecordcount());
                } else {
                    //Log.e("jindi","Recordcount:null");
                }
            } catch (DbException e) {
                e.printStackTrace();
                Log.e("jindi", e.toString());
            }

        }
        mMenuAdapter.notifyDataSetChanged();
    }

    public void GetInstructor() {
        RequestParams params = new RequestParams(define.Net1 + define.GetRouteEntryPic);
        params.addQueryStringParameter("FInterID", String.valueOf(selectWorkCardPlan.getFroutingid()));
        params.addQueryStringParameter("FPlanBill", String.valueOf(selectWorkCardPlan.getPlanbill()));
        //Log.e("jindi", params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Gson g = new Gson();
                GYLXResult af = g.fromJson(result, GYLXResult.class);
                //String report = result.substring(result.indexOf("ReturnMsg"), result.indexOf(",")).replace("ReturnMsg\":", "").replace("\"", "");
                //reportCount = (new BigDecimal(report)).intValue();
                //tv_reportednumber.setText(String.valueOf("已汇报数:" + reportCount));
                //Log.e("jindi", af.getData().get(0).getFfullname());
                try {
                    if (af.getData().get(0).getFfullname() != null) {
                        String url = af.getData().get(0).getFfullname();
                        String downloadFileName = url.substring(url.lastIndexOf("/") + 1, url.length());
                        Log.e("jindi", downloadFileName);
                        downloadInstructor(define.Net4 + URLEncoder.encode(url, "UTF-8"), downloadFileName);
                    }
                } catch (Exception e) {
                    Log.e("jindi", e.toString());
                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                btn_instructor.setEnabled(true);
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

    public void downloadInstructor(final String url, final String downloadFileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                download(url, downloadFileName);
            }
        }).start();
    }

    //下载具体操作
    private void download(String downloadUrl, final String downloadFileName) {
        try {
            URL url = new URL(downloadUrl);
            //打开连接
            URLConnection conn = url.openConnection();
            //打开输入流
            InputStream is = conn.getInputStream();
            //获得长度
            int contentLength = conn.getContentLength();
            Log.e("jindi", "contentLength = " + contentLength);
            //创建文件夹 MyDownLoad，在存储卡下
            String dirName = Environment.getExternalStorageDirectory() + "/Instructor/";
            File file = new File(dirName);
            //不存在创建
            if (!file.exists()) {
                file.mkdir();
            }
            //下载后的文件名
            String fileName = dirName + downloadFileName;
            Log.e("jindi", fileName.toString());
            File file1 = new File(fileName);
            if (file1.exists()) {
                file1.delete();
            }
            //创建字节流
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(fileName);
            //写数据
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            //完成后关闭流
            Log.e("jindi", "download-finish");
            os.close();
            is.close();
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_instructor.setEnabled(true);
                }
            });
            Uri path = Uri.fromFile(file1);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,
                                "没有安装查看word/pdf软件",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Log.e("jindi", e.toString());
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "找不到文件", Toast.LENGTH_SHORT).show();
                    btn_instructor.setEnabled(true);
                }
            });
        }
    }

    public void GetSCProcessOutPutQtyBysuitID() {
        RequestParams params = new RequestParams(define.Net2 + define.GetSCProcessOutPutQtyBysuitID);
        params.addQueryStringParameter("FSourceInterId", String.valueOf(selectWorkCardPlan.getFinterid()));
        params.addQueryStringParameter("suitID", define.suitID);
        Log.e("jindi", params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String report = result.substring(result.indexOf("ReturnMsg"), result.indexOf(",")).replace("ReturnMsg\":", "").replace("\"", "");
                reportCount = (new BigDecimal(report)).intValue();
                tv_reportednumber.setText(String.valueOf("已汇报数:" + reportCount));
                //Log.e("jindi", "reportCount:" + reportCount);
                refreshData();
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

    public void submitData() {
        //保存最近工序计划
        saveGxpgPlan();

        Gson g = new Gson();
        RequestParams params = new RequestParams(define.Net2 + define.InsertProcessWorkCardEntry);

        //RequestParams params = new RequestParams(define.IP798056 + define.InsertProcessWorkCardEntry2);

        for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
            sc_ProcessWorkCardEntryList.get(i).setFentryid(i);
        }
        //params.setAsJsonContent(true);
        //params.setBodyContent(g.toJson(sc_ProcessWorkCardEntryList));

        List<Sc_ProcessWorkCardEntryNative> sc_ProcessWorkCardEntryListNative=GsonFactory.jsonToArrayList(g.toJson(sc_ProcessWorkCardEntryList),Sc_ProcessWorkCardEntryNative.class);
        Utils.e("jindi",g.toJson(sc_ProcessWorkCardEntryListNative));
        params.addParameter("PushJsonCondition", g.toJson(sc_ProcessWorkCardEntryListNative));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                //重新设置数据
                Log.e("jindi", result);
                if (result.contains("OK")||result.contains("success")) {
                    Alerter.create(act)
                            .setTitle("提示")
                            .setText("提交成功")
                            .setBackgroundColorRes(R.color.colorAlert)
                            .show();
                    //提交成功后反写
                    ReWrite();
                    for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
                        reportNumberList.set(i, sc_ProcessWorkCardEntryList.get(i).getReportNumber());
                    }
                    //提交成功后刷新
                    getData(finterid,true);
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
                Log.e("jindi", "saveData:" + ex.getMessage());
                tv_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("提交失败:工艺路线已做变更，请返回删除后重新下推");
                Alerter.create(act)
                        .setTitle("提示")
                        .setText("提交失败:工艺路线已做变更，请返回删除后重新下推")
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

    public void saveData() {
        //保存最近工序计划
        saveGxpgPlan();

        Gson g = new Gson();
        RequestParams params = new RequestParams(define.Net2 + define.InsertProcessWorkCardEntry);

        //RequestParams params = new RequestParams(define.IP798056 + define.InsertProcessWorkCardEntry2);

        for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
            sc_ProcessWorkCardEntryList.get(i).setFentryid(i);
        }
        //params.setAsJsonContent(true);
        //params.setBodyContent(g.toJson(sc_ProcessWorkCardEntryList));

        List<Sc_ProcessWorkCardEntryNative> sc_ProcessWorkCardEntryListNative=GsonFactory.jsonToArrayList(g.toJson(sc_ProcessWorkCardEntryList),Sc_ProcessWorkCardEntryNative.class);
        Utils.e("jindi",g.toJson(sc_ProcessWorkCardEntryListNative));
        params.addParameter("PushJsonCondition", g.toJson(sc_ProcessWorkCardEntryListNative));

        //params.addParameter("PushJsonCondition", g.toJson(sc_ProcessWorkCardEntryList));
        // params.addParameter("FDetpmentID", dataPref.getString(define.SharedFDeptmentid, "0"));
        // params.addParameter("Type", "insert");
        // params.addParameter("suitID", define.suitID);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                //重新设置数据
                Log.e("jindi", "saveData:" + result);
                if (result.contains("OK")||result.contains("success")) {
                    //提交成功后反写
                    saveReWrite();
                    for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
                        reportNumberList.set(i, sc_ProcessWorkCardEntryList.get(i).getReportNumber());
                    }
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
                Log.e("jindi", "saveData:" + ex.getMessage());
                tv_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("提交失败:工艺路线已做变更，请返回删除后重新下推");
                Alerter.create(act)
                        .setTitle("提示")
                        .setText("提交失败:工艺路线已做变更，请返回删除后重新下推")
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

    public void saveReWrite() {
        List<PushJsonCondition> pushJsonConditionList = new ArrayList<PushJsonCondition>();
        for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
            PushJsonCondition pushJsonCondition = new PushJsonCondition();
            pushJsonCondition.setFEntryID(String.valueOf(sc_ProcessWorkCardEntryList.get(i).getFentryid()));
            pushJsonCondition.setFInterID(String.valueOf(sc_ProcessWorkCardEntryList.get(i).getFinterid()));
            pushJsonCondition.setFSize(String.valueOf(sc_ProcessWorkCardEntryList.get(i).getFsize()));
            pushJsonConditionList.add(pushJsonCondition);
        }

        Gson g = new Gson();
        RequestParams params = new RequestParams(define.Net2 + define.SCProcessWorkCardReWriteBysuitID);
        params.addBodyParameter("PushJsonCondition", g.toJson(pushJsonConditionList));
        params.addBodyParameter("suitID", define.suitID);

        RequestParams params2 = new RequestParams(define.Net2 + define.SCProcessWorkCardReWriteBysuitID);
        params2.addBodyParameter("PushJsonCondition", g.toJson(pushJsonConditionList));
        params2.addBodyParameter("suitID", define.suitID);
        Log.e("jindi", params2.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                //重新设置数据
                Log.e("jindi", "saveReWrite:" + result);
                if (result.contains("success")) {
                    saveGetData(finterid);
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
        RequestParams params = new RequestParams(define.Net2 + define.SCProcessWorkCardReWriteBysuitID);
        params.addBodyParameter("PushJsonCondition", g.toJson(pushJsonConditionList));
        params.addBodyParameter("suitID", define.suitID);
        //Log.e("jindi",params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                //重新设置数据
                Log.e("jindi", result);
                if (result.contains("success")) {

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
            sc_ProcessWorkCardEntryList.get(i).setHaveSave(true);
            GxpgPlan gxpgPlan = new GxpgPlan();
            gxpgPlan.setStyle(processWorkCardPlanEntryList.get(i).getPlantbody());
            gxpgPlan.setProcessname(sc_ProcessWorkCardEntryList.get(i).getFprocessname());
            gxpgPlan.setUsername(sc_ProcessWorkCardEntryList.get(i).getName());
            gxpgPlan.setUsernumber(sc_ProcessWorkCardEntryList.get(i).getJobNumber());
            gxpgPlan.setEmpid(sc_ProcessWorkCardEntryList.get(i).getFempid());
            //gxpgPlan.setPer(1.0f);
            gxpgPlan.setPer(sc_ProcessWorkCardEntryList.get(i).getFqty().floatValue() / (processWorkCardPlanEntryList.get(i).getHavedispatchingnumber().floatValue()));


            //Log.e("jindi", String.valueOf(processWorkCardPlanEntryList.get(i).getHavedispatchingnumber().floatValue() - selectWorkCardPlan.getReportednumber().floatValue()));
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
            Log.e("jindi", e.toString());
        }
    }


    //暂存运算计工数
    public void saveGxpgPlanReportNumber(boolean showTip) {

        //存储工序计划
        ArrayList<GxpgPlanReportNumberSave> GxpgPlanReportNumberSaveList = new ArrayList<>();
        for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
            //先删除原先保留的工序计划
            try {
                WhereBuilder b = WhereBuilder.b();
                b.and("planbill", " = ", selectWorkCardPlan.getPlanbill());
                b.and("orderbill", "=", selectWorkCardPlan.getOrderbill());
                b.and("processname", "=", sc_ProcessWorkCardEntryList.get(i).getFprocessname());
                b.and("usernumber", "=", sc_ProcessWorkCardEntryList.get(i).getJobNumber());
                dbManager.delete(GxpgPlanReportNumberSave.class, b);
            } catch (DbException e) {
                e.printStackTrace();
            }
            GxpgPlanReportNumberSave gxpgPlanReportNumberSave = new GxpgPlanReportNumberSave();
            gxpgPlanReportNumberSave.setOrderbill(selectWorkCardPlan.getOrderbill());
            gxpgPlanReportNumberSave.setPlanbill(selectWorkCardPlan.getPlanbill());
            gxpgPlanReportNumberSave.setProcessname(sc_ProcessWorkCardEntryList.get(i).getFprocessname());
            gxpgPlanReportNumberSave.setUsername(sc_ProcessWorkCardEntryList.get(i).getName());
            gxpgPlanReportNumberSave.setUsernumber(sc_ProcessWorkCardEntryList.get(i).getJobNumber());
            gxpgPlanReportNumberSave.setRecordcount(sc_ProcessWorkCardEntryList.get(i).getReportNumber());
            Log.e("jindi", selectWorkCardPlan.getOrderbill() + "," + selectWorkCardPlan.getPlanbill() + "," + sc_ProcessWorkCardEntryList.get(i).getFprocessname() + "," + sc_ProcessWorkCardEntryList.get(i).getJobNumber());
            //Log.e("jindi", String.valueOf(processWorkCardPlanEntryList.get(i).getHavedispatchingnumber().floatValue() - selectWorkCardPlan.getReportednumber().floatValue()));
            //Log.e("jindi", "getFqty:"+sc_ProcessWorkCardEntryList.get(i).getFqty().floatValue()+" getHavedispatchingnumber()"+processWorkCardPlanEntryList.get(i).getHavedispatchingnumber().floatValue());

            //Log.e("jindi", "stype:" + processWorkCardPlanEntryList.get(i).getPlantbody() + "processname:" + sc_ProcessWorkCardEntryList.get(i).getFprocessname() + ",processname:" + sc_ProcessWorkCardEntryList.get(i).getName() +
            // ",Qty:" + sc_ProcessWorkCardEntryList.get(i).getFqty());
            GxpgPlanReportNumberSaveList.add(gxpgPlanReportNumberSave);
        }
        try {
            dbManager.save(GxpgPlanReportNumberSaveList);
            if(showTip) {
                Toast.makeText(mContext, "计工暂存成功", Toast.LENGTH_LONG).show();
            }
        } catch (DbException e) {
            e.printStackTrace();
            Log.e("jindi", e.toString());
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


                SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_purple)
                        .setImage(R.mipmap.ic_action_close)
                        .setText("关闭") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(closeItem);// 添加一个按钮到右侧侧菜单。

                SwipeMenuItem openItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_green)
                        .setImage(R.mipmap.ic_action_add)
                        .setText("开启") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(openItem);// 添加一个按钮到右侧侧菜单。

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

                if (sc_ProcessWorkCardEntryList.get(adapterPosition).getIsOpen()) {
                    ProcessWorkCardPlanEntry processWorkCardPlanEntry = (ProcessWorkCardPlanEntry) processWorkCardPlanEntryList.get(adapterPosition).clone();
                    processWorkCardPlanEntry.setFid(0L);
                    processWorkCardPlanEntryList.add(adapterPosition + 1, processWorkCardPlanEntry);

                    Sc_ProcessWorkCardEntry sc_processWorkCardEntry = (Sc_ProcessWorkCardEntry) sc_ProcessWorkCardEntryList.get(adapterPosition).clone();
                    sc_processWorkCardEntry.setFentryid(sc_processWorkCardEntry.getFentryid() + 1);
                    //插入工序以后的所有entryid都要加1
                    for (int i = adapterPosition + 1; i < sc_ProcessWorkCardEntryList.size(); i++) {
                        sc_ProcessWorkCardEntryList.get(i).setFentryid(sc_ProcessWorkCardEntryList.get(i).getFentryid() + 1);
                    }
                    sc_processWorkCardEntry.setFid(0L);
                    sc_processWorkCardEntry.setFqty(new BigDecimal(0));
                    sc_processWorkCardEntry.setFfinishqty(new BigDecimal(0));
                    sc_processWorkCardEntry.setReportNumber(0f);
                    sc_processWorkCardEntry.setHaveSave(false);
                    sc_ProcessWorkCardEntryList.add(adapterPosition + 1, sc_processWorkCardEntry);

                    mMenuAdapter.notifyItemInserted(adapterPosition + 1);
                } else {
                    Toast.makeText(mContext, "关闭工序无法复制", Toast.LENGTH_LONG).show();
                }
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
                                    //if(sc_ProcessWorkCardEntryList.get(adapterPosition).getHaveSave()) {
                                    //Toast.makeText(mContext,"本道工序已保存到预排,无法删除",Toast.LENGTH_LONG).show();
                                    //}else{
                                    if(sc_ProcessWorkCardEntryList.get(adapterPosition).getFid()>0){
                                        DeleteProcessWorkCardEntryByFID(sc_ProcessWorkCardEntryList.get(adapterPosition).getFid());
                                    }
                                    processWorkCardPlanEntryList.remove(adapterPosition);
                                    sc_ProcessWorkCardEntryList.remove(adapterPosition);

                                    mMenuAdapter.notifyItemRemoved(adapterPosition);
                                    //}
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
            } else if (menuPosition == 2) {
                sc_ProcessWorkCardEntryList.get(adapterPosition).setIsOpen(false);
                for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
                    if (sc_ProcessWorkCardEntryList.get(i).getFprocessname().equals(sc_ProcessWorkCardEntryList.get(adapterPosition).getFprocessname())) {
                        sc_ProcessWorkCardEntryList.get(i).setIsOpen(false);
                        mMenuAdapter.notifyItemChanged(i);
                    }
                }
                mMenuAdapter.notifyItemChanged(adapterPosition);

                try {
                    WhereBuilder b = WhereBuilder.b();
                    b.and("style", "=", processWorkCardPlanEntryList.get(adapterPosition).getPlantbody());
                    b.and("processname", "=", sc_ProcessWorkCardEntryList.get(adapterPosition).getFprocessname());
                    dbManager.delete(GxpgSwitch.class, b);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                GxpgSwitch gxpgSwitch = new GxpgSwitch();
                gxpgSwitch.setStyle(processWorkCardPlanEntryList.get(adapterPosition).getPlantbody());
                gxpgSwitch.setProcessname(sc_ProcessWorkCardEntryList.get(adapterPosition).getFprocessname());
                gxpgSwitch.setOpen(false);


                try {
                    dbManager.save(gxpgSwitch);
                } catch (DbException e) {
                    e.printStackTrace();
                }

            } else if (menuPosition == 3)

            {
                sc_ProcessWorkCardEntryList.get(adapterPosition).setIsOpen(true);
                for (int i = 0; i < sc_ProcessWorkCardEntryList.size(); i++) {
                    if (sc_ProcessWorkCardEntryList.get(i).getFprocessname().equals(sc_ProcessWorkCardEntryList.get(adapterPosition).getFprocessname())) {
                        sc_ProcessWorkCardEntryList.get(i).setIsOpen(true);
                        mMenuAdapter.notifyItemChanged(i);
                    }
                }
                mMenuAdapter.notifyItemChanged(adapterPosition);

                try {
                    WhereBuilder b = WhereBuilder.b();
                    b.and("style", "=", processWorkCardPlanEntryList.get(adapterPosition).getPlantbody());
                    b.and("processname", "=", sc_ProcessWorkCardEntryList.get(adapterPosition).getFprocessname());
                    dbManager.delete(GxpgSwitch.class, b);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                GxpgSwitch gxpgSwitch = new GxpgSwitch();
                gxpgSwitch.setStyle(processWorkCardPlanEntryList.get(adapterPosition).getPlantbody());
                gxpgSwitch.setProcessname(sc_ProcessWorkCardEntryList.get(adapterPosition).getFprocessname());
                gxpgSwitch.setOpen(true);
                try {
                    dbManager.save(gxpgSwitch);
                } catch (DbException e) {
                    e.printStackTrace();
                }
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
        RequestParams params = new RequestParams(define.Net2 + define.SCProcessWorkCard2SCProcessOutPutBysuitID);
        params.addParameter("PushJsonCondition", g.toJson(pushJsonConditionList));
        params.addParameter("UserID", dataPref.getString(define.SharedUserId, "0"));
        params.addParameter("suitID", define.suitID);

        RequestParams params2 = new RequestParams(define.Net2 + define.SCProcessWorkCard2SCProcessOutPutBysuitID);
        params2.addParameter("PushJsonCondition", g.toJson(pushJsonConditionList));
        params2.addParameter("UserID", dataPref.getString(define.SharedUserId, "0"));
        params2.addParameter("suitID", define.suitID);
        Log.e("jindi", params2.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1).replaceAll("\\\\", "").replaceAll("\"\\[", "\\[").replaceAll("]\"", "]");
                Log.e("jindi", "SCProcessWorkCard2SCProcessOutPutBysuitID:" + result);
                if (result.contains("success")) {
                    Gson g = new Gson();
                    processOutPut = g.fromJson(result, ProcessOutPut.class);

                    for (int i = 0; i < processOutPut.getReturnMsg().size(); i++) {
                        processOutPut.getReturnMsg().get(i).setFworkcardinterid(processOutPut.getReturnMsg().get(i).getFworkcardinterid());
                        processOutPut.getReturnMsg().get(i).setFcheckerid(processOutPut.getReturnMsg().get(i).getFbillerid());
                        processOutPut.getReturnMsg().get(i).setFemp(sc_ProcessWorkCardEntryList.get(i).getName());
                        processOutPut.getReturnMsg().get(i).setFempid(sc_ProcessWorkCardEntryList.get(i).getFempid());
                        processOutPut.getReturnMsg().get(i).setFdeptnumber(String.valueOf(sc_ProcessWorkCardEntryList.get(i).getFdeptmentid()));
                        processOutPut.getReturnMsg().get(i).setFnewworkcardinterid(sc_ProcessWorkCardEntryList.get(i).getFsourceinterid());
                        processOutPut.getReturnMsg().get(i).setFnewworkcardentryid(sc_ProcessWorkCardEntryList.get(i).getFsourceentryid());
                        processOutPut.getReturnMsg().get(i).setFnewworkcardentryfid(sc_ProcessWorkCardEntryList.get(i).getFsourceentryfid());

                        processOutPut.getReturnMsg().get(i).setFprocessflowid(sc_ProcessWorkCardEntryList.get(i).getFprocessflowid());
                        processOutPut.getReturnMsg().get(i).setFprocessingmethod(sc_ProcessWorkCardEntryList.get(i).getFprocessingmethod());

                        processOutPut.getReturnMsg().get(i).setFoperplanninginterid(sc_ProcessWorkCardEntryList.get(i).getFoperplanninginterid());

                        processOutPut.getReturnMsg().get(i).setFoperplanningentryid(sc_ProcessWorkCardEntryList.get(i).getFoperplanningentryid());

                        processOutPut.getReturnMsg().get(i).setFsourceentryfid(sc_ProcessWorkCardEntryList.get(i).getFsourceentryfid());

                        processOutPut.getReturnMsg().get(i).setFoperplanningentryfid(sc_ProcessWorkCardEntryList.get(i).getFoperplanningentryfid());

                        processOutPut.getReturnMsg().get(i).setFrouteentryfid(sc_ProcessWorkCardEntryList.get(i).getFrouteentryfid());


                        Log.e("jindi", "Foperplanninginterid:" + sc_ProcessWorkCardEntryList.get(i).getFoperplanninginterid());

                        Log.e("jindi", "Foperplanningentryid:" + sc_ProcessWorkCardEntryList.get(i).getFoperplanningentryid());

                        Log.e("jindi", "sourceentryfid:" + sc_ProcessWorkCardEntryList.get(i).getFsourceentryfid());
                        Log.e("jindi", "Foperplanningentryfid:" + sc_ProcessWorkCardEntryList.get(i).getFoperplanningentryfid());

                        Log.e("jindi", "Frouteentryfid:" + sc_ProcessWorkCardEntryList.get(i).getFrouteentryfid());

                        //Log.e("jindi","Frouteentryfid:"+sc_ProcessWorkCardEntryList.get(i).getFrouteentryfid());
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
                    boolean surpass = false;
                    for (int i = 0; i < processOutPut.getReturnMsg().size(); i++) {
                        //float per = sc_ProcessWorkCardEntryList.get(i).getFqty().floatValue() / processWorkCardPlanEntryList.get(i).getHavedispatchingnumber().floatValue();

                        //GxpgPlan gxpgPlan = dbManager.selector(GxpgPlan.class).where("style", " = ", selectWorkCardPlan.getPlantbody()).and("processname", "=", gxpgActivity.processWorkCardPlanEntryList.get(i).getProcessname()).and("username", "=", gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getName()).findFirst();
                        float count = 0;
                        int OutputCount = 0;
                        for (int j = 0; j < gxpgActivity.sc_ProcessWorkCardEntryList.size(); j++) {
                            if (gxpgActivity.processWorkCardPlanEntryList.get(j).getProcessname().equals(gxpgActivity.processWorkCardPlanEntryList.get(i).getProcessname())) {
                                count += sc_ProcessWorkCardEntryList.get(j).getReportNumber();
                                OutputCount += (int) sc_ProcessWorkCardEntryList.get(j).getReportedqty();
                            }
                        }

                        /*
                        if (OutputList != null) {
                            for (int j = 0; j < OutputList.size(); j++) {
                                if (gxpgActivity.processWorkCardPlanEntryList.get(i).getProcesscode().equals(OutputList.get(j).getFprocessnumber())) {
                                    OutputCount += OutputList.get(j).getFqty().intValue();
                                }
                            }
                        }
                        */

                        if (!selectWorkCardPlan.getFcanreportbynostockin()) {
                            if (count > maxCount) {
                                maxCount = count;
                            }
                            if (maxCount > Float.valueOf(norecord) + 0.01f) {
                                break;
                            }/* else if (count + OutputCount > Havedispatchingnumber) {
                                Toast.makeText(mContext, "【" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessnumber() + "】" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessname() + ",超过总数:" + (Havedispatchingnumber - count - OutputCount), Toast.LENGTH_LONG).show();
                                surpass = true;
                                break;
                            }*/ else {
                                float qty = sc_ProcessWorkCardEntryList.get(i).getReportNumber();
                                DecimalFormat df = new DecimalFormat("#.0");
                                processOutPut.getReturnMsg().get(i).setFqty(new BigDecimal(df.format(qty)));
                                Log.e("jindi", sc_ProcessWorkCardEntryList.get(i).getFprocessname() + "  qty:" + df.format(qty));
                            }
                        } else {
                            float qty = sc_ProcessWorkCardEntryList.get(i).getReportNumber();
                            DecimalFormat df = new DecimalFormat("#.0");
                            processOutPut.getReturnMsg().get(i).setFqty(new BigDecimal(df.format(qty)));
                            Log.e("jindi", sc_ProcessWorkCardEntryList.get(i).getFprocessname() + "  qty:" + df.format(qty));
                        }
                        Log.e("jindi", "maxCount：" + maxCount);
                    }
                    thisRecordCount = (int) maxCount;
                    Log.e("jindi", "norecord:" + norecord);
                    if (!selectWorkCardPlan.getFcanreportbynostockin()) {
                        if (maxCount > Float.valueOf(norecord) + 0.01f) {
                            Toast.makeText(mContext, "计工数超过汇报数", Toast.LENGTH_LONG).show();
                            btn_report.setEnabled(true);
                        } else if (maxCount <= 0) {
                            Toast.makeText(mContext, "计工数不能小于零", Toast.LENGTH_LONG).show();
                            btn_report.setEnabled(true);
                        } else if (!surpass) {
                            //recordCount = (int) maxCount;
                            Log.e("jindi", "reportCount:" + recordCount);
                            submitProcessOutPut();
                        }
                    } else {
                        String message = "";
                        for (int i = 0; i < gxpgActivity.sc_ProcessWorkCardEntryList.size(); i++) {
                            float count = 0;
                            int OutputCount = 0;
                            for (int j = 0; j < gxpgActivity.sc_ProcessWorkCardEntryList.size(); j++) {
                                if (gxpgActivity.processWorkCardPlanEntryList.get(i).getProcessname().equals(gxpgActivity.processWorkCardPlanEntryList.get(j).getProcessname())) {
                                    count += gxpgActivity.sc_ProcessWorkCardEntryList.get(j).getReportNumber();
                                    OutputCount += (int) sc_ProcessWorkCardEntryList.get(j).getReportedqty();
                                }
                            }

                            /*
                            int OutputCount = 0;
                            if (OutputList != null) {
                                for (int j = 0; j < OutputList.size(); j++) {
                                    if (gxpgActivity.processWorkCardPlanEntryList.get(i).getProcesscode().equals(OutputList.get(j).getFprocessnumber())) {
                                        OutputCount += OutputList.get(j).getFqty().intValue();
                                    }
                                }
                            }
                            */
                            if (count + OutputCount > Havedispatchingnumber && !message.contains(gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessname())) {
                                message += "【" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessnumber() + "】" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessname() + ",超过总数:" + (Havedispatchingnumber - count - OutputCount) + "\n";
                            }
                        }

                        if (message.length() > 2) {
                            btn_report.setEnabled(true);
                            Toast.makeText(mContext, "计工数不能超过总数，请点击计工检查", Toast.LENGTH_LONG).show();
                        } else {
                            submitProcessOutPut();
                        }

                    }

                } else {
                    btn_report.setEnabled(true);
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
                tv_tip.setText("数据载入失败3:" + ex.toString());
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
        RequestParams params = new RequestParams(define.Net2 + define.SCProcessOutPutSaveBysuitID);

        List<Sc_ProcessOutPutEntry> sc = new ArrayList<Sc_ProcessOutPutEntry>();
        for (int i = 0; i < processOutPut.getReturnMsg().size(); i++) {
            if (processOutPut.getReturnMsg().get(i).getFqty().intValue() != 0 && sc_ProcessWorkCardEntryList.get(i).getIsOpen()) {
                sc.add(processOutPut.getReturnMsg().get(i));
            }
        }
        //写入日志
        LogToFile.WordCardId = selectWorkCardPlan.getPlanbill() + " " + selectWorkCardPlan.getOrderbill();
        List<Log_ProcessOutPutEntry> log = GsonFactory.jsonToArrayList(g.toJson(processOutPut.getReturnMsg()), Log_ProcessOutPutEntry.class);
        LogToFile.e("submitProcessOutPut()", g.toJson(log));

        params.addParameter("PushJsonCondition", g.toJson(sc));
        params.addParameter("FDetpmentID", dataPref.getString(define.SharedFDeptmentid, "0"));
        params.addParameter("Type", "insert");
        params.addParameter("suitID", define.suitID);

        RequestParams params2 = new RequestParams(define.Net2 + define.SCProcessOutPutSaveBysuitID);
        params2.addParameter("PushJsonCondition", g.toJson(sc));
        params2.addParameter("FDetpmentID", dataPref.getString(define.SharedFDeptmentid, "0"));
        params2.addParameter("Type", "insert");
        params2.addParameter("suitID", define.suitID);
        String logs = params2.toString();
        Utils.e("jindi", logs);
        //Log.e("jindi", params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("jindi", "submitProcessOutPut:" + result);
                if (result.contains("success")) {
                    Toast.makeText(mContext, "工序汇报录入中...", Toast.LENGTH_LONG).show();
                    //
                    SCProcessOutPutReWriteBysuitID();
                } else {
                    String ReturnMsg = result.substring(result.indexOf("ReturnMsg"), result.indexOf(",")).replace("ReturnMsg\":", "").replace("\"", "");
                    Toast.makeText(mContext, ReturnMsg, Toast.LENGTH_LONG).show();
                    btn_report.setEnabled(true);
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

    private void GetDayWorkCardPlanQtyBysuitID() {
        RequestParams params = new RequestParams(define.Net2 + define.GetDayWorkCardPlanQtyBysuitID);
        params.addParameter("FDeptID", dataPref.getString(define.SharedFDeptmentid, "0"));
        params.addParameter("FDate", Utils.getCurrentTime());
        params.addParameter("suitID", define.suitID);
        //String log=params.toString();
        //Utils.e("jindi",log);
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
                if (result.contains("success")) {
                    String todayTarget = result.substring(result.indexOf("ReturnMsg"), result.indexOf(",")).replace("ReturnMsg\":", "").replace("\"", "");
                    tv_todayTarget.setText("今日目标:" + todayTarget);
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
        RequestParams params = new RequestParams(define.Net2 + define.SCProcessOutPutReWriteBysuitID);
        params.addParameter("PushJsonCondition", g.toJson(pushJsonConditionList));
        params.addParameter("suitID", define.suitID);

        RequestParams params2 = new RequestParams(define.Net2 + define.SCProcessOutPutReWriteBysuitID);
        params2.addParameter("PushJsonCondition", g.toJson(pushJsonConditionList));
        params2.addParameter("suitID", define.suitID);
        Log.e("jindi", params2.toString());

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
                    //删除暂存数据
                    try {
                        WhereBuilder b = WhereBuilder.b();
                        b.and("planbill", " = ", selectWorkCardPlan.getPlanbill());
                        b.and("orderbill", "=", selectWorkCardPlan.getOrderbill());
                        dbManager.delete(GxpgPlanReportNumberSave.class, b);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    getData(finterid,false);
                    //GetWorkCardProcessQty();
                    recordCount += thisRecordCount;
                    refreshData();
                } else {
                    saveGxpgPlanReportNumber(false);
                    Toast.makeText(mContext, "工序汇报录入失败,请按校正按钮", Toast.LENGTH_LONG).show();
                    btn_report.setEnabled(true);
                    /*
                    //失败后等待两秒重新反写
                    try {
                        Thread.sleep(2000);
                        againAutoSCProcessOutPutReWriteBysuitID();
                    } catch (InterruptedException e) {
                        return;
                    }
                    */
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


    private void againSCProcessOutPutReWriteBysuitID() {
        RequestParams params = new RequestParams(define.Net2 + define.ResetWorkCardPushData);
        params.addParameter("workcardID", selectWorkCardPlan.getFinterid());
        params.addParameter("suitID", define.suitID);
        params.setConnectTimeout(120000);
        params.setReadTimeout(120000);
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
                if (result.contains("success")) {
                    Toast.makeText(mContext, "工序汇报校正成功", Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(2000);
                        GetWorkCardProcessQty();
                    } catch (InterruptedException e) {
                        return;
                    }

                } else {
                    Toast.makeText(mContext, "工序汇报校正失败,请重新校正。", Toast.LENGTH_LONG).show();
                }
                btn_report.setEnabled(true);
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
        RequestParams params = new RequestParams(define.Net1 + define.GetWorkCardProcessQty);
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
                recordCount = pq.getData().get(0).getCumulativenumber().intValue();
                Log.e("jindi", "recordCount:" + recordCount);
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
        wechatPostByFEmpIDData.setFirst("工单汇报校对");
        wechatPostByFEmpIDData.setKeyword1("指令号:" + processWorkCardPlanEntryList.get(position).getPlanbill() + " 工厂型体:" + processWorkCardPlanEntryList.get(position).getPlantbody() + " " + processWorkCardPlanEntryList.get(position).getProcessname());
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
