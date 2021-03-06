package com.goldemperor.ScReport.android;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.goldemperor.CxStockIn.android.URLCode;
import com.goldemperor.ScReport.ScReportActivity;
import com.goldemperor.model.MessageEnum;
import com.goldemperor.model.MessageObject;
import com.goldemperor.model.SystemConfig;

public class StockBarCodeService extends Thread {
    protected static final String TAG = "ScReportActivity";
    //正式库
    public static final String asmxURL = SystemConfig.WebServiceURL;//"http://192.168.88.254:8011/ErpForAndroidStockServer.asmx";// getString(R.string.cxstockin_webservice_url);//"http://localhost:3087/ErpForAndroidStockServer.asmx";//
    //开发库
    // public static final String asmxURL = "http://localhost:3087/ErpForAndroidStockServer.asmx";
    //public static final String asmxURL = "http://192.168.99.79:8012/ErpForAndroidStockServer.asmx";
    private static String _BarCode = "";//扫入条码
    private static String _MoNO = "";//指令单号
    public Handler myHandler;
    public Context myContext;
    public UserLoginHelper myUserLoginHelper;
    public String _DefaultSuitID="1";//默认账套为金帝集团有限公司 即01.01账套
    public StockBarCodeService(Handler handler, Context context, UserLoginHelper userLoginHelper) {
        myHandler = handler;
        myContext = context;
        myUserLoginHelper = userLoginHelper;
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        try {
            //设定账套下拉框 ,发送显示指令单命令 Looper.prepare();
            String XMLContent = PublicService.GetWebServiceComnon(myContext, asmxURL, "GetDataTableAccountSuit", "suitID", _DefaultSuitID);
            Log.i(TAG, "XMLContent:" + XMLContent);//获得移动设备IMEI码
            Message message = new Message();
            MessageObject myMessageObject = new MessageObject();
            myMessageObject.Content = URLCode.toURLDecoded(XMLContent);
            myMessageObject.context = this.myContext;
            message.what = MessageEnum.ShowZzt;
            message.obj = myMessageObject;
            myHandler.sendMessage(message);


            //设定组织信息下拉框
            String XMLContent2 = PublicService.GetWebServiceComnon(myContext,asmxURL, "GetOrganization", "suitID", _DefaultSuitID);
            Message message2 = new Message();
            MessageObject myMessageObject2 = new MessageObject();
            myMessageObject2.Content = URLCode.toURLDecoded(XMLContent2);
            myMessageObject2.context = this.myContext;
            message2.what = MessageEnum.ShowOrg;
            message2.obj = myMessageObject2;
            myHandler.sendMessage(message2);
            //Looper.loop();
            //设定单据类型
            Message message3 = new Message();
            message3.what = MessageEnum.ShowBillType;
            message3.obj = "ShowBillType";
            myHandler.sendMessage(message3);
            //创建登录配置信息对象
            if(ScReportActivity.mainActivity_instance.myConfig==null)
                ScReportActivity.mainActivity_instance.myConfig=new Config();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
