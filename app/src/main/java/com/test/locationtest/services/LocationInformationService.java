package com.test.locationtest.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.test.locationtest.Bean.AddressComponentsBean;
import com.test.locationtest.Bean.LocationBean;
import com.test.locationtest.SharedPreferencesConst;
import com.test.locationtest.SharedPreferencesTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by caibinglong
 * on 2017/5/22.
 */

public class LocationInformationService extends Service {
    public static final String tag = "LocationInformationService";
    private static final String INFORMATION_URL = "http://maps.google.cn/maps/api/geocode/json?language=zh-CN&sensor=true&latlng=%1$s,%2$s";

    // private long intervalTime = 1000 * 60 * 30;
    private long intervalTime = 5000;
    private long locationRefreshMinTime = 5000;
    private long locationRefreshMinDistance = 0;

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    private String latitude;
    private String longitude;

    private long lastTime;
    private long thisTime;

    private String locality;
    private String country;

    private boolean isAnalyzeJson = false;

    private boolean hasCountry = false;
    private boolean hasLocality = false;

    private ArrayList<LocationBean> locationList = new ArrayList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        //LogUtil.e(tag, "onCreate()");
        super.onCreate();
        shared = getSharedPreferences(SharedPreferencesConst.LOCATION_INFORMATION, 0);
        editor = shared.edit();
        initData();
        getLocalInformation();
    }

    private void initData() {
        //LogUtil.e(tag, "initData()");
        latitude = shared.getString(SharedPreferencesConst.LOCATION_INFORMATION_LATITUDE, "0.00");
        longitude = shared.getString(SharedPreferencesConst.LOCATION_INFORMATION_LONGITUDE, "0.00");
        lastTime = shared.getLong(SharedPreferencesConst.LOCATION_INFORMATION_UPDATE_LAST_TIME, 0);
        locality = shared.getString(SharedPreferencesConst.LOCATION_INFORMATION_LOCALITY, " ");
        country = shared.getString(SharedPreferencesConst.LOCATION_INFORMATION_COUNTRY, " ");
    }

    /**
     * 获取手机当前位置的经纬度
     */
    private void getLocalInformation() {
        //获取LocationManager实例
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //如果GPS可用
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, locationRefreshMinTime, locationRefreshMinDistance, locationListener);
            } else {
                //如果GPS不可用
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, locationRefreshMinTime, locationRefreshMinDistance, locationListener);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Location信息变化时调用的接口
    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //LogUtil.e(tag, "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            //LogUtil.e(tag, "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            //LogUtil.e(tag, "onProviderDisabled");
        }

        @Override
        public void onLocationChanged(Location location) {
            //LogUtil.e(tag, "onLocationChanged");
            if (location != null) {
                latitude = location.getLatitude() + "";
                longitude = location.getLongitude() + "";
                //LogUtil.e(tag, "经度:" + latitude + "\n" + "纬度:" + longitude);
                editor.putString(SharedPreferencesConst.LOCATION_INFORMATION_LATITUDE, latitude);
                editor.putString(SharedPreferencesConst.LOCATION_INFORMATION_LONGITUDE, longitude);
                editor.apply();
//                SharedPreferencesTool.putValue(getApplicationContext(), SharedPreferencesConst.LOCATION_INFORMATION,
//                        SharedPreferencesConst.LOCATION_INFORMATION_LATITUDE, latitude);
                thisTime = location.getTime();
                //LogUtil.e(tag, "thisTime = " + thisTime);
                //LogUtil.e(tag, "lastTime = " + lastTime);
                //LogUtil.e(tag, "thisTime - lastTime = " + (thisTime - lastTime));
                if (thisTime - lastTime > intervalTime || locality.equals(" ") || country.equals(" ")) {
                    lastTime = thisTime;
                    editor.putLong(SharedPreferencesConst.LOCATION_INFORMATION_UPDATE_LAST_TIME, thisTime);
                    editor.apply();
                    if (isAnalyzeJson) {

                    } else {
                        analyzeJson();
                    }
                }
            }
        }
    };

    //根据经纬度访问谷歌地图的api
    private void analyzeJson() {
        isAnalyzeJson = true;
        //LogUtil.e(tag, "analyzeJson()");
        String urlString = String.format(INFORMATION_URL, Double.valueOf(latitude), Double.valueOf(longitude));
        parseJsonString(getJsonStringFromUrl(urlString));
        for (int i = 0; i < locationList.size(); i++) {
            ArrayList<String> typesList = locationList.get(i).typesList;
            int typesListSize = typesList.size();
            for (int j = 0; j < typesListSize; j++) {
                if (typesList.get(j).equals("country")) {
                    hasCountry = true;
                    country = locationList.get(i).formatted_address;
                    editor.putString(SharedPreferencesConst.LOCATION_INFORMATION_COUNTRY, locationList.get(i).formatted_address);
                    editor.apply();
                }
            }

            ArrayList<AddressComponentsBean> address_components_list = locationList.get(i).addressComponentsBean_list;
            int address_components_list_size = address_components_list.size();
            for (int k = 0; k < address_components_list_size; k++) {
                AddressComponentsBean address_components = address_components_list.get(k);
                ArrayList<String> address_components_typesList = address_components.typesList;
                int address_components_typesListSize = address_components_typesList.size();
                for (int j = 0; j < address_components_typesListSize; j++) {
                    if (address_components_typesList.get(j).equals("locality")) {
                        hasLocality = true;
                        locality = address_components.long_name;
                        editor.putString(SharedPreferencesConst.LOCATION_INFORMATION_LOCALITY, address_components.long_name);
                        editor.apply();
                    }

                    if (address_components_typesList.get(j).equals("country")) {
                        hasCountry = true;
                        country = address_components.long_name;
                        editor.putString(SharedPreferencesConst.LOCATION_INFORMATION_COUNTRY, address_components.long_name);
                        editor.apply();
                    }
                }
            }
        }

        if (!hasCountry) {
            country = " ";
            editor.putString(SharedPreferencesConst.LOCATION_INFORMATION_COUNTRY, country);
            editor.apply();
        }

        if (!hasLocality) {
            locality = " ";
            editor.putString(SharedPreferencesConst.LOCATION_INFORMATION_LOCALITY, locality);
            editor.apply();
        }
        //LogUtil.e(tag, "analyzeJson(),locality = " + locality);
        //LogUtil.e(tag, "analyzeJson(),country = " + country);
        isAnalyzeJson = false;
        hasCountry = false;
        hasLocality = false;
    }

    private String getJsonStringFromUrl(String url) {
        //LogUtil.e(tag, "getJsonStringFromUrl(),url = " + url);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());

        URL cityInfoUrl;
        byte[] data = new byte[]{};
        try {
            cityInfoUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) cityInfoUrl.openConnection();
            connection.setReadTimeout(5 * 1000);
            connection.setRequestMethod("GET");
            InputStream inStream = connection.getInputStream();
            data = readInputStream(inStream);
            return (new String(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (new String(data));
    }

    private byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    private void parseJsonString(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray results = json.getJSONArray("results");
            int resultsLength = results.length();
            for (int i = 0; i < resultsLength; i++) {
                JSONObject results_item = results.getJSONObject(i);
                locationList.add(LocationBean.fromJson(results_item));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
