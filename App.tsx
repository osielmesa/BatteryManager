/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React from 'react';
import { SafeAreaView, StatusBar, useColorScheme, View, NativeModules, Button, StyleSheet } from 'react-native';
function App(): JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';
  const { BatteryModule } = NativeModules; // here we use the name returned in our getName method of BatteryModule java class
  const onPress = async () => {
    try {
      const res = await BatteryModule.nativeLogger('RN to Android', 'This is a log coming from JS');
      console.log(res);
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <View>
        <Button title="Click to log data in android console!" color="#303F9F" onPress={onPress} />
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
});

export default App;
