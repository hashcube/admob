package com.tealeaf.plugin.plugins;
import com.tealeaf.TeaLeaf;
import com.tealeaf.logger;
import android.os.Bundle;

import com.tealeaf.plugin.IPlugin;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;

import com.tealeaf.EventQueue;
import com.tealeaf.event.*;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;

public class AdmobPlugin implements IPlugin {
	private InterstitialAd mAd;


	public class AdmobAdNotAvailable extends com.tealeaf.event.Event {

	public class AdmobAdAvailable extends com.tealeaf.event.Event {

		public AdmobAdAvailable() {
			super("AdmobAdAvailable");
		}
	}


	private class PluginDelegate implements AdListener {

		@Override
		public void onAdLoaded() {
			EventQueue.pushEvent(new AdmobAdAvailable());
		}
	}

	public AdmobPlugin() {
	}

	public void onCreateApplication(Context applicationContext) {
	}


	public void onCreate(Activity activity, Bundle savedInstanceState) {
		mAd = new InterstitialAd(activity);
		mAd.setAdUnitId('mycustomid');
		mAd.setAdListener(new PluginDelegate());
	}

	public void showInterstitial(String jsonData) {
		final InterstitialAd intAd = mAd;
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				if (intAd.isLoaded()) {
					intAd.show();
				}
			}
		});
	}

	public void cacheInterstitial(String jsonData) {
		 // Create ad request.
    AdRequest adRequest = new AdRequest.Builder().build();

    // Begin loading your interstitial.
    mAd.loadAd(adRequest);
	}

	public void onResume() {
	}

	public void onStart() {
	}

	public void onPause() {
	}

	public void onStop() {
	}

	public void onDestroy() {
	}

	public void onNewIntent(Intent intent) {
	}

	public void setInstallReferrer(String referrer) {
	}

	public void onActivityResult(Integer request, Integer result, Intent data) {
	}

	public boolean consumeOnBackPressed() {
		return true;
	}

	public void onBackPressed() {
	}
}
