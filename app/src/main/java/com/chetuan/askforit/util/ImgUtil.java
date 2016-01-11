package com.chetuan.askforit.util;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;

/**
 * Created by app on 15/11/9.
 */
public class ImgUtil {



    private SimpleDraweeView imgView;

    public void load(String url, SimpleDraweeView imageView){
        imgView = imageView;

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {

            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                super.onFinalImageSet(id, imageInfo, anim);
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                FLog.d("Final image received! " + "Size %d x %d", "Quality level %d, good enough: %s, full quality: %s",
                        imageInfo.getWidth(),
                        imageInfo.getHeight(),
                        qualityInfo.getQuality(),
                        qualityInfo.isOfGoodEnoughQuality(),
                        qualityInfo.isOfFullQuality());

                Log.e("tianqin >>>", "onLoadSuccess():" + id);
                //回调
                onLoadSuccess(imgView, imageInfo, anim);

            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
                Log.e("tianqin >>>", "onIntermediateImageSet():" + id);
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                Log.e("tianqin >>>", "onFailure():" + id);
                onLoadFailure(id, throwable);
            }
        };

        Uri uri = Uri.parse(url);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(uri)
                .build();

        imageView.setController(controller);

    }


    protected void onLoadSuccess(SimpleDraweeView view, ImageInfo imageInfo, Animatable anim) {

    }

    protected void onLoadFailure(String id, Throwable throwable) {
        Log.e("tianqin >>>", "onLoadSuccess():" + id);
    }


//    public void load(String url, ImageView imageView)
//    {
//        IImage.displayImage(url, imageView, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                ImgUtil.this.onLoadingComplete(imageUri, view, loadedImage);
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//
//            }
//        });
//    }
//
//    public void loadWithoutDefault(String url, ImageView imageView)
//    {
//
//        IImage.displayImage(url, imageView, options, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                ImgUtil.this.onLoadingComplete(imageUri, view, loadedImage);
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//
//            }
//        });
//    }
//
//    protected void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
//    {
//        view.postInvalidate();
//    }
}
