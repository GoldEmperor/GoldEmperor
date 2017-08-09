package com.goldemperor.Update;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.goldemperor.R;
import com.goldemperor.model.MessageEnum;
import com.goldemperor.model.UpdataInfo;

import java.io.InputStream;

/**
 * Created by xufanglou on 2016-08-27.
 */
    /*
     * 从服务器获取xml解析并进行比对版本号
	 */
public class CheckVersionTask implements Runnable {
    protected static final String TAG = "CxStockInActivity";
    public Handler myHandler;
    public Context myContext;
    public UpdataInfo myinfo;

    public CheckVersionTask(Handler handler, Context context) {
        myHandler = handler;
        myContext = context;
    }

    public void run() {
        try {
            //从资源文件获取服务器update.xml地址
            String path = myContext.getResources().getString(R.string.updatexmlurl);
            //包装成url的对象
            InputStream stream = HttpUtilHelper.GetInputStreamFromURL(path);
//            StringBuilder sb= new StringBuilder("");
//
//
//            URL url = new URL(path);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(5000);
//            InputStream is = conn.getInputStream();
            myinfo = UpdataInfoParser.getUpdataInfo(stream);//获得服务器上的版本号
            int ServerVersionCode = VersionService.getVersionCode(myContext);//获得服务器的版本号
            String Version=myinfo.getVersion();
            //int NowversionCode = Integer.parseInt(Version);//本地的版本号
            String ServerVersionName = VersionService.getVersionName(myContext);
//            if (myinfo.getVersion().equals(versionCode)) {
            if (!ServerVersionName.equals(Version)) {
                Log.i(TAG, "服务器版本号大于本地版本号 ,提示用户升级 ");
                Message msg = new Message();
                msg.what = MessageEnum.UPDATA_CLIENT;
                msg.obj = myinfo;
                myHandler.sendMessage(msg);

            } else {
                Log.i(TAG, "无需升级");
                Message msg = new Message();
                msg.what = MessageEnum.LoginMain;
                msg.obj = myinfo;
                myHandler.sendMessage(msg);
            }
        } catch (Exception e) {
            // 待处理
            Message msg = new Message();
            msg.what = MessageEnum.GET_UNDATAINFO_ERROR;
            myHandler.sendMessage(msg);
            e.printStackTrace();
        }
    }
}

