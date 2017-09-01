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
package com.goldemperor.PgdActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goldemperor.GxReport.Order;
import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.Utils;
import com.goldemperor.MainActivity.define;
import com.goldemperor.R;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class PgdAdapter extends SwipeMenuAdapter<PgdAdapter.DefaultViewHolder> {

    private List<WorkCardPlan > ls;

    private OnItemClickListener mOnItemClickListener;

    public static PgdActivity pgdActivity;
    public static List<ScrollListenerHorizontalScrollView> ScrollViewList=new ArrayList<ScrollListenerHorizontalScrollView>();
    public PgdAdapter(List<WorkCardPlan> ls,PgdActivity pgdActivity) {
        this.ls = ls;
        this.pgdActivity=pgdActivity;
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
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_pgd_item1, parent, false);
    }

    @Override
    public PgdAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {

        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
        viewHolder.mOnItemClickListener = mOnItemClickListener;
        ScrollViewList.add(viewHolder.ScrollView);
        Log.e("jindi","ScrollViewList"+ScrollViewList.size());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PgdAdapter.DefaultViewHolder holder, int position) {
        holder.setData(ls.get(position));
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,ScrollListenerHorizontalScrollView.OnScrollListener{

        OnItemClickListener mOnItemClickListener;
        LinearLayout linearLayout;
        public ScrollListenerHorizontalScrollView ScrollView;
        TextView tv_followNumber;
        TextView tv_number;
        TextView tv_date;
        TextView tv_group;
        TextView tv_style;


        TextView tv_materialcode;
        TextView tv_materialname;
        TextView tv_unit;
        TextView tv_batch;
        TextView tv_planstarttime;
        TextView tv_planendtime;

        TextView tv_size35;
        TextView tv_size36;
        TextView tv_size37;
        TextView tv_size38;
        TextView tv_size39;
        TextView tv_size40;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.item);
            linearLayout.setOnClickListener(this);
            ScrollView= (ScrollListenerHorizontalScrollView) itemView.findViewById(R.id.ScrollView);
            ScrollView.setOnScrollListener(this);

            //ScrollView.setOnScrollStateChangedListener(this);
            //ScrollView.setOnScrollChangeListener(this);
            tv_followNumber = (TextView) itemView.findViewById(R.id.tv_followNumber);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_group = (TextView) itemView.findViewById(R.id.tv_group);
            tv_style= (TextView) itemView.findViewById(R.id.tv_style);


            tv_materialcode = (TextView) itemView.findViewById(R.id.tv_materialcode);
            tv_materialname = (TextView) itemView.findViewById(R.id.tv_materialname);
            tv_unit = (TextView) itemView.findViewById(R.id.tv_unit);
            tv_batch = (TextView) itemView.findViewById(R.id.tv_batch);
            tv_planstarttime= (TextView) itemView.findViewById(R.id.tv_planstarttime);
            tv_planendtime= (TextView) itemView.findViewById(R.id.tv_planendtime);

            tv_size35 = (TextView) itemView.findViewById(R.id.tv_size35);
            tv_size36 = (TextView) itemView.findViewById(R.id.tv_size36);
            tv_size37 = (TextView) itemView.findViewById(R.id.tv_size37);
            tv_size38= (TextView) itemView.findViewById(R.id.tv_size38);
            tv_size39= (TextView) itemView.findViewById(R.id.tv_size39);
            tv_size40= (TextView) itemView.findViewById(R.id.tv_size40);
        }

        public void setData(WorkCardPlan o) {
            tv_followNumber.setText(o.getPlanbill());
            tv_number.setText(o.getOrderbill());
            try {
                tv_date.setText(Utils.dateConvert(o.getOrderdate()));
                tv_planstarttime.setText(Utils.dateConvert(o.getPlanstarttime()));
                tv_planendtime.setText(Utils.dateConvert(o.getPlanendtime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tv_group.setText(o.getFgroup());
            tv_style.setText(o.getPlantbody());

            tv_materialcode.setText(o.getMaterialcode());
            tv_materialname.setText(o.getMaterialname());
            tv_unit.setText(o.getUnit());
            tv_batch.setText(o.getBatch());

            tv_size35.setText(o.getFsize35());
            tv_size36.setText(o.getFsize36());
            tv_size37.setText(o.getFsize37());
            tv_size38.setText(o.getFsize38());
            tv_size39.setText(o.getFsize39());
            tv_size40.setText(o.getFsize40());

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }

        }

        @Override
        public void onScroll(int scrollX) {
            for(int i=0;i<ScrollViewList.size();i++){
                ScrollViewList.get(i).scrollTo(scrollX,ScrollView.getScrollY());
            }
            pgdActivity.ScrollView.scrollTo(scrollX,ScrollView.getScrollY());

        }
    }

}
