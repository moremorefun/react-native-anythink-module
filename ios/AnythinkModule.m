#import "AnythinkModule.h"
#import <AnyThinkSDK/AnyThinkSDK.h>
#import <CoreData/CoreData.h>

@implementation AnythinkModule

- (UIViewController *)topViewController{
    return [self topViewController:[UIApplication sharedApplication].keyWindow.rootViewController];
}

- (UIViewController *)topViewController:(UIViewController *)rootViewController
{
    if (rootViewController.presentedViewController == nil) {
        return rootViewController;
    }
    
    if ([rootViewController.presentedViewController isKindOfClass:[UINavigationController class]]) {
        UINavigationController *navigationController = (UINavigationController *)rootViewController.presentedViewController;
        UIViewController *lastViewController = [[navigationController viewControllers] lastObject];
        return [self topViewController:lastViewController];
    }
    
    UIViewController *presentedViewController = (UIViewController *)rootViewController.presentedViewController;
    return [self topViewController:presentedViewController];
}

RCT_EXPORT_MODULE()

// Example method
// See // https://reactnative.dev/docs/native-modules-ios
//RCT_REMAP_METHOD(multiply,
//                 multiplyWithA:(nonnull NSNumber*)a withB:(nonnull NSNumber*)b
//                 withResolver:(RCTPromiseResolveBlock)resolve
//                 withRejecter:(RCTPromiseRejectBlock)reject)
//{
//  NSNumber *result = @([a floatValue] * [b floatValue]);
//
//  resolve(result);
//}

- (NSArray<NSString *> *)supportedEvents
{
    return @[
        @"onRewardVideoAutoLoaded",
        @"onRewardVideoAutoLoadFail",
        @"onRewardedVideoAdPlayStart",
        @"onRewardedVideoAdPlayEnd",
        @"onRewardedVideoAdPlayFailed",
        @"onRewardedVideoAdClosed",
        @"onRewardedVideoAdPlayClicked",
        @"onReward",
        
        @"onInterstitialAutoLoaded",
        @"onInterstitialAutoLoadFail",
        @"onInterstitialAdClicked",
        @"onInterstitialAdShow",
        @"onInterstitialAdClose",
        @"onInterstitialAdVideoStart",
        @"onInterstitialAdVideoEnd",
        @"onInterstitialAdVideoError",
        
        @"onSplashAdLoaded",
        @"onSplashAdLoadTimeout",
        @"onSplashNoAdError",
        @"onSplashAdShow",
        @"onSplashAdClick",
        @"onSplashAdDismiss",
        
        @"onBannerLoaded",
        @"onBannerFailed",
        @"onBannerClicked",
        @"onBannerShow",
        @"onBannerClose",
        @"onBannerAutoRefreshed",
        @"onBannerAutoRefreshFail"
    ];
}

RCT_REMAP_METHOD(ATSDKInit,
                 startWithAppID:(NSString*)appID appKey:(NSString*)appKey)
{
    [[ATAPI sharedInstance] startWithAppID:appID appKey:appKey error:nil];
}

RCT_REMAP_METHOD(ATSDKSetNetworkLogDebug,
                 setLogEnabled:(BOOL)logEnabled)
{
    [ATAPI setLogEnabled:logEnabled];
}

RCT_REMAP_METHOD(ATSDKGetSDKVersionName,
                 getVersion:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
    NSString *ver = [ATAPI sharedInstance].version;
    resolve(ver);
}

RCT_REMAP_METHOD(ATSDKIntegrationChecking,
                 integrationChecking)
{
    [ATAPI integrationChecking];
}

RCT_REMAP_METHOD(ATSDKIsCnSDK,
                 isCnSDK:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
    resolve(@YES);
}

RCT_REMAP_METHOD(ATSDKTestModeDeviceInfo,
                 testModeDeviceInfo:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
    resolve(@"");
}

RCT_REMAP_METHOD(ATSDKSetChannel,
                 setChannel:(nonnull NSString*)channel)
{
    [ATAPI sharedInstance].channel = channel;
}

RCT_REMAP_METHOD(ATSDKSetSubChannel,
                 setSubChannel:(nonnull NSString*)subChannel)
{
    [ATAPI sharedInstance].subchannel = subChannel;
}

RCT_REMAP_METHOD(ATSDKInitCustomMap,
                 setCustomData:(nonnull NSDictionary*)settings)
{
    [[ATAPI sharedInstance] setCustomData: settings];
}

RCT_REMAP_METHOD(ATSDKInitPlacementCustomMap,
                 setCustomDataForPlacementID:(nonnull NSString*)placementID settings:(nonnull NSDictionary*)settings)
{
    [[ATAPI sharedInstance] setCustomData:settings forPlacementID:placementID];
}

RCT_REMAP_METHOD(ATSDKSetExcludePackageList,
                 setExludeAppleIdArray:(nonnull NSArray*)ids)
{
    [[ATAPI sharedInstance] setExludeAppleIdArray:ids];
}

RCT_REMAP_METHOD(ATSDKSetFilterAdSourceIdList,
                 extraInfoForPlacementID:(NSString*)placementID requestIDs:(nonnull NSArray*)requestIDs)
{
    for (id requestID in requestIDs) {\
        if ([requestID isKindOfClass:[NSString class]]) {
            [[ATAdManager sharedManager] extraInfoForPlacementID:placementID requestID:requestID];
        }
    }
}

RCT_REMAP_METHOD(ATRewardVideoAutoAdInit,
                 aTRewardedVideoAutoAdManagerInit:(NSArray*)placementIDs)
{
    [ATRewardedVideoAutoAdManager sharedInstance].delegate = self;
    [[ATRewardedVideoAutoAdManager sharedInstance] addAutoLoadAdPlacementIDArray:placementIDs];
}

RCT_REMAP_METHOD(ATRewardVideoAutoAdIsAdReady,
                 autoLoadRewardedVideoReadyForPlacementID:(NSString *)placementID
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
    BOOL isReady = [[ATRewardedVideoAutoAdManager sharedInstance] autoLoadRewardedVideoReadyForPlacementID:placementID];
    NSNumber *boolNumber = [NSNumber numberWithBool:isReady];
    resolve(boolNumber);
}

RCT_REMAP_METHOD(ATRewardVideoAutoAdCheckAdStatus,
                 checkRewardedVideoLoadStatusForPlacementID:(NSString *)placementID
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
    ATCheckLoadModel *info = [[ATRewardedVideoAutoAdManager sharedInstance] checkRewardedVideoLoadStatusForPlacementID:placementID];
    resolve(@{
        @"isLoading": [NSNumber numberWithBool:info.isLoading],
        @"isReady": [NSNumber numberWithBool:info.isReady],
        @"adInfo": info.adOfferInfo
    });
}

RCT_REMAP_METHOD(ATRewardVideoAutoAdShow,
                 showAutoLoadRewardedVideoWithPlacementID:(NSString*)placementID)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [[ATRewardedVideoAutoAdManager sharedInstance]
         showAutoLoadRewardedVideoWithPlacementID:placementID
         inViewController:[self topViewController]
         delegate:self
        ];
    });
}

// MARK:- ATAdLoadingDelegate
- (void)didFailToLoadADWithPlacementID:(NSString *)placementID error:(NSError *)error { 
    [self sendEventWithName: @"onRewardVideoAutoLoadFail" body: @{@"placementId": placementID, @"adError": error}];
}

- (void)didFinishLoadingADWithPlacementID:(NSString *)placementID { 
    [self sendEventWithName: @"onRewardVideoAutoLoaded" body: placementID];
}


- (void)didFinishLoadingSplashADWithPlacementID:(NSString *)placementID isTimeout:(BOOL)isTimeout { 
    
}

- (void)didTimeoutLoadingSplashADWithPlacementID:(NSString *)placementID { 
    
}

// MARK:- ATRewardedVideoDelegate
- (void)rewardedVideoAgainDidClickForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra { 
    
}

- (void)rewardedVideoAgainDidEndPlayingForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra { 
    
}

- (void)rewardedVideoAgainDidFailToPlayForPlacementID:(NSString *)placementID error:(NSError *)error extra:(NSDictionary *)extra { 
    
}

- (void)rewardedVideoAgainDidRewardSuccessForPlacemenID:(NSString *)placementID extra:(NSDictionary *)extra { 
    
}

- (void)rewardedVideoAgainDidStartPlayingForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra { 
    
}

- (void)rewardedVideoDidClickForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra { 
    
}

- (void)rewardedVideoDidCloseForPlacementID:(NSString *)placementID rewarded:(BOOL)rewarded extra:(NSDictionary *)extra { 
    
}

- (void)rewardedVideoDidDeepLinkOrJumpForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra result:(BOOL)success { 
    
}

- (void)rewardedVideoDidEndPlayingForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra { 
    
}

- (void)rewardedVideoDidFailToPlayForPlacementID:(NSString *)placementID error:(NSError *)error extra:(NSDictionary *)extra { 
    
}

- (void)rewardedVideoDidRewardSuccessForPlacemenID:(NSString *)placementID extra:(NSDictionary *)extra { 
    
}

- (void)rewardedVideoDidStartPlayingForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra { 
    
}

@end
