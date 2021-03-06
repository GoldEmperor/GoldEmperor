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
package com.goldemperor.GxReport;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.define;
import com.goldemperor.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class MenuAdapter extends SwipeMenuAdapter<MenuAdapter.DefaultViewHolder> {

    private ArrayList<Order> ls;

    private OnItemClickListener mOnItemClickListener;

    private SharedPreferences dataPref;

    public MenuAdapter(ArrayList<Order> ls,Context con) {
        this.ls = ls;
        dataPref = con.getSharedPreferences(define.SharedName, 0);
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
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gxreport_item_menu, parent, false);
    }

    @Override
    public MenuAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
        viewHolder.mOnItemClickListener = mOnItemClickListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MenuAdapter.DefaultViewHolder holder, int position) {
        holder.setData(ls.get(position),dataPref);
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClickListener mOnItemClickListener;
        TextView tv_number;
        TextView tv_id;
        TextView tv_name;
        TextView tv_qua;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_qua= (TextView) itemView.findViewById(R.id.tv_qua);
        }

        public void setData(Order o,SharedPreferences dataPref) {
            tv_number.setText("单号:"+o.getFCardNo());
            tv_id.setText("工号:"+dataPref.getString(define.SharedJobNumber,"无"));
            tv_name.setText("姓名:"+dataPref.getString(define.SharedUser,"无"));
            tv_qua.setText("数量:"+o.getFQty());
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

}
