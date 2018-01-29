package com.goldemperor.PzActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.goldemperor.GxReport.GxReport;
import com.goldemperor.LoginActivity.LoginActivity;
import com.goldemperor.MainActivity.GsonFactory;
import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.Utils;
import com.goldemperor.MainActivity.define;
import com.goldemperor.PgdActivity.*;
import com.goldemperor.PgdActivity.PgdAdapter;
import com.goldemperor.R;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.goldemperor.PzActivity.PgdActivity.selectWorkCardPlan;
import static com.goldemperor.PzActivity.PgdActivity.showWorkCardPlan;

/**
 * Created by Nova on 2017/10/28.
 */

public class YCListActivity extends AppCompatActivity implements ScrollListenerHorizontalScrollView.OnScrollListener {

    List<Sc_WorkCardAbnormity> Sc_WorkCardAbnormityList = new ArrayList<Sc_WorkCardAbnormity>();
    private TwinklingRefreshLayout refreshLayout;
    private SwipeMenuRecyclerView mMenuRecyclerView;
    private FancyButton btn_zj;
    private Context mContext;
    private Activity act;
    private TextView tv_tip;
    public ScrollListenerHorizontalScrollView ScrollView;
    public static YCListAdapter mMenuAdapter;
    private SharedPreferences dataPref;
    private SharedPreferences.Editor dataEditor;

    private List<String[]> nameList = new ArrayList<String[]>();
    public static int YcCount = 0;

    public static List<AbnormityModel> abnormityModel = new ArrayList<AbnormityModel>();
    public static List<String[]> selectAbnormity = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态啦
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_yclist);
        //隐藏标题栏
        getSupportActionBar().hide();
        dataPref = this.getSharedPreferences(define.SharedName, 0);
        dataEditor = dataPref.edit();
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        ScrollView = (ScrollListenerHorizontalScrollView) findViewById(R.id.ScrollView);
        ScrollView.setOnScrollListener(this);
        act = this;
        mContext = this;
        btn_zj = (FancyButton) findViewById(R.id.btn_zj);
        btn_zj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, com.goldemperor.PzActivity.ZjActivity.class);
                startActivityForResult(i, 0);
            }
        });

        //初始化下拉刷新
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout);

        SinaRefreshView headerView = new SinaRefreshView(this);
        headerView.setArrowResource(R.drawable.arrow);
        headerView.setTextColor(0xff745D5C);
//        TextHeaderView headerView = (TextHeaderView) View.inflate(this,R.layout.header_tv,null);
        refreshLayout.setHeaderView(headerView);

        refreshLayout.setEnableLoadmore(false);

        refreshLayout.setOverScrollRefreshShow(true);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                //获取数据
                tv_tip.setVisibility(View.GONE);
                getData();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {

            }
        });

        getData();
        getAbnormityData();
        mMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        // 设置菜单创建器。
        mMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);


        mMenuAdapter = new YCListAdapter(Sc_WorkCardAbnormityList, this);
        mMenuAdapter.setOnItemClickListener(onItemClickListener);
        mMenuRecyclerView.setAdapter(mMenuAdapter);
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
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。

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
                DeleteByFInterID(String.valueOf(Sc_WorkCardAbnormityList.get(adapterPosition).getFinterID()));
            }
            closeable.smoothCloseMenu();// 关闭被点击的菜单。
        }
    };
    String recheckText="返工合格";
    /**
     * Item点击监听。
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(final int position) {
            String[] singleChoiceItems = {"返工合格", "报废"};
            int defaultSelectedIndex = 0;//单选框默认值：从0开始

            //创建对话框
            new AlertDialog.Builder(mContext)
                    .setTitle("复审")//设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)//设置对话框图标
                    .setSingleChoiceItems(singleChoiceItems, defaultSelectedIndex, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which==0){
                                recheckText="返工合格";
                            }else{
                                recheckText="报废";
                            }
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ReCheckAbnormity(String.valueOf(Sc_WorkCardAbnormityList.get(position).getFinterID()),recheckText);

                            dialog.dismiss();
                        }
                    })//设置对话框[肯定]按钮
                    .setNegativeButton("取消", null)//设置对话框[否定]按钮
                    .show();
        }
    };

    public void ReCheckAbnormity(String finterid,String textString) {
        RequestParams params = new RequestParams(define.Net1 + define.ReCheckAbnormity);
        params.addQueryStringParameter("FInterID", finterid);
        params.addQueryStringParameter("textString", textString);
        Log.e("jindi",params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("jindi", result);
                getData();
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("jindi", ex.toString());
                Toast.makeText(mContext, "复审失败", Toast.LENGTH_LONG).show();
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

    public void DeleteByFInterID(String finterid) {
        RequestParams params = new RequestParams(define.Net1 + define.DeleteByFInterID);
        params.addQueryStringParameter("FInterID", finterid);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("jindi", result);
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                });

            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("jindi", ex.toString());
                Toast.makeText(mContext, "删除失败", Toast.LENGTH_LONG).show();
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

    @Override
    public void onScroll(int scrollX) {
        for (int i = 0; i < mMenuAdapter.ScrollViewList.size(); i++) {
            mMenuAdapter.ScrollViewList.get(i).scrollTo(scrollX, ScrollView.getScrollY());
        }
    }

    private void getData() {
        tv_tip.setText("数据载入中...");
        tv_tip.setVisibility(View.VISIBLE);
        Sc_WorkCardAbnormityList.clear();
        nameList.clear();
        RequestParams params = new RequestParams(define.Net1 + define.GetWorkCardAbnormity);
        params.addQueryStringParameter("FInterID", String.valueOf(selectWorkCardPlan.getFinterid()));
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

                Gson g = new Gson();
                YcResult ycResult = g.fromJson(result, YcResult.class);
                YcCount = 0;
                if (ycResult.getData() != null) {
                    YcCount = Integer.valueOf(ycResult.getCount());
                    for (int i = 0; i < ycResult.getData().size(); i++) {
                        Sc_WorkCardAbnormityList.add(ycResult.getData().get(i));
                        boolean hasSame = false;


                        if (dataPref.getString(define.SharedEmpId, "").equals(String.valueOf(Sc_WorkCardAbnormityList.get(i).getFempID()))) {
                            Sc_WorkCardAbnormityList.get(i).setFname(dataPref.getString(define.SharedUser, ""));
                            Sc_WorkCardAbnormityList.get(i).setFdeptmentName(dataPref.getString(define.SharedDeptmentName, ""));
                            hasSame = true;
                            if (i == ycResult.getData().size() - 1) {
                                mMenuAdapter.notifyDataSetChanged();
                            }
                        } else {
                            for (int j = 0; j < nameList.size(); j++) {
                                if (nameList.get(j)[0].equals(String.valueOf(Sc_WorkCardAbnormityList.get(i).getFempID()))) {
                                    Sc_WorkCardAbnormityList.get(i).setFname(nameList.get(j)[1]);
                                    Sc_WorkCardAbnormityList.get(i).setFdeptmentName(nameList.get(j)[2]);
                                    hasSame = true;
                                    if (i == ycResult.getData().size() - 1) {
                                        mMenuAdapter.notifyDataSetChanged();
                                    }
                                }

                            }
                        }
                        if (!hasSame) {
                            GetUserID(i);
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    tv_tip.setVisibility(View.GONE);
                } else {
                    tv_tip.setText("暂无数据");
                }

                refreshLayout.finishRefreshing();
            }


            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("jindi", ex.toString());
                tv_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("数据载入失败,请检查网络:" + ex.toString());

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

    public void GetUserID(final int position) {
        RequestParams params = new RequestParams(define.Net2 + define.GetUserID);
        params.addQueryStringParameter("FEmpID", String.valueOf(Sc_WorkCardAbnormityList.get(position).getFempID()));
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("jindi", result);
                if (result.contains("FUserID")) {
                    String Fname = result.substring(result.indexOf("<Fname>"), result.indexOf("</Fname>"));
                    Fname = Fname.replaceAll("<Fname>", "").replaceAll("</Fname>", "");
                    Sc_WorkCardAbnormityList.get(position).setFname(Fname);

                    String FDeptmentName = result.substring(result.indexOf("<FDeptmentName>"), result.indexOf("</FDeptmentName>"));
                    FDeptmentName = FDeptmentName.replaceAll("<FDeptmentName>", "").replaceAll("</FDeptmentName>", "");
                    Sc_WorkCardAbnormityList.get(position).setFdeptmentName(FDeptmentName);
                    String[] name = new String[3];
                    name[0] = String.valueOf(Sc_WorkCardAbnormityList.get(position).getFempID());
                    name[1] = Fname;
                    name[2] = FDeptmentName;
                    nameList.add(name);
                    mMenuAdapter.notifyDataSetChanged();
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

    private void getAbnormityData() {
        abnormityModel.clear();
        selectAbnormity.clear();

        RequestParams params;
        if (selectWorkCardPlan.getFgroup().contains("针车")) {
            params = new RequestParams(define.Net1 + define.GetAbnormityByName);
            params.addQueryStringParameter("FName", "针车");
        } else {
            params = new RequestParams(define.Net1 + define.GetAbnormityByID);
            params.addQueryStringParameter("paramString", "39,40,41,42,43");
        }
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson g = new Gson();
                ReasonResult reasonResult = g.fromJson(result, ReasonResult.class);

                if (reasonResult.getData() != null) {
                    for (int i = 0; i < reasonResult.getData().size(); i++) {
                        abnormityModel.add(reasonResult.getData().get(i));
                        String[] temp = new String[4];
                        temp[0] = reasonResult.getData().get(i).getFname();
                        temp[1] = String.valueOf(reasonResult.getData().get(i).getFitemID());
                        temp[2] = "无";
                        temp[3] = "未选";
                        selectAbnormity.add(temp);
                    }
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mMenuAdapter != null) {
            getData();
        }

    }
}
