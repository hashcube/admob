import util.setProperty as setProperty;

var onAdDismissed, onAdAvailable, onAdNotAvailable;

var admob = Class(function () {
  this.init = function(opts) {

    setProperty(this, "onAdDismissed", {
      set: function(f) {
             if (typeof f === "function") {
               onAdDismissed = f;
             } else {
               onAdDismissed = null;
             }
           },
      get: function() {
             return onOfferClose;
           }
    });

    setProperty(this, "onAdAvailable", {
      set: function(f) {
             if (typeof f === "function") {
               onAdAvailable = f;
             } else {
               onAdAvailable = null;
             }
           },
      get: function() {
             return onAdAvailable;
           }
    });

    setProperty(this, "onAdNotAvailable", {
      set: function(f) {
             if (typeof f === "function") {
               onAdNotAvailable = f;
             } else {
               onAdNotAvailable = null;
             }
           },
      get: function() {
             return onAdNotAvailable;
           }
    });

    NATIVE.events.registerHandler("AdmobAdDismissed", function() {
      logger.log("{admob} ad dismissed ");
      if (typeof onAdDismissed === "function") {
        onAdDismissed();
      }
    });

    NATIVE.events.registerHandler("AdmobAdAvailable", function() {
      logger.log("{admob} ad available");
      if (typeof onAdAvailable === "function") {
        onAdAvailable("admob");
      }
    });

    NATIVE.events.registerHandler("AdmobAdNotAvailable", function() {
      logger.log("{admob} ad not available");
      if (typeof onAdNotAvailable === "function") {
        onAdNotAvailable();
      }
    });
  }

  this.showInterstitial = function () {
    logger.log("{admob} Showing interstitial");
    NATIVE.plugins.sendEvent("AdmobPlugin", "showInterstitial", JSON.stringify({}));
  }

  this.cacheInterstitial = function () {
    logger.log("{admob} caching interstitial");
    NATIVE.plugins.sendEvent("AdmobPlugin", "cacheInterstitial", JSON.stringify({}));
  }
});

exports = new admob();
