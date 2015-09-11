package com.simple.imagebrowser.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 捕获 java.lang.IllegalArgumentException: pointerIndex out of range异常,避免Crash .
 * 
 * see
 * http://stackoverflow.com/questions/18383083/arrayindexoutofboundsexception
 * -in-photoview-viewpager
 * 
 * @author mrsimple
 * 
 */
public class HackyViewPager extends ViewPager {

	public HackyViewPager(Context context) {
		super(context);
	}

	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
		} catch (ArrayIndexOutOfBoundsException e) {

		}
		return false;
	}
}
