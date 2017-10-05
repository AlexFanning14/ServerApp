package com.example.alexfanning.serverapp.activities;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alexfanning.serverapp.R;
import com.example.alexfanning.serverapp.fileitems.File;
import com.example.alexfanning.serverapp.fileitems.FileDataAdapter;
import com.example.alexfanning.serverapp.fileitems.FileLoader;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<File[]> {

    private RecyclerView mRv;
    private TextView mTvError;
    private ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        mRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRv.setHasFixedSize(true);
        getSupportLoaderManager().initLoader(FileLoader.FILE_LOADER_ID,null,this);
        setUpLoaders();

    }
    private void findViews(){
        mRv = (RecyclerView) findViewById(R.id.rv_files);
        mTvError = (TextView) findViewById(R.id.tv_error);
        mPb = (ProgressBar) findViewById(R.id.pb_load);
    }

    private void setUpLoaders(){
        LoaderManager lm = getSupportLoaderManager();
        Loader<File[]> fileLoader = lm.getLoader(FileLoader.FILE_LOADER_ID);
        if (fileLoader == null){
            lm.initLoader(FileLoader.FILE_LOADER_ID,null,this);
        }else{
            lm.restartLoader(FileLoader.FILE_LOADER_ID,null,this).forceLoad();
        }
    }


    @Override
    public Loader<File[]> onCreateLoader(int id, Bundle args) {
        if (id == FileLoader.FILE_LOADER_ID){
            FileLoader fl = new FileLoader(this);
            return fl;
        }
        mPb.setVisibility(View.VISIBLE);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<File[]> loader, File[] files) {
        if (loader.getId() == FileLoader.FILE_LOADER_ID){
            if ( files == null){
                mRv.setVisibility(View.INVISIBLE);
                mTvError.setVisibility(View.VISIBLE);
                mPb.setVisibility(View.GONE);
            }else if (files.length == 0){
                mRv.setVisibility(View.INVISIBLE);
                mTvError.setVisibility(View.VISIBLE);
                mPb.setVisibility(View.GONE);
            }else{
                FileDataAdapter fda = new FileDataAdapter(files,this);
                mRv.setAdapter(fda);
                mRv.setVisibility(View.VISIBLE);
                mTvError.setVisibility(View.GONE);
                mPb.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<File[]> loader) {

    }



}
