package io;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.controls.templates.RangeTemplate;
import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class io {

    private static final String INIT_FILE = "init_stations.json";
    public static final String FILE= "stations.json";
    private static final String ARRAY_NAME = "stations";

    /*
    * Initiates the backend from the assets folder
     */
    public static void initBackend(Context context) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(INIT_FILE)));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                sb.append(mLine);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        JSONObject json = null;
        try {
            json = new JSONObject(sb.toString());
        } catch (JSONException e) {Log.v("JSON_ERROR_1", e.getMessage());}
        writeJSON(json, context);

    }

    /*
     * Overwrites the entire stations.json file with the given object
     */
    public static void writeJSON(JSONObject json, Context context){
        String userString = json.toString();
        File file = new File(context.getFilesDir(),FILE);
        BufferedWriter bufferedWriter;
        FileWriter fileWriter;

        try {
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(userString);
            bufferedWriter.close();
        } catch (IOException e) {
            Log.v("WRITE_ERROR", e.getMessage());
        }
    }

    /*
     * Writes the given bitmap to the images folder
     */
    public static void writeJSON(Bitmap bmp, Context context) {

    }

    /*
     * Reads the stations.json file and parses to a JSON-object
     */
    public static JSONArray readJSON(Context context) {
        File file = new File(context.getFilesDir(),FILE);
        FileReader fileReader;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            JSONObject reader = new JSONObject(stringBuilder.toString());
            return reader.getJSONArray(ARRAY_NAME);
        } catch (FileNotFoundException e) { e.printStackTrace(); }
          catch (IOException e) {           e.printStackTrace(); }
          catch (JSONException e) {         e.printStackTrace();
        }

        return null;
    }

    /*
     * Submits the form from the review page
     */
    public static void submitForm(String stationName, String category, String comment, int stars, String picturePath, Context context) {
        JSONObject report = new JSONObject();
        JSONObject rating = new JSONObject();
        JSONObject station = null;
        try {
            report.put("category", category);
            report.put("comment", comment);
            report.put("picture", picturePath);
            rating.put("stars", stars);

            station = readStation(stationName, context);
            // Add report
            JSONArray reports = station.getJSONArray("reports");
            reports.put(report);
            //Add rating
            JSONArray ratings = station.getJSONArray("ratings");
            ratings.put(rating);

        } catch (JSONException e) {
            Log.v("PUT_ERROR", e.getMessage());
        }
        Log.v("STATION", station.toString());
        appendSubmission(station, context);
    }

    /*
     * Appends to and updates the stations.json file
     */
    private static void appendSubmission(JSONObject station, Context context) {
        JSONArray stations = readJSON(context);
        JSONArray newStations = new JSONArray();
        int index;
        try {
            for (index = 0; index < stations.length();index++) {
                if(stations.getJSONObject(index).get("name").equals(station.get("name")))
                    newStations.put(station);
                else
                    newStations.put(stations.get(index));
            }
            JSONObject obj = new JSONObject();
            obj.put("stations", newStations);
            writeJSON(obj, context);
        } catch (JSONException e) {
            Log.v("PUT_ERROR", e.getMessage());
        }
    }

    /*
     * Returns the desired staion as a JSON-object
     */
    public static JSONObject readStation(String stationName, Context context) {
        int index = 0;
        JSONArray stations = readJSON(context);

        try {
            while (index < stations.length()) {
                if (stations.getJSONObject(index).get("name").toString().equals(stationName))
                    return stations.getJSONObject(index);
                index++;
            }
        } catch (JSONException e) {
            Log.v("MEMEERROR", e.getMessage());
        }

        return null;
    }

    public static float calculateMeanRating(JSONObject station){
        float meanRating = 0;
        JSONArray ratings = null;
        int i;
        try {
            ratings = station.getJSONArray("ratings");

            for (i = 0; i < ratings.length();i++) {
                meanRating += Float.parseFloat(ratings.get(i).toString());
                // TODO Print and test method
            }

            return meanRating/i;

        } catch (JSONException e) {
            Log.v("JSON_ERROR", e.getMessage());
        }



    }


    //TODO Store image,  Add a bunch of stations to init_stations.json,

}
