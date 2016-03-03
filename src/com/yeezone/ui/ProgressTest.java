package com.yeezone.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ProgressTest extends ProgressBar{

	public ProgressTest(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		Drawable d = getProgressDrawable();
		Rect r = d.getBounds();
//		System.out.println(r.left+":"+r.top+":"+r.right+":"+r.bottom+"-------");
		super.onDraw(canvas);
		
	}
}
