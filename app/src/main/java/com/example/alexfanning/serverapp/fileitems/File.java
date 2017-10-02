package com.example.alexfanning.serverapp.fileitems;

/**
 * Created by alex.fanning on 02/10/2017.
 */

public class File {

    private int id;
    private String name;
    private String path;

    public File(int _id, String _name, String _path){
        id = _id;
        name = _name;
        path = _path;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }


}
