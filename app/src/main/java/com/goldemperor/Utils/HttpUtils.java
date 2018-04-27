package com.goldemperor.Utils;

import android.util.Xml;

import com.goldemperor.model.ProcessSendModel;

import org.xmlpull.v1.XmlPullParser;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * File Name : HttpUtils
 * Created by : PanZX on  2018/4/25 11:08
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：Http工具类
 */
public class HttpUtils{
    public static final String Success="onSuccess";
    public static final String Error="onError";
    public static final String Cancelled="onCancelled";
    public static final String Finished="onFinished";
    public static void post(RequestParams RP, final httpcallback httpcallback) {

        x.http().post(RP, new Callback.CommonCallback<String>(){
            @Override
            public void onSuccess(String result) {
                httpcallback.postcallback(Success,result);
                LOG.e("onSuccess:" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpcallback.postcallback(Error,"");
                LOG.e("onError:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                httpcallback.postcallback(Cancelled,"");
                LOG.e("onCancelled:" + cex.toString());
            }

            @Override
            public void onFinished() {
                httpcallback.postcallback(Finished,"");
                LOG.e("onFinished");
            }
        });
    }
    public static void get(RequestParams RP, final httpcallback httpcallback) {

        x.http().get(RP, new Callback.CommonCallback<String>(){
            @Override
            public void onSuccess(String result) {
                httpcallback.postcallback(Success,result);
                LOG.e("onSuccess:" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpcallback.postcallback(Error,"");
                LOG.e("onError:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                httpcallback.postcallback(Cancelled,"");
                LOG.e("onCancelled:" + cex.toString());
            }

            @Override
            public void onFinished() {
                httpcallback.postcallback(Finished,"");
                LOG.e("onFinished");
            }
        });
    }


    public  interface httpcallback{
          void postcallback(String Finish, String paramString);
    }
}
