package com.reactnativeanythinkmodule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anythink.banner.api.ATBannerListener;
import com.anythink.banner.api.ATBannerView;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.facebook.react.bridge.ReactApplicationContext;

public class AnythinkBannerFL extends FrameLayout {
  ATBannerView mBannerView;

  public AnythinkBannerFL( Context context, ReactApplicationContext mCallerContext) {
    super(context);
    mBannerView = new ATBannerView(mCallerContext.getCurrentActivity());
    mBannerView.setLayoutParams(new FrameLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT
    ));
    mBannerView.setPlacementId("b62025d72e19ec");
    this.addView(mBannerView);

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

    mBannerView.loadAd();
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
//    mBannerView.setLayoutParams(new FrameLayout.LayoutParams(
//      getWidth(),
//      getHeight()
//    ));
//    Log.i("AnythinkBannerView", "InView OnGlobalLayoutListener width:" + getWidth() + " height: " + getHeight() );
//    Log.i("AnythinkBannerView", "InView OnGlobalLayoutListener measuredWidth:" + getMeasuredWidth() + " measuredHeight: " + getMeasuredHeight() );
  }
}
