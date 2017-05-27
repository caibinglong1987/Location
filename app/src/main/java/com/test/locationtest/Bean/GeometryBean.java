package com.test.locationtest.Bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by caibinglong
 * on 2017/5/22.
 */

public class GeometryBean implements Serializable {
    private static final String tag = "GeometryBean";
    private static final long serialVersionUID = 1L;

    public String location_type;
    public String lat;
    public String lng;

    public ArrayList<String> location = new ArrayList<>();

    public static GeometryBean fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        GeometryBean localItem = new GeometryBean();
        localItem.location_type = jsonObject.optString("location_type");

        JSONObject location = jsonObject.getJSONObject("location");
        localItem.lat = location.optString("lat");
        localItem.lng = location.optString("lng");

        return localItem;
    }
}
