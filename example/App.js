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
  Dimensions,
  NativeEventEmitter,
  NativeModules,
  PixelRatio,
  Platform,
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
        AnythinkModuleBridge.setListeners(
          AnythinkModuleEventEmitter,
          (type, args) => {
            console.log('[App] AnythinkModuleEventEmitter', type, args);
          },
        );
      }
      // 加载广告
      AnythinkModuleBridge.ATRewardVideoAutoAdInit(['b61b16cfd7abae']);
      AnythinkModuleBridge.ATInterstitialAutoAdInit(['b62023abcd0c9c']);
      AnythinkModuleBridge.ATSplashAdInit(
        'b61c4951566e55',
        5000,
        '{"unit_id":1308102,"nw_firm_id":15,"adapter_class":"com.anythink.network.toutiao.TTATSplashAdapter","content":"{\\"button_type\\":\\"0\\",\\"dl_type\\":\\"0\\",\\"slot_id\\":\\"887668414\\",\\"personalized_template\\":\\"0\\",\\"zoomoutad_sw\\":\\"1\\",\\"app_id\\":\\"5261386\\"}"}',
      );
      AnythinkModuleBridge.ATSplashAdLoadAd('b61c4951566e55');
      let wInPiexel = Dimensions.get('window').width * PixelRatio.get();
      if (Platform.OS === 'ios') {
        wInPiexel = Dimensions.get('window').width;
      }
      AnythinkModuleBridge.ATBannerViewInit(
        'b62025d72e19ec',
        JSON.stringify({
          width: wInPiexel,
          height: (wInPiexel * 90) / 600,
        }),
      );
      AnythinkModuleBridge.ATBannerViewLoadAd('b62025d72e19ec');
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
