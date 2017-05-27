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

public class LocationBean implements Serializable {
    private static final long serialVersionUID = 1L;

    public String formatted_address;
    public String place_id;

    public ArrayList<AddressComponentsBean> addressComponentsBean_list = new ArrayList<AddressComponentsBean>();
    public ArrayList<GeometryBean> geometryBean_list = new ArrayList<GeometryBean>();
    public ArrayList<String> typesList = new ArrayList<>();

    public static LocationBean fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        LocationBean localItem = new LocationBean();
        localItem.formatted_address = jsonObject.optString("formatted_address");
        localItem.place_id = jsonObject.optString("place_id");

        JSONArray address_components = jsonObject.optJSONArray("address_components");
        if (address_components != null) {
            for (int i = 0; i < address_components.length(); i++) {
                if (localItem.addressComponentsBean_list != null) {
                    localItem.addressComponentsBean_list.add(AddressComponentsBean.fromJson(address_components.getJSONObject(i)));
                }
            }
        }

        JSONArray geometry = jsonObject.optJSONArray("geometry");
        if (geometry != null) {
            for (int i = 0; i < geometry.length(); i++) {
                if (localItem.geometryBean_list != null) {
                    localItem.geometryBean_list.add(GeometryBean.fromJson(geometry.getJSONObject(i)));
                }
            }
        }

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
