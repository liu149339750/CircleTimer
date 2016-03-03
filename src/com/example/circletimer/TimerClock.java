package com.example.circletimer;

import com.example.circletimer.R;
import com.example.circletimer.R.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TimerClock extends View{

	private Paint mPaint;
	private Paint mDialPaint;
	private Paint mDegreePaint;
	private Paint mTimePaint;
	private Bitmap mBg;
	private Bitmap mDialBitmap;
	private Bitmap mTwoArrow;
	private Bitmap mOneArrow;
	private Bitmap mHole;
	private int btw;
	private int bth;
	private int width;
	private int height;
	private float scale;
	Matrix matrix = new Matrix();
	private final int  degree_margin = 15;
	private final int degree_size = 22;
	private final int time_size = 28;
	
	private int cx;
	private int cy;
	
	private BitmapDrawable mDrawable;
	
	
	private float lx;
	private float ly;
	
	private double radian;
	private double fradian;
	
	
	private double total_radian;
	private long remain_time;
	private CountDownTimer mTimer;
	
	private boolean isRuning = false;
	
	
	public TimerClock(Context context) {
		super(context);
		init();
	}
	
	
	public TimerClock(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public TimerClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}




	private void init() {
		mPaint = new Paint();
		mPaint.setFilterBitmap(true);
		mPaint.setAntiAlias(true);
		mDialPaint = new Paint();
		mDialPaint.setFilterBitmap(true);
		mDialPaint.setAntiAlias(true);
		mDegreePaint = new Paint(Paint.LINEAR_TEXT_FLAG);
		mDegreePaint.setTextSize(degree_size);
		mDegreePaint.setColor(Color.BLACK);
		mDegreePaint.setTextAlign(Align.CENTER);
		mTimePaint = new Paint();
		mTimePaint.setTextSize(time_size);
		mTimePaint.setStyle(Style.FILL);
		mTimePaint.setFakeBoldText(true);
		mTimePaint.setTextAlign(Align.CENTER);
		mDialBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timer_dial);
		Bitmap shadow = BitmapFactory.decodeResource(getResources(), R.drawable.timer_clock_bg);
		BitmapShader bs = new BitmapShader(shadow, TileMode.CLAMP, TileMode.CLAMP);
		mPaint.setShader(bs);
		
		mOneArrow = BitmapFactory.decodeResource(getResources(), R.drawable.timer_one_arrow);
		mTwoArrow = BitmapFactory.decodeResource(getResources(), R.drawable.timer_two_arrow);
		mHole = BitmapFactory.decodeResource(getResources(), R.drawable.timer_hole);


		mBg = BitmapFactory.decodeResource(getResources(), R.drawable.timer_clock_bg);
		mDrawable = new BitmapDrawable(mBg);
		mDrawable.getPaint().setShader(bs);
		btw = mBg.getWidth();
		bth = mBg.getHeight();
		
	}
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		width = getMeasuredWidth();
//		height = getMeasuredHeight();
//		if(width > mBg.getWidth() && height > mBg.getHeight()) {
//			scale = 1;
//		} else {
//			float sx = ((float)width - getPaddingLeft() - getPaddingRight())/mBg.getWidth();
//			float sy = ((float)height - getPaddingTop() - getPaddingBottom())/mBg.getHeight();
//			scale = sx > sy ? sy : sx;
//		}
//		
//		cx = width/2;
//		cy = height/2;
//	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
		if(width > mBg.getWidth() && height > mBg.getHeight()) {
			scale = 1;
		} else {
			float sx = ((float)width - getPaddingLeft() - getPaddingRight())/mBg.getWidth();
			float sy = ((float)height - getPaddingTop() - getPaddingBottom())/mBg.getHeight();
			scale = sx > sy ? sy : sx;
		}
		
		cx = width/2;
		cy = height/2;
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			radian = getRadian(x, y);
			fradian = radian;
			break;
		case MotionEvent.ACTION_MOVE:
			handlePoint(x,y);
			break;
		case MotionEvent.ACTION_UP:
			radian = 0;
			if(isRuning)
				stop();
			else
				start();
			break;
		default:
			break;
		}
		return true;
	}


	private void handlePoint(float mx, float my) {
		double nr = getRadian(mx, my);
		double r = nr - fradian;
//		System.out.println(Math.toDegrees(nr)+"("+mx+","+my+")");
		if(fradian - nr > Math.PI)
			total_radian += Math.PI/10;
		else if(nr - fradian > Math.PI)
			total_radian -= Math.PI/10;
		else 
			total_radian += r;
		if(total_radian < 0)
			total_radian = 0;
		else if(total_radian > 2* Math.PI)
			total_radian = 2* Math.PI;
		fradian = nr;
		invalidate();
		
	}
	
	private double getRadian(float x1,float y1) {
		double a = (float) Math.atan(Math.abs((y1-cy)/(x1-cx)));
		double result = 0; 
		if(x1 > cx && y1 > cy) { //right down, the second quadrant
			result = a + Math.PI/2;
		} else if(x1 > cx && y1 < cy) { //right top,the first quadrant
			result = Math.PI/2 - a;
		} else if(x1 < cx && y1 > cy) { //left down,the third quadrant
			result = 3*Math.PI/2 - a;
		} else if(x1 < cx && y1 < cy) { //left top, the fourth quadrant
			result = 3*Math.PI/2 + a;
		}
		return result;
	}
	
	public void start() {
		System.out.println("start");
		isRuning = true;
		final long time = (int) Math.round(((total_radian*60) /(2*Math.PI))) * 60 * 1000;
		remain_time = time;
		
		if(mTimer != null)
			mTimer.cancel();
		mTimer = new CountDownTimer(time,1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				remain_time = millisUntilFinished;
				System.out.println(millisUntilFinished);
				invalidate();
			}
			
			@Override
			public void onFinish() {
				isRuning = false;
				stop();
			}
		};
		mTimer.start();
	}
	
	public void stop() {
		isRuning = false;
		remain_time = 0;
		total_radian = 0;
		if(mTimer!=null)
			mTimer.cancel();
		invalidate();
	}


	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.argb(66, 0, 66, 88));
		
		drawBg(canvas);
		
		drawDegree(canvas); 
		
		drawArrow(canvas);
		
		drawTime(canvas);
		
		drawProgress(canvas);
	}


	private void drawProgress(Canvas canvas) {
		
	}


	private void drawTime(Canvas canvas) {
		String text = null;
		if(isRuning) {
			text = getTimeSecText(remain_time);
		} else {
			int t = (int) Math.round(((total_radian*60) /(2*Math.PI))) ;
			text = getTimeMinText(t);
		}
		canvas.drawText(text, cx, cy, mTimePaint);
	}


	private String getTimeMinText(int t) {
		String time = null;
		if(t < 10)
			time = "0" + t + ":00";
		else if(t <= 60)
			time = t + ":00";
		else {
			time = t/60 + ":"+(t%60 > 9 ? t%60:("0"+t%60)) + ":00";
		}
		return time;
	}
	
	private String getTimeSecText(long t) {
		int m = (int) (t/(1000*60));
		int s = (int) ((t/1000)%60);
		String text = null;
		if(m < 10)
			text = "0" + m;
		else
			text = m + "";
		text += ":";
		if(s < 10)
			text += "0"+s;
		else 
			text += s;
		return text;
	}

	private void drawArrow(Canvas canvas) {
		int r = (int) (mBg.getWidth() * scale /2) - 100;
		if(scale == 1) {
			System.out.println("total_radian = " + total_radian);
			canvas.save();
			canvas.rotate((int)Math.toDegrees(total_radian), cx, cy); // total_radian * 180/Math.PI
			canvas.drawBitmap(mTwoArrow, (width - mBg.getWidth())/2, (height - mBg.getHeight())/2, mPaint);
			canvas.drawBitmap(mHole, cx - mHole.getWidth()/2, cy - r - mHole.getHeight()/2 , mPaint);
			canvas.restore();
		} else {
			canvas.save();
			matrix.reset();
			canvas.translate(0, (height - mBg.getHeight())/2);
			matrix.setScale(scale, scale,0,(height - mBg.getHeight())/2);
			matrix.postRotate((int)Math.toDegrees(total_radian), cx, cy - (height - mBg.getHeight())/2);
			canvas.drawBitmap(mTwoArrow, matrix, mPaint);
			canvas.restore();
			canvas.save();
			canvas.rotate((int)Math.toDegrees(total_radian), cx, cy); // total_radian * 180/Math.PI
			canvas.drawBitmap(mHole, cx - mHole.getWidth()/2, cy - r - mHole.getHeight()/2 , mPaint);
			canvas.restore();
		}
	}


	private void drawDegree(Canvas canvas) {
		int r = (int) (mBg.getWidth() * scale /2);
		int dx0 = cx ;
		int dy0 = cy - r;
		canvas.drawText("0/·ÖÖÓ", dx0 , dy0 + degree_margin, mDegreePaint);
		int dx1 = cx + (int) (r * Math.sin((2*Math.PI)/6));
		int dy1 = cy - (int) (r * Math.cos((2*Math.PI)/6));
		canvas.drawText("10", dx1 - degree_margin, dy1 + degree_margin, mDegreePaint);
		int dx2 = dx1;
		int dy2 = cy + (int) (r * Math.cos((2*Math.PI)/6));
		canvas.drawText("20", dx2 - degree_margin, dy2 , mDegreePaint);
		int dx3 = cx;
		int dy3 = cy + r;
		canvas.drawText("30", dx3 , dy3 - degree_margin, mDegreePaint);
		int dx4 = cx - (int) (r * Math.sin((2*Math.PI)/6));
		int dy4 = dy2;
		canvas.drawText("40", dx4 + degree_margin, dy4, mDegreePaint);
		int dx5 = dx4;
		int dy5 = dy1;
		canvas.drawText("50", dx5 + degree_margin, dy5 + degree_margin, mDegreePaint);
	}


	private void drawBg(Canvas canvas) {
		if(scale == 1) {
			canvas.drawBitmap(mBg, (width - mBg.getWidth())/2, (height - mBg.getHeight())/2, mPaint);
			canvas.drawBitmap(mDialBitmap, (width - mBg.getWidth())/2, (height - mBg.getHeight())/2, mDialPaint);
		} else {
			canvas.save();
			matrix.reset();
			canvas.translate(0, (height - mBg.getHeight())/2);
			matrix.setScale(scale, scale,0,(height - mBg.getHeight())/2);
			canvas.drawBitmap(mBg, matrix, mPaint);
			canvas.drawBitmap(mDialBitmap, matrix, mPaint);
//			canvas.translate(0, -(height - mBg.getHeight())/2);
			canvas.restore();
		}
	}
	
	
}
