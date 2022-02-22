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
