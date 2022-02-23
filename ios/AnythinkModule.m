#import "AnythinkModule.h"
#import <AnyThinkSDK/AnyThinkSDK.h>

@implementation AnythinkModule

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
                 setCustomDataForPlacementID:(nonnull NSString*)placementID settings:(nonnull NSString*)settings)
{
}

RCT_REMAP_METHOD(ATSDKSetExcludePackageList,
                 setExludeAppleIdArray:(nonnull NSArray*)ids)
{
    [[ATAPI sharedInstance] setExludeAppleIdArray:ids];
}

RCT_REMAP_METHOD(ATSDKSetFilterAdSourceIdList,
                 extraInfoForPlacementID:(NSString*)placementID requestIDs:(nonnull NSArray*)requestIDs)
{
    for (id requestID in requestIDs) {
        if ([requestID isKindOfClass:[NSString class]]) {
            [[ATAdManager sharedManager] extraInfoForPlacementID:placementID requestID:requestID];
        }
    }
}

@end
