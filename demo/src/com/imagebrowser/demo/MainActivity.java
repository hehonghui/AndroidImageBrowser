package com.imagebrowser.demo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.imagebrowser.demo.adapter.GlideImagePagerAdapter;
import com.imagebrowser.demo.adapter.UILImagePagerAdapter;
import com.simple.imagebrowser.ImageBrowser;
import com.simple.imagebrowser.adapter.ImageAdapter;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		findViewById(R.id.image_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ImageAdapter adapter = new UILImagePagerAdapter(
						MainActivity.this, mockImages());
				showImageBrowser(adapter);
			}
		});

		findViewById(R.id.glide_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ImageAdapter adapter = new GlideImagePagerAdapter(
						MainActivity.this, mockImages());
				showImageBrowser(adapter);
			}
		});
	}

	private void showImageBrowser(ImageAdapter adapter) {
		ImageBrowser browser = new ImageBrowser(MainActivity.this);
		// 设置显示图片的Adapter
		browser.setImageAdapter(adapter);
		browser.setTapDismiss(true);
		browser.show();
	}

	private List<String> mockImages() {
		List<String> imagesUrls = new ArrayList<String>();
		imagesUrls
				.add("http://imgsrc.baidu.com/forum/pic/item/09be3f094b36acaf0ad6eb717cd98d1000e99cde.jpg");
		imagesUrls.add("http://tupian.enterdesk.com/2014/mxy/05/15/1/7.jpg");
		imagesUrls
				.add("http://b.hiphotos.baidu.com/image/pic/item/14ce36d3d539b600ab94fdc3ea50352ac65cb768.jpg");
		imagesUrls.add("http://img6.faloo.com/Picture/0x0/0/747/747488.jpg");
		imagesUrls
				.add("http://61.144.56.195/forum/201302/19/144727b4b4zbfbyhbmfv4a.jpg");
		return imagesUrls;
	}
}
