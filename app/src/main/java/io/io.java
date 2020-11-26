package io;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;



public class io {



    public String[] readStations (String path) throws JSONException {
        return null;
    }

    public static String readStation (String path, String name) throws JSONException {

        JSONObject reader = new JSONObject("{\n" +
                "    \"stations\": \n" +
                "    [\n" +
                "        {\n" +
                "            \"name\" : \"Kungsportsplatsen\",\n" +
                "            \"image\" : \"----------\",\n" +
                "            \"ratings\" : \n" +
                "            [\n" +
                "                {\"stars\": 2},\n" +
                "                {\"stars\": 5}\n" +
                "            ],\n" +
                "            \"reports\" : \n" +
                "            [\n" +
                "                {\n" +
                "                    \"category\" : \"opinion\",\n" +
                "                    \"comment\" : \"Hållplatsen är sämst\",\n" +
                "                    \"picture\" : \"-----\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"category\" : \"Suggestion\",\n" +
                "                    \"comment\" : \"Måla den röd\",\n" +
                "                    \"picture\" : \"-----\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\" : \"Nordstan\",\n" +
                "            \"image\" : \"----------\",\n" +
                "            \"ratings\" : \n" +
                "            [\n" +
                "                {\"stars\": \"-----\"}\n" +
                "            ],\n" +
                "            \"reports\" : \n" +
                "            [\n" +
                "                {\n" +
                "                    \"category\" : \"-----\",\n" +
                "                    \"comment\" : \"-----\",\n" +
                "                    \"picture\" : \"-----\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"category\" : \"-----\",\n" +
                "                    \"comment\" : \"-----\",\n" +
                "                    \"picture\" : \"-----\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "\n" +
                "}");
        JSONArray stations = reader.getJSONArray("stations");

        for (int i = 0; i < stations.length(); i++) {
            JSONObject c = stations.getJSONObject(i);
            Log.v("STAT" + i, c.getString("name"));
        }


        return null;
    }

    public void writeStations (String path) {

    }

    public void writeStation (String path) {

    }

}
