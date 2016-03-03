package com.example.circletimer;

import com.yeezone.ui.DegreeClock;
import com.yeezone.ui.VerticalSeekBar;
import com.yeezone.ui.VerticalSeekBar.PositionListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements PositionListener,OnSeekBarChangeListener,View.OnClickListener{
	private Drawable mDrawable;
	private PopupWindow mPopupWindow;
	private int mProgress;
	private int width;
	private int height;
	
	private VerticalSeekBar mSeekBar1;
	private VerticalSeekBar mSeekBar2;
	private VerticalSeekBar mSeekBar3;
	private VerticalSeekBar mSeekBar4;
	
	private DegreeClock mClock;
	
	private Button mStart;
	private Button mStop;
	private Button mPause;
	private View mControls;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_activity_main);
		mDrawable = getResources().getDrawable(R.drawable.oval);
		width = mDrawable.getIntrinsicWidth();
		height = mDrawable.getIntrinsicHeight();
		mSeekBar1 = (VerticalSeekBar) findViewById(R.id.seekbar1);
		mSeekBar1.setPositionListener(this);
		mSeekBar2 = (VerticalSeekBar) findViewById(R.id.seekbar2);
		mSeekBar2.setPositionListener(this);
		mSeekBar3 = (VerticalSeekBar) findViewById(R.id.seekbar3);
		mSeekBar3.setPositionListener(this);
		mSeekBar4 = (VerticalSeekBar) findViewById(R.id.seekbar4);
		mSeekBar4.setPositionListener(this);
		mSeekBar4.setOnSeekBarChangeListener(this);
		
		mClock = (DegreeClock) findViewById(R.id.timerClock);
		mStart = (Button) findViewById(R.id.start);
		mStop = (Button) findViewById(R.id.stop);
		mPause = (Button) findViewById(R.id.pause);
		mControls = findViewById(R.id.control_buttons);
		mStart.setOnClickListener(this);
		mStop.setOnClickListener(this);
		mPause.setOnClickListener(this);
	}

	
	@SuppressLint("NewApi")
	public PopupWindow getPopWindow(int p){
		if(mPopupWindow != null){
			TextView text = (TextView) mPopupWindow.getContentView().findViewById(R.id.text);
			text.setText("" + p);
			return mPopupWindow;
		}
		View view = getLayoutInflater().inflate(R.layout.popup_view, null);
		TextView text = (TextView) view.findViewById(R.id.text);
		text.setText(""+p);
//		LinearLayout ll = new LinearLayout(this);
//		ll.setBackground(mDrawable);
//		TextView text = new TextView(this);
//		text.setId(android.R.id.text1);
//		text.setText(""+p);
//		ll.addView(text);
		PopupWindow pw = new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,false);
		pw.setTouchable(false);
		pw.setOutsideTouchable(true);
		mPopupWindow = pw;
		return mPopupWindow;
	}

	@Override
	public void position(VerticalSeekBar seek,float x, float y, int p) {
		if((mProgress == seek.getMax() && mProgress == p)||(mProgress == seek.getMinProgress() && mProgress == p) )
			return;
		int lx = (int) (x - width/2);
		PopupWindow pw = getPopWindow(p);
		pw.update((int)lx, (int)(y - height + seek.getHeight()/15), -1, -1);
		mProgress = p;
	}

	@Override
	public void onTouchStart(VerticalSeekBar seek,float x, float y,int p) {
		PopupWindow pw = getPopWindow( p);
		int lx = (int) (x - width/2);
		pw.showAtLocation(findViewById(R.id.seekContainer), Gravity.NO_GRAVITY, lx, (int)(y - height+ seek.getHeight()/10));
		mProgress = p;
	}

	@Override
	public void onTouchStop(VerticalSeekBar seek) {
		PopupWindow pw = getPopWindow(0);
		pw.dismiss();
	}


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		System.out.println("progress="+progress);
	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		System.out.println("onStartTrackingTouch");
	}


	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		System.out.println("onStopTrackingTouch");
	}


	@Override
	public void onClick(View v) {
		if(v == mStart) {
			mClock.start();
			mStart.setVisibility(View.GONE);
			mControls.setVisibility(View.VISIBLE);
		}else if(v == mPause) {
			mClock.pause();
			mControls.setVisibility(View.GONE);
			mStart.setVisibility(View.VISIBLE);
		} else if(v == mStop) {
			mClock.stop();
			mControls.setVisibility(View.GONE);
			mStart.setVisibility(View.VISIBLE);
		}
	}

}
