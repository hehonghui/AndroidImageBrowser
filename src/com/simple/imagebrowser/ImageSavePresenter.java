package com.simple.imagebrowser;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.simple.imagebrowser.utils.MD5;

/**
 * 存储图片的Presenter
 * 
 * @author mrsimple
 * 
 */
class ImageSavePresenter {

	private Context mContext;
	private String mCachePath;

	public ImageSavePresenter(Context context) {
		mContext = context;
		mCachePath = getCacheDir();
	}

	/**
	 * 保存图片
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void saveImage(String url, Bitmap bitmap) {
		if (TextUtils.isEmpty(url) || bitmap == null) {
			return;
		}

		FileOutputStream fileOutputStream = null;
		try {
			String fileName = MD5.toMD5(url);
			File imgFile = new File(mCachePath + File.separator + fileName
					+ ".png");
			fileOutputStream = new FileOutputStream(imgFile);
			bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
			fileOutputStream.flush();
			// 更新媒体库
			updateMediaStore(imgFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeSilently(fileOutputStream);
		}
	}

	private String getCacheDir() {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			cachePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
		} else {
			cachePath = mContext.getCacheDir().getPath();
		}

		// 构建缓存目录
		File cacheFile = new File(cachePath + File.separator
				+ getAppName(mContext));
		if (!cacheFile.exists()) {
			cacheFile.mkdir();
		}
		return cacheFile.getAbsolutePath();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext()
					.getPackageManager();
			String packageName = context.getPackageName();
			applicationInfo = packageManager.getApplicationInfo(packageName, 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		return (String) packageManager.getApplicationLabel(applicationInfo);
	}

	/**
	 * 如果该输入流不为空，则关闭该输入流</br>
	 * 
	 * @param closeable
	 *            可关闭的对象,例如流和数据库等
	 */
	public static void closeSilently(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
			}
		}
	}

	private void updateMediaStore(File savedFile) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri contentUri = Uri.fromFile(savedFile);
		mediaScanIntent.setData(contentUri);
		mContext.sendBroadcast(mediaScanIntent);
	}
}
