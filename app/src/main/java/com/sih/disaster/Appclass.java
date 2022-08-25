package com.sih.disaster;

import android.app.Application;

import com.onesignal.OneSignal;

public class Appclass extends Application {
    private static final String ONESIGNAL_APP_ID = "6aa65da6-7052-451a-bad9-c1ae262ffb9d";
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.promptForPushNotifications();
    }
}
