package com.chetuan.askforit.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chetuan.askforit.activityParam.ParasBase;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YT on 2015/11/10.
 */
public class ActivityUtil {
    private ActivityUtil(){

    }
    private static  ActivityUtil instance = null;

    public static ActivityUtil getInstance(){
        if(instance==null){
            synchronized(ActivityUtil.class){
                if(instance==null){
                    instance=new ActivityUtil();
                }
            }
        }
        return instance;
    }

    private ArrayList<Activity> activities = new ArrayList<>();

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void addActivity(Activity activity)
    {
        int activitySize = activities.size();
        ArrayList<Activity> tempActivities = new ArrayList<>();
        for(int i = 0; i < activitySize; i ++)
        {
            Activity temp = activities.get(i);
            if(temp != null && !temp.isFinishing())
            {
                tempActivities.add(temp);
            }
        }
        activities.clear();
        activities = tempActivities;

        if(activity != null && !activity.isFinishing())
        {
            activities.add(activity);
        }
    }

    public void startNewActivity(Context context, Class clazz, Serializable paras)
    {
        if(context != null && clazz !=null){
            Intent itt = new Intent(context, clazz);
            if(paras != null)
            {
                itt.putExtra(paras.getClass().getSimpleName(), paras);
            }
            context.startActivity(itt);
        }
    }

    public void startNewActivityForResult(Context context, Class clazz, int requestCode, ParasBase paras)
    {
        if(context != null && clazz !=null) {

            Intent itt = new Intent(context, clazz);
            if(paras != null)
            {
                itt.putExtra(paras.getClass().getSimpleName(), paras);
            }
            if(context instanceof Activity)
            {
                ((Activity) context).startActivityForResult(itt, requestCode);
            }
        }
    }

    public ParasBase getDefaultParas(Class clazz)
    {
        try {
            Object obj = clazz.newInstance();
            if(obj instanceof ParasBase)
            {
                return (ParasBase) obj;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Serializable getParas(Intent itt, Class clazz)
    {
        if (itt != null && clazz !=null){
            Serializable paras = itt.getSerializableExtra(clazz.getSimpleName());
            return paras;
        }
        return null;
    }

}
