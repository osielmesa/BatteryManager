package com.batterymanager;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.util.Log;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

public class BatteryModule extends ReactContextBaseJavaModule {

    private int count = 0;

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
}
