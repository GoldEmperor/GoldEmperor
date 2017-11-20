/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.goldemperor.PzActivity;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.Utils;
import com.goldemperor.PgdActivity.WorkCardPlan;
import com.goldemperor.R;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.goldemperor.PzActivity.YCListActivity.abnormityModel;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class YCListAdapter extends SwipeMenuAdapter<YCListAdapter.DefaultViewHolder> {

    private List<Sc_WorkCardAbnormity> ls;

    private OnItemClickListener mOnItemClickListener;

    public static com.goldemperor.PzActivity.YCListActivity ycListActivity;

    public static List<ScrollListenerHorizontalScrollView> ScrollViewList = new ArrayList<ScrollListenerHorizontalScrollView>();

    public YCListAdapter(List<Sc_WorkCardAbnormity> ls, YCListActivity ycListActivity) {
        this.ls = ls;
        this.ycListActivity = ycListActivity;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return ls == null ? 0 : ls.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_yclist_item, parent, false);
    }

    @Override
    public YCListAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {

        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
        viewHolder.mOnItemClickListener = mOnItemClickListener;
        ScrollViewList.add(viewHolder.ScrollView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(YCListAdapter.DefaultViewHolder holder, int position) {
        holder.setData(ls.get(position));
    }


    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ScrollListenerHorizontalScrollView.OnScrollListener {

        OnItemClickListener mOnItemClickListener;
        LinearLayout linearLayout;

        public ScrollListenerHorizontalScrollView ScrollView;

        private TextView tv_followNumber;
        private TextView tv_pgdTime;
        private TextView tv_name;

        private TextView tv_group;
        private TextView tv_exception;
        private TextView tv_qty;

        private TextView tv_freCheck;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.item);

            linearLayout.setOnClickListener(this);
            ScrollView = (ScrollListenerHorizontalScrollView) itemView.findViewById(R.id.ScrollView);
            ScrollView.setOnScrollListener(this);


            tv_followNumber = (TextView) itemView.findViewById(R.id.tv_followNumber);
            tv_pgdTime = (TextView) itemView.findViewById(R.id.tv_pgdTime);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);

            tv_group = (TextView) itemView.findViewById(R.id.tv_group);
            tv_exception = (TextView) itemView.findViewById(R.id.tv_exception);
            tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);

            tv_freCheck = (TextView) itemView.findViewById(R.id.tv_recheck);
            //ScrollView.setOnScrollStateChangedListener(this);
            //ScrollView.setOnScrollChangeListener(this);

        }

        public void setData(Sc_WorkCardAbnormity o) {
            tv_followNumber.setText(o.getFnumber());
            tv_pgdTime.setText(o.getFdate());
            tv_name.setText(o.getFname());
            tv_group.setText(o.getFdeptmentName());
            tv_qty.setText(String.valueOf(o.getFqty()));
            tv_freCheck.setText(o.getFreCheck());

            for(int i=0;i<abnormityModel.size();i++){
                if(o.getFexceptionID().equals(abnormityModel.get(i).getFitemID())){
                    if(o.getFexceptionLevel()==0) {
                        tv_exception.setText(abnormityModel.get(i).getFname() + "(轻微)");
                    }else{
                        tv_exception.setText(abnormityModel.get(i).getFname() + "(严重)");
                    }
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }

        }

        @Override
        public void onScroll(int scrollX) {
            for (int i = 0; i < ScrollViewList.size(); i++) {
                ScrollViewList.get(i).scrollTo(scrollX, ScrollView.getScrollY());
            }
            ycListActivity.ScrollView.scrollTo(scrollX, ScrollView.getScrollY());

        }
    }


}
