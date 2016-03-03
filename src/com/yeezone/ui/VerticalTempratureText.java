package com.yeezone.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

public class VerticalTempratureText extends View{

	private Paint mPaint;
	private Paint mLinePaint1;
	private Paint mLinePaint2;
	
	private final int text_color = Color.parseColor("#000000");
	private final int text_size = 24;
	private final int text_alph = (int) (255 * 0.5);
	
	private final int line_width_1 = 1;
	private final int line_length_1 = 10;
	private final int line_width_2 = 4;
	private final int line_length_2 = 15;
	private final int line_length_angle = 4;
	private final int text_line_margin = 10;
	
	private final int line_color = Color.parseColor("#000000");
	private final int line_alph_1 = (int) (255 * 0.1);
	private final int line_alph_2 = (int) (255 * 0.2);
	
	private int width;
	private int height;
	
	private String tempratures[] = {"60¡æ","50¡æ","40¡æ","30¡æ"};
	
	private int text_max_width;
	
	public VerticalTempratureText(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public VerticalTempratureText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public VerticalTempratureText(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mPaint = new Paint();
		mPaint.setColor(text_color);
		mPaint.setTextSize(text_size);
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "font/HelveticaCondensedBold.ttf");
		mPaint.setTypeface(tf);
		mPaint.setAlpha(text_alph);
		
		mLinePaint1 = new Paint();
		mLinePaint1.setColor(line_color);
		mLinePaint1.setAlpha(line_alph_1);
		mLinePaint1.setStrokeWidth(line_width_1);
		mLinePaint2 = new Paint();
		mLinePaint2.setColor(line_color);
		mLinePaint2.setAlpha(line_alph_2);
		mLinePaint2.setStyle(Style.FILL);
		mLinePaint2.setAntiAlias(true);
//		mLinePaint2.setStrokeWidth(line_width_2);
		
		for(int i =0;i<tempratures.length;i++){
			int a = (int) mPaint.measureText(tempratures[i]);
			if(a > text_max_width)
				text_max_width = a;
		}
		
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = text_max_width + text_line_margin;
		width += line_length_1 > line_length_2 ? line_length_1:line_length_2;
		
		FontMetrics fm = new FontMetrics();
		mPaint.getFontMetrics(fm);
		int th = (int) (fm.bottom - fm.top);
		System.out.println(th);
		int height = th * tempratures.length + text_line_margin * (tempratures.length - 1);
		System.out.println("width =" + width);
		setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0), resolveSizeAndState(height, heightMeasureSpec, 0));
		System.out.println("w =" + getMeasuredWidth());
		System.out.println("h =" + getMeasuredHeight()+">>"+MeasureSpec.getSize(heightMeasureSpec));
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w;
		height = h;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int cnt = tempratures.length;
		int d = (int) ((mPaint.descent() + mPaint.ascent())/2);
		System.out.println("height="+height);
		int ah = height/cnt;
		canvas.save();
		canvas.translate(0, ah/2);
		
		for(int i = 0;i<cnt;i++) {
			canvas.drawText(tempratures[i], 0, - d, mPaint);
			canvas.save();
			canvas.translate(text_line_margin + text_max_width, 0);
			canvas.drawRoundRect(new RectF(0, 0, line_length_2, line_width_2), line_length_angle, line_length_angle, mLinePaint2);
			if(i == cnt - 1){
				canvas.restore();
				break;
			}
			canvas.translate(0, ah/2);
			canvas.drawLine(0, 0, line_length_1, 0, mLinePaint1);
			canvas.restore();
			canvas.translate(0, ah);
		}
		canvas.restore();
		
	}
	
}
