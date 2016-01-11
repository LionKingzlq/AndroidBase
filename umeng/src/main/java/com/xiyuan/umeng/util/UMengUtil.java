package com.xiyuan.umeng.util;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by YT on 2015/12/29.
 */
public class UMengUtil {

    private static UMengUtil instance;

    private UMShareListener umShareListener;

    private ShareAction shareAction;

    private Activity context;

    public static void initPlatformsConfig()
    {
        //新浪微博
        PlatformConfig.setSinaWeibo("3628460667", "8e2dee3a39656ede0a32e3df956bcdb3");
        //微信、朋友圈
        PlatformConfig.setWeixin("wx27edf70b31a05b35", "2edd46b3d3bc35d3dff002c76abffc10");
        //QQ
        PlatformConfig.setQQZone("1105016509", "72VY7TF0KGEjnXbC");
        //YiXin
        PlatformConfig.setYixin("yxad505069a3ac4119b013f41727be5b87");
    }

    private void setContext(Activity context) {
        this.context = context;
        shareAction = new ShareAction(this.context).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                                                                        SHARE_MEDIA.SINA,  SHARE_MEDIA.YIXIN, SHARE_MEDIA.SMS)//SHARE_MEDIA.TENCENT,
                .setListenerList(umShareListener);
    }

    public static UMengUtil getInstance(Activity context) {
        if(instance == null)
        {
            instance = new UMengUtil();
        }
        if(instance.context != context)
        {
            instance.setContext(context);
        }
        return instance;
    }

    private UMengUtil(){
        umShareListener = new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA platform) {
                if(context != null)
                {
                    Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                if(context != null)
                {
                    Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }
        };
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
    }

    public void openShare(String title, String content, String imgUrl, String url)
    {
        if(context != null && shareAction != null)
        {
            shareAction.withTitle(title)
                    .withText(content)
                    .withMedia(new UMImage(context, imgUrl))
                    .withTargetUrl(url)
                    .open();
        }
    }

}

