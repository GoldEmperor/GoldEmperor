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

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.Utils;
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

    private List<WorkCardPlan> ls;

    private OnItemClickListener mOnItemClickListener;

    public static PgdActivity pgdActivity;

    public static List<ScrollListenerHorizontalScrollView> ScrollViewList = new ArrayList<ScrollListenerHorizontalScrollView>();

    public PgdAdapter(List<WorkCardPlan> ls, PgdActivity pgdActivity) {
        this.ls = ls;
        this.pgdActivity = pgdActivity;

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
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PgdAdapter.DefaultViewHolder holder, int position) {
        holder.setData(ls.get(position));
    }


    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ScrollListenerHorizontalScrollView.OnScrollListener {

        OnItemClickListener mOnItemClickListener;
        LinearLayout linearLayout;

        LinearLayout sizeItemLinearLayout;
        LinearLayout sizeNumItemLinearLayout;
        public ScrollListenerHorizontalScrollView ScrollView;
        TextView tv_followNumber;
        TextView tv_number;
        TextView tv_date;
        TextView tv_group;
        TextView tv_style;

        TextView tv_alreadynumber;
        TextView tv_nonumber;


        TextView tv_materialcode;
        TextView tv_materialname;
        TextView tv_unit;
        TextView tv_batch;
        TextView tv_planstarttime;
        TextView tv_count;

        TextView tv_planendtime;

        TextView tv_status;

        TextView tv_orderCount;

        TextView tv_reportednumber;

        TextView tv_reportednotnumber;

        TextView tv_reportednotInnumber;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.item);

            sizeItemLinearLayout = (LinearLayout) itemView.findViewById(R.id.sizeItem);

            sizeNumItemLinearLayout = (LinearLayout) itemView.findViewById(R.id.sizeNumItem);

            linearLayout.setOnClickListener(this);
            ScrollView = (ScrollListenerHorizontalScrollView) itemView.findViewById(R.id.ScrollView);
            ScrollView.setOnScrollListener(this);

            //ScrollView.setOnScrollStateChangedListener(this);
            //ScrollView.setOnScrollChangeListener(this);
            tv_orderCount = (TextView) itemView.findViewById(R.id.tv_orderCount);
            tv_followNumber = (TextView) itemView.findViewById(R.id.tv_followNumber);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_group = (TextView) itemView.findViewById(R.id.tv_group);
            tv_style = (TextView) itemView.findViewById(R.id.tv_style);

            tv_alreadynumber= (TextView) itemView.findViewById(R.id.tv_alreadynumber);
            tv_nonumber= (TextView) itemView.findViewById(R.id.tv_nonumber);

            tv_materialcode = (TextView) itemView.findViewById(R.id.tv_materialcode);
            tv_materialname = (TextView) itemView.findViewById(R.id.tv_materialname);
            tv_unit = (TextView) itemView.findViewById(R.id.tv_unit);
            tv_batch = (TextView) itemView.findViewById(R.id.tv_batch);
            tv_planstarttime = (TextView) itemView.findViewById(R.id.tv_planstarttime);
            tv_planendtime = (TextView) itemView.findViewById(R.id.tv_planendtime);
            tv_status= (TextView) itemView.findViewById(R.id.tv_status);

            tv_count = (TextView) itemView.findViewById(R.id.tv_count);


            tv_reportednumber = (TextView) itemView.findViewById(R.id.tv_reportednumber);

            tv_reportednotnumber = (TextView) itemView.findViewById(R.id.tv_reportednotnumber);

            tv_reportednotInnumber = (TextView) itemView.findViewById(R.id.tv_reportednotInnumber);
        }

        public void setData(WorkCardPlan o) {
            tv_orderCount.setText(String.valueOf(o.getFconfirmqty().intValue()));
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


            tv_alreadynumber.setText(String.valueOf(o.getAlreadynumberCount()));
            tv_nonumber.setText(String.valueOf(o.getNonumberCount()));
            tv_status.setText(o.getPlanStatus());
            if(o.getPlanStatus().equals("未排")){
                tv_status.setTextColor(Color.rgb(255, 0, 0));
            }else{
                tv_status.setTextColor(Color.rgb(0, 0, 0));
            }
            int count = 0;
            sizeItemLinearLayout.removeAllViews();
            sizeNumItemLinearLayout.removeAllViews();

            tv_reportednumber.setText(String.valueOf(o.getReportednumber().intValue()));

            tv_reportednotnumber.setText(String.valueOf(o.getReportednotnumber().intValue()));

            tv_reportednotInnumber.setText(String.valueOf((o.getReportednotInnumber()==null?0:o.getReportednotInnumber()).intValue()));

            for (int i = 0; i < o.getSizeList().size(); i++) {
                sizeItemLinearLayout.addView(addSizeView(o.getSizeList().get(i)[0][0]));
                sizeNumItemLinearLayout.addView(addSizeView(o.getSizeList().get(i)[0][1]));
                count += Integer.valueOf(o.getSizeList().get(i)[0][1]);
            }
            tv_count.setText(String.valueOf(count));


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
            pgdActivity.ScrollView.scrollTo(scrollX, ScrollView.getScrollY());

        }

        private View addSizeView(String text) {
            // TODO 动态添加布局(xml方式)
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    70, LinearLayout.LayoutParams.WRAP_CONTENT);
            LayoutInflater inflater3 = LayoutInflater.from(pgdActivity);
            View view = inflater3.inflate(R.layout.activity_gxpg_size_view, null);
            view.setLayoutParams(lp);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(text);
            return view;

        }
    }


}
