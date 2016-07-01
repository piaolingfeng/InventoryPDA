package com.pda.birdex.pda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.entity.Merchant;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.widget.BadgeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/6/23.
 */
public class MerchantOtherAdapter extends RecyclerView.Adapter<MerchantOtherAdapter.CountMissionItemAdapterHolder> {

    Context mContext;
    OnRecycleViewItemClickListener onRecycleViewItemClickListener;
    List<Merchant> list;

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        this.onRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    public MerchantOtherAdapter(Context mContext, List<Merchant> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void setList(List<Merchant> list) {
        this.list = list;
    }

    @Override
    public CountMissionItemAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CountMissionItemAdapterHolder(LayoutInflater.from(mContext).inflate(R.layout.item_countbussiness_other_layout, null));
    }

    @Override
    public void onBindViewHolder(CountMissionItemAdapterHolder holder, int position) {
        holder.position = position;
        holder.tv_title.setText(list.get(position).getMerchantName());
        if (list.get(position).getCount() != 0) {
            holder.badge_count.setVisibility(View.VISIBLE);
            if (list.get(position).getCount() > 99) {
                holder.badge_count.setText("99+");
            } else {
                holder.badge_count.setText(list.get(position).getCount()+"");
            }
        } else {
            holder.badge_count.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (list != null)
            size = list.size();
        else
            size = 0;
        return size;
    }

    public class CountMissionItemAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.badge_count)
        BadgeView badge_count;
        int position = 0;

        public CountMissionItemAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
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
