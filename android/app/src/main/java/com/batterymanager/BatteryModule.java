package com.batterymanager;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.util.Log;

public class BatteryModule extends ReactContextBaseJavaModule {

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
    }
}
