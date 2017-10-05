package com.example.alexfanning.serverapp.fileitems;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.alexfanning.serverapp.utils.JsonUtilities;
import com.example.alexfanning.serverapp.utils.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by alex.fanning on 04/10/2017.
 */

public class FileLoader extends AsyncTaskLoader<File[]> {
    public static final int FILE_LOADER_ID = 20;
    private Context c;

    public FileLoader(Context _c){
        super(_c);
        c = _c;
    }

    @Override
    public File[] loadInBackground() {
        File[] files = null;
        try{
            String fileJsonResponse = NetworkUtilities.getFileJsonResponse();
            files = JsonUtilities.getFilesFromJson(fileJsonResponse);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch(JSONException e){
            return null;
        }
        return files;
    }
}
