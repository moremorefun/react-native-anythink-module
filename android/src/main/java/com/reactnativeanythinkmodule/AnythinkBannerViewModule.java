package com.reactnativeanythinkmodule;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class AnythinkBannerViewModule extends SimpleViewManager<AnythinkBannerFL> {
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
  protected AnythinkBannerFL createViewInstance(@NonNull ThemedReactContext reactContext) {
    return new AnythinkBannerFL(reactContext, mCallerContext);
  }

  @ReactProp(name = "placementID")
  public void setPlacementID(AnythinkBannerFL view, @Nullable String placementID) {
    view.setPlacementID(placementID);
  }

  @Override
  public void onDropViewInstance(@NonNull AnythinkBannerFL view) {
    super.onDropViewInstance(view);
    view.destroy();
  }

  @Nullable
  @Override
  public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
    return MapBuilder.of(
      "onEvent", MapBuilder.of("registrationName", "onEvent"));
    // onNativeClick 是原生要发送的 event 名称，onReactClick 是 JS 端组件中注册的属性方法名称，中间的 registrationName 不可更改
  }

}
