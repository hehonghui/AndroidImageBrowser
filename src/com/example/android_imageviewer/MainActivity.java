package com.example.android_imageviewer;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MainActivity extends ActionBarActivity implements
		ViewPager.OnPageChangeListener, PhotoViewAttacher.OnViewTapListener {
	private String imageUri = null;
	private List<String> imageUrls = null;
	private String fileName = null;
	private ImageLoader imageLoader = null;
	private ShareActionProvider shareActionProvider = null;
	private int imageMode = 0; // 0 multiple, 1,single
	private int imagePosition = 0;
	ViewPager viewPager = null;

	public static class Extra {
		public static final String IMAGES = "com.github.snowdream.android.apps.imageviewer.IMAGES";
		public static final String IMAGE_POSITION = "com.github.snowdream.android.apps.imageviewer.IMAGE_POSITION";
		public static final String IMAGE_MODE = "com.github.snowdream.android.apps.imageviewer.IMAGE_MODE";
	}

	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
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

		ImageLoader.getInstance().init(config);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
		//
		initImageLoader(this);
		initData();
		loadData();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// EasyTracker.getInstance().activityStart(this); // Add this method.
	}

	@Override
	protected void onStop() {
		super.onStop();
		// EasyTracker.getInstance().activityStop(this); // Add this method.
	}

	public void initUI() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setOnPageChangeListener(this);
	}

	public void initData() {
		imageLoader = ImageLoader.getInstance();
		imageUrls = new ArrayList<String>();

		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				imageUrls = bundle.getStringArrayList(Extra.IMAGES);
				imagePosition = bundle.getInt(Extra.IMAGE_POSITION, 0);
				imageMode = bundle.getInt(Extra.IMAGE_MODE, 0);
				Log.i("", "The snowdream bundle path of the image is: "
						+ imageUri);
			}

			Uri uri = (Uri) intent.getData();
			if (uri != null) {
				imageUri = uri.getPath();
				fileName = uri.getLastPathSegment();
				getSupportActionBar().setSubtitle(fileName);
				Log.i("", "The path of the image is: " + imageUri);

				File file = new File(imageUri);

				imageUri = "file://" + imageUri;
				File dir = file.getParentFile();
				if (dir != null) {
					FileFilter fileFilter = new FileFilter() {
						@Override
						public boolean accept(File f) {
							if (f != null) {
								String extension = MimeTypeMap
										.getFileExtensionFromUrl(Uri.encode(f
												.getAbsolutePath()));
								if (!TextUtils.isEmpty(extension)) {
									String mimeType = MimeTypeMap
											.getSingleton()
											.getMimeTypeFromExtension(extension);
									if (!TextUtils.isEmpty(mimeType)
											&& mimeType.contains("image")) {
										return true;
									}
								}
							}
							return false;
						}
					};

					File[] files = dir.listFiles(fileFilter);

					if (files != null && files.length > 0) {
						int size = files.length;

						for (int i = 0; i < size; i++) {
							imageUrls.add("file://"
									+ files[i].getAbsolutePath());
						}
						imagePosition = imageUrls.indexOf(imageUri);
						imageMode = 1;
						Log.i("", "Image Position:" + imagePosition);
					}

				} else {
					imageUrls.add("file://" + imageUri);
					imagePosition = 0;
					imageMode = 0;
				}
			}
		}
		imageUrls
				.add("http://b.hiphotos.baidu.com/image/pic/item/e4dde71190ef76c666af095f9e16fdfaaf516741.jpg");
		imageUrls
				.add("http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg");
		imageUrls
				.add("http://b.hiphotos.baidu.com/image/pic/item/14ce36d3d539b600ab94fdc3ea50352ac65cb768.jpg");
		imageUrls
				.add("http://e.hiphotos.baidu.com/image/pic/item/0824ab18972bd40791a9ddfa79899e510fb3094e.jpg");
		imageUrls
				.add("http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg");
	}

	public void loadData() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		viewPager.setAdapter(new ImageViewerPagerAdapter(this, imageUrls,
				options));
		viewPager.setCurrentItem(imagePosition);
	}

	public void onViewTap(View view, float x, float y) {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar.isShowing()) {
			actionBar.hide();
		} else {
			actionBar.show();
		}
	}

	private Intent createShareIntent() {
		Intent shareIntent = null;

		if (!TextUtils.isEmpty(imageUri)) {
			shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("image/*");
			Uri uri = Uri.parse(imageUri);
			shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		}

		return shareIntent;
	}

	public void doShare(Intent shareIntent) {
		if (shareActionProvider != null) {
			shareActionProvider.setShareIntent(shareIntent);
		}
	}

	public void doSettings() {
		if (!TextUtils.isEmpty(imageUri)) {
			Uri uri = Uri.parse(imageUri);
			Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
			intent.setDataAndType(uri, "image/jpg");
			intent.putExtra("mimeType", "image/jpg");
			startActivityForResult(Intent.createChooser(intent,
					getText(R.string.action_settings)), 200);
		}
	}

	public void doEdit() {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		Log.i("", "onPageScrolled");
	}

	@Override
	public void onPageSelected(int position) {
		Log.i("", "onPageSelected position:" + position);
		if (imageUrls != null && imageUrls.size() > position) {
			imageUri = imageUrls.get(position);

			Uri uri = Uri.parse(imageUri);
			fileName = uri.getLastPathSegment();
			getSupportActionBar().setSubtitle(fileName);
			Log.i("", "The path of the image is: " + imageUri);
		}

		Intent shareIntent = createShareIntent();
		if (shareIntent != null) {
			doShare(shareIntent);
		}
		Log.i("", "onPageSelected imageUri:" + imageUri);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		Log.i("", "onPageScrollStateChanged state:" + state);
	}
}
