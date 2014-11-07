package com.tealeaf.plugin.plugins;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.tealeaf.EventQueue;
import com.tealeaf.TeaLeaf;
import com.tealeaf.logger;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import java.util.HashMap;

import com.tealeaf.plugin.IPlugin;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.tealeaf.EventQueue;
import com.tealeaf.event.*;

import LEADBOLT_PACKAGE.AdController;
import LEADBOLT_PACKAGE.AdListener;
import com.appfireworks.android.listener.AppModuleListener;
import com.appfireworks.android.track.AppTracker;

public class LeadBoltPlugin implements IPlugin {
	Context _ctx;

	private AdController ad;
	private Activity mActivity;
	private static final  String TAG = "{LEADBOLT}";
	String interstitialId = null, fireworksAPIKey = null;
	boolean adAvailable = false;


	public class LeadboltAdNotAvailable extends com.tealeaf.event.Event {

		public LeadboltAdNotAvailable() {
			super("LeadboltAdNotAvailable");
		}
	}

	public class LeadboltAdAvailable extends com.tealeaf.event.Event {

		public LeadboltAdAvailable() {
			super("LeadboltAdAvailable");
		}
	}

	public class LeadboltAdDismissed extends com.tealeaf.event.Event {

		public LeadboltAdDismissed() {
			super("LeadboltAdDismissed");
		}
	}

	private class PluginDelegate implements AdListener {

		@Override
		public void onAdLoaded() {
		}

		@Override
		public void onAdClicked() {
			logger.log(TAG, "ad clicked");
		}

		@Override
		public void onAdClosed() {
			logger.log( TAG, "default ad close");
			EventQueue.pushEvent(new LeadboltAdDismissed());
		}

		@Override
		public void onAdFailed() {
			logger.log(TAG, "default ad not available");
			EventQueue.pushEvent(new LeadboltAdNotAvailable());
		}

		@Override
		public void onAdCached() {
			logger.log(TAG, "default ad cached");
			EventQueue.pushEvent(new LeadboltAdAvailable());
		}
	}

	public LeadBoltPlugin() {
	}

	public void onCreateApplication(Context applicationContext) {
		_ctx = applicationContext;
	}

	public void initialize () {
		if (interstitialId == null || fireworksAPIKey == null) {
			logger.log(TAG, "Keys are null please verify", "interstitialId: ", interstitialId, "\t Fireworks Key", fireworksAPIKey);
		} else {
			AppTracker.startSession(_ctx, fireworksAPIKey);
			AppTracker.setModuleListener(new AppModuleListener () {
				@Override
				public void onModuleLoaded() {
					logger.log(TAG, "leadbolt directdeal ad shown");
				}

				@Override
				public void onModuleFailed () {
					logger.log(TAG, "leadbolt directdeal not available caching default ads");
					ad = new AdController(mActivity, interstitialId, new PluginDelegate());
					adAvailable = false;
					ad.loadAdToCache();
				}

				@Override
				public void onModuleClosed () {
					logger.log( TAG, "directdeal closed");
					adAvailable = false;
					EventQueue.pushEvent(new LeadboltAdDismissed());
				}

				@Override
				public void onModuleCached() {
					logger.log(TAG, "direct deal cached");
					adAvailable = true;
					EventQueue.pushEvent(new LeadboltAdAvailable());
				}

			});
		}
	}

	public void onCreate(Activity activity, Bundle savedInstanceState) {
		mActivity = activity;
		String leadBoltPackage = "LEADBOLT_PACKAGE";
		PackageManager manager = activity.getPackageManager();
		try {
			Bundle meta = manager.getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA).metaData;
			if (meta != null) {
				interstitialId = meta.get("LEADBOLT_SECTION_ID").toString();
				fireworksAPIKey = meta.get("LEADBOLT_FIREWORKS_KEY").toString();
				if(savedInstanceState == null) {
					initialize();
				}
			}
		} catch (Exception e) {
			logger.log(TAG, "ERROR");
			e.printStackTrace();
		}
	}

	public void showInterstitial(String jsonData) {
		final AdController ad = this.ad;
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				if (adAvailable) {
					AppTracker.loadModule(mActivity, "inapp");
				} else {
					ad.loadAd();
				}
			}
		});
	}

	public void cacheInterstitial(String jsonData) {
		AppTracker.loadModuleToCache(mActivity, "inapp");
	}

	public void onResume() {
		AppTracker.resume(_ctx);
	}

	public void onStart() {
	}

	public void onPause() {
		if (!mActivity.isFinishing()) {
			AppTracker.pause(_ctx);
		}
	}

	public void onStop() {
	}

	public void onDestroy() {
		if (mActivity.isFinishing()) {
			AppTracker.closeSession(_ctx, true);
		}
		if (this.ad != null) {
			this.ad.destroyAd();
		}
	}

	public void onNewIntent(Intent intent) {
	}

	public void setInstallReferrer(String referrer) {
	}

	public void onActivityResult(Integer request, Integer result, Intent data) {
	}

	public void logError(String error) {
	}

	public boolean consumeOnBackPressed() {
		return true;
	}

	public void onBackPressed() {
	}
}
