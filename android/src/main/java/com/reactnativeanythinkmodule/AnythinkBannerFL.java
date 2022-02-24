package com.reactnativeanythinkmodule;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

public class AnythinkBannerFL extends FrameLayout {
  public AnythinkBannerFL( Context context) {
    super(context);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    Log.i("AnythinkBannerView", "InView OnGlobalLayoutListener width:" + getWidth() + " height: " + getHeight() );
    Log.i("AnythinkBannerView", "InView OnGlobalLayoutListener measuredWidth:" + getMeasuredWidth() + " measuredHeight: " + getMeasuredHeight() );
  }
}
