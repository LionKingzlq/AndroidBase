package com.rl01.lib.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class HEPAES {
	private SecretKey _KEY;
	private IvParameterSpec _IV;
	private Cipher cipher;
	public HEPAES(String transformation){
		try {
			if(transformation.equals("AES/ECB/PKCS5PADDING") || 
			   transformation.equals("AES/CBC/PKCS5PADDING"))
			{
				cipher = Cipher.getInstance(transformation);
			}else{
				 throw new Exception("transformation = AES/ECB/PKCS5PADDING or AES/CBC/PKCS5PADDING");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public byte[] doAES(byte[] byteContent,int opmode){
		try{
			if(cipher.getAlgorithm().indexOf("ECB")!=-1){
				cipher.init(opmode, getKEY());
			}
			if(cipher.getAlgorithm().indexOf("CBC")!=-1){
				cipher.init(opmode, getKEY(), getIV());
			}
			byte[] resultByteContent = cipher.doFinal(byteContent);
			return resultByteContent;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	public String encrypt(byte[] byteContent) {
		try{
			return base64_Encode(doAES(byteContent, Cipher.ENCRYPT_MODE));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	public byte[] decrypt(String strContent) {
		try{
			return doAES(base64_Decode(strContent),	Cipher.DECRYPT_MODE);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	private String base64_Encode(byte[] byteContent) {
		try{
			//TODO
			return new String(Base64.encode(byteContent,Base64.DEFAULT));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	private byte[] base64_Decode(String base64Str) {
		try{
			//TODO
			return Base64.decode(base64Str.getBytes(),Base64.DEFAULT);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	public void setKEY(String keyStr,Boolean isMD5){
		_KEY = HEPAES.generateKey(keyStr,isMD5);
	}
	public SecretKey getKEY(){
		if(_KEY==null){
			_KEY = HEPAES.generateKey("",false);
		}
		return _KEY;
	}
	public static SecretKey generateKey(String seedStr , Boolean isMD5){  
        try {
			if(seedStr.equals("")){
				SecureRandom  random = new SecureRandom();
				seedStr = HEPAES.bytesToHexString(random.generateSeed(16));
			}
			//----------------------
			SecretKey secretKey;
			if(isMD5){
				secretKey = new SecretKeySpec(md5Hex(seedStr).substring(0, 16).getBytes("UTF-8"),"AES");
			}else{
				KeyGenerator kgen = KeyGenerator.getInstance("AES");
				SecureRandom  random = SecureRandom.getInstance("SHA1PRNG" );
				random.setSeed(seedStr.getBytes());   //��ɻ������ӵ��ֽ�
				kgen.init(128, random);
				secretKey = kgen.generateKey();
			}        	 
			System.out.println("Key:"+new String(secretKey.getEncoded(),"UTF-8"));
			return secretKey;
        } catch (Exception ex) {  
        	ex.printStackTrace();
        } 
        return null; 
	}
	public void setIV(String ivStr , Boolean isMD5){
		_IV = HEPAES.generateIV(ivStr,isMD5);
	}
	public IvParameterSpec getIV(){
		if(_IV==null){
			_IV = HEPAES.generateIV("",false);
		}
		return _IV;
	}
	public static IvParameterSpec generateIV(String seedStr , Boolean isMD5){
		 try {
			 
			 IvParameterSpec IV;
			 if(seedStr.equals("")){
				SecureRandom  random = new SecureRandom();
				seedStr = HEPAES.bytesToHexString(random.generateSeed(16));
			 }
			 if(isMD5){
				 seedStr = md5Hex(seedStr).substring(0, 16);
			 }else{
				 seedStr = (seedStr+"0000000000000000").substring(0, 16);
			 }
			 IV = new IvParameterSpec(seedStr.getBytes("UTF-8"));
			 System.out.println("IV(hex):"+HEPAES.bytesToHexString(IV.getIV()));
			 return IV;
		} catch (Exception ex) {  
			ex.printStackTrace();
        } 
        return null; 
	}
	public static File generateKeyFile(SecretKey key , String keyFilePath) {
		try{
			byte[] enCodeFormat = key.getEncoded();
	        File keyFile = new File(keyFilePath);
	        writeByteArrayToFile(keyFile, enCodeFormat);
	        return keyFile;
		}catch (Exception ex) {  
			System.out.println(ex.getMessage());
        }
		return null;
	}
	public static String bytesToHexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}
	
	public static final void writeByteArrayToFile(File file,byte[] data){
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(data);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String md5Hex(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
	
	public static final String decryptString(String key, String json) throws Exception{
		HEPAES aes = new HEPAES("AES/CBC/PKCS5PADDING");
		aes.setKEY(key,true);
		aes.setIV("vstudying.com",true);
		byte[] byteEncrypt = aes.decrypt(json);
		return new String(byteEncrypt);
	}
	
	public static final String ecryptString(String key, String json) throws Exception{
		HEPAES aes = new HEPAES("AES/CBC/PKCS5PADDING");
		aes.setKEY(key,true);
		aes.setIV("vstudying.com",true);
		return aes.encrypt(json.getBytes());
	}
	
}
