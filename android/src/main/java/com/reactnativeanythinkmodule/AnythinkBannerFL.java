package com.reactnativeanythinkmodule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.anythink.banner.api.ATBannerListener;
import com.anythink.banner.api.ATBannerView;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.facebook.react.bridge.ReactApplicationContext;

public class AnythinkBannerFL extends FrameLayout {
  ReactApplicationContext mCallerContext;
  ATBannerView mBannerView;

  public AnythinkBannerFL( Context context, ReactApplicationContext mCallerContext) {
    super(context);
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
      }

      @Override
      public void onBannerFailed(AdError adError) {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerFailed:" + adError);
      }

      @Override
      public void onBannerClicked(ATAdInfo atAdInfo) {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerClicked:" + atAdInfo);
      }

      @Override
      public void onBannerShow(ATAdInfo atAdInfo) {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerShow:" + atAdInfo);
      }

      @Override
      public void onBannerClose(ATAdInfo atAdInfo) {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerClose:" + atAdInfo);
      }

      @Override
      public void onBannerAutoRefreshed(ATAdInfo atAdInfo) {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerAutoRefreshed:" + atAdInfo);
      }

      @Override
      public void onBannerAutoRefreshFail(AdError adError) {
        Log.i("AnythinkBannerView", "AnythinkBannerFL onBannerAutoRefreshFail:" + adError);
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
