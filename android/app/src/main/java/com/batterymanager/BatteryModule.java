package com.batterymanager;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;

public class BatteryModule extends ReactContextBaseJavaModule {
    private int count = 0;

    private static String BATTERY_LEVEL_EVENT = "BATTERY_LEVEL_EVENT";
    private int batteryLevelListenersCount = 0;
    private HandlerThread hThread = new HandlerThread("HandlerThread");
    private Handler handler;
    private Runnable runnable;

    // This is the constructor of the class
    BatteryModule(ReactApplicationContext context) {
        super(context);
    }

    @NonNull
    @Override
    public String getName() {
        //This is the name we are going to use from javascript to access this module
        return "BatteryModule";
    }

    /**
     * Android logger function
     * Using @ReactMethod is how we expose a function to react native
     * @param tag The tag to use in the log
     * @param description The description to use in the log
     */
    @ReactMethod
    public void nativeLogger(String tag, String description) {
        Log.d(tag, description);
        count++;
    }

    /**
     * Android logger function with callback
     * Using @ReactMethod is how we expose a function to react native
     * @param tag The tag to use in the log
     * @param description The description to use in the log
     * @param callBack The callback function to call after logging the data
     */
    @ReactMethod
    public void nativeLogger(String tag, String description, Callback callBack) {
        Log.d(tag, description);
        count++;
        WritableMap map = Arguments.createMap();
        map.putString("status", "OK");
        map.putString("tag", tag);
        map.putString("description", description);
        map.putInt("count", count);
        callBack.invoke(map);
    }

    /**
     * Android logger function with a promise
     * Using @ReactMethod is how we expose a function to react native
     * @param tag The tag to use in the log
     * @param description The description to use in the log
     * @param promise The promise object to resolve/reject and use it from JS
     */
    @ReactMethod
    public void nativeLogger(String tag, String description, Promise promise) {
        try {
            Log.d(tag, description);
            count++;
            WritableMap map = Arguments.createMap();
            map.putString("status", "OK");
            map.putString("tag", tag);
            map.putString("description", description);
            map.putInt("count", count);
            promise.resolve(map);
        } catch (Exception e) {
            promise.reject("Native logger error", e);
        }
    }

    /**
     * Expose constants to RN side
     * @return a Map that will be used from JS side to get the constants
     */
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(BATTERY_LEVEL_EVENT, BATTERY_LEVEL_EVENT);
        return constants;
    }

    private void sendBatteryLevelEvent(float batteryPercentLevel) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(BATTERY_LEVEL_EVENT, batteryPercentLevel);
    }

    @ReactMethod
    public void addListener(String eventName) {
        batteryLevelListenersCount++;
        Log.i("Listener added", "One listener was added " + eventName);
        if(handler == null && runnable == null) {
            hThread.start();
            handler = new Handler(hThread.getLooper());

            runnable = new Runnable() {
                @Override
                public void run() {
                    // Getting battery level
                    IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    Intent batteryStatus = getReactApplicationContext().registerReceiver(null, iFilter);
                    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    float batteryPct = level * 100 / (float)scale;

                    sendBatteryLevelEvent(batteryPct); // sending the event with the value in percent
                    handler.postDelayed(this, 2000); // Execute the previous block every 2 seconds
                }
            };
            // Schedule the first execution
            handler.post(runnable);
        }
    }

    @ReactMethod
    public void removeListeners(Integer count) {
        batteryLevelListenersCount -= count;
        Log.i("Listener removed", "leaving " + batteryLevelListenersCount + " listeners active");
        if(batteryLevelListenersCount <= 0 && handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            handler = null;
            runnable = null;
        }
    }

    /**
     * Get the battery status in a callback
     * Using @ReactMethod is how we expose a function to react native
     */
    @ReactMethod
    public void getBatterLevel(Callback callBack) {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getReactApplicationContext().registerReceiver(null, iFilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level * 100 / (float)scale;

        callBack.invoke(batteryPct);
    }
}
