package com.chetuan.askforit;

import com.chetuan.askforit.constant.FrescoConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.rl01.lib.BaseApplication;
import com.rl01.lib.utils.DirUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.xiyuan.umeng.util.UMengUtil;

/**
 * Created by Abraham on 2016/1/11.
 */
public class AskForItApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        DirUtils.getInstance().initFolder();
        UMengUtil.initPlatformsConfig();
        Fresco.initialize(this, FrescoConfig.getImagePipelineConfig(this.getApplicationContext()));

        CrashReport.initCrashReport(getApplicationContext(), "900015681", BuildConfig.DEBUG);
    }
}