package com.rl01.lib.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.rl01.lib.BaseApplication;

public class ResUtils {

	public static final String anim = "anim";
	public static final String animator = "animator";
	public static final String array = "array";
	public static final String attr = "attr";
	public static final String bool = "bool";
	public static final String color = "color";
	public static final String dimen = "dimen";
	public static final String drawable = "drawable";
	public static final String id = "id";
	public static final String interpolator = "interpolator";
	public static final String layout = "layout";
	public static final String menu = "menu";
	public static final String mipmap = "mipmap";
	public static final String raw = "raw";
	public static final String string = "string";
	public static final String stringArray = "string-array";
	public static final String style = "style";
	public static final String styleable = "styleable";
	public static final String xml = "xml";

	public int getResAnimID(String paramString) {
		return resources.getIdentifier(paramString, anim, packageName);
	}

	public int getResAnimatorID(String paramString) {
		return resources.getIdentifier(paramString, animator, packageName);
	}

	public int getResArrayID(String paramString) {
		return resources.getIdentifier(paramString, array, packageName);
	}

	public int getResAttrID(String paramString) {
		return resources.getIdentifier(paramString, attr, packageName);
	}

	public int getResBoolID(String paramString) {
		return resources.getIdentifier(paramString, bool, packageName);
	}

	public int getResColorID(String paramString) {
		return resources.getIdentifier(paramString, color, packageName);
	}

	public int getResDimenID(String paramString) {
		return resources.getIdentifier(paramString, dimen, packageName);
	}

	public int getResDrawableID(String paramString) {
		return resources.getIdentifier(paramString, drawable, packageName);
	}

	public int getResIdID(String paramString) {
		return resources.getIdentifier(paramString, id, packageName);
	}

	public int getResInterpolatorID(String paramString) {
		return resources.getIdentifier(paramString, interpolator, packageName);
	}

	public int getResLayoutID(String paramString) {
		return resources.getIdentifier(paramString, layout, packageName);
	}

	public int getResMenuID(String paramString) {
		return resources.getIdentifier(paramString, menu, packageName);
	}

	public int getResMipmapID(String paramString) {
		return resources.getIdentifier(paramString, mipmap, packageName);
	}

	public int getResRawID(String paramString) {
		return resources.getIdentifier(paramString, raw, packageName);
	}

	public int getResStringArrayID(String paramString) {
		return resources.getIdentifier(paramString, stringArray, packageName);
	}

	public int getResStringID(String paramString) {
		return resources.getIdentifier(paramString, string, packageName);
	}

	public int getResStyleID(String paramString) {
		return resources.getIdentifier(paramString, style, packageName);
	}

	public int getResStyleableID(String paramString) {
		return resources.getIdentifier(paramString, styleable, packageName);
	}

	public int getResXmlID(String paramString) {
		return resources.getIdentifier(paramString, xml, packageName);
	}

	public Animation getAnimation(String paramString) {
		return AnimationUtils.loadAnimation(mContext, getResAnimID(paramString));
	}
	
	public Drawable getDrawable(String paramString) {
		return resources.getDrawable(getResDrawableID(paramString));
	}
	
	public String getString(Context paramContext, String paramString) {
		return resources.getString(getResStringID(paramString));
	}
	
	private Context mContext;
	private String packageName;
	private Resources resources;
	
	private ResUtils(Context mContext,String packageName){
		this.mContext = mContext.getApplicationContext();
		this.packageName = packageName;
		this.resources = mContext.getResources();
	}
	
	private static Map<String, ResUtils> resMap = new HashMap<String, ResUtils>();

	public static ResUtils getRes(Context paramContext,String packName) {
		ResUtils res = resMap.get(packName);
		if(res == null){
			res = new ResUtils(paramContext,packName);
		}
		return res;
	}
	
	public static ResUtils getRes(Context paramContext) {
		return getRes(paramContext,paramContext.getPackageName());
	}
	
	public static ResUtils getRes() {
		return getRes(BaseApplication.getInstance());
	}

}
