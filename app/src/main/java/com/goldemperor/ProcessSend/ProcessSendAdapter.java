
package com.goldemperor.ProcessSend;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goldemperor.R;
import com.goldemperor.Utils.LOG;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
import com.goldemperor.model.ProcessSendModel;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * File Name : ProcessSendAdapter
 * Created by : PanZX on  2018/4/27 9:08
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class ProcessSendAdapter extends SwipeMenuAdapter<ProcessSendAdapter.DefaultViewHolder> {

    private List<ProcessSendModel> PSML;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public static ProcessSendActvity pgdActivity;

    public static List<ScrollListenerHorizontalScrollView> ScrollViewList = new ArrayList<ScrollListenerHorizontalScrollView>();

    public ProcessSendAdapter(List<ProcessSendModel> ls, ProcessSendActvity pgdActivity) {
        this.PSML = ls;
        this.pgdActivity = pgdActivity;
        LOG.e("ProcessSendAdapter="+PSML.size());

    }

//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.mOnItemClickListener = onItemClickListener;
//    }
    public void Updata(List<ProcessSendModel> psml){
        PSML=psml;
        notifyDataSetChanged();
        LOG.e("Updata="+PSML.size());
    }
    @Override
    public int getItemCount() {
        LOG.e("getItemCount="+PSML.size());
        return PSML == null ? 0 : PSML.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        LOG.e("onCreateContentView");
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_process_send_item, parent, false);
    }

    @Override
    public ProcessSendAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        LOG.e("onCompatCreateViewHolder");
        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
//        viewHolder.mOnItemClickListener = mOnItemClickListener;
        ScrollViewList.add(viewHolder.SV_ProcessSend_Item);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProcessSendAdapter.DefaultViewHolder holder, int position) {
        LOG.e("onBindViewHolder");
        holder.setData(PSML.get(position));
    }


    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ScrollListenerHorizontalScrollView.OnScrollListener {

        public ScrollListenerHorizontalScrollView SV_ProcessSend_Item;
        TextView TV_FSingleDispatchedProcessQty;//已直推派工数量
        TextView TV_Organization;//生产组织
        TextView TV_MONumber;//生产订单编号
        TextView TV_Department;//生产车间
        TextView TV_FPlanStartDate;//计划开工日期
        TextView TV_FPlanFinishDate;//计划完工日期
        TextView TV_FBillNO;//工序计划单号
        TextView TV_FMtono;//计划跟踪号
        TextView TV_ProductNumber;//产品编码
        TextView TV_ProductName;//产品名称
        TextView TV_PartName;//部件
        TextView TV_ItemFNumber;//物料编码
        TextView TV_ItemName;//物料名称
        TextView TV_FProcessNumber;//工序编码
        TextView TV_FProcessNumber1;//工序名称
        TextView TV_FSize;//尺码
        TextView TV_FProcessQty;//工序数量
        TextView TV_FReqPoOrderQty;//已委外工序数量
        TextView TV_FDispatchedProcessQty;//已派工数量
        TextView TV_FFinishQty;//已汇报数量
        TextView TV_FReMainDispatchedProcessQty;//未派未委外数量
        TextView TV_FReMainReportQty;//未汇报数量

        public DefaultViewHolder(View itemView) {
            super(itemView);
            SV_ProcessSend_Item = (ScrollListenerHorizontalScrollView) itemView.findViewById(R.id.SV_ProcessSend_Item);

            TV_FSingleDispatchedProcessQty = (TextView) itemView.findViewById(R.id.TV_FSingleDispatchedProcessQty);
            TV_Organization = (TextView) itemView.findViewById(R.id.TV_Organization);
            TV_MONumber = (TextView) itemView.findViewById(R.id.TV_MONumber);
            TV_Department = (TextView) itemView.findViewById(R.id.TV_Department);
            TV_FPlanStartDate = (TextView) itemView.findViewById(R.id.TV_FPlanStartDate);
            TV_FPlanFinishDate = (TextView) itemView.findViewById(R.id.TV_FPlanFinishDate);
            TV_FBillNO = (TextView) itemView.findViewById(R.id.TV_FBillNO);
            TV_FMtono = (TextView) itemView.findViewById(R.id.TV_FMtono);
            TV_ProductNumber = (TextView) itemView.findViewById(R.id.TV_ProductNumber);
            TV_ProductName = (TextView) itemView.findViewById(R.id.TV_ProductName);
            TV_PartName = (TextView) itemView.findViewById(R.id.TV_PartName);
            TV_ItemFNumber = (TextView) itemView.findViewById(R.id.TV_ItemFNumber);
            TV_ItemName = (TextView) itemView.findViewById(R.id.TV_ItemName);
            TV_FProcessNumber = (TextView) itemView.findViewById(R.id.TV_FProcessNumber);
            TV_FProcessNumber1 = (TextView) itemView.findViewById(R.id.TV_FProcessNumber1);
            TV_FSize = (TextView) itemView.findViewById(R.id.TV_FSize);
            TV_FProcessQty = (TextView) itemView.findViewById(R.id.TV_FProcessQty);
            TV_FReqPoOrderQty = (TextView) itemView.findViewById(R.id.TV_FReqPoOrderQty);
            TV_FDispatchedProcessQty = (TextView) itemView.findViewById(R.id.TV_FDispatchedProcessQty);
            TV_FFinishQty = (TextView) itemView.findViewById(R.id.TV_FFinishQty);
            TV_FReMainDispatchedProcessQty = (TextView) itemView.findViewById(R.id.TV_FReMainDispatchedProcessQty);
            TV_FReMainReportQty = (TextView) itemView.findViewById(R.id.TV_FReMainReportQty);

            SV_ProcessSend_Item.setOnScrollListener(this);
        }

        public void setData(ProcessSendModel psm) {
            TV_FSingleDispatchedProcessQty.setText(psm.getFSingleDispatchedProcessQty()+"");
            TV_Organization.setText(psm.getOrganization()==null?"":psm.getOrganization());
            TV_MONumber.setText(psm.getMONumber()==null?"":psm.getMONumber());
            TV_Department.setText(psm.getDepartment()==null?"":psm.getDepartment());
            TV_FPlanStartDate.setText(psm.getFPlanStartDate()==null?"":psm.getFPlanStartDate());
            TV_FPlanFinishDate.setText(psm.getFPlanFinishDate()==null?"":psm.getFPlanFinishDate());
            TV_FBillNO.setText(psm.getFBillNO()==null?"":psm.getFBillNO());
            TV_FMtono.setText(psm.getFMtono()==null?"":psm.getFMtono());
            TV_ProductNumber.setText(psm.getProductNumber()==null?"":psm.getProductNumber());
            TV_ProductName.setText(psm.getProductName()==null?"":psm.getProductName());
            TV_PartName.setText(psm.getPartName()==null?"":psm.getPartName());
            TV_ItemFNumber.setText(psm.getItemFNumber()==null?"":psm.getItemFNumber());
            TV_ItemName.setText(psm.getItemName()==null?"":psm.getItemName());
            TV_FProcessNumber.setText(psm.getFProcessNumber()==null?"":psm.getFProcessNumber());
            TV_FProcessNumber1.setText(psm.getFProcessNumber1()==null?"":psm.getFProcessNumber1());
            TV_FSize.setText(psm.getFSize()==null?"":psm.getFSize());
            TV_FProcessQty.setText(String.valueOf(psm.getFProcessQty()));
            TV_FReqPoOrderQty.setText(psm.getFReqPoOrderQty()+"");
            TV_FDispatchedProcessQty.setText(psm.getFDispatchedProcessQty()+"");
            TV_FFinishQty.setText(psm.getFFinishQty()+"");
            TV_FReMainDispatchedProcessQty.setText(psm.getFReMainDispatchedProcessQty()+"");
            TV_FReMainReportQty.setText(psm.getFReMainReportQty()+"");
        }

        @Override
        public void onClick(View v) {
//            if (mOnItemClickListener != null) {
//                mOnItemClickListener.onItemClick(getAdapterPosition());
//            }

        }

        @Override
        public void onScroll(int scrollX) {
            for (int i = 0; i < ScrollViewList.size(); i++) {
                ScrollViewList.get(i).scrollTo(scrollX, SV_ProcessSend_Item.getScrollY());
            }
            pgdActivity.SV_ProcessSend.scrollTo(scrollX, SV_ProcessSend_Item.getScrollY());

        }


    }


}
