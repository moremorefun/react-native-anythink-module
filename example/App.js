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
  Button,
  Dimensions,
  NativeEventEmitter,
  NativeModules,
  PixelRatio,
  Platform,
  SafeAreaView,
  ScrollView,
  StatusBar,
  useColorScheme,
  StyleSheet,
  View,
} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';
import AnythinkModuleBridge from 'react-native-anythink-module';

const App: () => Node = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  let appID = 'a61b16cce7d524';
  let appKey = '97a20b7e3b342cc51bf9ea278a6972af';
  let rewardID = 'b61b16cfd7abae';
  let interID = 'b62023abcd0c9c';
  let splashID = 'b61c4951566e55';
  let splashIDDefault =
    '{"unit_id":1308102,"nw_firm_id":15,"adapter_class":"com.anythink.network.toutiao.TTATSplashAdapter","content":"{\\"button_type\\":\\"0\\",\\"dl_type\\":\\"0\\",\\"slot_id\\":\\"887668414\\",\\"personalized_template\\":\\"0\\",\\"zoomoutad_sw\\":\\"1\\",\\"app_id\\":\\"5261386\\"}"}';
  let bannerID = 'b62025d72e19ec';
  if (Platform.OS === 'ios') {
    appID = 'a61b166188a9b4';
    appKey = '97a20b7e3b342cc51bf9ea278a6972af';
    rewardID = 'b61b166324f190';
    interID = 'b6202409ac5c3d';
    splashID = 'b61c495138b4d6';
    splashIDDefault = '';
    bannerID = 'b62025e3b119d8';
  }

  useEffect(() => {
    (async () => {
      if (__DEV__) {
        // 设置为debug模式
        AnythinkModuleBridge.ATSDKSetNetworkLogDebug(true);
        // 检查SDK是否集成成功
        AnythinkModuleBridge.ATSDKIntegrationChecking();
      }
      // 初始化SDK
      AnythinkModuleBridge.ATSDKInit(appID, appKey);
      // 添加监听
      if (NativeModules.AnythinkModule) {
        const AnythinkModuleEventEmitter = new NativeEventEmitter(
          NativeModules.AnythinkModule,
        );
        AnythinkModuleBridge.setListeners(
          AnythinkModuleEventEmitter,
          (type, args) => {
            console.log('[App] AnythinkModuleEventEmitter', type, args);
            switch (type) {
              case 'onSplashAdShow':
                // 展示开屏广告
                AnythinkModuleBridge.ATSplashAdInitAndLoad(splashID, 5000, '');
                break;
              case 'onBannerClose':
                // 隐藏banner
                AnythinkModuleBridge.ATBannerViewInvisible(bannerID);
            }
          },
        );
      }
      // // 加载广告
      AnythinkModuleBridge.ATRewardVideoAutoAdInit([rewardID]);
      AnythinkModuleBridge.ATInterstitialAutoAdInit([interID]);
      AnythinkModuleBridge.ATSplashAdCheckSplashDefaultConfigList(splashID);
      AnythinkModuleBridge.ATSplashAdInitAndLoad(
        splashID,
        5000,
        splashIDDefault,
      );
      let wInPiexel = Dimensions.get('window').width * PixelRatio.get();
      if (Platform.OS === 'ios') {
        wInPiexel = Dimensions.get('window').width;
      }
      AnythinkModuleBridge.ATBannerViewInitAndLoad(bannerID, {
        width: wInPiexel,
        height: (wInPiexel * 90) / 600,
      });
      // AnythinkModuleBridge.ATBannerViewLoadAd('b62025d72e19ec');
    })();
  }, [appID, appKey, bannerID, interID, rewardID, splashID, splashIDDefault]);

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <ScrollView>
        <View style={styles.button}>
          <Button
            title="激励视频"
            onPress={async () => {
              const isReady =
                await AnythinkModuleBridge.ATRewardVideoAutoAdIsAdReady(
                  rewardID,
                );
              if (!isReady) {
                alert('激励视频未准备好');
              } else {
                AnythinkModuleBridge.ATRewardVideoAutoAdShow(rewardID);
              }
            }}
          />
        </View>
        <View style={styles.button}>
          <Button
            title="插屏"
            onPress={async () => {
              const isReady =
                await AnythinkModuleBridge.ATInterstitialAutoAdIsAdReady(
                  interID,
                );
              if (!isReady) {
                alert('插屏未准备好');
              } else {
                AnythinkModuleBridge.ATInterstitialAutoAdShow(interID);
              }
            }}
          />
        </View>
        <View style={styles.button}>
          <Button
            title="开屏"
            onPress={async () => {
              const isReady = await AnythinkModuleBridge.ATSplashAdIsAdReady(
                splashID,
              );
              if (!isReady) {
                alert('开屏未准备好');
              } else {
                AnythinkModuleBridge.ATSplashAdShow(splashID);
              }
            }}
          />
        </View>
        <View style={styles.button}>
          <Button
            title="Banner"
            onPress={async () => {
              AnythinkModuleBridge.ATBannerViewShow(bannerID, 'bottom');
            }}
          />
        </View>
        <View style={styles.button}>
          <Button
            title="删除Banner"
            onPress={async () => {
              AnythinkModuleBridge.ATBannerViewRemove(bannerID);
            }}
          />
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  button: {
    margin: 20,
  },
});

export default App;
