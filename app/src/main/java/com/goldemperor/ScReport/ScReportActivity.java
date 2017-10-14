package com.goldemperor.ScReport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldemperor.CxStockIn.android.NetworkHelper;
import com.goldemperor.CxStockIn.android.TelephonyManagerClass;
import com.goldemperor.R;
import com.goldemperor.ScReport.DialogLoadding.WeiboDialogUtils;
import com.goldemperor.ScReport.android.CommDB;
import com.goldemperor.ScReport.android.Config;
import com.goldemperor.ScReport.android.ScReportBarCodeDB;
import com.goldemperor.ScReport.android.PublicService;
import com.goldemperor.ScReport.android.SubmitBarCodeService;
import com.goldemperor.CxStockIn.deleteslide.ActionSheet;
import com.goldemperor.CxStockIn.deleteslide.MyAdapter;
import com.goldemperor.CxStockIn.widget.DelSlideListView;
import com.goldemperor.CxStockIn.widget.ListViewonSingleTapUpListenner;
import com.goldemperor.CxStockIn.widget.OnDeleteListioner;
import com.goldemperor.model.BarCode;
import com.goldemperor.model.DeviceInfoMap;
import com.goldemperor.model.MessageEnum;
import com.goldemperor.model.MessageObject;
import com.goldemperor.model.UpdataInfo;
import com.nlscan.android.scan.ScanManager;
import com.nlscan.android.scan.ScanSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimerTask;


public class ScReportActivity extends FragmentActivity implements OnDeleteListioner, ListViewonSingleTapUpListenner,ActionSheet.OnActionSheetSelected, OnCancelListener{

    //滑动删除变量
    LinkedList<String[]> mlist = new LinkedList<String[]>();
    Map<String, String> mBarcodeMap = new HashMap<String, String>();//获得条形码集合 包换ID,条码内容
    MyAdapter mMyAdapter;
    DelSlideListView mDelSlideListView;
    int delID = 0;
    //滑动删除变量
    public static String DeviceIMEI;//移动设备IMEI码
    public static Map<String, DeviceInfoMap> deviceInfoMap;//移动设备管理信息哈希表
    public static final String TAG = "ScReportActivity";
    private IntentFilter mFilter;
    private Button btnadd, btndelete, btnclear, btnsubmit;
    private EditText et_scanresult;
    public BarCodeHandler barCodeHandler;
    public TableLayout et_scan_table;
    public TextView txtcount;
    public SQLiteDatabase db;
    public String TABLE_NAME = "t_ScReportBarCode";

    private CommDB comDBHelper;
    public ScReportBarCodeDB ScReportBarCodeDBHelper;
    private RadioButton stockin;
    public Config myConfig;//=new Config();
    public static boolean IsNeedCheckVersion = true;//是否检查自动更新
    public static ScReportActivity mainActivity_instance = null;//声明的变量
    public static ScLoginActivity loginActivity_instance = null;//声明的变量
    private SimpleCursorAdapter adapter;
    private ScanManager mScanMgr;
    public UpdataInfo info;
//    //条形码列表的适配器
//    private ContactsCursorAdapter m_contactsAdapter;
//
//    //加载器监听器
//    private ContactsLoaderListener m_loaderCallback = new ContactsLoaderListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screport_activity_main);

        //滑动删除实例化
        this.mDelSlideListView = (DelSlideListView) this.findViewById(R.id.listv);
        mMyAdapter = new MyAdapter(this,mlist);
        mDelSlideListView.setAdapter(mMyAdapter);
        mDelSlideListView.setDeleteListioner(this);
        mDelSlideListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //获得选中项的HashMap对象

                et_scanresult.setText(mlist.get(arg2)[1]);

            }

        });
        mDelSlideListView.setSingleTapUpListenner(this);
        mMyAdapter.setOnDeleteListioner(this);

        //如下3行必须添加，否则HttpURLConnection.onnect 会出异常
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mScanMgr = ScanManager.getInstance();
        mScanMgr.setOutpuMode(ScanSettings.Global.VALUE_OUT_PUT_MODE_FILLING);//直接填充

        DeviceIMEI = new TelephonyManagerClass(this).getDeviceId();//移动设备唯一码，不要删除
        Log.i(TAG, "tm:" + DeviceIMEI);//获得移动设备IMEI码
        et_scanresult = (EditText) findViewById(R.id.et_workcard_no);
        // et_mono = (EditText) findViewById(R.id.et_mono);
        //et_scan_table = (TableLayout) findViewById(R.id.et_scan_table);
        //deviceInfoMap=WorkCardService.GetDeviceInfoMap();
        btnadd = (Button) this.findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new ClickEvent(this));

        btndelete = (Button) this.findViewById(R.id.btndelete);
        btndelete.setOnClickListener(new ClickEvent(this));

        btnclear = (Button) this.findViewById(R.id.btnclear);
        btnclear.setOnClickListener(new ClickEvent(this));
        btnsubmit = (Button) this.findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(new ClickEvent(this));
        txtcount = (TextView) this.findViewById(R.id.txtcount);

        stockin = (RadioButton) this.findViewById(R.id.stockin);

        stockin.setChecked(true);//默认设定选中入库单
        //先创建barCodeHandler 再创建数据库
        barCodeHandler = new BarCodeHandler(this, getMainLooper());

        //创建SQLLite 数据库
        IniDataBase();
        mFilter = new IntentFilter("ACTION_BAR_SCAN");
        //将广播的优先级调到最高1000
        mFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        if (ScReportBarCodeDBHelper == null)
            ScReportBarCodeDBHelper = new ScReportBarCodeDB(this, barCodeHandler, mMyAdapter, txtcount);
        mainActivity_instance = ScReportActivity.this;//以便在别的activity中可以直接调用
        mainActivity_instance.ScReportBarCodeDBHelper = ScReportBarCodeDBHelper;

        //加载器监听器

//        private ContactsLoaderListener m_loaderCallback = new ContactsLoaderListener();

//        initLoader();
//        setRiverListViewAdapter();

    }
    //滑动删除

    @Override
    public void onSingleTapUp() {

    }
    /*连续单击两次back键退出系统*/
    private long exitTime;
    @Override
    public void onBackPressed() {
        if((System.currentTimeMillis()-exitTime) > 500){
            Toast.makeText(getApplicationContext(), "连续按两次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else{
            super.onBackPressed();
        }
    }


    @Override
    public boolean isCandelete(int position) {
        return true;
    }


    @Override
    public void onDelete(int ID) {
        delID = ID;
        //获得所要删除的条形码信息
        String SelDelbarcode= mlist.get(delID)[1];
        //弹出删除确认窗体
        ActionSheet.showSheet(this, this, this,SelDelbarcode);
    }


    @Override
    public void onBack() {

    }

    @Override
    public void onCancel(DialogInterface arg0) {

    }

    @Override
    public void onClick(int whichButton) {
        switch (whichButton) {
            case 0://删除单条条形码
//                mlist.remove(delID);
                int delCodeID= Integer.parseInt(mlist.get(delID)[0]);
                ScReportBarCodeDBHelper.deleteTicketbarcode(delCodeID);
                mDelSlideListView.SnapToScreen();
//                mMyAdapter.notifyDataSetChanged();

                break;
            case 1:

                break;
            default:
                break;
        }
    }
    //滑动删除
    private void registerReceiver() {
        IntentFilter intFilter = new IntentFilter(ScanManager.ACTION_SEND_SCAN_RESULT);
        registerReceiver(mReceiver, intFilter);
    }

    private void unRegisterReceiver() {
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
        }
    }

    /**
     * 监听扫码数据的广播，当设置广播输出时作用该方法获取扫码数据
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ScanManager.ACTION_SEND_SCAN_RESULT.equals(action)) {
                byte[] bvalue1 = intent.getByteArrayExtra(ScanManager.EXTRA_SCAN_RESULT_ONE_BYTES);
                byte[] bvalue2 = intent.getByteArrayExtra(ScanManager.EXTRA_SCAN_RESULT_TWO_BYTES);
                String svalue1 = null;
                String svalue2 = null;
                try {
                    if (bvalue1 != null)
                        svalue1 = new String(bvalue1, "GBK");
                    if (bvalue2 != null)
                        svalue2 = new String(bvalue1, "GBK");
                    svalue1 = svalue1 == null ? "" : svalue1;
                    svalue2 = svalue2 == null ? "" : svalue2;
//                    tv_broadcast_result.setText(svalue1+"\n"+svalue2);
                    final String scanResult = svalue1.replaceAll("(\r\n|\r|\n|\n\r)", "");//svalue1+"\n"+svalue2//替换回车
                    String Mono = "";
                    et_scanresult.setText(scanResult);
                    et_scanresult.invalidate();
                    InsertBarCode(scanResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    et_scanresult.setText("data encode failed.");
                }

//                Random random = new Random();
//                tv_broadcast_result.setTextColor(Color.argb(255, random.nextInt(256),
//                        random.nextInt(256), random.nextInt(256)));
            }
        }
    };

    /**
     * 在内存创建数据库和数据表
     */
    void IniDataBase() {
        try {
            comDBHelper = new CommDB(this);
            comDBHelper.open();
            if (ScReportBarCodeDBHelper == null)
                ScReportBarCodeDBHelper = new ScReportBarCodeDB(this, barCodeHandler, mMyAdapter, txtcount);
            ScReportBarCodeDBHelper.open();
            ScReportBarCodeDBHelper.RefreshTable();

        } catch (SQLException e) {
        }
    }

    public class ClickEvent implements View.OnClickListener {
        public Context myContext;

        public ClickEvent(Context context) {
            super();
            myContext = context;
        }

        @Override
        public void onClick(View v) {
            try {
                v.setEnabled(false);
                switch (v.getId()) {
                    case R.id.btnadd:
                        InsertBarCode();
                        break;
                    case R.id.btndelete:
                        DeleteBarCode();
                        break;
                    case R.id.btnclear:
                        ArrayList<BarCode> lists = ScReportBarCodeDBHelper.GetAllData();
                        if (lists.size() == 0)
                            return;
                        ClearBarCode();
                        break;
                    case R.id.btnsubmit:
                        ArrayList<BarCode> lists2 = ScReportBarCodeDBHelper.GetAllData();
                        if (lists2.size() == 0) {
                            Toast.makeText(getApplicationContext(), "没有数据,无须提交", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SubmitBarCode();
                        break;
                    default:
                        break;
                }

            }
            catch (Exception ex)
            {

            }
            finally {
                v.setEnabled(true);
            }
            //Toast.makeText(myContext, "点击提交按钮", Toast.LENGTH_SHORT).show();
        }
    }

    //判断是否有非法字符
    public boolean CheckBarCodeValid(String Barcode) {
        String[] UnValidWord = new String[]{",", "，", "~", "@", "#", "$", "%", "^", "&", "*", "(", ")"};
        for (int i = 0; i < UnValidWord.length; i++) {   //输出你的数据}
            if (Barcode.contains(UnValidWord[i])) {
                Toast.makeText(this, "扫入非法字符", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    public void InsertBarCode() {
        String Barcode = et_scanresult.getText().toString().replace(" ", "").replace("\n", "");
        if (!CheckBarCodeValid(Barcode))
            return;
        InsertBarCode(Barcode);
    }

    //添加记录
    public void InsertBarCode(String Barcode) {
        ScReportBarCodeDBHelper.InsertBarcode(Barcode);
    }

    ///删除记录
    public void DeleteBarCode() {

    }

    ///清空记录
    public void ClearBarCode() {
        ScReportBarCodeDBHelper.deleteAllBarcodes();
    }

    public boolean CheckNetAvailable() {
        if (!NetworkHelper.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    ///提交记录
    public void SubmitBarCode() {
        String UserID = "0";
        if (ScReportActivity.loginActivity_instance != null) {
            UserID = ScReportActivity.loginActivity_instance.userLoginInfo.userInfo.getUserID();
            if (UserID == null)
                UserID = "0";
        }
        if (!CheckNetAvailable()) {
            return;
        }
        //有登录过，不再需要重复登录，直接提交数据就可以了
        if (Integer.parseInt(UserID) == 0) {
            Intent intent;
            intent = new Intent(this, ScLoginActivity.class);
//            //用Bundle携带数据
//            Bundle bundle=new Bundle();
//            //传递name参数为tinyphp
//            bundle.putString("MainActivityContext", "tinyphp");
//            intent.putExtras(bundle);
            startActivity(intent);
            this.finish();
        } else
            ScReportBarCodeDBHelper.ClearSubmitData(this, "ScReportActivity");
    }

    public void ClearSubmitData(Context myContext, String MessageType) {
        if (ScReportBarCodeDBHelper == null)
            ScReportBarCodeDBHelper = new ScReportBarCodeDB(myContext, barCodeHandler, mMyAdapter, txtcount);
        //CxStockBarCodeDBHelper=new CxStockBarCodeDB(getApplicationContext(),barCodeHandler,et_scan_table,txtcount);////CxStockBarCodeDBHelper=new CxStockBarCodeDB(this,barCodeHandler,et_scan_table,txtcount);
        ScReportBarCodeDBHelper.ClearSubmitData(myContext, MessageType);
    }

    //根据登录信息重新初始化控件信息
    public void IniMainActivtyControl() {
        if (ScReportActivity.loginActivity_instance.userLoginInfo.userInfo == null)
            return;
        String BillTypeID = ScReportActivity.loginActivity_instance.userLoginInfo.userInfo.getBillTypeID();
        if (BillTypeID == "108")
            stockin.setChecked(true);

    }
    @Override
    protected void onPause() {
        super.onPause();
        unRegisterReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
//        for(int i=0;i<50;i++){
//            mlist.add("滑动删除->"+i);
//        }
        mMyAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        mReceiver = null;
        mFilter = null;
        comDBHelper.close();
        comDBHelper = null;
        ScReportBarCodeDBHelper.close();
        ScReportBarCodeDBHelper = null;
        super.onDestroy();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    /*
     * 进入程序的主界面
     */
    private void LoginMain() {
        Intent intent = new Intent(this, ScReportActivity.class);
        startActivity(intent);
        IsNeedCheckVersion = false;
        //结束掉当前的activity
        this.finish();
    }

    public class MONOTimerTask extends TimerTask {
        public Handler myHandler;
        public Context myContext;

        public MONOTimerTask(Handler handler, Context context) {
            myHandler = handler;
            myContext = context;
        }

        @Override
        public void run() {
        }
    }

    public class BarCodeHandler extends Handler {
        public Context myContext;

        private BarCodeHandler(Context context, Looper looper) {
            super(looper);
            myContext = context;
        }

        private void redrawscan_table() {
            et_scan_table.invalidate();
            et_scan_table.refreshDrawableState();
        }

        android.app.Dialog dd;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MessageEnum.ClearBarCodeData://删除本地所有数据
                    ScReportBarCodeDBHelper.ClearBarcodes();
//                    redrawscan_table();
                    break;
                case MessageEnum.SubmitClearData://成功提交数据，生产单据后，删除本地所有数据
                    MessageObject myMessageObject2 = (MessageObject) msg.obj;
                    String StockBillNO = "";
                    String Result = PublicService.parseResultJson(myMessageObject2.Content, StockBillNO);
                    if (Result.equals("success")) {
                        //new Dialog(myContext,this).ClearBarCodeDataDialog("提示", "已成功生成单据编号："+MainActivity.loginActivity_instance.userLoginInfo.userInfo.getStockBillNO());
                        ScReportBarCodeDBHelper.ClearBarcodes();//生产成功后再删除本地数据，否则不删除
                        //this.m.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "成功生成单据编号：" + ScReportActivity.loginActivity_instance.userLoginInfo.userInfo.getStockBillNO(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "失败提示：" + ScReportActivity.loginActivity_instance.userLoginInfo.userInfo.getStockBillNO(), Toast.LENGTH_SHORT).show();
                    }
                    btnsubmit.setEnabled(true);
                    WeiboDialogUtils.closeDialog(dd);
                    //DialogThridUtils.closeDialog(dd);
                    //redrawscan_table();
                    break;
                case MessageEnum.ClearSubmitData://先提交数据到webservice,再删除本地所有数据


                    MessageObject myMessageObject = (MessageObject) msg.obj;
                    Context LoginContext = myMessageObject.context;
                    String MessageType = myMessageObject.MessageType;
                    String AllDataJson = ScReportBarCodeDBHelper.GetAllDataJson();//先获得json数据
                    if (MessageType == "CxLoginActivty") {

                        Intent intent = new Intent(LoginContext, ScReportActivity.class);
                        startActivity(intent);
                        finish();
                        IniMainActivtyControl();
                        return;
//                        SubmitBarCodeService Athread = new SubmitBarCodeService(this, myContext, loginActivity_instance, AllDataJson);
//                        Athread.start();

                    }else
                    {
                        //dd= DialogThridUtils.showWaitDialog(MainActivity.this,"提交中..",false,false);
                        dd= WeiboDialogUtils.createLoadingDialog(ScReportActivity.this,"提交中..");
                    }
//                    else {
//                        SubmitBarCodeService Athread = new SubmitBarCodeService(this, myContext, loginActivity_instance, AllDataJson);
//                        Athread.start();
//                    }
                    btnsubmit.setEnabled(false);
                    SubmitBarCodeService Athread = new SubmitBarCodeService(this, myContext, loginActivity_instance, AllDataJson);
                    Athread.start();
                    //CxStockBarCodeDBHelper.ClearBarcodes();

                    break;
                case  MessageEnum.LoginMain:
                    LoginMain();
                    break;
            }
            super.handleMessage(msg);
        }
    }

}
