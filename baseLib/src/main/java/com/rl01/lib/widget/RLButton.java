package com.rl01.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.rl01.lib.base.R;
import com.rl01.lib.utils.StringUtils;
import com.rl01.lib.utils.logger;

public class RLButton extends Button{
	
	public static final String FONTS_PATH = "fonts/CenturyGothic.ttf";
	public static final boolean USE_SPECIAL_FONT = !logger.logFlag;
	
	private Context context = null;
	
	public RLButton(Context context) {
		this(context, null);
	}

	
	public RLButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		if(USE_SPECIAL_FONT){
			init(attrs);
		}
	}
	
	public void init(AttributeSet attrs){
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.rlfont);//TypedArray是一个数组容器
		int index = a.getInt(R.styleable.rlfont_rlTextFont, 0);
		a.recycle();
		String fontPath = "";
		switch (index) {
		case 0:
			break;
		case 1:
			fontPath = FONTS_PATH;
			break;
		case 2:
			fontPath = FONTS_PATH;
			break;
		case 3:
			fontPath = FONTS_PATH;
			break;
		}
		if(!StringUtils.isNull(fontPath)){
			Typeface typeFace =Typeface.createFromAsset(context.getAssets(),fontPath);
			this.setTypeface(typeFace);
		}
	}
	
}
