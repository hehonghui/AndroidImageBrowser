package com.imagebrowser.demo.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.simple.imagebrowser.adapter.ImageAdapter;

/**
 * 使用Universal ImageLoader加载的ImagePagerAdapter
 * 
 * @author mrsimple
 * 
 */
public class GlideImagePagerAdapter extends ImageAdapter {

	/**
	 * 
	 * @param context
	 * @param list
	 * @param options
	 */
	public GlideImagePagerAdapter(Context context, List<String> list) {
		super(context, list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.android_imageviewer.ImageAdapter#displayImage(com.example
	 * .android_imageviewer.ImageAdapter.ImageViewHolder, int, java.lang.String)
	 */
	protected void displayImage(final ImageViewHolder viewHolder, int position,
			String imageUrl) {

		Glide.with(mContext).load(imageUrl).asBitmap()
				.into(new SimpleTarget<Bitmap>() {
					@Override
					public void onResourceReady(Bitmap result,
							GlideAnimation<? super Bitmap> arg1) {
						// 更新
						viewHolder.photoView.setImageBitmap(result);
//						viewHolder.imageViewAttacher.update() ;
						updateCurrentBitmap(result);
					}
				});

		// 不需要保存图片时直接这么写即可
		// Glide.with(mContext).load(imageUrl).into(viewHolder.photoView) ;
	};
}
