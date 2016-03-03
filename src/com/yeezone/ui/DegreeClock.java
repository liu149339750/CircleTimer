package com.yeezone.ui;

import com.example.circletimer.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class DegreeClock extends View{

	private Paint circle_paint;
	private Paint mark_paint;
	private Paint mark_select_paint;
	private Paint degree_number_paint;
	private Paint dot_paint;
	private Paint time_paint;
	private Paint text_title_paint;
	private Paint btPaint;
	
	private Bitmap circle_point;
	
	private boolean mIsDragging;
	
	
	private final int diameter = 530;
	private final int radius = diameter/2;
	private final int mark_width = 1;
	private final int mark_lenght = 28;
	private final int mark_max_lenght = 46;
	private final int mark_margin = 10;
	private final int mark_normal_color = Color.parseColor("#E9E2D9");
	private final int mark_normal_select_color = Color.parseColor("#68C5D7");
	
	private final int degree_number_size = 20;
	private final int degree_number_color = Color.parseColor("#866A60");
	private final int degree_number_alph = (int) (255 * 0.6);
	private final int degree_number_margin = 20;
	
	private final int time_text_size = 100;
	private final int time_text_color = Color.parseColor("#FA7777");
	private final int dot_alph = 50;
	
	private final int text_title_size = 28;
	private final int text_title_color = Color.parseColor("#000000");
	private final int text_title_alph = (int) (255*0.6);
	
	private final int circle_color = Color.parseColor("#E9E2D9");
	
	
	private int width;
	private int height;
	private int cx;
	private int cy;
	
	private float px;
	private float py;
	private float lx;
	private float ly;
	private int bw;
	private int bh;
	
	private double radian; //当前弧度
	private double fradian; // 之前弧度
	private double total_radian;
	private long remain_time;
	private CountDownTimer mTimer;
	private boolean isRuning = false;
	
	private boolean strictMode = false;
	
	private String title = "时间设置";
	
	private int touchSlop;
	
	public DegreeClock(Context context) {
		super(context);
		init(context);
	}
	
	
	
	public DegreeClock(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}



	public DegreeClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}



	private void init(Context context) {
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		System.out.println(touchSlop);
		touchSlop = 5;
		circle_paint = new Paint();
		circle_paint.setStyle(Style.STROKE);
		circle_paint.setAntiAlias(true);
		circle_paint.setColor(circle_color);
		mark_paint = new Paint();
		mark_paint.setColor(mark_normal_color);
		mark_paint.setStrokeWidth(mark_width);
		mark_paint.setAntiAlias(true);
		mark_select_paint = new Paint(Paint.LINEAR_TEXT_FLAG);
		mark_select_paint.setColor(mark_normal_select_color);
		mark_select_paint.setStrokeWidth(mark_width);
		mark_select_paint.setAntiAlias(true);
		degree_number_paint = new Paint();
		degree_number_paint.setTextSize(degree_number_size);
		degree_number_paint.setColor(degree_number_color);
		degree_number_paint.setStyle(Style.STROKE);
		degree_number_paint.setAlpha(degree_number_alph);
		degree_number_paint.setFakeBoldText(true);
		degree_number_paint.setTextAlign(Align.CENTER);
		btPaint = new Paint();
		
		time_paint = new Paint();
		time_paint.setTextSize(time_text_size);
		time_paint.setColor(time_text_color);
		time_paint.setTextAlign(Align.CENTER);
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "font/HelveticaCondensedBold.ttf");
		time_paint.setTypeface(tf);
		dot_paint = new Paint();
		dot_paint.setColor(time_text_color);
		dot_paint.setTextSize(time_text_size/2);
		dot_paint.setAlpha(dot_alph);
		dot_paint.setTextAlign(Align.CENTER);
		dot_paint.setTypeface(tf);
		
		text_title_paint = new Paint();
		text_title_paint.setTextSize(text_title_size);
		text_title_paint.setTextAlign(Align.CENTER);
		text_title_paint.setColor(text_title_color);
		text_title_paint.setAlpha(text_title_alph);
		
		circle_point = BitmapFactory.decodeResource(getResources(), R.drawable.circle_point);
		
		bw = circle_point.getWidth();
		bh = circle_point.getHeight();
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		cx = width/2;
		cy = height/2;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
			return false;
		}
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
//		Log.w("warn", (x - px) +":" + (y - py)+">"+bw);
		if(strictMode && (Math.abs(x-px) > bw || (Math.abs(y-py) > bh))){
			System.out.println((Math.abs(x-px) > bw/2) + ":" +(Math.abs(y-py) > bh/2));
				return false;
		}

		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			if((Math.abs(x-px) > bw/2 || (Math.abs(y-py) > bh/2))){
					return false;
			}
			radian = getRadian(x, y);
			fradian = radian;
			lx = x;
			ly = y;
			break;
		case MotionEvent.ACTION_MOVE:
//			if(x - lx < touchSlop && y - ly < touchSlop)
//				return true;
			handlePoint(x,y);
			lx = x;
			ly = y;
			break;
		case MotionEvent.ACTION_UP:
			fradian = 0;
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
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(cx, cy, radius, circle_paint);
		drawDegree(canvas);
		drawCirclePoint(canvas);
		drawTime(canvas);
	}
	
	public void start() {
		isRuning = true;
		final long time = ((int)((total_radian*60) /(2*Math.PI)) * 60 * 1000);
		remain_time = time;
		System.out.println((int)((total_radian*60) /(2*Math.PI))+"---");
		if(mTimer != null)
			mTimer.cancel();
		mTimer = new CountDownTimer(time,1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				remain_time = millisUntilFinished;
				System.out.println("remain_time = " + remain_time);
				postInvalidate();
			}
			
			@Override
			public void onFinish() {
				stop();
			}
		};
		mTimer.start();
	}
	
	public void pause() {
		isRuning = false;
		if(mTimer!=null) 
			mTimer.cancel();
	}
	
	public void stop() {
		isRuning = false;
		remain_time = 0;
		total_radian = 0;
		if(mTimer!=null) 
			mTimer.cancel();
		mTimer = null;
		invalidate();
	}
	
	public int getSelectMin() {
		int t = (int) (total_radian/(2*Math.PI) * 60);
		return t;
	}

	private void drawTime(Canvas canvas) {
		String text = null;
		if(isRuning) {
			text = getTimeSecText(remain_time);
		} else {
			int t = (int) (total_radian/(2*Math.PI) * 60);
			text = getTimeMinText(t);
		}
		float m = time_paint.descent() + time_paint.ascent();
		text = text.replace(":", " ");
		canvas.drawText(text, cx, cy - m/2 , time_paint);
		
		canvas.drawText(":", cx, cy - (dot_paint.descent() + dot_paint.ascent())/2 , dot_paint);
		
		int ty = cy + radius/2;
		canvas.drawText(title, cx, ty, text_title_paint);
	}

	private String getTimeSecText(long t) {
//		int m = (int) (t/(1000*60));
//		int s = (int) ((t/1000)%60);
		int m = Math.round((float)t/(1000*60));
		int s = Math.round((float)t/1000)%60;
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

	private void drawCirclePoint(Canvas canvas) {
		if(isRuning)
			return;
		double tr = 0;                                       // info: make this a globle var
		if(isRuning){
			tr = remain_time/(60 * 1000) * 2* Math.PI/60;
			total_radian = tr;//2015.8.7
		} else 
			tr = total_radian;
		int margin = (mark_margin + mark_lenght)/2;
		float x =  cx + (float) (Math.sin(tr)*(radius - margin));
		float y =  cy - (float) (Math.cos(tr)*(radius - margin));
		px = x ;
		py = y ;
		canvas.drawBitmap(circle_point, x - bw/2, y - bh/2, btPaint);
	}

	private void drawDegree(Canvas canvas) {
		double a = Math.PI/60;
		int r = radius - mark_margin;
		int cnt = 0;
		if(isRuning){
			cnt = (int) (remain_time * 120/(60 * 1000 * 60));
		} else 
			cnt = (int) Math.round((total_radian*120/(2*Math.PI)));
		
//		System.out.println("cnt = " + cnt);
		for(int i=0;i<120;i++) {
			double radian = i * a;
			int l = mark_lenght;
			if(i == 0 || i == 30 || i == 60 || i == 90)
				l = mark_max_lenght;
			float x2 =  (float) (r*Math.sin(radian) + cx);
			float y2 =  (float) (cy - r*Math.cos(radian));
			float x1 =  (float) (x2 - l*Math.sin(radian));
			float y1 =  (float) (y2 + l*Math.cos(radian));
			if(i <= cnt)
				canvas.drawLine(x1, y1, x2, y2, mark_select_paint);
			else
				canvas.drawLine(x1, y1, x2, y2, mark_paint);
		}
		int b = (int) ((degree_number_paint.descent() - degree_number_paint.ascent())/2);
		int x = cx;
		int y = cy - r + mark_max_lenght + degree_number_margin + b;
		degree_number_paint.setTextAlign(Align.CENTER);
		canvas.drawText("60", x, y, degree_number_paint);
		x = cx + r - mark_max_lenght - degree_number_margin;
		y = cy + b;
//		degree_number_paint.setTextAlign(Align.RIGHT);
		canvas.drawText("15", x, y, degree_number_paint);
		x = cx;
		y = cy + r - mark_max_lenght - degree_number_margin + b;
//		degree_number_paint.setTextAlign(Align.CENTER);
		canvas.drawText("30", x, y, degree_number_paint);
		x = cx - r + mark_max_lenght + degree_number_margin;
		y = cy + b;
//		degree_number_paint.setTextAlign(Align.LEFT);
		canvas.drawText("45", x, y, degree_number_paint);
	}

}
