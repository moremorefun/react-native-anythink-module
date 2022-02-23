#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <AnyThinkSDK/AnyThinkSDK.h>
#import <AnyThinkRewardedVideo/AnyThinkRewardedVideo.h>
#import <AnyThinkInterstitial/AnyThinkInterstitial.h>
#import <AnyThinkSplash/AnyThinkSplash.h>
#import <AnyThinkBanner/AnyThinkBanner.h>

@interface AnythinkModule : RCTEventEmitter <RCTBridgeModule, ATAdLoadingDelegate, ATRewardedVideoDelegate, ATInterstitialDelegate, ATSplashDelegate, ATBannerDelegate>

@property(nonatomic, strong) NSMutableDictionary<NSString*, ATBannerView*>* bannerViews;

- (UIViewController *)topViewController;

- (UIViewController *)topViewController:(UIViewController *)rootViewController;

@end
