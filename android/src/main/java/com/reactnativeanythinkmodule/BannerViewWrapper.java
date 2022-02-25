package com.reactnativeanythinkmodule;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.anythink.banner.api.ATBannerView;

public class BannerViewWrapper extends ATBannerView {
  public BannerViewWrapper(Context context) {
    super(context);
  }

  @Override
  public void addView(View child, ViewGroup.LayoutParams params) {
    super.addView(child, params);

    int w = View.MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
    int h = View.MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), View.MeasureSpec.EXACTLY);
    this.measure(w, h);
    this.layout(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
  }
}
