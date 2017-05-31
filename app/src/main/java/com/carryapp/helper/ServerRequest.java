package com.carryapp.helper;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Owner on 04-11-2016.
 */
public class ServerRequest {
    String api;
    JSONObject jsonParams;
    Context context;

    public ServerRequest(String api, JSONObject jsonParams) {
        this.api = api;
        this.jsonParams = jsonParams;
    }

    public ServerRequest(String api) {
        this.api = api;
    }

    public JSONObject sendRequest() throws UnexpectedServerException {
        try {

            URL url = new URL(api);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            //Log.e("jsonParams", jsonParams.toString());
            try {
                writer.write(jsonParams.toString());
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }

            writer.close();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                Log.d("ServerResponse", new String(sb));
                String output = "{\"array\":" + new String(sb) + "}";
                return new JSONObject(output);
            } else {
                Log.e("Exception", "" + responseCode);
                throw new UnexpectedServerException("Please enter valid data.");
            }
        } catch (MalformedURLException me) {
            return Excpetion2JSON.getJSON(me);
        } catch (IOException ioe) {
            return Excpetion2JSON.getJSON(ioe);
        } catch (JSONException je) {
            return Excpetion2JSON.getJSON(je);
        }

    }

    public JSONObject sendGetRequestForArray(String access_token) {
        try {
            URL url = new URL(api);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", access_token);

            con.setDoInput(true);

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                String output = "{\"array\":" + new String(sb) + "}";
                Log.e("ServerResponse", output);
                return new JSONObject(output);
            } else {
                throw new UnexpectedServerException("Unexpected server exception with status code : " + responseCode);
            }
        } catch (MalformedURLException me) {
            me.printStackTrace();
            return Excpetion2JSON.getJSON(me);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return Excpetion2JSON.getJSON(ioe);
        } catch (UnexpectedServerException ue) {
            ue.printStackTrace();
            return Excpetion2JSON.getJSON(ue);
        } catch (JSONException je) {
            je.printStackTrace();
            return Excpetion2JSON.getJSON(je);
        }
    }

    public JSONObject sendPostRequest(String access_token) {
        try {

            URL url = new URL(api);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("Authorization", access_token);
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            Log.e("jsonParams", jsonParams.toString());
            Log.e("jsonParams", access_token);
            try {
                writer.write(jsonParams.toString());
            } catch (Exception e) {
                Log.e("Exception111", e.toString());
            }

            writer.close();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                Log.d("ServerResponse", new String(sb));
                String output = "{\"array\":" + new String(sb) + "}";
                return new JSONObject(output);
            } else {
                Log.e("Exception", "" + responseCode);
            }
        } catch (MalformedURLException me) {
            return Excpetion2JSON.getJSON(me);
        } catch (IOException ioe) {
            return Excpetion2JSON.getJSON(ioe);
        } catch (JSONException je) {
            return Excpetion2JSON.getJSON(je);
        }
        return null;
    }

    public JSONObject sendGetRequest() {
        try {

            URL url = new URL(api);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "cd82484ccae110f27781a2f2c11b73dd");
            con.setDoInput(true);

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                Log.d("ServerResponse", new String(sb));
                return new JSONObject(new String(sb));
            } else {
                throw new UnexpectedServerException("Unexpected server exception with status code : " + responseCode);
            }
        } catch (MalformedURLException me) {
            me.printStackTrace();
            return Excpetion2JSON.getJSON(me);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return Excpetion2JSON.getJSON(ioe);
        } catch (UnexpectedServerException ue) {
            ue.printStackTrace();
            return Excpetion2JSON.getJSON(ue);
        } catch (JSONException je) {
            je.printStackTrace();
            return Excpetion2JSON.getJSON(je);
        }
    }


}
