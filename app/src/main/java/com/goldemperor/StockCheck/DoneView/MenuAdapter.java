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
package com.goldemperor.StockCheck.DoneView;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goldemperor.StockCheck.sql.stock_check;
import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.R;
import com.goldemperor.MainActivity.define;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class MenuAdapter extends SwipeMenuAdapter<MenuAdapter.DefaultViewHolder> {

    private List<stock_check> ls;

    private OnItemClickListener mOnItemClickListener;

    public MenuAdapter(List<stock_check> ls) {
        this.ls = ls;
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
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
    }

    @Override
    public MenuAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
        viewHolder.mOnItemClickListener = mOnItemClickListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MenuAdapter.DefaultViewHolder holder, int position) {
        holder.setData(ls.get(position));
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_number;
        TextView tv_date;
        TextView tv_proposer;
        TextView tv_supplier;
        TextView tv_status;
        OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            tv_proposer = (TextView) itemView.findViewById(R.id.tv_proposer);
            tv_supplier = (TextView) itemView.findViewById(R.id.tv_supplier);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
        }

        public void setData(stock_check sc) {
            tv_number.setText("单号:"+sc.getNumber());
            tv_date.setText("日期:"+sc.getApplydate());
            tv_proposer.setText("申请人:"+sc.getProposer());
            String supplier = sc.getSupplier() == null ? "未填" : sc.getSupplier();
            tv_supplier.setText("供应商:" + supplier);
            if(sc.getStatus().equals(define.DONE)) {
                tv_status.setText("状态:已签收");
                SpannableStringBuilder builder = new SpannableStringBuilder(tv_status.getText().toString());
                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                builder.setSpan(redSpan, 3, tv_status.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_status.setText(builder);
            }else  if(sc.getStatus().equals(define.CASECLOSE)) {
                tv_status.setText("状态:已结案");
            }

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

}
