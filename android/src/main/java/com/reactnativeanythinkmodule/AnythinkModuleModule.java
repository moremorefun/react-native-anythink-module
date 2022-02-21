package com.reactnativeanythinkmodule;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

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
import java.util.Map;

@ReactModule(name = AnythinkModuleModule.NAME)
public class AnythinkModuleModule extends ReactContextBaseJavaModule {
  public static final String NAME = "AnythinkModule";
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
    }
    return null;
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
    if (packageList != null) {
      ArrayList<Object> objects = packageList.toArrayList();
      int size = objects.size();
      if (size > 0) {
        String[] packages = new String[size];
        String p;
        Object o;
        for (int i = 0; i < size; i++) {
          o = objects.get(i);
          if (o instanceof String) {
            p = (String) o;
            packages[i] = p;
          }
        }
        ATSDK.setExcludePackageList(Arrays.asList(packages));
      }
    }
  }

  @ReactMethod
  public void ATSDKSetFilterAdSourceIdList(String TopOnPlacementID, ReadableArray packageList) {
    if (packageList != null) {
      ArrayList<Object> objects = packageList.toArrayList();
      int size = objects.size();
      if (size > 0) {
        String[] packages = new String[size];
        String p;
        Object o;
        for (int i = 0; i < size; i++) {
          o = objects.get(i);
          if (o instanceof String) {
            p = (String) o;
            packages[i] = p;
          }
        }
        ATSDK.setFilterAdSourceIdList(TopOnPlacementID, Arrays.asList(packages));
      }
    }
  }

  @ReactMethod
  public void ATRewardVideoAutoAdInit(ReadableArray placementIds) {
    if (placementIds != null) {
      ArrayList<Object> objects = placementIds.toArrayList();
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
        ATRewardVideoAutoAd.init(this.getReactApplicationContext(), items, new ATRewardVideoAutoLoadListener() {
          @Override
          public void onRewardVideoAutoLoaded(String s) {
            AnythinkModuleModule.this.
              getReactApplicationContext().
              getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
              "onRewardVideoAutoLoaded",
              s
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
    }
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
    ATRewardVideoAutoAd.show(this.getCurrentActivity(), placementId, new ATRewardVideoAutoEventListener() {
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
    if (placementIds != null) {
      ArrayList<Object> objects = placementIds.toArrayList();
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
        ATRewardVideoAutoAd.addPlacementId(items);
      }
    }
  }

  @ReactMethod
  public void ATRewardVideoAutoRemovePlacementId(ReadableArray placementIds) {
    if (placementIds != null) {
      ArrayList<Object> objects = placementIds.toArrayList();
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
        ATRewardVideoAutoAd.removePlacementId(items);
      }
    }
  }

  @ReactMethod
  public void ATRewardVideoAutoSetLocalExtra(String placementId, String localExtra) {
    ATRewardVideoAutoAd.setLocalExtra(placementId, getMap(localExtra));
  }

  @ReactMethod
  public void ATInterstitialAutoAdInit(ReadableArray placementIds) {
    if (placementIds != null) {
      ArrayList<Object> objects = placementIds.toArrayList();
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
        ATInterstitialAutoAd.init(this.getReactApplicationContext(), items, new ATInterstitialAutoLoadListener() {
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
    }
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
    ATInterstitialAutoAd.show(this.getCurrentActivity(), placementId, new ATInterstitialAutoEventListener() {
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
    if (placementIds != null) {
      ArrayList<Object> objects = placementIds.toArrayList();
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
        ATInterstitialAutoAd.addPlacementId(items);
      }
    }
  }

  @ReactMethod
  public void ATInterstitialAutoAdRemovePlacementId(ReadableArray placementIds) {
    if (placementIds != null) {
      ArrayList<Object> objects = placementIds.toArrayList();
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
        ATInterstitialAutoAd.removePlacementId(items);
      }
    }
  }

  @ReactMethod
  public void ATInterstitialAutoAdSetLocalExtra(String placementId, String localExtra) {
    ATInterstitialAutoAd.setLocalExtra(placementId, getMap(localExtra));
  }

  @ReactMethod
  public void ATBannerViewInit(String placementId) {
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
      aTBannerViewMap.put(placementId, helper);
    }
  }

  @ReactMethod
  public void ATBannerViewSetLocalExtra(String placementId, String localExtra) {
    if (!aTBannerViewMap.containsKey(placementId)) {
      return;
    }
    ATBannerView ad = aTBannerViewMap.get(placementId);
    assert ad != null;
    ad.setLocalExtra( getMap(localExtra));
  }

  @ReactMethod
  public void ATBannerViewLoadAd(String placementId) {
    if (!aTBannerViewMap.containsKey(placementId)) {
      return;
    }
    ATBannerView ad = aTBannerViewMap.get(placementId);
    assert ad != null;
    ad.loadAd();
  }

  @ReactMethod
  public void ATBannerViewDestroy(String placementId) {
    if (!aTBannerViewMap.containsKey(placementId)) {
      return;
    }
    ATBannerView ad = aTBannerViewMap.get(placementId);
    assert ad != null;
    ad.destroy();
  }


  @ReactMethod
  public void ATBannerViewCheckAdStatus(String placementId, Promise promise) {
    if (!aTBannerViewMap.containsKey(placementId)) {
      return;
    }
    ATBannerView ad = aTBannerViewMap.get(placementId);
    assert ad != null;
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
    assert mBannerView != null;
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
