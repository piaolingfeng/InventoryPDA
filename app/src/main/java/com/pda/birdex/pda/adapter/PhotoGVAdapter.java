package com.pda.birdex.pda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.utils.glide.GlideUtils;

import java.util.List;

/**
 * Created by hyj on 2016/3/11.
 */
public class PhotoGVAdapter extends BaseAdapter{

    // 传过来的 list bitmap
    private List<String> bitmapList;

    // 传过来的 context
    private Context context;

    public PhotoGVAdapter(Context context, List<String> bitmapList){
        this.context = context;
        this.bitmapList = bitmapList;
    }

    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Override
    public Object getItem(int position) {
        if(position + 1 > bitmapList.size())
            return bitmapList.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.photo_gv_adapter,null);
            vh = new ViewHolder();
            vh.iv = (ImageView) convertView.findViewById(R.id.gv_iv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

//        Bitmap bitmap = bitmapList.get(position);
//        vh.iv.setImageBitmap(bitmap);
        ViewGroup.LayoutParams params = vh.iv.getLayoutParams();
        params.height = params.width;
        vh.iv.setLayoutParams(params);
        GlideUtils.setImageToLocalPath1(vh.iv, bitmapList.get(position));


        return convertView;
    }

    class ViewHolder {
        ImageView iv;
    }
}
