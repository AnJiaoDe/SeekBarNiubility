package com.cy.seekbarniubility;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.Nullable;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/7/11 18:01
 * @UpdateUser:
 * @UpdateDate: 2020/7/11 18:01
 * @UpdateRemark:
 * @Version:
 */
public class SeekBarSimple extends View {
    protected int width, height;
    protected int width_bar;
    protected Paint paint_indicator, paint_one, paint_bar, paint_second;
    protected int height_bar;
    protected Context context;
    protected int radius_indicator_normal, radius_indicator_touch, radius_indicator;

    protected float x_down;
    protected float cx;
    public static final int PROGRESS_MAX = 100;
    protected int progress = 0;
    protected int progress_second = 0;
    protected float second_right;
    protected int radius_bar = 0;
    protected float height_half;
    protected float height_rect_half;
    protected float sum;
    protected float diff;
    protected OnSeekBarChangeListener onSeekBarChangeListener;
    protected boolean byTouch = false;
    //    protected boolean isTracking = false;
    protected int r__;

    public SeekBarSimple(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint_indicator = new Paint();
        paint_bar = new Paint();
        paint_second = new Paint();
        paint_one = new Paint();

        paint_indicator.setAntiAlias(true);
        paint_bar.setAntiAlias(true);
        paint_second.setAntiAlias(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeekBarSimple);

        setColor_bar(typedArray.getColor(R.styleable.SeekBarSimple_color_bar, 0x33ffffff));
        setColor_one(typedArray.getColor(R.styleable.SeekBarSimple_color_one, 0xff2a83fc));
        setColor_second(typedArray.getColor(R.styleable.SeekBarSimple_color_second, 0x442a83fc));
        setColor_indicator(typedArray.getColor(R.styleable.SeekBarSimple_color_indicator, 0xffffffff));

        setHeight_bar(typedArray.getDimensionPixelSize(R.styleable.SeekBarSimple_height_bar, dpAdapt(3)));
        setRadius_indicator_normal(typedArray.getDimensionPixelSize(R.styleable.SeekBarSimple_radius_indicator_normal, dpAdapt(4)));
        radius_indicator = radius_indicator_normal;
        setRadius_indicator_touch(typedArray.getDimensionPixelSize(R.styleable.SeekBarSimple_radius_indicator_touch, dpAdapt(8)));
        setRadius_bar(typedArray.getDimensionPixelSize(R.styleable.SeekBarSimple_radius_bar, 0));
        typedArray.recycle();
    }

    public void setColor_indicator(int color_indicator) {
        paint_indicator.setColor(color_indicator);
    }

    public void setColor_second(int color_second) {
        paint_second.setColor(color_second);
    }

    public void setColor_one(int color_one) {
        paint_one.setColor(color_one);
    }

    public void setColor_bar(int color_rect) {
        paint_bar.setColor(color_rect);
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
            onSeekBarChangeListener.onProgressChanged(this, this.progress,false);
    }

    public int getProgress_second() {
        return progress_second;
    }

    public void setProgress_second(int progress_second) {
        int progress_last = progress;
        this.progress = Math.max(0, Math.min(PROGRESS_MAX, progress_second));
        if (progress_last == progress) return;
        second_right = progress_second * 1f / PROGRESS_MAX * width_bar;
        invalidate();
    }

    public void setRadius_indicator_normal(int radius_indicator_normal) {
        this.radius_indicator_normal = radius_indicator_normal;
    }

    public void setRadius_indicator_touch(int radius_indicator_touch) {
        this.radius_indicator_touch = radius_indicator_touch;
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        r__ = Math.max(radius_indicator_normal, radius_indicator_touch);
        if (!byTouch) cx = progress * 1f / PROGRESS_MAX * width_bar + r__;
        //最底层的rect
        canvas.drawRoundRect(r__, diff, width - r__, sum, radius_bar, radius_bar, paint_bar);
        draw_others(canvas);
        byTouch = false;
    }

    protected void draw_others(Canvas canvas) {
        draw_up(canvas);
    }

    protected void draw_up(Canvas canvas) {
//        //2级缓存进度
//        canvas.drawRoundRect(cx, diff, second_right, sum, radius_bar, radius_bar, paint_second);
//        //紧跟indicator的rect
        canvas.drawRoundRect(r__, diff, cx, sum, radius_bar, radius_bar, paint_one);
        //indicator
        canvas.drawCircle(cx, height_half, radius_indicator, paint_indicator);
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
                if (invalidate_byTouch(event) && onSeekBarChangeListener != null)
                    onSeekBarChangeListener.onProgressChanged(this, progress,false);
                break;
            case MotionEvent.ACTION_UP:
                radius_indicator = radius_indicator_normal;
                //因为手指放下到抬起，ACTION_MOVE不一定会执行，所以加上onProgressChanged
                if (invalidate_byTouch(event) && onSeekBarChangeListener != null)
                    onSeekBarChangeListener.onProgressChanged(this, progress,true);
                if (onSeekBarChangeListener != null)
                    onSeekBarChangeListener.onStopTouch(this, progress);
                break;
        }
        return true;
    }

    private boolean invalidate_byTouch(MotionEvent event) {
        cx = event.getX();
        int progress_last = progress;
        progress = Math.max(0, Math.min((int) ((cx - r__) / width_bar * PROGRESS_MAX), PROGRESS_MAX));
        cx = Math.min(width - r__, Math.max(cx, r__));
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

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }


    public static abstract class OnSeekBarChangeListener {

        public abstract void onProgressChanged(SeekBarSimple seekBarSimple, int progress, boolean touchStop);

        public void onStartTouch(SeekBarSimple seekBarSimple, int progress) {
        }

        public void onStopTouch(SeekBarSimple seekBarSimple, int progress) {
        }
    }
}
