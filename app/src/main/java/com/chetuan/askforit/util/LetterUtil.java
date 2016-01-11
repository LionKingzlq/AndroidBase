package com.chetuan.askforit.util;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Created by xujie on 2015/9/17.
 */
public class LetterUtil {

    private LetterUtil(){

    }

    private static  LetterUtil instance = null;

    public static LetterUtil getInstance(){
        if(instance==null){
            synchronized(LetterUtil.class){
                if(instance==null){
                    instance=new LetterUtil();
                }
            }
        }
        return instance;
    }

    public String[] getFirstPinyin(char chinese)
    {
        return PinyinHelper.toHanyuPinyinStringArray(chinese);
    }

    public boolean isLetter(char c)
    {
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 112);
    }
}
