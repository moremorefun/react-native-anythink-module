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
  ATSDKTestModeDeviceInfo: function (): Promise<string> {
    return AnythinkModule.ATSDKTestModeDeviceInfo();
  },
};

export default AnythinkModuleBridge;
