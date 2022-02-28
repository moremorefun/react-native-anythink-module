#import <React/RCTViewManager.h>

@interface AnythinkBannerViewManager : RCTViewManager
@end

@implementation AnythinkBannerViewManager

RCT_EXPORT_MODULE(AnythinkBannerView)

- (UIView *)view
{
  return [[UIView alloc] init];
}

@end
