package com.reactnativeanythinkmodule;

import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

public class AnythinkViewModule extends SimpleViewManager<FrameLayout> {
  public static final String REACT_CLASS = "AnythinkView";
  ReactApplicationContext mCallerContext;

  public AnythinkViewModule(ReactApplicationContext reactContext) {
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

  @ReactProp(name = "src")
  public void setSrc(FrameLayout view, @Nullable ReadableArray sources) {
  }
}
