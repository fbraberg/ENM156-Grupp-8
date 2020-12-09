package io;


import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


public class io {

    private static final String FILE_NAME = "stations.json";
    private static final String ARRAY_NAME = "stations";

    public static JSONArray readStations (Context context) throws JSONException, IOException {

        String content = loadJSONAsString(FILE_NAME, context);
        JSONObject reader = new JSONObject(content);

        return reader.getJSONArray(ARRAY_NAME);
    }

    public static JSONObject readStation (String name, Context context)  {
        try {

            String content = loadJSONAsString(FILE_NAME, context);

            JSONObject reader = new JSONObject(content);
            JSONArray stations = reader.getJSONArray("name");

            int index = 0;
            JSONObject station;
            // Find the right station based on name
            while (index < stations.length()) {
                station = stations.getJSONObject(index);
                if (station.getString("name").equals(name))
                    return station;
            }
            return stations.getJSONObject(index);
        } catch (JSONException e) {
            Log.v("MEMEERROR1", e.getMessage());
        }
        return null;
    }

    public static void writeStation(JSONObject json, Context context) {

        String fileContents = json.toString();
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v("LELELELELELEL", loadJSONAsString(FILE_NAME, context));
    }


     //This one works in but it needs to be better
    private static String loadJSONAsString(String path, Context context) {
        File file = new File(context.getFilesDir(),FILE_NAME);
        StringBuilder stringBuilder = new StringBuilder();

        try {
            /*if( !file.exists() ){
                if( !file.createNewFile()){
                    return "COULDN'T CREATE FILE: " + FILE_NAME;
                }
            }*/
            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Log.v("MAYBE", "lele: " + bufferedReader.readLine());
            String line = bufferedReader.readLine();

            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

        } catch (FileNotFoundException e) { e.printStackTrace(); }
          catch (IOException e)           { e.printStackTrace(); }

        return stringBuilder.toString();

    }
}
