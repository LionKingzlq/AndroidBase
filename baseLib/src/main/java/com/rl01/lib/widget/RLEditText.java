package com.rl01.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;

import com.rl01.lib.base.R;
import com.rl01.lib.utils.StringUtils;

public class RLEditText extends EditText{
	
	private static final int MAX_PRICE_LENGTH = 4;
	public static final boolean USE_SPECIAL_FONT = false;
	
	private Context context = null;
	private CharSequence hint = "";
	
	public RLEditText(Context context) {
		super(context, null);
	}

	public RLEditText(Context context, AttributeSet attrs) {
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
	
	public void setPrice(){
		this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_PRICE_LENGTH)});  
		this.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
		this.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(hint == null || hint.length() == 0){
					hint = getHint();
				}
				if(s.length()>0){
					setHint("");
				}else{
					setHint(hint);
				}
				
			}
		});
	}
	
}
