package io;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.JsonReader;
import android.util.Log;

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
    private static final String FILE= "stations.json";
    private static final String ARRAY_NAME = "stations";

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
            return new JSONArray(stringBuilder.toString());
        } catch (FileNotFoundException e) { e.printStackTrace(); }
          catch (IOException e) {           e.printStackTrace(); }
          catch (JSONException e) {         e.printStackTrace();
        }

        return null;
    }

    public static void submitForm(String stationName, String category, String comment, int rating, String picturePath) {
        JSONObject station = readStation("sd", null);
    }

    public static JSONObject readStation(String stationName, JSONArray stations) {
        int index = 0;
        while (index < stations.length()) {
            //if(stations.get(index))
            //index++
        }
        return null;
    }

    public static void writeJSON(Bitmap bmp, Context context) {

    }

}
