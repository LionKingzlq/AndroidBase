package com.chetuan.askforit.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by YT on 2015/9/23.
 */
public class CircleLoadingView extends TextView {

    private int backColor;

    private int frontColor;

    private int width;

    private int height;

    private int innerRadius;

    private int outRadius;

    private int max = 100;

    private int current;

    private Paint paint;

    public CircleLoadingView(Context context) {
        super(context);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)getLayoutParams();
        if(lp != null)
        {
            width = lp.width;
            height = lp.height;
            outRadius = width / 2;
            innerRadius = outRadius - 4;
        }
        backColor = getCurrentHintTextColor();
        frontColor = getCurrentTextColor();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(outRadius - innerRadius);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void show()
    {
        current = 0;
        setVisibility(View.VISIBLE);
        invalidate();
    }

    public void hide()
    {
        setVisibility(View.GONE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(getVisibility() != View.VISIBLE)
        {
            return;
        }
        int halfStoke = (outRadius - innerRadius) / 2;
        RectF rectFP = new RectF(width/ 2 - innerRadius - halfStoke, height / 2 - innerRadius - halfStoke, width / 2 + innerRadius + halfStoke, height / 2 + innerRadius + halfStoke);
        paint.setColor(backColor);
        canvas.drawArc(rectFP, 0, 360, false, paint);
        paint.setColor(frontColor);
        current += max / 50.0f + 0.5f;
        int startAngle = -90 + 360 * current / max;
        canvas.drawArc(rectFP, startAngle, 90, false, paint);
        invalidate();
    }

}
