package com.goldemperor.ScReport.android;

import android.util.Log;

import com.goldemperor.ScReport.ScReportActivity;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by xufanglou on 2016-07-20.
 */
public class URLCode {
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            Log.i(ScReportActivity.TAG, "toURLEncoded error:" + paramString);
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            Log.i(ScReportActivity.TAG, "toURLEncoded error:" + paramString, localException);
        }

        return "";
    }

    public static String toURLDecoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            Log.i(ScReportActivity.TAG,"toURLDecoded error:" + paramString);
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLDecoder.decode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            Log.i(ScReportActivity.TAG,"toURLDecoded error:" + paramString, localException);
        }

        return "";
    }

}
