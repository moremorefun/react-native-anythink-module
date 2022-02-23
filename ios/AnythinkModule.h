#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <AnyThinkSDK/AnyThinkSDK.h>
#import <AnyThinkRewardedVideo/AnyThinkRewardedVideo.h>
#import <AnyThinkInterstitial/AnyThinkInterstitial.h>

@interface AnythinkModule : RCTEventEmitter <RCTBridgeModule, ATAdLoadingDelegate, ATRewardedVideoDelegate, ATInterstitialDelegate>

- (UIViewController *)topViewController;

- (UIViewController *)topViewController:(UIViewController *)rootViewController;

@end
