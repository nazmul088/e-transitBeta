/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BusStopJSONParser {

    /**
     * Receives a JSONObject and returns a list of lists containing latitude and longitude
     * @return
     */
    public HashMap<String, String> parseJsonObject(JSONObject jObject) throws JSONException {

        HashMap<String, String> dataList = new HashMap<String, String>();
        String name = jObject.getString("name");
        String latitude = jObject.getJSONObject("geometry").getJSONObject("location").getString("lat");
        String longitde = jObject.getJSONObject("geometry").getJSONObject("location").getString("lng");
        dataList.put("name",name);
        dataList.put("lat",latitude);
        dataList.put("lng",longitde);
        return dataList;

    }

    private List<HashMap<String,String>> parseJsonArray(JSONArray jsonArray) throws JSONException {

        List <HashMap<String,String>> dataList = new ArrayList<>();
        for(int i=0; i<jsonArray.length();i++){
            HashMap<String,String> data = parseJsonObject((JSONObject) jsonArray.get(i));
            dataList.add(data);

        }
        return dataList;
    }

    public List<HashMap<String,String>> parseResult(JSONObject object) throws JSONException {
        JSONArray jsonArray = null;
        jsonArray = object.getJSONArray("results");

        return parseJsonArray(jsonArray);


    }
}


