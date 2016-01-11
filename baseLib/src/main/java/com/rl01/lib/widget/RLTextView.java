package com.rl01.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.rl01.lib.base.R;
import com.rl01.lib.utils.StringUtils;

public class RLTextView extends TextView{
	
	public static final boolean USE_SPECIAL_FONT = false;
	private Context context = null;
	
	public RLTextView(Context context) {
		this(context, null);
	}

	
	public RLTextView(Context context, AttributeSet attrs) {
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
			fontPath = RLButton.FONTS_PATH;
			break;
		case 2:
			fontPath = RLButton.FONTS_PATH;
			break;
		case 3:
			fontPath = RLButton.FONTS_PATH;
			break;
		}
		if(!StringUtils.isNull(fontPath)){
			Typeface typeFace =Typeface.createFromAsset(context.getAssets(),fontPath);
			this.setTypeface(typeFace);
		}
	}
	
	public void setPrice(String price){
		if(getPaddingTop() == 0 ){
			this.setPadding(getPaddingLeft(), getPaddingTop()+3, getPaddingRight(), getPaddingBottom());
		}
		SpannableString msp = new SpannableString(String.format(this.getContext().getString(R.string.ky_price),price));
		msp.setSpan(new RelativeSizeSpan(0.7f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		msp.setSpan(new SuperscriptSpan(), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		setText(msp); 
	}
	
}
