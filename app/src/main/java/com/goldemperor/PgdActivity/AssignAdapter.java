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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.R;
import com.goldemperor.Widget.PinchImageView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class AssignAdapter extends SwipeMenuAdapter<AssignAdapter.DefaultViewHolder> {

    private List<GxpgPlan> ls;

    private OnItemClickListener mOnItemClickListener;



    public AssignAdapter(List<GxpgPlan> ls) {
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
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gxpg_assign_list, parent, false);
    }

    @Override
    public AssignAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {

        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
        viewHolder.mOnItemClickListener = mOnItemClickListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AssignAdapter.DefaultViewHolder holder, int position) {
        holder.setData(ls.get(position));
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnItemClickListener mOnItemClickListener;
        private TextView tv_planBody;

        private TextView tv_itemname;
        private TextView tv_staff;

        private TextView tv_per;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            tv_itemname = (TextView) itemView.findViewById(R.id.tv_itemname);

            tv_planBody = (TextView) itemView.findViewById(R.id.tv_planBody);
            tv_staff = (TextView) itemView.findViewById(R.id.tv_staff);

            tv_per = (TextView) itemView.findViewById(R.id.tv_per);
        }

        public void setData(GxpgPlan gxpgPlan) {
            tv_planBody.setText(gxpgPlan.getStyle());
            tv_itemname.setText(gxpgPlan.getProcessname());
            tv_staff.setText(gxpgPlan.getUsername());
            DecimalFormat df = new DecimalFormat("#0.00");
            tv_per.setText(df.format(gxpgPlan.getPer()));
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }

        }
    }

}
