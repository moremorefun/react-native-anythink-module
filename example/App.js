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
  NativeEventEmitter,
  NativeModules,
  SafeAreaView,
  ScrollView,
  StatusBar,
  useColorScheme,
} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';
import AnythinkModuleBridge from 'react-native-anythink-module';

const App: () => Node = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  useEffect(() => {
    (async () => {
      if (__DEV__) {
        // 设置为debug模式
        AnythinkModuleBridge.ATSDKSetNetworkLogDebug(true);
        // 检查SDK是否集成成功
        AnythinkModuleBridge.ATSDKIntegrationChecking();
      }
      // 初始化SDK
      AnythinkModuleBridge.ATSDKInit(
        'a61b16cce7d524',
        '97a20b7e3b342cc51bf9ea278a6972af',
      );
      // if (NativeModules.AnythinkModule) {
      //   const AnythinkModuleEventEmitter = new NativeEventEmitter(
      //     NativeModules.AnythinkModule,
      //   );
      //   AnythinkModuleEventEmitter.addListener(
      //     'onRewardVideoAutoLoaded',
      //     args => {
      //       console.log(
      //         '[App] AnythinkModuleEventEmitter',
      //         'onRewardVideoAutoLoaded',
      //         args,
      //       );
      //     },
      //   );
      //   AnythinkModuleEventEmitter.addListener(
      //     'onRewardVideoAutoLoadFail',
      //     args => {
      //       console.log(
      //         '[App] AnythinkModuleEventEmitter',
      //         'onRewardVideoAutoLoadFail',
      //         args,
      //       );
      //     },
      //   );
      //   console.log(
      //     '[App] AnythinkModuleEventEmitter',
      //     AnythinkModuleEventEmitter,
      //   );
      // }
      // 添加监听
      if (NativeModules.AnythinkModule) {
        const AnythinkModuleEventEmitter = new NativeEventEmitter(
          NativeModules.AnythinkModule,
        );
        AnythinkModuleBridge.setListeners(AnythinkModuleEventEmitter);
      }

      // 初始化自动加载
      AnythinkModuleBridge.ATRewardVideoAutoAdInit(['b61b16cfd7abae']);
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
