package com.example.alexfanning.serverapp.utils;

import android.net.Uri;

import com.example.alexfanning.serverapp.fileitems.File;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by alex.fanning on 05/10/2017.
 */

public class NetworkUtilities {
    public static final String FILE_ENPOINT = "http://192.168.1.10:5000/files";
    public static final String DOWNLOAD_FILE_ENPOINT = "http://192.168.1.10:5000/downloadFile/";

    public static String getFileJsonResponse() throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) buildFileUrl().openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()){
                return scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }

    private static URL buildFileUrl(){
        Uri builtUri = Uri.parse(FILE_ENPOINT).buildUpon().build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }



}
