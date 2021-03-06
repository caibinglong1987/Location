package com.test.locationtest;

import android.app.Application;

/**
 * Created by caibinglong
 * on 2017/5/22.
 */

public class HXApp extends Application {
    public static final int BASE_VALUE_PERMISSION = 0X0001;
    public static final int PERMISSION_REQ_ID_RECORD_AUDIO = BASE_VALUE_PERMISSION + 1;
    public static final int PERMISSION_REQ_ID_CAMERA = BASE_VALUE_PERMISSION + 2;
    public static final int PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = BASE_VALUE_PERMISSION + 3;
    public static final int PERMISSION_LOCATION = BASE_VALUE_PERMISSION + 4;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
