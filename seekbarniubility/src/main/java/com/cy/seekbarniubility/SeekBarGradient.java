package com.cy.seekbarniubility;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public class SeekBarGradient extends View {
    protected int width, height;
    protected int width_bar;
    protected Paint paint_indicator, paint_bar;
    protected int height_bar;
    protected Context context;
    protected int radius_indicator_normal, radius_indicator_touch, radius_indicator;

    protected float x_down;
    protected float cx;
    public static final int PROGRESS_MAX = 100;
    protected int progress = 0;
    protected float second_right;
    protected int radius_bar = 0;
    protected float height_half;
    protected float height_rect_half;
    protected float sum;
    protected float diff;
    protected SeekBarGradient.OnSeekBarChangeListener onSeekBarChangeListener;
    protected boolean byTouch = false;
    //    protected boolean isTracking = false;
    protected @ColorInt int[] colors_gradient;
    protected float[] positions_gradient;
    protected Shader.TileMode tileMode_gradient;

    protected int r__;

    public SeekBarGradient(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint_indicator = new Paint();
        paint_bar = new Paint();

        paint_indicator.setAntiAlias(true);
        paint_bar.setAntiAlias(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeekBarGradient);

        setLinearGradient(new int[]{typedArray.getColor(R.styleable.SeekBarGradient_cy_color_start, 0xff00ff00),
                        typedArray.getColor(R.styleable.SeekBarGradient_cy_color_end, 0xffff0000)},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        setColor_indicator(typedArray.getColor(R.styleable.SeekBarGradient_color_indicator, 0xffffffff));

        setHeight_bar(typedArray.getDimensionPixelSize(R.styleable.SeekBarGradient_height_bar, dpAdapt(3)));
        setRadius_indicator_normal(typedArray.getDimensionPixelSize(R.styleable.SeekBarGradient_radius_indicator_normal, dpAdapt(4)));
        radius_indicator = radius_indicator_normal;
        setRadius_indicator_touch(typedArray.getDimensionPixelSize(R.styleable.SeekBarGradient_radius_indicator_touch, dpAdapt(8)));
        setRadius_bar(typedArray.getDimensionPixelSize(R.styleable.SeekBarGradient_radius_bar, 0));
        typedArray.recycle();

    }

    public void setLinearGradient(int[] colors_gradient, float[] positions_gradient, Shader.TileMode tileMode_gradient) {
        this.colors_gradient = colors_gradient;
        this.positions_gradient = positions_gradient;
        this.tileMode_gradient = tileMode_gradient;
        requestLayout();
    }

    public void setColor_indicator(int color_indicator) {
        paint_indicator.setColor(color_indicator);
    }


    public void setHeight_bar(int height_bar) {
        this.height_bar = height_bar;
    }

    public void setRadius_bar(int radius_bar) {
        this.radius_bar = radius_bar;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        int progress_last = this.progress;
        this.progress = Math.max(0, Math.min(PROGRESS_MAX, progress));
        if (progress_last == this.progress) return;
        invalidate();
        if (onSeekBarChangeListener != null)
            onSeekBarChangeListener.onProgressChanged(this, progress);
    }

    public void setRadius_indicator_normal(int radius_indicator_normal) {
        this.radius_indicator_normal = radius_indicator_normal;
    }

    public void setRadius_indicator_touch(int radius_indicator_touch) {
        this.radius_indicator_touch = radius_indicator_touch;
    }

    public Paint getPaint_bar() {
        return paint_bar;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 2 * Math.max(radius_indicator_normal, radius_indicator_touch) + dpAdapt(10));
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        width_bar = width - 2 * Math.max(radius_indicator_normal, radius_indicator_touch);
        height_half = height * 1f / 2;
        height_rect_half = height_bar * 1f / 2;
        sum = height_half + height_rect_half;
        diff = height_half - height_rect_half;

        paint_bar.setShader(new LinearGradient(0, 0, width_bar, 0, colors_gradient, positions_gradient, tileMode_gradient));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        r__ = Math.max(radius_indicator_normal, radius_indicator_touch);
        if (!byTouch) cx = progress * 1f / PROGRESS_MAX * width_bar + r__;
        //最底层的rect
        canvas.drawRoundRect(r__, diff, width - r__, sum, radius_bar, radius_bar, paint_bar);
        //indicator
        canvas.drawCircle(cx, height_half, radius_indicator, paint_indicator);
        byTouch = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final ViewParent parent = getParent();
        if (parent != null) parent.requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x_down = event.getX();
                if (onSeekBarChangeListener != null)
                    onSeekBarChangeListener.onStartTouch(this, progress);
                break;
            case MotionEvent.ACTION_MOVE:
                radius_indicator = radius_indicator_touch;
                invalidate_byTouch(event);
                if (invalidate_byTouch(event) && onSeekBarChangeListener != null)
                    onSeekBarChangeListener.onProgressChanged(this, progress);
                break;
            case MotionEvent.ACTION_UP:
                radius_indicator = radius_indicator_normal;
                //因为手指放下到抬起，ACTION_MOVE不一定会执行，所以加上onProgressChanged
                if (invalidate_byTouch(event) && onSeekBarChangeListener != null)
                    onSeekBarChangeListener.onProgressChanged(this, progress);
                if (onSeekBarChangeListener != null)
                    onSeekBarChangeListener.onStopTouch(this, progress);
                break;
        }
        return true;
    }

    private boolean invalidate_byTouch(MotionEvent event) {
        cx = event.getX();
        int progress_last = progress;
        progress = Math.max(0, Math.min((int) (cx * PROGRESS_MAX * 1f / width), PROGRESS_MAX));
        if (cx < r__) {
            cx = r__;
        } else if (cx > width - r__) {
            cx = width - r__;
        }
        byTouch = true;
        invalidate();

        if (progress_last == progress) return false;
        return true;
    }

    protected int dpAdapt(float dp) {
        return dpAdapt(dp, 360);
    }

    protected int dpAdapt(float dp, float widthDpBase) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int heightPixels = dm.heightPixels;//高的像素
        int widthPixels = dm.widthPixels;//宽的像素
        float density = dm.density;//density=dpi/160,密度比
        float heightDP = heightPixels / density;//高度的dp
        float widthDP = widthPixels / density;//宽度的dp
        float w = widthDP > heightDP ? heightDP : widthDP;
        return (int) (dp * w / widthDpBase * density + 0.5f);
    }

    public void setOnSeekBarChangeListener(SeekBarGradient.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }


    public static interface OnSeekBarChangeListener {

        void onProgressChanged(SeekBarGradient SeekBarGradient, int progress);

        void onStartTouch(SeekBarGradient SeekBarGradient, int progress);

        void onStopTouch(SeekBarGradient SeekBarGradient, int progress);
    }

}
