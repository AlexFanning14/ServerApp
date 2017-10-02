package com.example.alexfanning.serverapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alexfanning.serverapp.R;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRv;
    private TextView mTvError;
    private ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    private void findViews(){
        mRv = (RecyclerView) findViewById(R.id.rv_files);
        mTvError = (TextView) findViewById(R.id.tv_error);
        mPb = (ProgressBar) findViewById(R.id.pb_load);
    }

}
