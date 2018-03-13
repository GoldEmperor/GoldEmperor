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
package com.goldemperor.GylxActivity;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.Utils;
import com.goldemperor.PgdActivity.PgdActivity;
import com.goldemperor.PgdActivity.RouteEntry;
import com.goldemperor.PgdActivity.WorkCardPlan;
import com.goldemperor.R;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class GylxAdapter extends SwipeMenuAdapter<GylxAdapter.DefaultViewHolder> {

    private List<RouteEntry> ls;

    private OnItemClickListener mOnItemClickListener;



    public GylxAdapter(List<RouteEntry> ls) {
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
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gylx_item, parent, false);
    }

    @Override
    public GylxAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {

        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
        viewHolder.mOnItemClickListener = mOnItemClickListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GylxAdapter.DefaultViewHolder holder, int position) {
        holder.setData(ls.get(position));
    }


    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnItemClickListener mOnItemClickListener;

        TextView tv_processcode;
        TextView tv_processname;
        TextView tv_price;
        TextView tv_part;

        TextView tv_note;

        public DefaultViewHolder(View itemView) {
            super(itemView);


            tv_processcode = (TextView) itemView.findViewById(R.id.tv_processcode);
            tv_processname = (TextView) itemView.findViewById(R.id.tv_processname);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);

            tv_part = (TextView) itemView.findViewById(R.id.tv_part);
            tv_note = (TextView) itemView.findViewById(R.id.tv_note);
        }

        public void setData(RouteEntry o) {

            tv_processcode.setText(o.getFprocessnumber());
            tv_processname.setText(o.getFprocessname());
            tv_price.setText(String.valueOf(o.getFprice()));
            tv_part.setText(o.getPartname());
            tv_note.setText(o.getFnote());
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }

        }
    }


}
