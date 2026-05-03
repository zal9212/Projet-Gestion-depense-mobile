package com.tp.gestiondepenses.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CategoryDistributionView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<CategoryData> data = new ArrayList<>();
    private RectF rectF = new RectF();

    public CategoryDistributionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(List<CategoryData> data) {
        this.data = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data.isEmpty()) return;

        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) / 2 * 0.8f;
        rectF.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);

        double total = 0;
        for (CategoryData d : data) total += d.amount;

        float startAngle = -90;
        for (CategoryData d : data) {
            float sweepAngle = (float) ((d.amount / total) * 360);
            try {
                paint.setColor(Color.parseColor(d.color));
            } catch (Exception e) {
                paint.setColor(Color.GRAY);
            }
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
            startAngle += sweepAngle;
        }

        // Draw center hole for donut style
        try {
            paint.setColor(Color.parseColor("#121212")); // Should match background
        } catch (Exception e) {
            paint.setColor(Color.BLACK);
        }
        canvas.drawCircle(width / 2, height / 2, radius * 0.6f, paint);
    }

    public static class CategoryData {
        public String name;
        public double amount;
        public String color;

        public CategoryData(String name, double amount, String color) {
            this.name = name;
            this.amount = amount;
            this.color = color;
        }
    }
}
