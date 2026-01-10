package com.inflps.speakerCleaner.WIDGET;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class FrequencyWaveView extends View {
    private final Paint wavePaint = new Paint();
    private final Path wavePath = new Path();
    private int waveColor = Color.parseColor("#00BFFF");
    private float strokeWidth = 8f;
    private float phase = 0f;
    private float amplitude = 0f;
    private float targetAmplitude = 0f;
    private float frequencyHz = 150f;
    private long lastFrameTime = 0;
    private boolean isPulsing = false;
    private float pulsePhase = 0f;
    private boolean isAnimating = false;
    
    public FrequencyWaveView(Context context) {
        super(context);
        init();
    }
    
    public FrequencyWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        wavePaint.setColor(waveColor);
        wavePaint.setStyle(Paint.Style.STROKE);
        wavePaint.setStrokeJoin(Paint.Join.ROUND);
        wavePaint.setStrokeWidth(strokeWidth);
        wavePaint.setAntiAlias(true);
        wavePaint.setStrokeCap(Paint.Cap.ROUND);
    }
    
    public void setFrequency(int hz) {
        this.frequencyHz = hz;
    }
    
    public void setPlaying(boolean isPlaying) {
        this.isAnimating = true;
        this.targetAmplitude = isPlaying ? 1.0f : 0.0f;
        lastFrameTime = System.currentTimeMillis();
        invalidate();
    }
    
    public void setPulsing(boolean pulsing) {
        this.isPulsing = pulsing;
        if (!pulsing) {
            pulsePhase = 0;
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long timeNow = System.currentTimeMillis();
        if (lastFrameTime == 0) lastFrameTime = timeNow;
        float deltaTime = (timeNow - lastFrameTime) / 1000f; 
        lastFrameTime = timeNow;
        if (deltaTime > 0.1f) deltaTime = 0.1f; 
        
        int width = getWidth();
        int height = getHeight();
        float centerY = height / 2f;
        float maxWaveHeight = (height / 2f) * 0.9f;
        float fadeSpeed = 5.0f * deltaTime;

        if (amplitude < targetAmplitude) {
            amplitude += fadeSpeed;
            if (amplitude > targetAmplitude) amplitude = targetAmplitude;
        } else if (amplitude > targetAmplitude) {
            amplitude -= fadeSpeed;
            if (amplitude < targetAmplitude) amplitude = targetAmplitude;
        }
        
        if (amplitude < 0.01f && targetAmplitude == 0f) {
            isAnimating = false;
            return;
        }
        
        float currentPulseFactor = 1.0f;
        if (isPulsing) {
            pulsePhase += 4.0f * (2 * Math.PI) * deltaTime;
            if (pulsePhase > 2 * Math.PI) pulsePhase -= (2 * Math.PI);
            float pulseSin = (float) ((Math.sin(pulsePhase) + 1.0f) / 2.0f);
            currentPulseFactor = 0.2f + (pulseSin * 0.8f);
        } else {
            pulsePhase = 0;
        }
        
        wavePath.reset();
        wavePath.moveTo(0, centerY);
        float density = 10f + (frequencyHz / 20f);
        float fadeWidth = width * 0.15f; 
        
        for (int x = 0; x <= width; x += 5) {
            float angle = (float) ((x / (float) width) * (Math.PI * density)) + phase;
            float rawSine = (float) Math.sin(angle);
            float edgeFade = 1.0f;
            if (x < fadeWidth) {
                edgeFade = x / fadeWidth;
            } else if (x > width - fadeWidth) {
                edgeFade = (width - x) / fadeWidth;
            }
            
            float y = centerY + (rawSine * maxWaveHeight * amplitude * currentPulseFactor * edgeFade);
            wavePath.lineTo(x, y);
        }
        
        canvas.drawPath(wavePath, wavePaint);
        
        float scrollSpeedPerSec = (0.15f + (frequencyHz / 3000f)) * 60f; 
        phase -= scrollSpeedPerSec * deltaTime;
        
        if (isAnimating || amplitude > 0.01f) {
            invalidate();
        }
    }
}
