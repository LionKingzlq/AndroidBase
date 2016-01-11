package com.chetuan.askforit.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import com.rl01.lib.utils.logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Utils {

    private Utils(){

    }

    private static  Utils instance = null;

    public static Utils getInstance(){
        if(instance==null){
            synchronized(Utils.class){
                if(instance==null){
                    instance=new Utils();
                }
            }
        }
        return instance;
    }

    public static void moveFile(String srcFileName, String destDirName) throws Exception {

//	    File srcFile = new File(srcFileName);  
//	    if(!srcFile.exists() || !srcFile.isFile())   
//	        return false;  
//	    
//	    return srcFile.renameTo(new File(destDirName));  
        FileInputStream fis = new FileInputStream(new File(srcFileName));
        File out = new File(destDirName);
        if (out.exists()) {
            out.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(out);
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = fis.read(buffer)) != -1) {
            fos.write(buffer, 0, length);
        }
        fos.flush();
        fos.close();
        fis.close();
    }


    public static String getBASE64(String str) {
        String s = "";
        try {
            byte[] encodeBase64 = Base64.encode(str.getBytes("UTF-8"), Base64.NO_WRAP);
            s = new String(encodeBase64);
        } catch (Exception e) {
            logger.e(e);
        }
        return s;
    }


    /**
     * 获取手机通讯录的姓名和手机
     * @param
     * @return
     */
//	public static HashMap<String,String> getContact(Context ctx){
//		ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION)
//	}

    /**
     * 将输入流转成字符串形式
     *
     * @param is
     * @return
     */
    public static String getTextFromStream(InputStream is) {

        byte[] b = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while ((len = is.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            String text = new String(bos.toByteArray());
            bos.close();
            return text;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将集合转成String存储起来
     *
     * @param SceneList
     * @return
     * @throws IOException
     */
    public static String list2String(LinkedList<String> SceneList)
            throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }


    /**
     * 将字符串转成集合
     *
     * @param SceneListString
     * @return
     * @throws StreamCorruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static LinkedList<String> String2SceneList(String SceneListString)
            throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        LinkedList SceneList = (LinkedList) objectInputStream
                .readObject();
        objectInputStream.close();
        return SceneList;
    }

    /**
     * 拨打电话
     *
     * @param ctx
     * @param number
     */
    public static void dial(Context ctx, String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        ctx.startActivity(intent);
    }

    /**
     * 显示土司
     *
     * @param ctx
     * @param content
     */
    public static void showToast(Context ctx, String content, int length) {
        Toast.makeText(ctx, content, length).show();
    }

    /**
     * 判断字符串能否转化为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串能否转化为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumericReg(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断一个数是否是整数
     *
     * @param str
     * @return
     */
    public static boolean isPostiveInteger(String str) {
        if (isNumericReg(str)) {
            int intNumber = Integer.valueOf(str);
            double doubelNumber = Double.valueOf(str);
            if (intNumber - doubelNumber == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 截取时间
     *
     * @param txt_updateTime
     * @return
     */
    public static String subTime(TextView txt_updateTime) {
        int index = txt_updateTime.getText().toString().indexOf(":");
        return txt_updateTime.getText().toString().substring(index + 1);
    }
}
