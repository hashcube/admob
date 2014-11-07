import util.setProperty as setProperty;

var onAdDismissed, onAdAvailable, onAdNotAvailable;

var LeadBolt = Class(function () {
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

		NATIVE.events.registerHandler("LeadboltAdDismissed", function() {
			logger.log("{leadbolt} ad dismissed ");
			if (typeof onAdDismissed === "function") {
				onAdDismissed();
			}
		});

		NATIVE.events.registerHandler("LeadboltAdAvailable", function() {
			logger.log("{leadbolt} ad available");
			if (typeof onAdAvailable === "function") {
				onAdAvailable("leadbolt");
			}
		});

		NATIVE.events.registerHandler("LeadboltAdNotAvailable", function() {
			logger.log("{leadbolt} ad not available");
			if (typeof onAdNotAvailable === "function") {
				onAdNotAvailable();
			}
		});

	}

	this.showInterstitial = function () {
		logger.log("{leadBolt} Showing interstitial");

		NATIVE.plugins.sendEvent("LeadBoltPlugin", "showInterstitial", JSON.stringify({}));
	}

	this.cacheInterstitial = function () {
		logger.log("{leadBolt} caching interstitial");

		NATIVE.plugins.sendEvent("LeadBoltPlugin", "cacheInterstitial", JSON.stringify({}));
	}
});

exports = new LeadBolt();
