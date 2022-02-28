#import <React/RCTViewManager.h>
#import <AnyThinkBanner/AnyThinkBanner.h>        // 引入头文件

@interface BannerWrapView : UIView <ATAdLoadingDelegate, ATBannerDelegate>

@property(nonatomic) Boolean  isFrameInit;
@property(nonatomic) Boolean  isAdded;
@property (nonatomic, copy) NSString *mPlacementID;
@property (nonatomic, copy) RCTBubblingEventBlock onEvent;

- (void) setPlacementID:(NSString *)placementID;

@end

@implementation BannerWrapView
- (void)layoutSubviews {
    NSLog(@"layoutSubviews frame %f %f %f %f", self.frame.origin.x, self.frame.origin.y, self.frame.size.width, self.frame.size.height);
    _isFrameInit = YES;
    [self setPlacementID:_mPlacementID];
}
- (void) setPlacementID:(NSString *)placementID {
    _mPlacementID = placementID;
    if (_mPlacementID != nil && _mPlacementID.length > 0 && _isFrameInit) {
        // 设置extra
          NSDictionary *extra = @{
              kATAdLoadingExtraBannerAdSizeKey : [NSValue valueWithCGSize:self.frame.size],
              kATAdLoadingExtraBannerSizeAdjustKey : @NO
          };
          // 加载banner广告
          [[ATAdManager sharedManager] loadADWithPlacementID:_mPlacementID extra:extra delegate:self];
    }
}
- (void)didFailToLoadADWithPlacementID:(NSString *)placementID error:(NSError *)error {
    if (self.onEvent) {
        self.onEvent(@{
            @"type": @"onBannerFailed",
            @"placementID": placementID,
            @"adError": error.description
        });
    }
}

- (void)didFinishLoadingADWithPlacementID:(NSString *)placementID {
    if (!_isAdded) {
        if ([placementID isEqualToString:_mPlacementID]) {
            ATBannerView *bannerView = [[ATAdManager sharedManager] retrieveBannerViewForPlacementID:placementID];
            bannerView.delegate = self;
            bannerView.frame = CGRectMake(
                    0,
                    0,
                    self.frame.size.width,
                    self.frame.size.height
            );
            [self addSubview:bannerView];
            _isAdded = YES;
        }
    } else {
        [[self subviews]
         makeObjectsPerformSelector:@selector(removeFromSuperview)];
    }
    if (self.onEvent) {
        self.onEvent(@{
            @"type": @"onBannerLoaded",
            @"placementID": placementID
        });
    }
}

- (void)bannerView:(ATBannerView *)bannerView didAutoRefreshWithPlacement:(NSString *)placementID extra:(NSDictionary *)extra {
    if (self.onEvent) {
        self.onEvent(@{
            @"type": @"onBannerAutoRefreshed",
            @"placementID": placementID,
            @"atAdInfo": extra
        });
    }
}

- (void)bannerView:(ATBannerView *)bannerView didClickWithPlacementID:(NSString *)placementID extra:(NSDictionary *)extra {
    if (self.onEvent) {
        self.onEvent(@{
            @"type": @"onBannerClicked",
            @"placementID": placementID,
            @"atAdInfo": extra
        });
    }
}

- (void)bannerView:(ATBannerView *)bannerView didDeepLinkOrJumpForPlacementID:(NSString *)placementID extra:(NSDictionary *)extra result:(BOOL)success {
    
}

- (void)bannerView:(ATBannerView *)bannerView didShowAdWithPlacementID:(NSString *)placementID extra:(NSDictionary *)extra {
    if (self.onEvent) {
        self.onEvent(@{
            @"type": @"onBannerShow",
            @"placementID": placementID,
            @"atAdInfo": extra
        });
    }
}

- (void)bannerView:(ATBannerView *)bannerView didTapCloseButtonWithPlacementID:(NSString *)placementID extra:(NSDictionary *)extra {
    if (self.onEvent) {
        self.onEvent(@{
            @"type": @"onBannerClose",
            @"placementID": placementID,
            @"atAdInfo": extra
        });
    }
}

- (void)bannerView:(ATBannerView *)bannerView failedToAutoRefreshWithPlacementID:(NSString *)placementID error:(NSError *)error {
    if (self.onEvent) {
        self.onEvent(@{
            @"type": @"onBannerFailed",
            @"placementID": placementID,
            @"adError": error.description
        });
    }
}

@end

@interface AnythinkBannerViewManager : RCTViewManager
@end

@implementation AnythinkBannerViewManager

RCT_EXPORT_MODULE(AnythinkBannerView)
RCT_EXPORT_VIEW_PROPERTY(onEvent, RCTBubblingEventBlock)

- (UIView *)view
{
  return [[BannerWrapView alloc] init];
}

RCT_CUSTOM_VIEW_PROPERTY(placementID, NSString, BannerWrapView)
{
  [view setPlacementID:json];
}

@end
