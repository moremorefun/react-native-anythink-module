package com.reactnativeanythinkmodule;

import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

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

}
