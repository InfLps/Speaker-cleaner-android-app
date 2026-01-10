package com.inflps.speakerCleaner.WIDGET;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.widget.AppCompatSeekBar;

public class Slidebar extends AppCompatSeekBar {
	
	private OnSeekBarChangeListener mUserListener;
	private ValueAnimator insetAnim;
	private int currentInset = 0;
	
	
	public Slidebar(Context context) {
		super(context);
		init();
	}
	
	public Slidebar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public Slidebar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	private void init() {
		GradientDrawable background = new GradientDrawable();
		background.setColor(Color.parseColor("#E0E0E0"));
		background.setCornerRadius(30f); 
		
		GradientDrawable progressShape = new GradientDrawable(
		GradientDrawable.Orientation.LEFT_RIGHT,
		new int[]{Color.parseColor("#8286B5"), Color.parseColor("#60648E")});
		progressShape.setCornerRadius(30f);
		
		ClipDrawable progressClip = new ClipDrawable(
		progressShape, Gravity.LEFT, ClipDrawable.HORIZONTAL);
		
		Drawable[] layers = new Drawable[]{background, progressClip};
		LayerDrawable trackDrawable = new LayerDrawable(layers);
		
		trackDrawable.setId(0, android.R.id.background);
		trackDrawable.setId(1, android.R.id.progress);
		
		setProgressDrawable(trackDrawable);
		
		GradientDrawable thumbShape = new GradientDrawable();
		thumbShape.setShape(GradientDrawable.OVAL);
		thumbShape.setColor(Color.WHITE);
		thumbShape.setStroke(2, Color.parseColor("#8286B5")); 
		thumbShape.setSize(60, 60); 
		setThumb(thumbShape);
		setSplitTrack(false);
		setPadding(45, 0, 45, 0); 
		setMinimumHeight(40);
	}
	
	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
		this.mUserListener = listener;
		super.setOnSeekBarChangeListener(mInternalListener);
	}
	
	private final OnSeekBarChangeListener mInternalListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (mUserListener != null) {
				mUserListener.onProgressChanged(seekBar, progress, fromUser);
			}
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			animateThumb(1.3f); 
			if (mUserListener != null) {
				mUserListener.onStartTrackingTouch(seekBar);
			}
		}
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			animateThumb(1.0f); 
			if (mUserListener != null) {
				mUserListener.onStopTrackingTouch(seekBar);
			}
		}
	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			animateThumb(1.3f);
			break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
			animateThumb(1.0f);
			break;
		}
		return super.onTouchEvent(event);
	}
	
	private void animateThumb(float scale) {
		Drawable thumb = getThumb();
		if (!(thumb instanceof LayerDrawable)) return;
		
		LayerDrawable layers = (LayerDrawable) thumb;
		GradientDrawable fill = (GradientDrawable) layers.findDrawableByLayerId(1);
		
		if (insetAnim != null) insetAnim.cancel();
		
		int targetInset = (scale > 1.0f) ? 15 : 0;
		int startInset = currentInset;
		
		insetAnim = ValueAnimator.ofInt(startInset, targetInset);
		insetAnim.setDuration(200);
		insetAnim.setInterpolator(new DecelerateInterpolator());
		
		insetAnim.addUpdateListener(animation -> {
			currentInset = (int) animation.getAnimatedValue();
			layers.setLayerInset(1, currentInset, currentInset, currentInset, currentInset);
			setThumb(layers);
		});
		
		insetAnim.start();
	}
}