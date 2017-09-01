package com.goldemperor.Update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xufanglou on 2016-08-27.
 */
public class DownLoadManager {
    public static File getFileFromServer(String path, final ProgressDialog pd, final Activity act) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        final int maxlen = conn.getContentLength();
        //获取到文件的大小
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd.setMax(maxlen);
            }
        });
        InputStream is = conn.getInputStream();




        FileOutputStream fos = act.openFileOutput("updata.apk",act.MODE_WORLD_READABLE);
        // File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
        //FileOutputStream fos = new FileOutputStream("updata.apk",act.MODE_WORLD_READABLE);

        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int len;
        int total = 0;
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            total += len;
            final int Progress = total;
            //获取当前下载量
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.setProgress(Progress);
                }
            });
        }
        fos.close();
        bis.close();
        is.close();

        String PATH = act.getFilesDir()+"/";

        File apkdir=new File (PATH);

        apkdir.mkdirs();

        File file=new File(apkdir, "updata.apk");
        return file;
    }
}
