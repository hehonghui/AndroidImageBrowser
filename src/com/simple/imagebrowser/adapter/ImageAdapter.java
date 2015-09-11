package com.simple.imagebrowser.adapter;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.simple.imagebrowser.R;

/**
 * 显示图片的ImageAdapter基类,用户只需要覆写displayImage函数即可实现使用不同的ImageLoader进行加载图片,使得扩展性更好.
 * 
 * PhotoView的ScaleType在这里设置为fitCenter.更ScaleType请参考
 * http://blog.csdn.net/tanranran/article/details/41039535
 * 
 * @author mrsimple
 * 
 */
public abstract class ImageAdapter extends PagerAdapter {
	protected final List<String> mImageUrls = new ArrayList<String>();
	protected Context mContext = null;
	protected LayoutInflater mLayoutInflater;
	private OnViewTapListener mOnViewTapListener;
	private Bitmap mCurrentBitmap ;

	/**
	 * 
	 * @param context
	 *            Context
	 * @param imageList
	 *            要展示的图片列表
	 */
	public ImageAdapter(Context context, List<String> imageList) {
		mContext = context;
		mImageUrls.addAll(imageList);
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mImageUrls.size();
	}

	public String getItem(int position) {
		return mImageUrls.get(position);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	/**
	 * 
	 * @param container
	 * @param position
	 * @return
	 */
	private ImageViewHolder createItemView(ViewGroup container, int position) {
		View itemView = mLayoutInflater.inflate(R.layout.image_item, container,
				false);
		ImageViewHolder viewHolder = new ImageViewHolder(itemView);
		return viewHolder;
	}

	@Override
	public final Object instantiateItem(ViewGroup container, int position) {
		ImageViewHolder viewHolder = createItemView(container, position);
		// 设置点按事件
		viewHolder.photoView.setOnViewTapListener(mOnViewTapListener);
		// 加载图片
		displayImage(viewHolder, position, getItem(position));
		// 将该视图添加到ViewGroup中
		container.addView(viewHolder.itemView);
		return viewHolder.itemView;
	}

	/**
	 * 设置点击PhotoView时的事件处理
	 * 
	 * @param listener
	 */
	public void setOnViewTapListener(OnViewTapListener listener) {
		mOnViewTapListener = listener;
	}
	
	public void updateCurrentBitmap(BitmapDrawable drawable) {
		mCurrentBitmap = drawable.getBitmap() ;
	}
	
	public void updateCurrentBitmap(Bitmap bitmap) {
		mCurrentBitmap = bitmap;
	}
	
	public Bitmap getCurrentBitmap() {
		return mCurrentBitmap;
	}

	/**
	 * 加载图片,子类需要实现这个函数
	 * 
	 * @param viewHolder
	 * @param position
	 * @param imageUrl
	 */
	protected abstract void displayImage(ImageViewHolder viewHolder,
			int position, String imageUrl);

	/**
	 * ImageViewHolder, 存储显示图片布局的各个子视图
	 * 
	 * @author mrsimple
	 * 
	 */
	public static class ImageViewHolder {
		/**
		 * ViewPager的Item View
		 */
		public View itemView;
		/**
		 * 显示图片的PhotoView, [定制的PhotoView]
		 */
		public PhotoView photoView;
		/**
		 * 显示加载进度的Progressbar
		 */
		public ProgressBar progressBar;

		/**
		 * @param itemView
		 */
		public ImageViewHolder(View itemView) {
			this.itemView = itemView;
			photoView = (PhotoView) itemView.findViewById(R.id.imageView);
			progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
		}

	}

}
