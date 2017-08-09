package com.goldemperor.StockCheck.DoneView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.goldemperor.R;
import com.goldemperor.sql.stock_check;
import com.goldemperor.MainActivity.GsonFactory;
import com.goldemperor.MainActivity.ListViewDecoration;
import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.define;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nova on 2017/7/17.
 */

public class DoneView {

    private Activity mContext;

    private MenuAdapter mMenuAdapter;

    private List<stock_check> mDataList;

    private SwipeMenuRecyclerView mMenuRecyclerView;

    public DoneView(final Activity act, final View view) {
        mContext = act;

        mDataList = new ArrayList<>();


        mMenuRecyclerView = (SwipeMenuRecyclerView) view.findViewById(R.id.recycler_view);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(act));// 布局管理器。
        mMenuRecyclerView.addItemDecoration(new ListViewDecoration(act));// 添加分割线。


        mMenuAdapter = new MenuAdapter(mDataList);

        mMenuAdapter.setOnItemClickListener(onItemClickListener);
        mMenuRecyclerView.setAdapter(mMenuAdapter);

        getData();

    }

    public void getData() {
        RequestParams params = new RequestParams(define.GetData);

        params.addQueryStringParameter("status1",  define.DONE);
        params.addQueryStringParameter("status2", define.CASECLOSE);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                //解析result
                //重新设置数据
                if (result.length()>1) {
                    ArrayList<stock_check> dataTemp = GsonFactory.jsonToArrayList(result, stock_check.class);
                    mDataList.clear();
                    if (!dataTemp.isEmpty()) {
                        for (int i = 0; i < dataTemp.size(); i++) {
                            stock_check temp = dataTemp.get(i);
                            mDataList.add(temp);
                        }
                    }

                    mContext.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mMenuAdapter.notifyDataSetChanged();
                        }
                    });
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


    /**
     * Item点击监听。
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Intent i = new Intent(mContext, DoneLookActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", String.valueOf(mDataList.get(position).getId()));
            i.putExtras(bundle);
            mContext.startActivityForResult(i, define.UPDATA);
        }
    };


}
