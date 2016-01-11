package com.rl01.lib;

import java.util.List;

import android.app.Application;
import android.graphics.drawable.Drawable;

import com.rl01.lib.utils.StringUtils;
import com.rl01.lib.utils.logger;

public class BaseApplication extends Application {

	private boolean needFinishActivity = false;

	private static BaseApplication instance = null;

	public BaseApplication() {
		BaseApplication.instance = this;
	}

	public static BaseApplication getInstance() {
		if (instance == null) {
			instance = new BaseApplication();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

//	public final void initParseInApplication(
//			Class<? extends Activity> actiClass, String key, String pwd) {
//		Parse.initialize(this, key, pwd);
//		Parse.setLogLevel(Parse.LOG_LEVEL_NONE);
//	}

	/*public final void removeParseTag() {
		ParseInstallation.getCurrentInstallation().remove("alias");
		ParseInstallation.getCurrentInstallation().remove("channels");
		ParseInstallation.getCurrentInstallation().saveInBackground(
				new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) {
							PreferUtils.getInstance().storePushInitFlag(false);
							logger.e("--------remove----------------true-------------------");
						} else {
							PreferUtils.getInstance().storePushInitFlag(true);
							logger.e("--------remove----------------false------------------");
						}
						logger.e(e);
					}
				});
	}

	public final void initParseTag() {

		ParseInstallation.getCurrentInstallation().remove("alias");
		ParseInstallation.getCurrentInstallation().remove("channels");

		String alias = PreferUtils.getInstance().getAlias();
		logger.e("alias:" + alias);
		ParseInstallation.getCurrentInstallation().put("alias", alias);
		List<String> strs = new ArrayList<String>();

		splitData(PreferUtils.getInstance().getUserInfo("channel"), strs,false);

		if (PreferUtils.getInstance().getUserType() == 2) {// TODO Teacher
			splitData(PreferUtils.getInstance().getUserInfo("subject"), strs,true);
		}

		if (strs.size() > 0) {
			logger.e("--parse-------strs-----" + strs);
			ParseInstallation.getCurrentInstallation().put("channels", strs);
		}

		ParseInstallation.getCurrentInstallation().saveInBackground(
				new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) {
							PreferUtils.getInstance().storePushInitFlag(true);
							logger.e("-----------put-------------true-------------------");
						} else {
							PreferUtils.getInstance().storePushInitFlag(false);
							logger.e("-----------put-------------false------------------");
						}
						logger.e("-----ParseException--------");
						logger.e(e);
					}
				});
	}*/

	private void splitData(String channels, List<String> strs,boolean flag) {
		if(flag){
			logger.e("category:" + channels);
			if (!StringUtils.isNull(channels)) {
				if (channels.contains(",")) {
					String[] ss = channels.split(",");
					for (String s : ss) {
						if (StringUtils.notNull(s)) {
							strs.add("c"+s);
						}
					}
				} else {
					strs.add("c"+channels);
				}
			}
		}else{
			logger.e("channels:" + channels);
			if (!StringUtils.isNull(channels)) {
				if (channels.contains(",")) {
					String[] ss = channels.split(",");
					for (String s : ss) {
						if (StringUtils.notNull(s)) {
							strs.add(s);
						}
					}
				} else {
					strs.add(channels);
				}
			}
		}
	}

	public void storeSkinRes(int skinRes) {

	}

	public int getSkinRes() {
		return -1;
	}

	public String getChannelId() {
		return "10001";
	}

	public boolean isNeedFinishActivity() {
		return needFinishActivity;
	}

	public void setNeedFinishActivity(boolean needFinishActivity) {
		this.needFinishActivity = needFinishActivity;
	}

	public final static String getPackName() {
		return getInstance().getPackageName();
	}

	public final static String getChildString(String name) {
		return getInstance().getString(getChildStringId(name));
	}

	public final static Drawable getChildDrawable(String name) {
		return getInstance().getResources().getDrawable(
				getChildDrawableId(name));
	}

	public final static int getChildStringId(String name) {
		return getInstance().getResources().getIdentifier(name, "string",
				getPackName());
	}

	public final static int getChildDrawableId(String name) {
		return getInstance().getResources().getIdentifier(name, "drawable",
				getPackName());
	}

	public final static int getChildStyleId(String name) {
		return getInstance().getResources().getIdentifier(name, "style",
				getPackName());
	}

	public final static int getChildId(String name) {
		return getInstance().getResources().getIdentifier(name, "id",
				getPackName());
	}

	public final static int getChildLayout(String name) {
		return getInstance().getResources().getIdentifier(name, "layout",
				getPackName());
	}
}
