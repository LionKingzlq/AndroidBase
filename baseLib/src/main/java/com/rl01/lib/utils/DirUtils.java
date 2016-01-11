package com.rl01.lib.utils;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;

import com.rl01.lib.BaseApplication;

public class DirUtils {
	
	public static final String ACTION_SDCARD_STATUS_CHANGED = BaseApplication.getPackName() + ".action.sdcard.changed";
	public static final String FILESTARTWITH = "file://";
	public static final String ASSETS_BASE = "/android_asset/";
	private static final String DIR_BASE = "ChinaCar";
	private static final String DIR_BASE_DISPLAY = "chinacar";
	private static final String DIR_MIDDLE = BaseApplication.getPackName().substring(BaseApplication.getPackName().lastIndexOf(".")+1);
//	private static final String DIR_DOWNLOAD_UPDATE = "/mnt/sdcard/removable_sdcard/"+ DIR_BASE + File.separator + DIR_MIDDLE +File.separator;
	
	private String DEFAULT_DIR_PATH = "";
	private String CACHE_VIDEO = "";
	private String CACHE_SOUND = "";
	private String CACHE_PHOTO = "";
	//升级文件路径
	private String TEMPLATE_ROOT = "";
	
	private String DISPLAY_PATH = "";
	
	//前缀
	public static final String PRE_PHOTO="p_";
	public static final String PRE_AUDIO="a_";
	public static final String PRE_VIDEO="v_";
	public static final String PRE_HANDNOTE="hand_"; 
	public static final String PRE_LOCAL = "temp_";
	
	private static DirUtils dirUtils = null;
	
	private DirUtils(){
		initFolderName();
	}
	
	public static DirUtils getInstance(){
		if(dirUtils == null){
			dirUtils = new DirUtils();
		}
		return dirUtils;
	}
	
	/**
	 * 外部存储是否可用
	 * 
	 * @return
	 */
	public static  boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	
	private void initFolderName(){
		boolean mounted = externalMemoryAvailable();
		String publicRootName = null;
		try {
			String default_dir_temp = "";
			if(mounted){
				//TODO
//				File file = Environment.getExternalStoragePublicDirectory(DIR_BASE);
//				if(file != null){
//					default_dir_temp = file.getAbsolutePath();
//				}else{
//					default_dir_temp = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + DIR_BASE;
//				}
				default_dir_temp = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + DIR_BASE;
			}else{
				default_dir_temp = BaseApplication.getInstance().getFilesDir() + File.separator + DIR_BASE;
			}
			// sdcard路径
			publicRootName = default_dir_temp + File.separator;
		} catch (Exception e) {
			logger.e(e);
		}
		//root path
		createFolderPrivate(publicRootName);
		DEFAULT_DIR_PATH = publicRootName + DIR_MIDDLE + File.separator;
		//root path
		createFolderPrivate(DEFAULT_DIR_PATH);
		logger.e(DEFAULT_DIR_PATH);
		
		CACHE_VIDEO = DEFAULT_DIR_PATH + "video" + File.separator;
		createFolderPrivate(CACHE_VIDEO);
		CACHE_SOUND = DEFAULT_DIR_PATH + "audio" + File.separator;
		createFolderPrivate(CACHE_SOUND);
		CACHE_PHOTO = DEFAULT_DIR_PATH + "photo" + File.separator;
		createFolderPrivate(CACHE_PHOTO);
		
		DISPLAY_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + DIR_BASE_DISPLAY + File.separator;
		createFolderPrivate(DISPLAY_PATH);
		
//		TEMPLATE_ROOT = ASSETS_BASE; //从安装包加载
		
//		TEMPLATE_ROOT = DEFAULT_DIR_PATH + "assets" + File.separator;//从sdcard加载
		
		TEMPLATE_ROOT = BaseApplication.getInstance().getFilesDir() + File.separator 
				+ DIR_BASE + File.separator + DIR_MIDDLE + File.separator 
				+ "assets" + File.separator; //从内存加载
		
		if(!ASSETS_BASE.equals(TEMPLATE_ROOT)){
			createFolderPrivate(TEMPLATE_ROOT);
		}
	}
	
	public boolean getMounted(){
		return externalMemoryAvailable();
	}
	
	public void initFolder() {
		logger.e("init folder path over");
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		//intentFilter.addAction(Intent.ACTION_MEDIA_SHARED);
		intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		//intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		//intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addDataScheme("file");
		BaseApplication.getInstance().registerReceiver(broadcastRec, intentFilter);//注册监听函数
	}
	
	private boolean createFolderPrivate(String path) {
		boolean flag = false;
		try {
			//logger.i("---createFolderPrivate---path" + path);
			File file = new File(path);
			if (!file.exists()) {
				//logger.i("---create folder---start");
				file.mkdirs();
			} else {
				if (!file.isDirectory()) {
					logger.i("---delete file---start");
					file.delete();
					logger.i("---create folder---start");
					file.mkdirs();
				}
			}

			if (file.exists() && file.canRead() && file.canWrite()) {
				//logger.i("---create folder---success");
				flag = true;
			} else {
				logger.i("---create folder---fail");
				flag = false;
			}
		} catch (Exception e) {
			logger.e(e);
		}
		return flag;
	}
	
	public String getCacheVideoPath(){
		return CACHE_VIDEO;
	}
	
	public String getCacheSoundPath(){
		return CACHE_SOUND;
	}
	
	public String getCachePhotoPath(){
		return CACHE_PHOTO;
	}
	
	/**
	 * 返回工程主文件夹
	 * @return
	 */
	public String getDefaultDirPath(){
		return DEFAULT_DIR_PATH;
	}
	
	/**
	 * 获取模版地址
	 * @return
	 */
	public String getTemplatePath(){
		return TEMPLATE_ROOT;
	}
	
	public String getDisplayPath(){
		return DISPLAY_PATH;
	}
	
	public String onlineVideoPathToLocal(String online){
		String sourcePath = "";
		try {
			sourcePath = getCacheVideoPath() + online.substring(online.lastIndexOf("/"));
		} catch (Exception e) {
			logger.e(e);
		}
		return sourcePath;
	}
	
	
	private boolean flag = true;
	private final BroadcastReceiver broadcastRec = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)){//SD
				//卡已经成功挂载
				flag = true;
				sendCast(true);
				initFolderName();
				//handler.sendEmptyMessageDelayed(100, 2000);				
			}else if(intent.getAction().equals(Intent.ACTION_MEDIA_REMOVED)//各种未挂载状态
					||intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTED)
					||intent.getAction().equals(Intent.ACTION_MEDIA_BAD_REMOVAL)){
				//卡被拔出
				if(flag){
					flag = false;
					//暂停下载
					//DownloadUtil.getInstance().pauseAll();
					sendCast(false);
					initFolderName();
				}
			}
		}
	};
	
//	private Handler handler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if(msg.what == 100){
//				//应该启动线程下载
//				if(!UIUtils.getServiceIsStart(BaseApplication.getInstance(),CssDownloadService.class.getName())){
//					BaseApplication.getInstance().startService(new Intent(BaseApplication.getInstance(),CssDownloadService.class));
//				}
//				DownloadUtil.getInstance().startDownload();
//			}
//		}
//	};
	
	private void sendCast(boolean flag){
		Intent intent  = new Intent(ACTION_SDCARD_STATUS_CHANGED);
		intent.setPackage(BaseApplication.getPackName());
		intent.putExtra("status", flag);
		BaseApplication.getInstance().sendBroadcast(intent);
	}
	
	public void cleanPic(){
		try {
			FileUtils.deleteFile(getCachePhotoPath());
			FileUtils.deleteFile(BaseApplication.getInstance().getFilesDir() + File.separator + DIR_MIDDLE+ File.separator +"content_img" + File.separator);
		} catch (Exception e) {
			logger.e(e);
		}
	}
	
	public void cleanVideo(){
		try {
			FileUtils.deleteFile(getCacheVideoPath());
		} catch (Exception e) {
			logger.e(e);
		}
	}
	
	/**
	 * 协议路径和本地路径替换
	 * @param oldPath 如果路径为ci://开头则替换成本地路径sdcard/...
	 * @return 
	 */
	public static String changeFilePath(String oldPath){
		if(StringUtils.isNull(oldPath)){
			return "";
		}
		String path = oldPath;
		if(oldPath.startsWith("ci://")){
			path = path.replace("ci://", DirUtils.getInstance().getDefaultDirPath());
		}
		return path;
	}
	
}
