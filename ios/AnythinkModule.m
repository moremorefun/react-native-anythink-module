#import "AnythinkModule.h"
#import <CoreData/CoreData.h>

@implementation AnythinkModule

- (UIViewController *)topViewController {
    return [self topViewController:[UIApplication sharedApplication].keyWindow.rootViewController];
}

- (UIViewController *)topViewController:(UIViewController *)rootViewController {
    if (rootViewController.presentedViewController == nil) {
        return rootViewController;
    }

    if ([rootViewController.presentedViewController isKindOfClass:[UINavigationController class]]) {
        UINavigationController *navigationController = (UINavigationController *) rootViewController.presentedViewController;
        UIViewController *lastViewController = [[navigationController viewControllers] lastObject];
        return [self topViewController:lastViewController];
    }

    UIViewController *presentedViewController = (UIViewController *) rootViewController.presentedViewController;
    return [self topViewController:presentedViewController];
}

RCT_EXPORT_MODULE()

- (NSArray<NSString *> *)supportedEvents {
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
            startWithAppID:
            (NSString *) appID appKey:
    (NSString *) appKey)
{
    [[ATAPI sharedInstance] startWithAppID:appID appKey:appKey error:nil];
}

RCT_REMAP_METHOD(ATSDKSetNetworkLogDebug,
            setLogEnabled:
            (BOOL) logEnabled)
{
    [ATAPI setLogEnabled:logEnabled];
}

RCT_REMAP_METHOD(ATSDKGetSDKVersionName,
            getVersion:
            (RCTPromiseResolveBlock) resolve
            withRejecter:
            (RCTPromiseRejectBlock) reject)
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
            isCnSDK:
            (RCTPromiseResolveBlock) resolve
            withRejecter:
            (RCTPromiseRejectBlock) reject)
{
    resolve(@YES);
}

RCT_REMAP_METHOD(ATSDKTestModeDeviceInfo,
            testModeDeviceInfo:
            (RCTPromiseResolveBlock) resolve
            withRejecter:
            (RCTPromiseRejectBlock) reject)
{
    resolve(@"");
}

RCT_REMAP_METHOD(ATSDKSetChannel,
            setChannel:
            (nonnull NSString*)channel)
{
    [ATAPI sharedInstance].channel = channel;
}

RCT_REMAP_METHOD(ATSDKSetSubChannel,
            setSubChannel:
            (nonnull NSString*)subChannel)
{
    [ATAPI sharedInstance].subchannel = subChannel;
}

RCT_REMAP_METHOD(ATSDKInitCustomMap,
            setCustomData:
            (nonnull NSDictionary*)settings)
{
    [[ATAPI sharedInstance] setCustomData:settings];
}

RCT_REMAP_METHOD(ATSDKInitPlacementCustomMap,
            setCustomDataForPlacementID:
            (nonnull NSString*)placementID settings:(nonnull NSDictionary*)settings)
{
    [[ATAPI sharedInstance] setCustomData:settings forPlacementID:placementID];
}

RCT_REMAP_METHOD(ATSDKSetExcludePackageList,
            setExludeAppleIdArray:
            (nonnull NSArray*)ids)
{
    [[ATAPI sharedInstance] setExludeAppleIdArray:ids];
}

RCT_REMAP_METHOD(ATSDKSetFilterAdSourceIdList,
            extraInfoForPlacementID:
            (NSString *) placementID requestIDs:
    (nonnull NSArray*)requestIDs)
{
    for (id requestID in requestIDs) {
        \
        if ([requestID isKindOfClass:[NSString class]]) {
            [[ATAdManager sharedManager] extraInfoForPlacementID:placementID requestID:requestID];
        }
    }
}

RCT_REMAP_METHOD(ATRewardVideoAutoAdInit,
            aTRewardedVideoAutoAdManagerInit:
            (NSArray *) placementIDs)
{
    [ATRewardedVideoAutoAdManager sharedInstance].delegate = self;
    [[ATRewardedVideoAutoAdManager sharedInstance] addAutoLoadAdPlacementIDArray:placementIDs];
}

RCT_REMAP_METHOD(ATRewardVideoAutoAdIsAdReady,
            autoLoadRewardedVideoReadyForPlacementID:
            (NSString *) placementID
            withResolver:
            (RCTPromiseResolveBlock) resolve
            withRejecter:
            (RCTPromiseRejectBlock) reject)
{
    BOOL isReady = [[ATRewardedVideoAutoAdManager sharedInstance] autoLoadRewardedVideoReadyForPlacementID:placementID];
    NSNumber *boolNumber = [NSNumber numberWithBool:isReady];
    resolve(boolNumber);
}

RCT_REMAP_METHOD(ATRewardVideoAutoAdCheckAdStatus,
            checkRewardedVideoLoadStatusForPlacementID:
            (NSString *) placementID
            withResolver:
            (RCTPromiseResolveBlock) resolve
            withRejecter:
            (RCTPromiseRejectBlock) reject)
{
    ATCheckLoadModel *info = [[ATRewardedVideoAutoAdManager sharedInstance] checkRewardedVideoLoadStatusForPlacementID:placementID];
    resolve(@{
            @"isLoading": [NSNumber numberWithBool:info.isLoading],
            @"isReady": [NSNumber numberWithBool:info.isReady],
            @"adInfo": info.adOfferInfo
    });
}

RCT_REMAP_METHOD(ATRewardVideoAutoAdShow,
            showAutoLoadRewardedVideoWithPlacementID:
            (NSString *) placementID)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [[ATRewardedVideoAutoAdManager sharedInstance]
                showAutoLoadRewardedVideoWithPlacementID:placementID
                                        inViewController:[self topViewController]
                                                delegate:self
        ];
    });
}

RCT_REMAP_METHOD(ATRewardVideoAutoAddPlacementId,
            addAutoLoadAdPlacementIDArray:
            (NSArray *) placementIDArray)
{
    [[ATRewardedVideoAutoAdManager sharedInstance] addAutoLoadAdPlacementIDArray:placementIDArray];
}

RCT_REMAP_METHOD(ATRewardVideoAutoRemovePlacementId,
            removeAutoLoadAdPlacementIDArray:
            (NSArray *) placementIDArray)
{
    [[ATRewardedVideoAutoAdManager sharedInstance] removeAutoLoadAdPlacementIDArray:placementIDArray];
}

RCT_REMAP_METHOD(ATRewardVideoAutoSetLocalExtra,
            rewardVideoAutoSetLocalExtra:
            (NSString *) placementID
            withExtra:
            (NSDictionary *) extra)
{
    [[ATRewardedVideoAutoAdManager sharedInstance]
            setLocalExtra:extra
              placementID:placementID
    ];
}

RCT_REMAP_METHOD(ATInterstitialAutoAdInit,
                 aTInterstitialAutoAdManagerInit:
            (NSArray *) placementIDs)
{
    [ATInterstitialAutoAdManager sharedInstance].delegate = self;
    [[ATInterstitialAutoAdManager sharedInstance] addAutoLoadAdPlacementIDArray:placementIDs];
}

RCT_REMAP_METHOD(ATInterstitialAutoAdIsAdReady,
                 autoLoadInterstitialReadyForPlacementID:
            (NSString *) placementID
            withResolver:
            (RCTPromiseResolveBlock) resolve
            withRejecter:
            (RCTPromiseRejectBlock) reject)
{
    BOOL isReady = [[ATInterstitialAutoAdManager sharedInstance] autoLoadInterstitialReadyForPlacementID:placementID];
    NSNumber *boolNumber = [NSNumber numberWithBool:isReady];
    resolve(boolNumber);
}

RCT_REMAP_METHOD(ATInterstitialAutoAdCheckAdStatus,
                 checkInterstitialLoadStatusForPlacementID:
            (NSString *) placementID
            withResolver:
            (RCTPromiseResolveBlock) resolve
            withRejecter:
            (RCTPromiseRejectBlock) reject)
{
    ATCheckLoadModel *info = [[ATInterstitialAutoAdManager sharedInstance] checkInterstitialLoadStatusForPlacementID:placementID];
    resolve(@{
            @"isLoading": [NSNumber numberWithBool:info.isLoading],
            @"isReady": [NSNumber numberWithBool:info.isReady],
            @"adInfo": info.adOfferInfo
    });
}

RCT_REMAP_METHOD(ATInterstitialAutoAdShow,
                 showAutoLoadInterstitialWithPlacementID:
            (NSString *) placementID)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [[ATInterstitialAutoAdManager sharedInstance]
         showAutoLoadInterstitialWithPlacementID:placementID
                                        inViewController:[self topViewController]
                                                delegate:self
        ];
    });
}

RCT_REMAP_METHOD(ATInterstitialAutoAdAddPlacementId,
            addInterstitialAutoLoadAdPlacementIDArray:
            (NSArray *) placementIDArray)
{
    [[ATInterstitialAutoAdManager sharedInstance] addAutoLoadAdPlacementIDArray:placementIDArray];
}

RCT_REMAP_METHOD(ATInterstitialAutoAdRemovePlacementId,
            removeInterstitialAutoLoadAdPlacementIDArray:
            (NSArray *) placementIDArray)
{
    [[ATInterstitialAutoAdManager sharedInstance] removeAutoLoadAdPlacementIDArray:placementIDArray];
}

RCT_REMAP_METHOD(ATInterstitialAutoAdSetLocalExtra,
                 interstitialSetLocalExtra:
            (NSString *) placementID
            withExtra:
            (NSDictionary *) extra)
{
    [[ATInterstitialAutoAdManager sharedInstance]
            setLocalExtra:extra
              placementID:placementID
    ];
}

// MARK:- ATAdLoadingDelegate
- (void)didFailToLoadADWithPlacementID:(NSString *)placementID error:(NSError *)error {
    [self sendEventWithName:@"onRewardVideoAutoLoadFail"
                       body:@{
                               @"placementId": placementID,
                               @"adError": error
                       }];
}

- (void)didFinishLoadingADWithPlacementID:(NSString *)placementID {
    [self sendEventWithName:@"onRewardVideoAutoLoaded" body:placementID];
}


- (void)didFinishLoadingSplashADWithPlacementID:(NSString *)placementID isTimeout:(BOOL)isTimeout {

}

- (void)didTimeoutLoadingSplashADWithPlacementID:(NSString *)placementID {

}

// MARK:- ATRewardedVideoDelegate
- (void)rewardedVideoAgainDidClickForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra {
    [self sendEventWithName:@"onRewardedVideoAdPlayClicked"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)rewardedVideoAgainDidEndPlayingForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra {
    [self sendEventWithName:@"onRewardedVideoAdPlayEnd"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)rewardedVideoAgainDidFailToPlayForPlacementID:(NSString *)placementID error:(NSError *)error extra:(NSDictionary *)extra {
    [self sendEventWithName:@"onRewardedVideoAdPlayFailed"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra,
                               @"adError": error
                       }];
}

- (void)rewardedVideoAgainDidRewardSuccessForPlacemenID:(NSString *)placementID extra:(NSDictionary *)extra {
    [self sendEventWithName:@"onReward"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)rewardedVideoAgainDidStartPlayingForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra {
    [self sendEventWithName:@"onRewardedVideoAdPlayStart"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)rewardedVideoDidClickForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra {
    [self sendEventWithName:@"onRewardedVideoAdPlayClicked"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)rewardedVideoDidCloseForPlacementID:(NSString *)placementID rewarded:(BOOL)rewarded extra:(NSDictionary *)extra {
    [self sendEventWithName:@"onRewardedVideoAdClosed"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)rewardedVideoDidDeepLinkOrJumpForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra result:(BOOL)success {

}

- (void)rewardedVideoDidEndPlayingForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra {
    [self sendEventWithName:@"onRewardedVideoAdPlayEnd"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)rewardedVideoDidFailToPlayForPlacementID:(NSString *)placementID error:(NSError *)error extra:(NSDictionary *)extra {
    [self sendEventWithName:@"onRewardedVideoAdPlayFailed"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra,
                               @"adError": error
                       }];
}

- (void)rewardedVideoDidRewardSuccessForPlacemenID:(NSString *)placementID extra:(NSDictionary *)extra {
    [self sendEventWithName:@"onReward"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)rewardedVideoDidStartPlayingForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra {
    [self sendEventWithName:@"onRewardedVideoAdPlayStart"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

// MARK:- ATInterstitialDelegate
- (void)interstitialDeepLinkOrJumpForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra result:(BOOL)success { 
    
}

- (void)interstitialDidClickForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra { 
    [self sendEventWithName:@"onInterstitialAdClicked"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)interstitialDidCloseForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra { 
    [self sendEventWithName:@"onInterstitialAdClose"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)interstitialDidEndPlayingVideoForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra { 
    [self sendEventWithName:@"onInterstitialAdVideoEnd"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)interstitialDidFailToPlayVideoForPlacementID:(NSString *)placementID error:(NSError *)error extra:(NSDictionary *)extra { 
    [self sendEventWithName:@"onInterstitialAdVideoError"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra,
                               @"adError": error
                       }];
}

- (void)interstitialDidShowForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra { 
    [self sendEventWithName:@"onInterstitialAdShow"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)interstitialDidStartPlayingVideoForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra { 
    [self sendEventWithName:@"onInterstitialAdVideoStart"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra
                       }];
}

- (void)interstitialFailedToShowForPlacementID:(NSString *)placementID error:(NSError *)error extra:(NSDictionary *)extra { 
    [self sendEventWithName:@"onInterstitialAdVideoError"
                       body:@{
                               @"placementId": placementID,
                               @"atAdInfo": extra,
                               @"adError": error
                       }];
}

@end
