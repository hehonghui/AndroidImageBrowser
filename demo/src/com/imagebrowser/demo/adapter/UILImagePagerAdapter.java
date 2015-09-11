package com.imagebrowser.demo.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.simple.imagebrowser.adapter.ImageAdapter;

/**
 * 使用Universal ImageLoader加载的ImagePagerAdapter
 * 
 * @author mrsimple
 * 
 */
public class UILImagePagerAdapter extends ImageAdapter {
	/**
	 * 
	 */
	private static ImageLoaderConfiguration sConfig;
	/**
	 * 
	 */
	private DisplayImageOptions mDisplayOptions = null;

	/**
	 * 
	 * @param context
	 * @param list
	 * @param options
	 */
	public UILImagePagerAdapter(Context context, List<String> list) {
		super(context, list);
		initDisplayOptions();
		initImageLoader(mContext);
	}

	/**
	 * 初始化ImageLoader
	 * 
	 * @param context
	 */
	private static void initImageLoader(Context context) {
		if (sConfig != null) {
			return;
		}

		File cacheDir = StorageUtils.getCacheDirectory(context);
		sConfig = new ImageLoaderConfiguration.Builder(context)
				.memoryCacheExtraOptions(480, 800)
				// default = device screen dimensions
				.denyCacheImageMultipleSizesInMemory()
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// default
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(10 * 1024 * 1024))
				.memoryCacheSize(10 * 1024 * 1024)
				.memoryCacheSizePercentage(13)
				.diskCache(new UnlimitedDiskCache(cacheDir))
				// default
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(context)) // default
				.imageDecoder(new BaseImageDecoder(true)) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs().build();

		ImageLoader.getInstance().init(sConfig);
	}

	private void initDisplayOptions() {
		this.mDisplayOptions = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	/*
	 * (non-Javadoc)
	 * @see com.example.android_imageviewer.ImageAdapter#displayImage(com.example.android_imageviewer.ImageAdapter.ImageViewHolder, int, java.lang.String)
	 */
	protected void displayImage(final ImageViewHolder viewHolder, int position,
			String imageUrl) {
		// 加载图片
		ImageLoader.getInstance().displayImage(imageUrl, viewHolder.photoView,
				mDisplayOptions, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						viewHolder.progressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
//						viewHolder.imageViewAttacher.update();
						viewHolder.progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						viewHolder.photoView.setImageBitmap(loadedImage);
//						viewHolder.imageViewAttacher.update();
						viewHolder.progressBar.setVisibility(View.GONE);
						// 更新当前bitmap
						updateCurrentBitmap(loadedImage) ;
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						viewHolder.progressBar.setVisibility(View.GONE);
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {
						Log.i("", "onProgressUpdate " + current + "/" + "total");
					}
				});
	};
}
