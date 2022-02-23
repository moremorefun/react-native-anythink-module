#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <AnyThinkRewardedVideo/AnyThinkRewardedVideo.h>        // 引入头文件

@interface AnythinkModule :  RCTEventEmitter <RCTBridgeModule, ATAdLoadingDelegate, ATRewardedVideoDelegate>

- (UIViewController *)topViewController;
- (UIViewController *)topViewController:(UIViewController *)rootViewController;

@end
