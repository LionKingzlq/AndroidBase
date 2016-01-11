package com.chetuan.askforit.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by YT on 2015/7/29.
 */
public class BitmapUtil {

    private BitmapUtil(){

    }
    private static  BitmapUtil instance = null;

    public static BitmapUtil getInstance(){
        if(instance==null){
            synchronized(BitmapUtil.class){
                if(instance==null){
                    instance=new BitmapUtil();
                }
            }
        }
        return instance;
    }


    //
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float radius) {

        if (bitmap == null){
            return null;
        }

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        //
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        //
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        //
        canvas.drawRoundRect(rectF, radius, radius, paint);
        //
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //
        return output;
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null){
            return null;
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    public Drawable bitmapToDrawble(Bitmap bitmap,Context mcontext){

        if(bitmap ==null || mcontext == null) {
            return null;
        }
        Drawable drawable = new BitmapDrawable(mcontext.getResources(), bitmap);
        return drawable;
    }

    public byte[] bitmap2Bytes(Bitmap bm) {
        if (bm == null){
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public void saveBitmapInExternalStorage(Bitmap bitmap, Context context) {
        if(bitmap == null || context == null){
            return;
        }
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File extStorage = new File(Environment
                        .getExternalStorageDirectory().getPath() + "/orimuse");// orimuse为SD卡下一个文件夹
                if (!extStorage.exists()) {
                    extStorage.mkdirs();
                }
                File file = new File(extStorage, System.currentTimeMillis()
                        + ".png");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fOut);// 压缩图片
                fOut.flush();
                fOut.close();
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void saveMyBitmap(Context context, String bitName,
                                    Bitmap mBitmap) {

        if(context == null || mBitmap == null){
            return;
        }

        File f = new File(Environment.getExternalStorageDirectory().getPath()
                + "/" + bitName + ".png");

        try {
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
