package com.pda.birdex.pda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.response.CommonItemEntity;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/6/16.
 */
public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexAdapterHolder> {

    // com.pda.birdex.pda.widget.BadgeView 值
    List<CommonItemEntity> list;

    private Context mContext;
    OnRecycleViewItemClickListener onRecycleViewItemClickListener;


    public IndexAdapter(Context mContext, List<CommonItemEntity> list) {
        this.list = list;
        this.mContext = mContext;
    }

    public void setList(List<CommonItemEntity> list) {
        this.list = list;
    }

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        this.onRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    @Override
    public IndexAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IndexAdapterHolder(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_layout, null));
    }


    @Override
    public void onBindViewHolder(IndexAdapterHolder holder, int position) {
        holder.position = position;
        holder.item_context.setText(list.get(position).getName());
        if (list.get(position).getCount() != "") {
            holder.item_bv.setVisibility(View.VISIBLE);
            int count = Integer.parseInt(list.get(position).getCount());
            if (count > 99) {
                holder.item_bv.setText("99+");
            } else {
                if(count == 0)
                    holder.item_bv.setVisibility(View.GONE);
                else
                holder.item_bv.setText(list.get(position).getCount());
            }
        } else {
            holder.item_bv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (list != null) {
            size = list.size();
        }
        return size;
    }

    public class IndexAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int position = 0;

        @Bind(R.id.item_context)
        TextView item_context;

        @Bind(R.id.item_bv)
        com.pda.birdex.pda.widget.BadgeView item_bv;

        public IndexAdapterHolder(View itemView) {
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
