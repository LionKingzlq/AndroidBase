package com.chetuan.askforit.util;

/**
 * Created by yangwc  .
 */
public class RegexUtil {

    private RegexUtil(){

    }

    private static  RegexUtil instance = null;

    public static RegexUtil getInstance(){
        if(instance==null){
            synchronized(ResoureUtil.class){
                if(instance==null){
                    instance=new RegexUtil();
                }
            }
        }
        return instance;
    }

    public boolean isTelephone(String telephone)
    {
        if(telephone == null || telephone.equals(""))
        {
            return false;
        }
        String regex1 = "\\d{3}-\\d{8}";
        String regex2 = "\\d{11}";
        return telephone.matches(regex1) || telephone.matches(regex2);
    }

    public boolean isMobile(String mobile)
    {
        if(mobile == null || mobile.equals(""))
        {
            return false;
        }
        String regex = "[1][3758]\\d{9}";
        return mobile.matches(regex);
    }

    public boolean isEmail(String email)
    {
        if(email == null || email.equals(""))
        {
            return false;
        }
        String regex = "[0-9a-zA-Z\\-_]{1,}@[0-9a-zA-Z\\-_]{1,63}\\.[0-9a-zA-Z\\-_]{1,}";
        return email.matches(regex);
    }

}
