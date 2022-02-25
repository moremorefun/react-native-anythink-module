package com.reactnativeanythinkmodule;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.anythink.banner.api.ATBannerListener;
import com.anythink.banner.api.ATBannerView;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTModernEventEmitter;

public class AnythinkBannerFL extends FrameLayout {
  ReactApplicationContext mCallerContext;
  ThemedReactContext mContext;
  ATBannerView mBannerView;

  public AnythinkBannerFL( ThemedReactContext context, ReactApplicationContext mCallerContext) {
    super(context);
    this.mContext = context;
    this.mCallerContext = mCallerContext;
  }

  public void setPlacementID(@Nullable String placementID) {
    this.removeAllViews();
    if (mBannerView != null) {
      mBannerView.destroy();
      mBannerView = null;
    }
    mBannerView = new BannerViewWrapper(mCallerContext.getCurrentActivity());
    mBannerView.setLayoutParams(new FrameLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT
    ));
    mBannerView.setBannerAdListener(new ATBannerListener() {
      @Override
      public void onBannerLoaded() {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerLoaded");
        WritableMap event = Arguments.createMap();
        event.putString("type", "onBannerLoaded");
        mContext.getJSModule(RCTModernEventEmitter.class).receiveEvent(
          mContext.getSurfaceId(),
          AnythinkBannerFL.this.getId(),
          "onEvent",
          event);
      }

      @Override
      public void onBannerFailed(AdError adError) {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerFailed:" + adError);
        WritableMap event = Arguments.createMap();
        event.putString("type", "onBannerFailed");
        event.putString("adError", adError.toString());
        mContext.getJSModule(RCTModernEventEmitter.class).receiveEvent(
          mContext.getSurfaceId(),
          AnythinkBannerFL.this.getId(),
          "onEvent",
          event);
      }

      @Override
      public void onBannerClicked(ATAdInfo atAdInfo) {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerClicked:" + atAdInfo);
        WritableMap event = Arguments.createMap();
        event.putString("type", "onBannerClicked");
        event.putString("atAdInfo", atAdInfo.toString());
        mContext.getJSModule(RCTModernEventEmitter.class).receiveEvent(
          mContext.getSurfaceId(),
          AnythinkBannerFL.this.getId(),
          "onEvent",
          event);
      }

      @Override
      public void onBannerShow(ATAdInfo atAdInfo) {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerShow:" + atAdInfo);
        WritableMap event = Arguments.createMap();
        event.putString("type", "onBannerShow");
        event.putString("atAdInfo", atAdInfo.toString());
        mContext.getJSModule(RCTModernEventEmitter.class).receiveEvent(
          mContext.getSurfaceId(),
          AnythinkBannerFL.this.getId(),
          "onEvent",
          event);
      }

      @Override
      public void onBannerClose(ATAdInfo atAdInfo) {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerClose:" + atAdInfo);
        WritableMap event = Arguments.createMap();
        event.putString("type", "onBannerClose");
        event.putString("atAdInfo", atAdInfo.toString());
        mContext.getJSModule(RCTModernEventEmitter.class).receiveEvent(
          mContext.getSurfaceId(),
          AnythinkBannerFL.this.getId(),
          "onEvent",
          event);
      }

      @Override
      public void onBannerAutoRefreshed(ATAdInfo atAdInfo) {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerAutoRefreshed:" + atAdInfo);
        WritableMap event = Arguments.createMap();
        event.putString("type", "onBannerAutoRefreshed");
        event.putString("atAdInfo", atAdInfo.toString());
        mContext.getJSModule(RCTModernEventEmitter.class).receiveEvent(
          mContext.getSurfaceId(),
          AnythinkBannerFL.this.getId(),
          "onEvent",
          event);
      }

      @Override
      public void onBannerAutoRefreshFail(AdError adError) {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerAutoRefreshFail:" + adError);
        WritableMap event = Arguments.createMap();
        event.putString("type", "onBannerAutoRefreshFail");
        event.putString("adError", adError.toString());
        mContext.getJSModule(RCTModernEventEmitter.class).receiveEvent(
          mContext.getSurfaceId(),
          AnythinkBannerFL.this.getId(),
          "onEvent",
          event);
      }
    });
    this.addView(mBannerView);

    mBannerView.setPlacementId(placementID);
    mBannerView.loadAd();
  }

  public void destroy(){
    this.removeAllViews();
    if (mBannerView != null) {
      mBannerView.destroy();
      mBannerView = null;
    }
  }
}
