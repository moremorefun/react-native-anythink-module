import { NativeModules, Platform } from 'react-native';

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

const AnythinkModuleBridge = {
  setListeners: function (emitter: any) {
    // if (NativeModules.AnythinkModule) {
    // const AnythinkModuleEventEmitter = new NativeEventEmitter(
    //   NativeModules.AnythinkModule
    // );
    // AnythinkModuleEventEmitter.addListener(
    //   'onRewardVideoAutoLoaded',
    //   args => {
    //     console.log(
    //       '[App] AnythinkModuleEventEmitter',
    //       'onRewardVideoAutoLoaded',
    //       args,
    //     );
    //   },
    // );
    emitter.addListener('onRewardVideoAutoLoadFail', (args:any) => {
      console.log(
        'AnythinkModuleEventEmitter',
        'onRewardVideoAutoLoadFail',
        args
      );
    });
    console.log('AnythinkModuleEventEmitter', emitter);
    // }
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
  ATSDKInitCustomMap: function (customMap: string) {
    AnythinkModule.ATSDKInitCustomMap(customMap);
  },
  ATSDKInitPlacementCustomMap: function (
    TopOnPlacementID: string,
    customMap: string
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
    customMap: string
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
    customMap: string
  ) {
    AnythinkModule.ATInterstitialAutoAdSetLocalExtra(
      TopOnPlacementID,
      customMap
    );
  },

  ATSplashAdInit: function (
    placementId: string,
    fetchAdTimeout: number,
    defaultAdSourceConfig: string
  ) {
    AnythinkModule.ATSplashAdInit(
      placementId,
      fetchAdTimeout,
      defaultAdSourceConfig
    );
  },
  ATSplashAdSetLocalExtra: function (
    TopOnPlacementID: string,
    customMap: string
  ) {
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

  ATBannerViewInit: function (placementId: string) {
    AnythinkModule.ATBannerViewInit(placementId);
  },
  ATBannerViewSetLocalExtra: function (
    TopOnPlacementID: string,
    customMap: string
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
