package com.goldemperor.GxReport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.goldemperor.R;

import java.util.ArrayList;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * Created by Nova on 2017/8/15.
 */

public class GxReportScan extends AppCompatActivity implements QRCodeView.Delegate {

    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    private QRCodeView mQRCodeView;
    private ArrayList<String> QRCodeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态啦
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gxreport_scan);
        //隐藏标题栏
        getSupportActionBar().hide();
        mQRCodeView = (ZBarView) findViewById(R.id.zbarview);
        mQRCodeView.setDelegate(this);
        mQRCodeView.startSpot();
        mQRCodeView.changeToScanBarcodeStyle();
        Bundle bundle = this.getIntent().getExtras();
        QRCodeList=bundle.getStringArrayList("QRCodeList");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mQRCodeView.showScanRect();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
    @Override
    public void onScanQRCodeSuccess(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        if(!QRCodeList.contains(result)&&result.length()>3){
            QRCodeList.add(result);
        }
        vibrate();
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent();
        mIntent.putStringArrayListExtra("QRCodeList",QRCodeList);
        // 设置结果，并进行传送
        this.setResult(1, mIntent);
        super.onBackPressed();

    }
}
