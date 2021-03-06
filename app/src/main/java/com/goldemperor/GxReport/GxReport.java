package com.goldemperor.GxReport;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.goldemperor.MainActivity.ListViewDecoration;
import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.define;
import com.goldemperor.R;

import com.goldemperor.StockCheck.ExceptionalView.DisposeActivity;
import com.goldemperor.StockCheck.ExceptionalView.ExceptionalLookActivity;
import com.google.gson.Gson;
import com.tapadoo.alerter.Alerter;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Nova on 2017/8/15.
 */

public class GxReport extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private BootstrapButton scan;

    private BootstrapButton submit;

    private Context mContext;
    private Activity activity;
    private ArrayList<Order> QRCodeList;

    private MenuAdapter mMenuAdapter;

    private TextView scanCount;
    private TextView productCount;
    private SwipeMenuRecyclerView mMenuRecyclerView;

    private SharedPreferences dataPref;
    /**
     * Item点击监听。
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {

        }
    };
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
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.wait)
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
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                //Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                //Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }

            // TODO 如果是删除：推荐调用Adapter.notifyItemRemoved(position)，不推荐Adapter.notifyDataSetChanged();
            if (menuPosition == 0) {// 处理按钮被点击。

                QRCodeList.remove(adapterPosition);
                mMenuAdapter.notifyItemRemoved(adapterPosition);
                setData();
                //Toast.makeText(mContext,String.valueOf(mDataList.get(adapterPosition).getId()),Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态啦
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gxreport);
        //隐藏标题栏
        getSupportActionBar().hide();
        dataPref = this.getSharedPreferences(define.SharedName, 0);
        mContext = this;
        activity = this;
        QRCodeList = new ArrayList<Order>();
        scan = (BootstrapButton) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, GxReportScan.class);
                i.putParcelableArrayListExtra("QRCodeList", QRCodeList);
                activity.startActivityForResult(i, 1);
            }
        });

        submit = (BootstrapButton) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QRCodeList.size() != 0) {
                    submitData();
                } else {
                    Alerter.create(activity)
                            .setTitle("提示")
                            .setText("请扫入条码")
                            .setBackgroundColorRes(R.color.colorAlert)
                            .show();
                }
            }
        });

        mMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
        mMenuRecyclerView.addItemDecoration(new ListViewDecoration(activity));// 添加分割线。

        // 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
        // 设置菜单创建器。
        mMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);

        mMenuAdapter = new MenuAdapter(QRCodeList, this);

        mMenuAdapter.setOnItemClickListener(onItemClickListener);
        mMenuRecyclerView.setAdapter(mMenuAdapter);
        scanCount = (TextView) findViewById(R.id.tv_scanCount);
        scanCount.setText("条码数量:" + QRCodeList.size());
        productCount = (TextView) findViewById(R.id.tv_productCount);
        float productCountTemp = 0;
        for (int i = 0; i < QRCodeList.size(); i++) {
            productCountTemp += Float.valueOf(QRCodeList.get(i).getFQty());
        }
        productCount.setText("产品总数:" + productCountTemp + "件");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<Order> QRCodeListTemp = data.getParcelableArrayListExtra("QRCodeList");
        QRCodeList.clear();
        QRCodeList.addAll(QRCodeListTemp);
        mMenuAdapter.notifyDataSetChanged();
        setData();
    }

    public void submitData() {
        submit.setEnabled(false);
        List<SubmitJson> sjl = new ArrayList<SubmitJson>();
        for (int i = 0; i < QRCodeList.size(); i++) {
            SubmitJson sj = new SubmitJson();
            sj.setD_BarCode(QRCodeList.get(i).getFCardNo());
            sj.setD_EmpID(dataPref.getString(define.SharedEmpId, "none"));
            sj.setD_Qty(QRCodeList.get(i).getFQty());
            sjl.add(sj);
        }
        Gson g = new Gson();
        String sjlJson = g.toJson(sjl);
        String path =define.Net2+ define.SubmitProcessBarCode2CollectBill;

        RequestParams params = new RequestParams(path);
        params.setConnectTimeout(60000);
        params.setReadTimeout(60000);
        String sjlJsonEncode = "";
        try {
            sjlJsonEncode = URLEncoder.encode(sjlJson, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("jindi", "encodeError");
        }
        params.addBodyParameter("barcodeJson", sjlJsonEncode);
        params.addBodyParameter("OrganizeID", define.suitID);
        params.addBodyParameter("BillTypeID", "1");
        params.addBodyParameter("EmpID", dataPref.getString(define.SharedEmpId, "none"));
        params.addBodyParameter("UserID", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result

                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.e("jindi", result);

                String StockBillNO = result.substring(result.lastIndexOf("StockBillNO"), result.lastIndexOf("\"}")).replace("StockBillNO\":\"", "");
                submit.setEnabled(true);
                if (result.contains("success")) {
                    Alerter.create(activity)
                            .setTitle("提示")
                            .setText("提交成功,工序汇报单号:" + StockBillNO)
                            .setBackgroundColorRes(R.color.colorAlert)
                            .show();
                    QRCodeList.clear();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMenuAdapter.notifyDataSetChanged();
                        }
                    });
                    setData();
                } else {
                    Alerter.create(activity)
                            .setTitle("提示")
                            .setText("提交失败:" + StockBillNO)
                            .setBackgroundColorRes(R.color.colorAlert)
                            .show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                submit.setEnabled(true);
                Alerter.create(activity)
                        .setTitle("提示")
                        .setText("提交失败,网络错误:" + ex.toString())
                        .setBackgroundColorRes(R.color.colorAlert)
                        .show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    public void setData() {
        scanCount.setText("条码数量:" + QRCodeList.size());
        float productCountTemp = 0;
        for (int i = 0; i < QRCodeList.size(); i++) {
            productCountTemp += Float.valueOf(QRCodeList.get(i).getFQty());
        }
        productCount.setText("产品总数:" + productCountTemp + "件");
    }

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
