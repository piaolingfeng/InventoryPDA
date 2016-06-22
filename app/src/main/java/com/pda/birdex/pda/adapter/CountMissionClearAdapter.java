package com.pda.birdex.pda.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chuming.zhuang on 2016/6/22.
 */
public class CountMissionClearAdapter extends RecyclerView.Adapter<CountMissionClearAdapter.CountMissionClearAdapterHolder> {

    @Override
    public CountMissionClearAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CountMissionClearAdapterHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CountMissionClearAdapterHolder extends RecyclerView.ViewHolder{

        public CountMissionClearAdapterHolder(View itemView) {
            super(itemView);
        }
    }
}
