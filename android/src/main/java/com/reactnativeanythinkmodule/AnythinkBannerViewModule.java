package com.reactnativeanythinkmodule;

import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

public class AnythinkBannerViewModule extends SimpleViewManager<FrameLayout> {
  public static final String REACT_CLASS = "AnythinkBannerView";
  ReactApplicationContext mCallerContext;

  public AnythinkBannerViewModule(ReactApplicationContext reactContext) {
    mCallerContext = reactContext;
  }

  @NonNull
  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @NonNull
  @Override
  protected FrameLayout createViewInstance(@NonNull ThemedReactContext reactContext) {
    return new FrameLayout(reactContext);
  }

}
