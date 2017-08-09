package com.goldemperor.CxStockIn.android;

import android.util.Log;
import android.util.Xml;


import org.apache.http.util.EncodingUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xufanglou on 2016-07-15.
 */
public class MobileAddressClass {
    protected static final String TAG = "CxStockInActivity";

    public MobileAddressClass() {

    }

    /**
     * 获取文件内容
     *
     * @param fileName
     * @return
     */
    public static String readFileSdcard(String fileName) {
        String res = "";
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            // res = EncodingUtils.getString(buffer, "GB2312");
            res = EncodingUtils.getString(buffer, "utf-8");
        } catch (Exception e) {
            Log.i(TAG, "Exception = " + e.getMessage());
        } finally {
            try {
                fin.close();
            } catch (IOException e) {
                Log.i(TAG, "IOException = " + e.getMessage());
            }
        }
        return res;
    }

    /**
     * 获取电话号码地理位置
     *
     * @param FileName
     * @param mobile
     * @return
     * @throws Exception
     */
    public String getMobileAddress(String FileName, String mobile) {
        try {
            // 替换xml文件中的电话号码
            String XMLContent = readFileSdcard(FileName);
            Log.i(TAG, "xml" + XMLContent);
            String soap = readSoapFile(XMLContent, mobile);
            Log.i(TAG, "xml = " + soap);
            byte[] data = soap.getBytes();
            // 提交Post请求
            URL url = new URL("http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            Log.i(TAG, "POST");
            conn.setConnectTimeout(5 * 1000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            OutputStream outStream = conn.getOutputStream();
            outStream.write(data);
            Log.i(TAG, "writedata");
            outStream.flush();
            outStream.close();
            if (conn.getResponseCode() == 200) {
                // 解析返回信息
                Log.i(TAG, "parseResponseXML");
                return parseResponseXML(conn.getInputStream());
            }
            return "Error";
        } catch (Exception ex) {
            Log.i(TAG, "getMobileAddress Exception = " + ex.getMessage());
            return "Error";
        }

    }

    public String GetStreamUTF(InputStream inStream)  {
        try {
            StringBuffer stringbuffer = new StringBuffer();
            String line = null;
            BufferedReader br = null;
            // 使用IO流读取数据
            br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
            while ((line = br.readLine()) != null) {
                stringbuffer.append(line);
            }
            br.close();
            return stringbuffer.toString();
        } catch (IOException ex) {
            Log.i(TAG, "GetStreamUTF Exception = " + ex.getMessage());
            return "";
        }
    }

    /**
     * 获取电话号码地理位置
     *
     * @param inStream
     * @param mobile
     * @return
     * @throws Exception
     */
    public String getMobileAddress(InputStream inStream, String mobile) {
        try {
            // 替换xml文件中的电话号码
            Log.i(TAG, "替换xml文件中的电话号码 ");
            String xmlcontent= GetStreamUTF(inStream);
            String soap = readSoapFile(xmlcontent, mobile);
           // String soap = readSoapFile(inStream, mobile);
            Log.i(TAG, "xml = " + soap);
            byte[] data = soap.getBytes();
            // 提交Post请求
            URL url = new URL("http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            Log.i(TAG, "POST");
            conn.setConnectTimeout(5 * 1000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            OutputStream outStream = conn.getOutputStream();
            outStream.write(data);
            Log.i(TAG, "writedata");
            outStream.flush();
            outStream.close();
            if (conn.getResponseCode() == 200) {
                // 解析返回信息
                Log.i(TAG, "parseResponseXML");
                return parseResponseXML(conn.getInputStream());
            }
            return "Error";
        } catch (Exception ex) {
            Log.i(TAG, "getMobileAddress Exception = " + ex.getMessage());
            return "Error";
        }

    }

    public String readSoapFile(String soapxml, String mobile) throws Exception {
        // 从流中获取文件信息
        // 占位符参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        // 替换文件中占位符
        return replace(soapxml, params);
    }

    public String readSoapFile(InputStream inStream, String mobile) throws Exception {
        // 从流中获取文件信息
        byte[] data = readInputStream(inStream);
        String soapxml = new String(data);
        // 占位符参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        // 替换文件中占位符
        return replace(soapxml, params);
    }

    /**
     * 读取流信息
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public byte[] readInputStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inputStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 替换文件中占位符
     *
     * @param xml
     * @param params
     * @return
     * @throws Exception
     */
    public String replace(String xml, Map<String, String> params) throws Exception {
        String result = xml;
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String name = "\\$" + entry.getKey();
                Pattern pattern = Pattern.compile(name);
                Matcher matcher = pattern.matcher(result);
                if (matcher.find()) {
                    result = matcher.replaceAll(entry.getValue());
                }
            }
        }
        return result;
    }

    /**
     * 解析XML文件
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static String parseResponseXML(InputStream inStream) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inStream, "UTF-8");
        int eventType = parser.getEventType();// 产生第一个事件
        Log.i(TAG, "eventType" + eventType);
        while (eventType != XmlPullParser.END_DOCUMENT) {
            // 只要不是文档结束事件
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    String name = parser.getName();// 获取解析器当前指向的元素的名称
                    if ("getMobileCodeInfoResult".equals(name)) {
                        return parser.nextText();
                    }
                    break;
            }
            eventType = parser.next();
        }
        return null;
    }

}
