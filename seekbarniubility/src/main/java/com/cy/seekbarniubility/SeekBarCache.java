package com.cy.seekbarniubility;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;


import androidx.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/7/11 22:16
 * @UpdateUser:
 * @UpdateDate: 2020/7/11 22:16
 * @UpdateRemark:
 * @Version: 1.0
 */
public class SeekBarCache extends SeekBarSimple {
    //有序
    private Map<Float, Float> map = new LinkedHashMap<>();
    private Map<Long, Long> map_long = new LinkedHashMap<>();

    public SeekBarCache(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBarCache addCacheRecord( long index_start, long index_end) {
        map_long.put(index_start, index_end);
        return this;
    }

    public void refreshCacheProgress(long contentLength) {
        for (Long index_start : map_long.keySet()) {
            float l = radius_indicator_touch+(index_start +1)* 1f / contentLength * width_bar;
            float r =  radius_indicator_touch+(map_long.get(index_start)+1) * 1f / contentLength * width_bar;
            map.put(l, r);
        }
        map_long.clear();
        invalidate();
    }

    @Override
    protected void draw_others(Canvas canvas) {
        for (Float left : map.keySet()) {
            Float right = map.get(left);
            if (right > cx+radius_indicator_normal) canvas.drawRoundRect(Math.max(cx, left), diff,right, sum,
                    radius_bar, radius_bar, paint_second);
        }
        draw_up(canvas);
    }

}
