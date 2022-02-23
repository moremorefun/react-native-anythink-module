#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <AnyThinkSDK/AnyThinkSDK.h>
#import <AnyThinkRewardedVideo/AnyThinkRewardedVideo.h>
#import <AnyThinkInterstitial/AnyThinkInterstitial.h>
#import <AnyThinkSplash/AnyThinkSplash.h>

@interface AnythinkModule : RCTEventEmitter <RCTBridgeModule, ATAdLoadingDelegate, ATRewardedVideoDelegate, ATInterstitialDelegate, ATSplashDelegate>

- (UIViewController *)topViewController;

- (UIViewController *)topViewController:(UIViewController *)rootViewController;

@end
