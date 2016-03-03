package com.yeezone.ui;

import com.example.circletimer.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class VerticalSeekBarLayout extends LinearLayout{
	private Drawable mDot;
	private int dotRes[] = {R.drawable.dot1,R.drawable.dot2,R.drawable.dot3};
	public VerticalSeekBarLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public VerticalSeekBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public VerticalSeekBarLayout(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		int a = (int) Math.round(Math.random()*(dotRes.length-1));
		mDot = getResources().getDrawable(dotRes[a]);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		System.out.println("VerticalSeekBarLayout   onDraw");
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = getMeasuredHeight();
		height += mDot.getIntrinsicHeight();
		setMeasuredDimension(resolveSizeAndState(getMeasuredWidth(), widthMeasureSpec, 0), resolveSize(height, heightMeasureSpec));
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
//		canvas.drawColor(Color.GREEN);
		super.dispatchDraw(canvas);
		int dw = mDot.getIntrinsicWidth();
		int dh = mDot.getIntrinsicHeight();
		for(int i =1;i<getChildCount();i++){
			View v = getChildAt(i);
			int w = v.getWidth();
			int l = v.getLeft();
			int b = v.getBottom();
			mDot.setBounds(l+ (w-dw)/2, b, l + (w+dw)/2, b + dh);
			mDot.draw(canvas);
		}
	}
	

}
