package com.reactnativeanythinkmodule;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.anythink.banner.api.ATBannerListener;
import com.anythink.banner.api.ATBannerView;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.ATAdStatusInfo;
import com.anythink.core.api.ATSDK;
import com.anythink.core.api.AdError;
import com.anythink.core.api.DeviceInfoCallback;
import com.anythink.interstitial.api.ATInterstitialAutoAd;
import com.anythink.interstitial.api.ATInterstitialAutoEventListener;
import com.anythink.interstitial.api.ATInterstitialAutoLoadListener;
import com.anythink.rewardvideo.api.ATRewardVideoAutoAd;
import com.anythink.rewardvideo.api.ATRewardVideoAutoEventListener;
import com.anythink.rewardvideo.api.ATRewardVideoAutoLoadListener;
import com.anythink.splashad.api.ATSplashAd;
import com.anythink.splashad.api.ATSplashAdListener;
import com.anythink.splashad.api.IATSplashEyeAd;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ReactModule(name = AnythinkModuleModule.NAME)
public class AnythinkModuleModule extends ReactContextBaseJavaModule {
  public static final String NAME = "AnythinkModule";

  private static final HashMap<String, ATSplashAd> aTSplashAdMap = new HashMap<>();
  private static final HashMap<String, Dialog> aTSplashAdDialogMap = new HashMap<>();

  private static final HashMap<String, ATBannerView> aTBannerViewMap = new HashMap<>();

  public AnythinkModuleModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  public static Map<String, Object> getMap(String jsonString) {
    JSONObject jsonObject;
    try {
      jsonObject = new JSONObject(jsonString);
      @SuppressWarnings("unchecked")
      Iterator<String> keyIter = jsonObject.keys();
      String key;
      Object value;
      Map<String, Object> valueMap = new HashMap<String, Object>();
      while (keyIter.hasNext()) {
        key = (String) keyIter.next();
        value = jsonObject.get(key);
        valueMap.put(key, value);
      }
      return valueMap;
    } catch (JSONException e) {
      e.printStackTrace();
      Log.e(NAME, "json string to map err: " + e.toString() + " str: " + jsonString);
    }
    return null;
  }

  public static String[] convertToStrings(ReadableArray arr) {
    ArrayList<Object> objects = arr.toArrayList();
    int size = objects.size();
    if (size > 0) {
      String[] items = new String[size];
      String p;
      Object o;
      for (int i = 0; i < size; i++) {
        o = objects.get(i);
        if (o instanceof String) {
          p = (String) o;
          items[i] = p;
        }
      }
      return items;
    }
    return new String[size];
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void ATSDKInit(String TopOnAppID, String TopOnAppKey) {
    ATSDK.init(this.getReactApplicationContext(), TopOnAppID, TopOnAppKey);
  }

  @ReactMethod
  public void ATSDKSetNetworkLogDebug(Boolean debug) {
    ATSDK.setNetworkLogDebug(debug);
  }

  @ReactMethod
  public void ATSDKGetSDKVersionName(Promise promise) {
    String vName = ATSDK.getSDKVersionName();
    promise.resolve(vName);
  }

  @ReactMethod
  public void ATSDKIntegrationChecking() {
    ATSDK.integrationChecking(this.getReactApplicationContext());
  }

  @ReactMethod
  public void ATSDKIsCnSDK(Promise promise) {
    boolean b = ATSDK.isCnSDK();
    promise.resolve(b);
  }

  @ReactMethod
  public void ATSDKTestModeDeviceInfo(Promise promise) {
    ATSDK.testModeDeviceInfo(this.getReactApplicationContext(), new DeviceInfoCallback() {
      @Override
      public void deviceInfo(String s) {
        promise.resolve(s);
      }
    });
  }

  @ReactMethod
  public void ATSDKSetChannel(String channel) {
    ATSDK.setChannel(channel);
  }

  @ReactMethod
  public void ATSDKSetSubChannel(String subChannel) {
    ATSDK.setSubChannel(subChannel);
  }

  @ReactMethod
  public void ATSDKInitCustomMap(String customMap) {
    ATSDK.initCustomMap(getMap(customMap));
  }

  @ReactMethod
  public void ATSDKInitPlacementCustomMap(String TopOnPlacementID, String customMap) {
    ATSDK.initPlacementCustomMap(TopOnPlacementID, getMap(customMap));
  }

  @ReactMethod
  public void ATSDKSetExcludePackageList(ReadableArray packageList) {
    List<String> strs = Arrays.asList(convertToStrings(packageList));
    if (strs.size() > 0) {
      ATSDK.setExcludePackageList(strs);
    } else {
      Log.e(NAME, "ATSDKSetExcludePackageList list err: " + packageList.toString());
    }
  }

  @ReactMethod
  public void ATSDKSetFilterAdSourceIdList(String TopOnPlacementID, ReadableArray packageList) {
    List<String> strs = Arrays.asList(convertToStrings(packageList));
    if (strs.size() > 0) {
      ATSDK.setFilterAdSourceIdList(TopOnPlacementID, strs);
    } else {
      Log.e(NAME, "ATSDKSetExcludePackageList list err: " + packageList.toString());
    }
  }

  @ReactMethod
  public void ATRewardVideoAutoAdInit(ReadableArray placementIds) {
    String[] strs = convertToStrings(placementIds);
    if (strs.length == 0) {
      Log.e(NAME, "ATRewardVideoAutoAdInit list err: " + placementIds.toString());
      return;
    }
    ATRewardVideoAutoAd.init(this.getReactApplicationContext(), strs, new ATRewardVideoAutoLoadListener() {
      @Override
      public void onRewardVideoAutoLoaded(String placementId) {
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onRewardVideoAutoLoaded",
          placementId
        );
      }

      @Override
      public void onRewardVideoAutoLoadFail(String s, AdError adError) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", s);
        writableMap.putString("adError", adError.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onRewardVideoAutoLoadFail",
          writableMap
        );
      }
    });
  }

  @ReactMethod
  public void ATRewardVideoAutoAdIsAdReady(String placementId, Promise promise) {
    boolean ready = ATRewardVideoAutoAd.isAdReady(placementId);
    promise.resolve(ready);
  }

  @ReactMethod
  public void ATRewardVideoAutoAdCheckAdStatus(String placementId, Promise promise) {
    ATAdStatusInfo info = ATRewardVideoAutoAd.checkAdStatus(placementId);
    WritableMap writableMap = Arguments.createMap();
    writableMap.putBoolean("isLoading", info.isLoading());
    writableMap.putBoolean("isReady", info.isReady());
    writableMap.putString("adInfo", info.getATTopAdInfo().toString());
    promise.resolve(writableMap);
  }

  @ReactMethod
  public void ATRewardVideoAutoAdShow(String placementId) {
    Activity a = this.getCurrentActivity();
    if (a == null) {
      Log.e(NAME, "ATRewardVideoAutoAdShow placementId:" + placementId + " getCurrentActivity=null");
      return;
    }

    ATRewardVideoAutoAd.show(a, placementId, new ATRewardVideoAutoEventListener() {
      @Override
      public void onRewardedVideoAdPlayStart(ATAdInfo atAdInfo) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", placementId);
        writableMap.putString("atAdInfo", atAdInfo.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onRewardedVideoAdPlayStart",
          writableMap
        );
      }

      @Override
      public void onRewardedVideoAdPlayEnd(ATAdInfo atAdInfo) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", placementId);
        writableMap.putString("atAdInfo", atAdInfo.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onRewardedVideoAdPlayEnd",
          writableMap
        );
      }

      @Override
      public void onRewardedVideoAdPlayFailed(AdError adError, ATAdInfo atAdInfo) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", placementId);
        writableMap.putString("adError", adError.toString());
        writableMap.putString("atAdInfo", atAdInfo.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onRewardedVideoAdPlayFailed",
          writableMap
        );
      }

      @Override
      public void onRewardedVideoAdClosed(ATAdInfo atAdInfo) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", placementId);
        writableMap.putString("atAdInfo", atAdInfo.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onRewardedVideoAdClosed",
          writableMap
        );
      }

      @Override
      public void onRewardedVideoAdPlayClicked(ATAdInfo atAdInfo) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", placementId);
        writableMap.putString("atAdInfo", atAdInfo.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onRewardedVideoAdPlayClicked",
          writableMap
        );
      }

      @Override
      public void onReward(ATAdInfo atAdInfo) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", placementId);
        writableMap.putString("atAdInfo", atAdInfo.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onReward",
          writableMap
        );
      }
    });
  }

  @ReactMethod
  public void ATRewardVideoAutoAddPlacementId(ReadableArray placementIds) {
    String[] ids = convertToStrings(placementIds);
    if (ids.length == 0) {
      Log.e(NAME, "ATRewardVideoAutoAddPlacementId id size 0: " + placementIds.toString());
      return;
    }
    ATRewardVideoAutoAd.addPlacementId(ids);
  }

  @ReactMethod
  public void ATRewardVideoAutoRemovePlacementId(ReadableArray placementIds) {
    String[] ids = convertToStrings(placementIds);
    if (ids.length == 0) {
      Log.e(NAME, "ATRewardVideoAutoRemovePlacementId id size 0: " + placementIds.toString());
      return;
    }
    ATRewardVideoAutoAd.removePlacementId(ids);
  }

  @ReactMethod
  public void ATRewardVideoAutoSetLocalExtra(String placementId, String localExtra) {
    ATRewardVideoAutoAd.setLocalExtra(placementId, getMap(localExtra));
  }

  @ReactMethod
  public void ATInterstitialAutoAdInit(ReadableArray placementIds) {
    String[] ids = convertToStrings(placementIds);
    if (ids.length == 0) {
      Log.e(NAME, "ATInterstitialAutoAdInit id size 0: " + placementIds.toString());
      return;
    }
    ATInterstitialAutoAd.init(this.getReactApplicationContext(), ids, new ATInterstitialAutoLoadListener() {
      @Override
      public void onInterstitialAutoLoaded(String s) {
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onInterstitialAutoLoaded",
          s
        );
      }

      @Override
      public void onInterstitialAutoLoadFail(String s, AdError adError) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", s);
        writableMap.putString("adError", adError.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onInterstitialAutoLoadFail",
          writableMap
        );
      }
    });
  }

  @ReactMethod
  public void ATInterstitialAutoAdIsAdReady(String placementId, Promise promise) {
    boolean ready = ATInterstitialAutoAd.isAdReady(placementId);
    promise.resolve(ready);
  }

  @ReactMethod
  public void ATInterstitialAutoAdCheckAdStatus(String placementId, Promise promise) {
    ATAdStatusInfo info = ATInterstitialAutoAd.checkAdStatus(placementId);
    WritableMap writableMap = Arguments.createMap();
    writableMap.putBoolean("isLoading", info.isLoading());
    writableMap.putBoolean("isReady", info.isReady());
    writableMap.putString("adInfo", info.getATTopAdInfo().toString());
    promise.resolve(writableMap);
  }

  @ReactMethod
  public void ATInterstitialAutoAdShow(String placementId) {
    Activity a = this.getCurrentActivity();
    if (a == null) {
      Log.e(NAME, "ATInterstitialAutoAdShow getCurrentActivity==null");
      return;
    }
    ATInterstitialAutoAd.show(a, placementId, new ATInterstitialAutoEventListener() {
      @Override
      public void onInterstitialAdClicked(ATAdInfo atAdInfo) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", placementId);
        writableMap.putString("atAdInfo", atAdInfo.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onInterstitialAdClicked",
          writableMap
        );
      }

      @Override
      public void onInterstitialAdShow(ATAdInfo atAdInfo) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", placementId);
        writableMap.putString("atAdInfo", atAdInfo.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onInterstitialAdShow",
          writableMap
        );
      }

      @Override
      public void onInterstitialAdClose(ATAdInfo atAdInfo) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", placementId);
        writableMap.putString("atAdInfo", atAdInfo.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onInterstitialAdClose",
          writableMap
        );
      }

      @Override
      public void onInterstitialAdVideoStart(ATAdInfo atAdInfo) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", placementId);
        writableMap.putString("atAdInfo", atAdInfo.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onInterstitialAdVideoStart",
          writableMap
        );
      }

      @Override
      public void onInterstitialAdVideoEnd(ATAdInfo atAdInfo) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", placementId);
        writableMap.putString("atAdInfo", atAdInfo.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onInterstitialAdVideoEnd",
          writableMap
        );
      }

      @Override
      public void onInterstitialAdVideoError(AdError adError) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("placementId", placementId);
        writableMap.putString("adError", adError.toString());
        AnythinkModuleModule.this.
          getReactApplicationContext().
          getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
          "onInterstitialAdVideoError",
          writableMap
        );
      }
    });
  }

  @ReactMethod
  public void ATInterstitialAutoAdAddPlacementId(ReadableArray placementIds) {
    String[] ids = convertToStrings(placementIds);
    if (ids.length == 0) {
      Log.e(NAME, "ATInterstitialAutoAdAddPlacementId id size 0: " + placementIds.toString());
      return;
    }
    ATInterstitialAutoAd.addPlacementId(ids);
  }

  @ReactMethod
  public void ATInterstitialAutoAdRemovePlacementId(ReadableArray placementIds) {
    String[] ids = convertToStrings(placementIds);
    if (ids.length == 0) {
      Log.e(NAME, "ATInterstitialAutoAdAddPlacementId id size 0: " + placementIds.toString());
      return;
    }
    ATInterstitialAutoAd.removePlacementId(ids);
  }

  @ReactMethod
  public void ATInterstitialAutoAdSetLocalExtra(String placementId, String localExtra) {
    ATInterstitialAutoAd.setLocalExtra(placementId, getMap(localExtra));
  }

  @ReactMethod
  public void ATSplashAdInit(String placementId, int fetchAdTimeout, String defaultAdSourceConfig) {
    ATSplashAd helper;

    if (!aTSplashAdMap.containsKey(placementId)) {
      helper = new ATSplashAd(
        AnythinkModuleModule.this.getReactApplicationContext(),
        placementId,
        new ATSplashAdListener() {
          @Override
          public void onAdLoaded(boolean isTimeout) {
            WritableMap writableMap = Arguments.createMap();
            writableMap.putString("placementId", placementId);
            writableMap.putBoolean("isTimeout", isTimeout);
            AnythinkModuleModule.this.
              getReactApplicationContext().
              getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
              "onAdLoaded",
              writableMap
            );
          }

          @Override
          public void onAdLoadTimeout() {
            WritableMap writableMap = Arguments.createMap();
            writableMap.putString("placementId", placementId);
            AnythinkModuleModule.this.
              getReactApplicationContext().
              getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
              "onAdLoadTimeout",
              writableMap
            );
          }

          @Override
          public void onNoAdError(AdError adError) {
            WritableMap writableMap = Arguments.createMap();
            writableMap.putString("placementId", placementId);
            writableMap.putString("adError", adError.toString());
            AnythinkModuleModule.this.
              getReactApplicationContext().
              getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
              "onNoAdError",
              writableMap
            );
          }

          @Override
          public void onAdShow(ATAdInfo atAdInfo) {
            WritableMap writableMap = Arguments.createMap();
            writableMap.putString("placementId", placementId);
            writableMap.putString("atAdInfo", atAdInfo.toString());
            AnythinkModuleModule.this.
              getReactApplicationContext().
              getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
              "onAdShow",
              writableMap
            );
          }

          @Override
          public void onAdClick(ATAdInfo atAdInfo) {
            WritableMap writableMap = Arguments.createMap();
            writableMap.putString("placementId", placementId);
            writableMap.putString("atAdInfo", atAdInfo.toString());
            AnythinkModuleModule.this.
              getReactApplicationContext().
              getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
              "onAdClick",
              writableMap
            );
          }

          @Override
          public void onAdDismiss(ATAdInfo atAdInfo, IATSplashEyeAd iatSplashEyeAd) {
            WritableMap writableMap = Arguments.createMap();
            writableMap.putString("placementId", placementId);
            writableMap.putString("atAdInfo", atAdInfo.toString());
            writableMap.putString("iatSplashEyeAd", iatSplashEyeAd.toString());
            AnythinkModuleModule.this.
              getReactApplicationContext().
              getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
              "onAdDismiss",
              writableMap
            );
          }
        },
        fetchAdTimeout,
        defaultAdSourceConfig
      );
      aTSplashAdMap.put(placementId, helper);
    }
  }

  @ReactMethod
  public void ATSplashAdSetLocalExtra(String placementId, String localExtra) {
    if (!aTSplashAdMap.containsKey(placementId)) {
      return;
    }
    ATSplashAd ad = aTSplashAdMap.get(placementId);
    if (ad == null) {
      Log.e(NAME, "ATSplashAdSetLocalExtra placementId: " + placementId + " ad==null");
      return;
    }
    ad.setLocalExtra(getMap(localExtra));
  }

  @ReactMethod
  public void ATSplashAdIsAdReady(String placementId, Promise promise) {
    if (!aTSplashAdMap.containsKey(placementId)) {
      return;
    }
    ATSplashAd ad = aTSplashAdMap.get(placementId);
    if (ad == null) {
      Log.e(NAME, "ATSplashAdIsAdReady placementId: " + placementId + " ad==null");
      return;
    }
    boolean b = ad.isAdReady();
    promise.resolve(b);
  }

  @ReactMethod
  public void ATSplashAdCheckAdStatus(String placementId, Promise promise) {
    if (!aTSplashAdMap.containsKey(placementId)) {
      return;
    }
    ATSplashAd ad = aTSplashAdMap.get(placementId);
    if (ad == null) {
      Log.e(NAME, "ATSplashAdCheckAdStatus placementId: " + placementId + " ad==null");
      return;
    }
    ATAdStatusInfo info = ad.checkAdStatus();
    WritableMap writableMap = Arguments.createMap();
    writableMap.putBoolean("isLoading", info.isLoading());
    writableMap.putBoolean("isReady", info.isReady());
    writableMap.putString("adInfo", info.getATTopAdInfo().toString());
    promise.resolve(writableMap);
  }

  @ReactMethod
  public void ATSplashAdLoadAd(String placementId) {
    if (!aTSplashAdMap.containsKey(placementId)) {
      return;
    }
    ATSplashAd ad = aTSplashAdMap.get(placementId);
    if (ad == null) {
      Log.e(NAME, "ATSplashAdLoadAd placementId: " + placementId + " ad==null");
      return;
    }
    ad.loadAd();
  }

  @ReactMethod
  public void ATSplashAdShow(String placementId) {
    if (!aTSplashAdMap.containsKey(placementId)) {
      return;
    }
    ATSplashAd ad = aTSplashAdMap.get(placementId);
    if (ad == null) {
      Log.e(NAME, "ATSplashAdShow placementId: " + placementId + " ad==null");
      return;
    }
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Activity a = AnythinkModuleModule.this.getCurrentActivity();
        if (a == null) {
          Log.e(NAME, "ATSplashAdShow placementId: " + placementId + " getCurrentActivity==null");
          return;
        }
        if (a.isFinishing()) {
          Log.e(NAME, "ATSplashAdShow placementId: " + placementId + " isFinishing");
          return;
        }
        Dialog dialog;
        if (!aTSplashAdDialogMap.containsKey(placementId)) {
          dialog = new Dialog(a, R.style.SplashScreen_SplashTheme);
          dialog.setContentView(R.layout.launch_screen);
          dialog.setCancelable(false);
          aTSplashAdDialogMap.put(placementId, dialog);
        } else {
          dialog = aTSplashAdDialogMap.get(placementId);
        }
        if (dialog == null) {
          Log.e(NAME, "ATSplashAdShow placementId: " + placementId + " dialog==null");
          return;
        }
        if (!dialog.isShowing()) {
          dialog.show();
        }
        FrameLayout adContainer = dialog.findViewById(R.id.splash_ad_container);
        ad.show(AnythinkModuleModule.this.getCurrentActivity(), adContainer);
      }
    });
  }

  @ReactMethod
  public void ATSplashAdHide(String placementId) {
    if (!aTSplashAdMap.containsKey(placementId)) {
      return;
    }
    ATSplashAd ad = aTSplashAdMap.get(placementId);
    if (ad == null) {
      Log.e(NAME, "ATSplashAdHide placementId: " + placementId + " ad==null");
      return;
    }
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Dialog dialog;
        if (!aTSplashAdDialogMap.containsKey(placementId)) {
          Log.e(NAME, "ATSplashAdHide placementId: " + placementId + " isFinishing");
          return;
        }
        dialog = aTSplashAdDialogMap.get(placementId);
        if (dialog == null) {
          Log.e(NAME, "ATSplashAdHide placementId: " + placementId + " dialog==null");
          return;
        }
        if (dialog.isShowing()) {
          boolean isDestroyed = false;
          Activity a = dialog.getOwnerActivity();
          if (a == null) {
            Log.e(NAME, "ATSplashAdHide placementId: " + placementId + " getOwnerActivity==null");
            return;
          }
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isDestroyed = a.isDestroyed();
          }
          if (!a.isFinishing() && !isDestroyed) {
            dialog.dismiss();
          }
          aTSplashAdDialogMap.remove(placementId);
        }
      }
    });
  }

  @ReactMethod
  public void ATSplashAdCheckSplashDefaultConfigList(String placementId) {
    if (!aTSplashAdMap.containsKey(placementId)) {
      return;
    }
    ATSplashAd ad = aTSplashAdMap.get(placementId);
    if (ad == null) {
      Log.e(NAME, "ATSplashAdCheckSplashDefaultConfigList placementId: " + placementId + " ad==null");
      return;
    }
    ATSplashAd.checkSplashDefaultConfigList(this.getReactApplicationContext(), placementId, null);
  }

  @ReactMethod
  public void ATBannerViewInit(String placementId, String settings) {
    ATBannerView helper;

    if (!aTBannerViewMap.containsKey(placementId)) {
      helper = new ATBannerView(AnythinkModuleModule.this.getReactApplicationContext());
      helper.setPlacementId(placementId);
      helper.setBannerAdListener(new ATBannerListener() {
        @Override
        public void onBannerLoaded() {
          AnythinkModuleModule.this.
            getReactApplicationContext().
            getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
            "onBannerLoaded",
            "{}"
          );
        }

        @Override
        public void onBannerFailed(AdError adError) {
          WritableMap writableMap = Arguments.createMap();
          writableMap.putString("placementId", placementId);
          writableMap.putString("adError", adError.toString());
          AnythinkModuleModule.this.
            getReactApplicationContext().
            getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
            "onBannerFailed",
            writableMap
          );
        }

        @Override
        public void onBannerClicked(ATAdInfo atAdInfo) {
          WritableMap writableMap = Arguments.createMap();
          writableMap.putString("placementId", placementId);
          writableMap.putString("atAdInfo", atAdInfo.toString());
          AnythinkModuleModule.this.
            getReactApplicationContext().
            getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
            "onBannerClicked",
            writableMap
          );
        }

        @Override
        public void onBannerShow(ATAdInfo atAdInfo) {
          WritableMap writableMap = Arguments.createMap();
          writableMap.putString("placementId", placementId);
          writableMap.putString("atAdInfo", atAdInfo.toString());
          AnythinkModuleModule.this.
            getReactApplicationContext().
            getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
            "onBannerShow",
            writableMap
          );
        }

        @Override
        public void onBannerClose(ATAdInfo atAdInfo) {
          WritableMap writableMap = Arguments.createMap();
          writableMap.putString("placementId", placementId);
          writableMap.putString("atAdInfo", atAdInfo.toString());
          AnythinkModuleModule.this.
            getReactApplicationContext().
            getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
            "onBannerClose",
            writableMap
          );
        }

        @Override
        public void onBannerAutoRefreshed(ATAdInfo atAdInfo) {
          WritableMap writableMap = Arguments.createMap();
          writableMap.putString("placementId", placementId);
          writableMap.putString("atAdInfo", atAdInfo.toString());
          AnythinkModuleModule.this.
            getReactApplicationContext().
            getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
            "onBannerAutoRefreshed",
            writableMap
          );
        }

        @Override
        public void onBannerAutoRefreshFail(AdError adError) {
          WritableMap writableMap = Arguments.createMap();
          writableMap.putString("placementId", placementId);
          writableMap.putString("adError", adError.toString());
          AnythinkModuleModule.this.
            getReactApplicationContext().
            getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
            "onBannerAutoRefreshFail",
            writableMap
          );
        }
      });

      try {
        JSONObject jsonObject = new JSONObject(settings);
        int width = 0;
        int height = 0;

        if (jsonObject.has(Const.WIDTH)) {
          width = jsonObject.optInt(Const.WIDTH);
        }
        if (jsonObject.has(Const.HEIGHT)) {
          height = jsonObject.optInt(Const.HEIGHT);
        }
        if (helper.getLayoutParams() == null) {
          FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
          helper.setLayoutParams(lp);
        } else {
          helper.getLayoutParams().width = width;
          helper.getLayoutParams().height = height;
        }
      } catch (Throwable e) {
        e.printStackTrace();
        Log.e(NAME, "ATBannerViewInit err: " + e.toString());
      }
      aTBannerViewMap.put(placementId, helper);
    }
  }

  @ReactMethod
  public void ATBannerViewSetLocalExtra(String placementId, String localExtra) {
    if (!aTBannerViewMap.containsKey(placementId)) {
      return;
    }
    ATBannerView ad = aTBannerViewMap.get(placementId);
    if (ad == null) {
      Log.e(NAME, "ATBannerViewSetLocalExtra placementId: " + placementId + " ad==null");
      return;
    }
    ad.setLocalExtra(getMap(localExtra));
  }

  @ReactMethod
  public void ATBannerViewLoadAd(String placementId) {
    if (!aTBannerViewMap.containsKey(placementId)) {
      return;
    }
    ATBannerView ad = aTBannerViewMap.get(placementId);
    if (ad == null) {
      Log.e(NAME, "ATBannerViewLoadAd placementId: " + placementId + " ad==null");
      return;
    }
    ad.loadAd();
  }

  @ReactMethod
  public void ATBannerViewDestroy(String placementId) {
    if (!aTBannerViewMap.containsKey(placementId)) {
      return;
    }
    ATBannerView ad = aTBannerViewMap.get(placementId);
    if (ad == null) {
      Log.e(NAME, "ATBannerViewLoadAd placementId: " + placementId + " ad==null");
      return;
    }
    if (ad.getParent() != null) {
      ((ViewGroup) ad.getParent()).removeView(ad);
    }
    ad.destroy();
  }

  @ReactMethod
  public void ATBannerViewCheckAdStatus(String placementId, Promise promise) {
    if (!aTBannerViewMap.containsKey(placementId)) {
      return;
    }
    ATBannerView ad = aTBannerViewMap.get(placementId);
    if (ad == null) {
      Log.e(NAME, "ATBannerViewCheckAdStatus placementId: " + placementId + " ad==null");
      return;
    }
    ATAdStatusInfo info = ad.checkAdStatus();
    WritableMap writableMap = Arguments.createMap();
    writableMap.putBoolean("isLoading", info.isLoading());
    writableMap.putBoolean("isReady", info.isReady());
    writableMap.putString("adInfo", info.getATTopAdInfo().toString());
    promise.resolve(writableMap);
  }

  @ReactMethod
  public void ATBannerViewShow(String placementId, String position) {
    if (!aTBannerViewMap.containsKey(placementId)) {
      return;
    }
    ATBannerView mBannerView = aTBannerViewMap.get(placementId);
    if (mBannerView == null) {
      Log.e(NAME, "ATBannerViewShow placementId: " + placementId + " ad==null");
      return;
    }
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        int width = 0;
        int height = 0;
        if (mBannerView.getLayoutParams() != null) {
          width = mBannerView.getLayoutParams().width;
          height = mBannerView.getLayoutParams().height;
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        if ("top".equals(position)) {
          layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        } else {
          layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        }
        if (mBannerView.getParent() != null) {
          ((ViewGroup) mBannerView.getParent()).removeView(mBannerView);
        }
        AnythinkModuleModule.this.getCurrentActivity().addContentView(mBannerView, layoutParams);
      }
    });
  }

  @ReactMethod
  public void ATBannerViewReShow(String placementId) {
    if (!aTBannerViewMap.containsKey(placementId)) {
      return;
    }
    ATBannerView mBannerView = aTBannerViewMap.get(placementId);
    assert mBannerView != null;
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        mBannerView.setVisibility(View.VISIBLE);
      }
    });
  }

  @ReactMethod
  public void ATBannerViewHide(String placementId) {
    if (!aTBannerViewMap.containsKey(placementId)) {
      return;
    }
    ATBannerView mBannerView = aTBannerViewMap.get(placementId);
    assert mBannerView != null;
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        mBannerView.setVisibility(View.GONE);
      }
    });
  }
}
