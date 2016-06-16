package com.pda.birdex.pda.utils.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

// Glide 在默认的 RGB_565 
// Bitmap 的格式转换到 ARGB_8888
public class GlideConfiguration implements GlideModule {

	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		// Apply options to the builder here.
		builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
	}

	@Override
	public void registerComponents(Context context, Glide glide) {
		// register ModelLoaders here.
	}

}
