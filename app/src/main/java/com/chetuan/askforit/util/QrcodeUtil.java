package com.chetuan.askforit.util;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by YT on 2015/11/18.
 */
public final class QrcodeUtil {

    private QrcodeUtil(){

    }

    private static  QrcodeUtil instance = null;

    public static QrcodeUtil getInstance(){
        if(instance==null){
            synchronized(QrcodeUtil.class){
                if(instance==null){
                    instance=new QrcodeUtil();
                }
            }
        }
        return instance;
    }


    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;

    public Bitmap createQrcode(String str, int width, int height) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, height);
            int[] pixels = new int[width*height];
            for(int y = 0; y<width; ++y){
                for(int x = 0; x<height; ++x){
                    if(matrix.get(x, y)){
                        pixels[y*width+x] = BLACK;
                    } else {
                        pixels[y*width+x] = WHITE;
                    }
                }
            }
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bmp.setPixels(pixels, 0, width, 0, 0, width, height);
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap createQrcode(String str)
    {
        return createQrcode(str, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

}
