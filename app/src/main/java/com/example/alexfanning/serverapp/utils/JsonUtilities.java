package com.example.alexfanning.serverapp.utils;

import com.example.alexfanning.serverapp.fileitems.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by alex.fanning on 05/10/2017.
 */

public class JsonUtilities  {
    private static final String TAG = JsonUtilities.class.getSimpleName();

    private static final String FILES_KEY = "Files";
    private static final String ID_KEY = "id";
    private static final String PATH_KEY = "path";
    private static final String NAME_KEY = "name";


    public static File[] getFilesFromJson(String fileJsonStr) throws JSONException{

        File[] files = null;
        JSONObject fileJsonObj = new JSONObject(fileJsonStr);
        JSONArray fileArray = fileJsonObj.getJSONArray(FILES_KEY);
        try{
            files = new File[fileArray.length()];
            for (int i = 0; i < fileArray.length(); i++ ){
                JSONObject f = fileArray.getJSONObject(i);
                int id = Integer.parseInt(f.getString(ID_KEY));
                String path = f.getString(PATH_KEY);
                String name = f.getString(NAME_KEY);
                files[i] = new File(id,name,path);
            }
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return files;
    }

}
