package com.tealeaf.plugin.plugins;
import com.tealeaf.TeaLeaf;
import com.tealeaf.logger;
import android.os.Bundle;

import android.content.pm.PackageManager;

import com.tealeaf.plugin.IPlugin;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;

import com.tealeaf.EventQueue;
import com.tealeaf.event.*;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdListener;

public class AdmobPlugin implements IPlugin {
  private InterstitialAd interstitial;
  Activity mActivity;
  private String TAG = "{admob}";
  private String adUnitID = null;
  AdRequest adRequest = null;

  public class AdmobAdAvailable extends com.tealeaf.event.Event {

    public AdmobAdAvailable() {
      super("AdmobAdAvailable");
    }
  }

  public class AdmobAdNotAvailable extends com.tealeaf.event.Event {

    public AdmobAdNotAvailable() {
      super("AdmobAdNotAvailable");
    }
  }

  public class AdmobAdDismissed extends com.tealeaf.event.Event {
    public AdmobAdDismissed () {
      super("AdmobAdDismissed");
    }
  }


  public void AdmobPlugin() {
  }

  public void onCreateApplication(Context applicationContext) {
  }


  public void onCreate(Activity activity, Bundle savedInstanceState) {
    mActivity = activity;
    PackageManager manager = activity.getPackageManager();
    try {
      Bundle meta = manager.getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA).metaData;
      if (meta != null) {
        adUnitID = meta.getString("ADMOB_AD_UNIT_ID");
      }
    } catch (Exception e) {
      logger.log("{admob} {exception}" ,e.getMessage());
    }
    logger.log(TAG ,adUnitID);
    mActivity.runOnUiThread(new Runnable() {
      public void run() {
        interstitial = new InterstitialAd(mActivity);
        interstitial.setAdUnitId(adUnitID);
        AdListener adListener = new AdListener() {
          @Override
          public void onAdLoaded() {
            logger.log(TAG, "ad loaded");
            logger.log(TAG, "Trying to show ad");
            EventQueue.pushEvent(new AdmobAdAvailable());
          }

          @Override
          public void onAdFailedToLoad(int errorCode) {
            logger.log(TAG, "no ad loaded");
            logger.log(TAG, getErrorReason(errorCode));
            EventQueue.pushEvent(new AdmobAdNotAvailable());
          }

          @Override
          public void onAdClosed() {
            logger.log(TAG, "ad killed");
            EventQueue.pushEvent(new AdmobAdDismissed());
          }

          @Override
          public void onAdLeftApplication(){
            // Ad left application
          }

          @Override
          public void onAdOpened() {
            // Ad opened
          }
        };
        interstitial.setAdListener(adListener);
      }
    });
  }

  public void showInterstitial(String jsonData) {
    final InterstitialAd final_interstitial = interstitial;
    logger.log(TAG, "Trying to show ad");
    mActivity.runOnUiThread(new Runnable() {
      public void run() {
        if (final_interstitial.isLoaded()) {
          final_interstitial.show();
        } else {
          logger.log(TAG, "ad not ready to show");
        }
      }
    });
  }

  public void cacheInterstitial(String jsonData) {
    adRequest = new AdRequest
      .Builder()
      .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
      .build();
    mActivity.runOnUiThread(new Runnable() {
      public void run() {
        interstitial.loadAd(adRequest);
      }
    });
  }

  public void onResume() {
  }

  public void onRenderResume() {
  }

  public void onStart() {
  }

  public void onFirstRun() {
  }

  public void onPause() {
  }

  public void onRenderPause() {
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

  /** Gets a string error reason from an error code. */
  private String getErrorReason(int errorCode) {
    String errorReason = "";
    switch(errorCode) {
      case AdRequest.ERROR_CODE_INTERNAL_ERROR:
        errorReason = "Internal error";
        break;
      case AdRequest.ERROR_CODE_INVALID_REQUEST:
        errorReason = "Invalid request";
        break;
      case AdRequest.ERROR_CODE_NETWORK_ERROR:
        errorReason = "Network Error";
        break;
      case AdRequest.ERROR_CODE_NO_FILL:
        errorReason = "No fill";
        break;
    }
    return errorReason;
  }
}
