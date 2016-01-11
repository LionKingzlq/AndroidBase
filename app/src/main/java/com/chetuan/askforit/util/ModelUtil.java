package com.chetuan.askforit.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * Created by YT on 2015/11/11.
 */
public class ModelUtil {

    private ModelUtil(){

    }
    private static  ModelUtil instance = null;

    public static ModelUtil getInstance(){
        if(instance==null){
            synchronized(ModelUtil.class){
                if(instance==null){
                    instance=new ModelUtil();
                }
            }
        }
        return instance;
    }


    private static Gson mGson = new Gson();

    public <T> T jsonToModel(JsonElement jsonElement, Class clazz)
    {
        T t = null;
        try
        {
            t = (T) mGson.fromJson(jsonElement, clazz);
        }
        catch (Exception e)
        {
            Log.e("xiyuan", e.getMessage(), e);
        }
        return t;
    }

    public <T> String modelToJsonStr( T t)
    {
        String json = "";
        try
        {
            json = mGson.toJson(t);
        }
        catch (Exception e)
        {
            Log.e("xiyuan", e.getMessage(), e);
        }
        return json;
    }

    public <T> JsonElement modelToJson( T t)
    {
        JsonElement json = null;
        try
        {
            json = mGson.toJsonTree(t);
        }
        catch (Exception e)
        {
            Log.e("xiyuan", e.getMessage(), e);
        }
        return json;
    }

}
