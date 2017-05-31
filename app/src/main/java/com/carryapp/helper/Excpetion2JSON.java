package com.carryapp.helper;

import org.json.JSONException;
import org.json.JSONObject;


public class Excpetion2JSON {
    public static JSONObject getJSON(Exception e) {
        try{
            JSONObject json = new JSONObject();
            json.put("result", -4);
            json.put("message", e.getMessage());
            return json;
        } catch(JSONException je) {
            return null;
        }
    }
}