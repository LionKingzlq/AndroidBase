package com.rl01.lib.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.StatFs;

public class FileUtils {

//	public static void deleteFile(String dirPath) {
//		File file = new File(dirPath);
//		if (file.isFile()) {
//			file.delete();
//		} else {
//			File[] files = file.listFiles();
//			if (files != null && files.length != 0) {
//				for (File f : file.listFiles()) {
//					f.delete();
//				}
//			}
//		}
//	}

	public static boolean canUpdateApk() {
		if (DirUtils.getInstance().getMounted()) {
			try {
				long t = getAvailableMemorySize(DirUtils.getInstance()
						.getCacheVideoPath());
				if (t > 0) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				logger.e(e);
			}
		}
		return false;
	}

	/**
	 * 获取手机外部可用空间大小
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getAvailableMemorySize(String f) {
		try {
			File path = new File(f);
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} catch (Exception e) {
			logger.e(e);
			return -1;
		}
	}

	public static void installAPK(String apkPath, Context activity) {
		if (apkPath != null) {
			File install = new File(apkPath);
			installAPK(install, activity);
		}
	}

	public static void installAPK(File apkfile, Context activity) {
		if (apkfile != null && apkfile.exists() && apkfile.canRead()) {
			Intent apkintent = new Intent(Intent.ACTION_VIEW);
			apkintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			apkintent.setDataAndType(Uri.fromFile(apkfile),
					"application/vnd.android.package-archive");
			activity.startActivity(apkintent);
		}
	}

	public static String getFileNameFromURL(String url) {
		if (url != null && url.contains("/")) {
			return url.substring(url.lastIndexOf("/") + 1);
		} else {
			return null;
		}
	}

	/**
	 * 流写入文件
	 * 
	 * @param ins
	 * @param file
	 * @throws Exception
	 */
	public static void inputStreamToTile(InputStream ins, File file)
			throws Exception {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
			os.flush();
		}
		os.close();
		ins.close();
	}

	/**
	 * 流写入文件
	 * 
	 * @param ins
	 * @param file
	 * @throws Exception
	 */
	public static void inputStreamToTile(InputStream ins, File file,
			Handler handler) throws Exception {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		int curPos = 0;
		while ((bytesRead = ins.read(buffer)) != -1) {
			curPos = curPos + bytesRead;
			handler.sendEmptyMessage(curPos);
			os.write(buffer, 0, bytesRead);
			os.flush();
		}
		os.close();
		ins.close();
	}

	/**
	 * 流写入文件
	 * 
	 * @param ins
	 * @param file
	 *            返回一个百分比数字比如50%
	 * @throws Exception
	 */
	public static void inputStreamToTile(InputStream ins, File file,
			Handler handler, int tag, long fileSize) throws Exception {
		if (fileSize <= 0) {
			logger.e("error error error file size");
			return;
		}
		OutputStream os = new FileOutputStream(file, true);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		long curPos = file.length();
		while ((bytesRead = ins.read(buffer)) != -1) {
			curPos = curPos + bytesRead;
			int progress = (int) Math.floor(((double) curPos * 100 / fileSize));
			handler.sendMessage(handler.obtainMessage(tag, progress));
			os.write(buffer, 0, bytesRead);
			os.flush();
		}
		os.close();
		ins.close();
	}

	/**
	 * 重命名文件
	 * 
	 * @param oldName
	 * @param newName
	 * @throws Exception
	 */
	public static void renameFile(String oldName, String newName)
			throws Exception {
		File newFile = new File(newName);
		if (newFile.exists()) {
			newFile.delete();
		}
		File oldFile = new File(oldName);
		if (oldFile.exists()) {
			oldFile.renameTo(newFile);
		}
	}

	/**
	 * 获取单个文件的MD5值！
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	/**
	 * 获取文件夹中文件的MD5值
	 * 
	 * @param file
	 * @param listChild
	 *            ;true递归子目录中的文件
	 * @return
	 */
	public static Map<String, String> getDirMD5(File file, boolean listChild) {
		if (!file.isDirectory()) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String md5;
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory() && listChild) {
				map.putAll(getDirMD5(f, listChild));
			} else {
				md5 = getFileMD5(f);
				if (md5 != null) {
					map.put(f.getPath(), md5);
				}
			}
		}
		return map;
	}

	public static boolean canUseSdCard() {
		if (DirUtils.getInstance().getMounted()) {
			try {
				long t = getAvailableMemorySize(DirUtils.getInstance()
						.getCacheVideoPath());
				if (t > 0) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				logger.e(e);
			}
		}
		return false;
	}

	public static byte[] obj2bytes(Object o) {
		byte[] temp = null;
		if (o != null) {
			try {
				ByteArrayOutputStream f = new ByteArrayOutputStream();
				ObjectOutputStream s = new ObjectOutputStream(f);
				s.writeObject(o);
				s.flush();
				temp = f.toByteArray();
				s.close();
				f.close();
			} catch (Exception e) {
				logger.e(e);
			}
		}
		return temp;
	}

	public static Object bytes2obj(byte[] byts) {
		Object temp = null;
		if (byts != null && byts.length > 0) {
			try {
				ByteArrayInputStream f = new ByteArrayInputStream(byts);
				ObjectInputStream o = new ObjectInputStream(f);
				temp = o.readObject();
				o.close();
				f.close();
			} catch (Exception e) {
				logger.e(e);
			}
		}
		return temp;
	}
	
	public static void deleteFile(String path) {
		if(!StringUtils.isNull(path)){
			try {
				File f = new File(path);
				if (!f.exists()) {
					return;
				}
				if (f.isFile()) {
					f.delete();
				}
			} catch (Exception e) {
				logger.e(e);
			}
		}else{
			logger.e("----delete---file---is---null---");
		}
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean delAllFile(String path) {
		boolean flag = false;
		try {
			File file = new File(path);
			if (!file.exists()) {
				return flag;
			}
			if (!file.isDirectory()) {
				return flag;
			}
			String[] tempList = file.list();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				if (path.endsWith(File.separator)) {
					temp = new File(path + tempList[i]);
				} else {
					temp = new File(path + File.separator + tempList[i]);
				}
				if (temp.isFile()) {
					temp.delete();
				}
				if (temp.isDirectory()) {
					delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
					delFolder(path + "/" + tempList[i]);// 再删除空文件夹
					flag = true;
				}
			}
		} catch (Exception e) {
			logger.e(e);
		}
		return flag;
	}

}
