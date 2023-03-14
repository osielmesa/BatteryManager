/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, { useEffect, useState } from 'react';
import {
  SafeAreaView,
  StatusBar,
  useColorScheme,
  View,
  NativeModules,
  Button,
  StyleSheet,
  NativeEventEmitter,
  Text,
} from 'react-native';
function App(): JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';
  const [batteryLevel, setBatteryLevel] = useState('');
  const { BatteryModule } = NativeModules; // here we use the name returned in our getName method of BatteryModule java class
  const onPress = async () => {
    try {
      const res = await BatteryModule.nativeLogger('RN to Android', 'This is a log coming from JS');
      console.log(res);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    const eventEmitter = new NativeEventEmitter(BatteryModule);
    const { BATTERY_LEVEL_EVENT } = BatteryModule.getConstants();
    const eventListener = eventEmitter.addListener(BATTERY_LEVEL_EVENT, percent => {
      console.log('Battery level received: ', percent);
      setBatteryLevel(percent);
    });
    return () => {
      eventListener.remove();
    };
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <View>
        <Button title="Click to log data in android console!" color="#303F9F" onPress={onPress} />
        {batteryLevel !== '' && <Text style={styles.batteryLevel}>{`${batteryLevel} %`}</Text>}
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  batteryLevel: {
    marginTop: 40,
    textAlign: 'center',
    fontSize: 20,
    color: 'tomato',
  },
});

export default App;
