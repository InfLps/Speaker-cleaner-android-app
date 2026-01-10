package com.inflps.speakerCleaner.WIDGET;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class SegmentedGaugeView extends View {

    private static final int COLOR_BACKGROUND = 0xFF2D2D2D;
    private static final int COLOR_DEFAULT = 0xFF00E5FF;
    private static final float START_ANGLE = 135f;
    private static final float TOTAL_SWEEP_ANGLE = 270f;
    private static final float GAP_DEGREES = 16f; 

    private Paint bgPaint;
    private Paint activePaint;
    private Paint textPaint;

    private final List<GaugeSegment> segments = new ArrayList<>();
    private final RectF arcRect = new RectF();

    private float currentProgress = 0f;
    private float displayProgress = 0f;
    private boolean showText = true;

    public SegmentedGaugeView(Context context) {
        super(context);
        init();
    }

    public SegmentedGaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);
        bgPaint.setColor(COLOR_BACKGROUND);

        activePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        activePaint.setStyle(Paint.Style.STROKE);
        activePaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);

        addSegment(1.0f, COLOR_DEFAULT);
    }

    public void clearSegments() {
        segments.clear();
        invalidate();
    }

    public void addSegment(float weight, int color) {
        segments.add(new GaugeSegment(weight, color));
        invalidate();
    }

    public void setShowText(boolean show) {
        this.showText = show;
        invalidate();
    }

    public void setProgress(float progress) {
        if (progress < 0) progress = 0;
        if (progress > 1) progress = 1;
        this.currentProgress = progress;
        animateProgress(progress);
    }

    private void animateProgress(float target) {
        ValueAnimator animator = ValueAnimator.ofFloat(displayProgress, target);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            displayProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int cx = width / 2;
        int cy = height / 2;

        float maxRadius = Math.min(width, height) / 2f * 0.85f;
        float strokeWidth = maxRadius * 0.12f;
        float drawRadius = maxRadius - (strokeWidth / 2); 
        arcRect.set(cx - drawRadius, cy - drawRadius, cx + drawRadius, cy + drawRadius);
        bgPaint.setStrokeWidth(strokeWidth);
        activePaint.setStrokeWidth(strokeWidth);
        textPaint.setTextSize(maxRadius * 0.45f);
        float totalWeight = 0;
        for (GaugeSegment s : segments) totalWeight += s.weight;

        int gapCount = Math.max(0, segments.size() - 1);
        float totalGapSpace = gapCount * GAP_DEGREES;
        float effectiveSweepAngle = TOTAL_SWEEP_ANGLE - totalGapSpace;
        float currentStartAngle = START_ANGLE;
        float segmentStartPercent = 0f;

        for (int i = 0; i < segments.size(); i++) {
            GaugeSegment s = segments.get(i);
            float segmentFraction = s.weight / totalWeight;
            float segmentSweep = segmentFraction * effectiveSweepAngle;
            float segmentEndPercent = segmentStartPercent + segmentFraction;
            canvas.drawArc(arcRect, currentStartAngle, segmentSweep, false, bgPaint);
            if (displayProgress > segmentStartPercent) {
                activePaint.setColor(s.color);
                float localProgress = (displayProgress - segmentStartPercent) / segmentFraction;
                if (localProgress > 1f) localProgress = 1f;
                float activeSweep = segmentSweep * localProgress;
                if (activeSweep > 0) {
                    canvas.drawArc(arcRect, currentStartAngle, activeSweep, false, activePaint);
                }
            }
            currentStartAngle += segmentSweep + GAP_DEGREES;
            segmentStartPercent += segmentFraction;
        }

        if (showText) {
            int percent = (int) (displayProgress * 100);
            float textHeight = textPaint.descent() - textPaint.ascent();
            float textOffset = (textHeight / 2) - textPaint.descent();
            canvas.drawText(percent + "%", cx, cy + textOffset, textPaint);
        }
    }

    private static class GaugeSegment {
        float weight;
        int color;
        GaugeSegment(float weight, int color) {
            this.weight = weight;
            this.color = color;
        }
    }
}
