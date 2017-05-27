package com.test.locationtest.Bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by caibinglong
 * on 2017/5/22.
 */

public class AddressComponentsBean implements Serializable {
    private static final String tag = "AddressComponentsBean";
    private static final long serialVersionUID = 1L;
    public String long_name;
    public String short_name;

    public ArrayList<String> typesList = new ArrayList<String>();

    public static AddressComponentsBean fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        AddressComponentsBean localItem = new AddressComponentsBean();
        localItem.long_name = jsonObject.optString("long_name");
        localItem.short_name = jsonObject.optString("short_name");

        JSONArray types = jsonObject.optJSONArray("types");

        if (types != null) {
            for (int i = 0; i < types.length(); i++) {
                if (localItem.typesList != null) {
                    localItem.typesList.add(types.getString(i));
                }
            }
        }
        return localItem;
    }
}