/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {useEffect} from 'react';
import type {Node} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  useColorScheme,
} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';

import {NativeModules, Platform} from 'react-native';

const LINKING_ERROR =
  "The package 'react-native-anythink-module' doesn't seem to be linked. Make sure: \n\n" +
  Platform.select({ios: "- You have run 'pod install'\n", default: ''}) +
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
      },
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

const App: () => Node = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  useEffect(() => {
    alert('useEffect');
    (async () => {
      AnythinkModuleBridge.ATSDKSetNetworkLogDebug(true);
      const ver = await AnythinkModuleBridge.ATSDKGetSDKVersionName();
      console.log('ver: ' + ver);
      AnythinkModuleBridge.ATSDKInit(
        'a61b16cce7d524',
        '97a20b7e3b342cc51bf9ea278a6972af',
      );
      const deviceInfo = await AnythinkModuleBridge.ATSDKTestModeDeviceInfo();
      console.log('deviceInfo: ' + deviceInfo);
      AnythinkModuleBridge.ATSDKIntegrationChecking();
    })();
  }, []);

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <ScrollView />
    </SafeAreaView>
  );
};

export default App;
