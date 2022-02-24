import {
  NativeEventEmitter,
  NativeModules,
  Platform,
  requireNativeComponent,
  UIManager,
  ViewStyle,
} from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-anythink-module' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const AnythinkModule = NativeModules.AnythinkModule
  ? NativeModules.AnythinkModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

const listenersMap: Map<string, any> = new Map<string, any>();

const AnythinkModuleBridge = {
  setListeners: function (cb: (event: string, data: any) => void) {
    if (!NativeModules.AnythinkModule) {
      return;
    }
    const AnythinkModuleEventEmitter = new NativeEventEmitter(
      NativeModules.AnythinkModule
    );
    function add(type: string) {
      if (listenersMap.has(type)) {
        const listener = listenersMap.get(type);
        listener.remove();
        listenersMap.delete(type);
      }
      listenersMap.set(
        type,
        AnythinkModuleEventEmitter.addListener(type, (args: any) => {
          cb(type, args);
        })
      );
    }
    add('onAdLoaded');
    add('onAdLoadFail');

    add('onRewardVideoAutoLoaded');
    add('onRewardVideoAutoLoadFail');
    add('onRewardedVideoAdPlayStart');
    add('onRewardedVideoAdPlayEnd');
    add('onRewardedVideoAdPlayFailed');
    add('onRewardedVideoAdClosed');
    add('onRewardedVideoAdPlayClicked');
    add('onReward');

    add('onInterstitialAutoLoaded');
    add('onInterstitialAutoLoadFail');
    add('onInterstitialAdClicked');
    add('onInterstitialAdShow');
    add('onInterstitialAdClose');
    add('onInterstitialAdVideoStart');
    add('onInterstitialAdVideoEnd');
    add('onInterstitialAdVideoError');

    add('onSplashAdLoaded');
    add('onSplashAdLoadTimeout');
    add('onSplashNoAdError');
    add('onSplashAdShow');
    add('onSplashAdClick');
    add('onSplashAdDismiss');

    add('onBannerLoaded');
    add('onBannerFailed');
    add('onBannerClicked');
    add('onBannerShow');
    add('onBannerClose');
    add('onBannerAutoRefreshed');
    add('onBannerAutoRefreshFail');
  },

  ATSDKInit: function (TopOnAppID: string, TopOnAppKey: string) {
    AnythinkModule.ATSDKInit(TopOnAppID, TopOnAppKey);
  },
  ATSDKSetNetworkLogDebug: function (debug: boolean) {
    AnythinkModule.ATSDKSetNetworkLogDebug(debug);
  },
  ATSDKGetSDKVersionName: function (): Promise<string> {
    return AnythinkModule.ATSDKGetSDKVersionName();
  },
  ATSDKIntegrationChecking: function () {
    return AnythinkModule.ATSDKIntegrationChecking();
  },
  ATSDKIsCnSDK: function (): Promise<boolean> {
    return AnythinkModule.ATSDKIsCnSDK();
  },
  ATSDKTestModeDeviceInfo: function (): Promise<string> {
    return AnythinkModule.ATSDKTestModeDeviceInfo();
  },
  ATSDKSetChannel: function (channel: string) {
    AnythinkModule.ATSDKSetChannel(channel);
  },
  ATSDKSetSubChannel: function (subChannel: string) {
    AnythinkModule.ATSDKSetSubChannel(subChannel);
  },
  ATSDKInitCustomMap: function (customMap: any) {
    AnythinkModule.ATSDKInitCustomMap(customMap);
  },
  ATSDKInitPlacementCustomMap: function (
    TopOnPlacementID: string,
    customMap: any
  ) {
    AnythinkModule.ATSDKInitPlacementCustomMap(TopOnPlacementID, customMap);
  },
  ATSDKSetExcludePackageList: function (packageList: Array<string>) {
    AnythinkModule.ATSDKSetExcludePackageList(packageList);
  },
  ATSDKSetFilterAdSourceIdList: function (
    TopOnPlacementID: string,
    packageList: Array<string>
  ) {
    AnythinkModule.ATSDKSetFilterAdSourceIdList(TopOnPlacementID, packageList);
  },

  ATRewardVideoAutoAdInit: function (placementIds: Array<string>) {
    AnythinkModule.ATRewardVideoAutoAdInit(placementIds);
  },
  ATRewardVideoAutoAdIsAdReady: function (
    placementId: string
  ): Promise<boolean> {
    return AnythinkModule.ATRewardVideoAutoAdIsAdReady(placementId);
  },
  ATRewardVideoAutoAdCheckAdStatus: function (
    placementId: string
  ): Promise<Map<string, any>> {
    return AnythinkModule.ATRewardVideoAutoAdCheckAdStatus(placementId);
  },
  ATRewardVideoAutoAdShow: function (placementId: string) {
    AnythinkModule.ATRewardVideoAutoAdShow(placementId);
  },
  ATRewardVideoAutoAddPlacementId: function (packageList: Array<string>) {
    AnythinkModule.ATRewardVideoAutoAddPlacementId(packageList);
  },
  ATRewardVideoAutoRemovePlacementId: function (packageList: Array<string>) {
    AnythinkModule.ATRewardVideoAutoRemovePlacementId(packageList);
  },
  ATRewardVideoAutoSetLocalExtra: function (
    TopOnPlacementID: string,
    customMap: any
  ) {
    AnythinkModule.ATRewardVideoAutoSetLocalExtra(TopOnPlacementID, customMap);
  },

  ATInterstitialAutoAdInit: function (placementIds: Array<string>) {
    AnythinkModule.ATInterstitialAutoAdInit(placementIds);
  },
  ATInterstitialAutoAdIsAdReady: function (
    placementId: string
  ): Promise<boolean> {
    return AnythinkModule.ATInterstitialAutoAdIsAdReady(placementId);
  },
  ATInterstitialAutoAdCheckAdStatus: function (
    placementId: string
  ): Promise<Map<string, any>> {
    return AnythinkModule.ATInterstitialAutoAdCheckAdStatus(placementId);
  },
  ATInterstitialAutoAdShow: function (placementId: string) {
    AnythinkModule.ATInterstitialAutoAdShow(placementId);
  },
  ATInterstitialAutoAdAddPlacementId: function (packageList: Array<string>) {
    AnythinkModule.ATInterstitialAutoAdAddPlacementId(packageList);
  },
  ATInterstitialAutoAdRemovePlacementId: function (packageList: Array<string>) {
    AnythinkModule.ATInterstitialAutoAdRemovePlacementId(packageList);
  },
  ATInterstitialAutoAdSetLocalExtra: function (
    TopOnPlacementID: string,
    customMap: any
  ) {
    AnythinkModule.ATInterstitialAutoAdSetLocalExtra(
      TopOnPlacementID,
      customMap
    );
  },

  ATSplashAdInitAndLoad: function (
    placementId: string,
    fetchAdTimeout: number,
    defaultAdSourceConfig: string
  ) {
    AnythinkModule.ATSplashAdInitAndLoad(
      placementId,
      fetchAdTimeout,
      defaultAdSourceConfig
    );
  },
  ATSplashAdSetLocalExtra: function (TopOnPlacementID: string, customMap: any) {
    AnythinkModule.ATSplashAdSetLocalExtra(TopOnPlacementID, customMap);
  },
  ATSplashAdIsAdReady: function (placementId: string): Promise<boolean> {
    return AnythinkModule.ATSplashAdIsAdReady(placementId);
  },
  ATSplashAdCheckAdStatus: function (
    placementId: string
  ): Promise<Map<string, any>> {
    return AnythinkModule.ATSplashAdCheckAdStatus(placementId);
  },
  ATSplashAdLoadAd: function (placementId: string) {
    AnythinkModule.ATSplashAdLoadAd(placementId);
  },
  ATSplashAdShow: function (placementId: string) {
    AnythinkModule.ATSplashAdShow(placementId);
  },
  ATSplashAdHide: function (placementId: string) {
    AnythinkModule.ATSplashAdHide(placementId);
  },
  ATSplashAdCheckSplashDefaultConfigList: function (placementId: string) {
    AnythinkModule.ATSplashAdCheckSplashDefaultConfigList(placementId);
  },

  ATBannerViewInitAndLoad: function (placementId: string, settings: any) {
    AnythinkModule.ATBannerViewInitAndLoad(placementId, settings);
  },
  ATBannerViewSetLocalExtra: function (
    TopOnPlacementID: string,
    customMap: any
  ) {
    AnythinkModule.ATBannerViewSetLocalExtra(TopOnPlacementID, customMap);
  },
  ATBannerViewLoadAd: function (placementId: string) {
    AnythinkModule.ATBannerViewLoadAd(placementId);
  },
  ATBannerViewDestroy: function (placementId: string) {
    AnythinkModule.ATBannerViewDestroy(placementId);
  },
  ATBannerViewCheckAdStatus: function (
    placementId: string
  ): Promise<Map<string, any>> {
    return AnythinkModule.ATBannerViewCheckAdStatus(placementId);
  },
  ATBannerViewShow: function (placementId: string, position: string) {
    AnythinkModule.ATBannerViewShow(placementId, position);
  },
  ATBannerViewVisible: function (placementId: string) {
    AnythinkModule.ATBannerViewVisible(placementId);
  },
  ATBannerViewInvisible: function (placementId: string) {
    AnythinkModule.ATBannerViewInvisible(placementId);
  },
  ATBannerViewRemove: function (placementId: string) {
    AnythinkModule.ATBannerViewRemove(placementId);
  },
};

export default AnythinkModuleBridge;

type AnythinkBannerViewProps = {
  width: number;
  height: number;
  placementID: string;
  style: ViewStyle;
};
const AnythinkBannerView =
  UIManager.getViewManagerConfig('AnythinkBannerView') != null
    ? requireNativeComponent<AnythinkBannerViewProps>('AnythinkBannerView')
    : () => {
        throw new Error(LINKING_ERROR);
      };
export { AnythinkBannerView };
