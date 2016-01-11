package com.chetuan.askforit.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {
	
	private Paint paint = null;
	
	public MyTextView(Context context) {
		super(context);
		init();
	}
	
	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	private void init(){
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(1);
		paint.setColor(getCurrentTextColor());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2, paint);
	}
	
}
