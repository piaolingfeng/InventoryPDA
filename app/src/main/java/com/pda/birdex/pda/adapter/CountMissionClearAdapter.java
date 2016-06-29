package com.pda.birdex.pda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.entity.TakingOrder;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/6/22.
 */
public class CountMissionClearAdapter extends RecyclerView.Adapter<CountMissionClearAdapter.CountMissionClearAdapterHolder> {

    Context mContext;
    List<TakingOrder> takingOrders;

    OnRecycleViewItemClickListener onRecycleViewItemClickListener;

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        this.onRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    public void setTakingOrders(List<TakingOrder> takingOrders) {
        this.takingOrders = takingOrders;
    }

    public CountMissionClearAdapter(Context mContext, List<TakingOrder> takingOrders) {
        this.mContext = mContext;
        this.takingOrders = takingOrders;
    }

    @Override
    public CountMissionClearAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CountMissionClearAdapterHolder(LayoutInflater.from(mContext).inflate(R.layout.item_countbussiness_layout, null));
    }

    @Override
    public void onBindViewHolder(CountMissionClearAdapterHolder holder, int position) {
        holder.position = position;
        holder.pll_item.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        holder.tv_clear_num.setText(takingOrders.get(position).getBaseInfo().getTakingOrderNo());
        holder.tv_last_time.setText(takingOrders.get(position).getBaseInfo().getDeadLine());
        holder.tv_status.setText(takingOrders.get(position).getBaseInfo().getTakingStatus());
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (takingOrders != null)
            size = takingOrders.size();
        return size;
    }

    public class CountMissionClearAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.tv_clear_num)
        TextView tv_clear_num;
        @Bind(R.id.tv_status)
        TextView tv_status;
        @Bind(R.id.tv_last_time)
        TextView tv_last_time;
        @Bind(R.id.pll_item)
        PercentLinearLayout pll_item;
        int position = 0;

        public CountMissionClearAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onRecycleViewItemClickListener != null) {
                onRecycleViewItemClickListener.onItemClick(position);
            }
        }
    }
}
