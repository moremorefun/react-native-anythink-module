package com.reactnativeanythinkmodule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.FrameLayout;

import com.anythink.banner.api.ATBannerView;
import com.facebook.react.bridge.ReactApplicationContext;

public class AnythinkBannerFL extends FrameLayout {
  ATBannerView mBannerView;

  public AnythinkBannerFL( Context context, ReactApplicationContext mCallerContext) {
    super(context);
    mBannerView = new ATBannerView(mCallerContext.getCurrentActivity());
    mBannerView.setPlacementId("b62025d72e19ec");
    this.addView(mBannerView);
    mBannerView.loadAd();
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
//    Log.i("AnythinkBannerView", "InView OnGlobalLayoutListener width:" + getWidth() + " height: " + getHeight() );
//    Log.i("AnythinkBannerView", "InView OnGlobalLayoutListener measuredWidth:" + getMeasuredWidth() + " measuredHeight: " + getMeasuredHeight() );
  }
}
