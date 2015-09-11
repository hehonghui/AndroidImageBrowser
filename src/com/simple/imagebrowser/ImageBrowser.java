package com.simple.imagebrowser;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.simple.imagebrowser.adapter.ImageAdapter;

/**
 * 图片浏览Dialog
 * 
 * @author mrsimple
 * 
 */
public class ImageBrowser extends Dialog implements
		ViewPager.OnPageChangeListener, PhotoViewAttacher.OnViewTapListener {

	private ViewPager mViewPager = null;
	private ImageAdapter mAdapter;
	private TextView mTextView;
	private TextView mSaveTv;
	private int mImagePosition = 0;
	private int mImageCount = 0;
	private boolean isTapDismiss = false;
	private ImageSavePresenter mPresenter;

	/**
	 * 
	 * @param context
	 */
	public ImageBrowser(Context context) {
		super(context, R.style.imagebrowser_dialog_style);
		setContentView(R.layout.imagebrowser_layout);
		setupWidgets();
		mPresenter = new ImageSavePresenter(context);
	}

	/**
	 * 初始化显示图片的ViewPager
	 */
	private void setupWidgets() {
		mViewPager = (ViewPager) findViewById(R.id.image_viewpager);
		mViewPager.setOnPageChangeListener(this);
		// 索引指示器
		mTextView = (TextView) findViewById(R.id.indicator_tv);

		mSaveTv = (TextView) findViewById(R.id.save_tv);
		mSaveTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = mAdapter.getItem(mImagePosition);
				Toast.makeText(getContext(), getSavedTips(url),
						Toast.LENGTH_SHORT).show();
				// 保存图片
				mPresenter.saveImage(url, mAdapter.getCurrentBitmap());
			}
		});
	}

	private String getSavedTips(String url) {
		return getContext().getResources().getString(R.string.saved) + url;
	}

	/**
	 * 是否显示保存按钮
	 * 
	 * @param show
	 */
	public void setShowSaveBtn(boolean show) {
		if (show) {
			mSaveTv.setVisibility(View.VISIBLE);
		} else {
			mSaveTv.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置显示图片的ViewPager Adapter
	 * 
	 * @param adapter
	 */
	public void setImageAdapter(ImageAdapter adapter) {
		mAdapter = adapter;
		// 设置点按事件
		mAdapter.setOnViewTapListener(this);
		// 设置adapter
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(mImagePosition);
		mImageCount = mAdapter.getCount();
		mTextView.setText("1/" + mImageCount);
	}

	/**
	 * 点击PhotoView时隐藏该Dialog
	 * 
	 * @param tapDismiss
	 */
	public void setTapDismiss(boolean tapDismiss) {
		this.isTapDismiss = tapDismiss;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		mImagePosition = position;
		// position 从0开始,而显示则是从1开始,因此position需要加1
		mTextView.setText(++position + "/" + mImageCount);
	}

	/*
	 * 用户点按PhotoView视图 (non-Javadoc)
	 * 
	 * @see
	 * uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener#onViewTap(android
	 * .view.View, float, float)
	 */
	@Override
	public void onViewTap(View view, float x, float y) {
		if (isTapDismiss) {
			dismiss();
		}
	}

}
