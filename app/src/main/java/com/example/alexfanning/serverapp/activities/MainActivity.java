package com.example.alexfanning.serverapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alexfanning.serverapp.R;
import com.example.alexfanning.serverapp.fileitems.File;
import com.example.alexfanning.serverapp.fileitems.FileDataAdapter;

public class MainActivity extends AppCompatActivity {

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
        File[] fs = new File[]{new File(1,"test","testPath"), new File(2,"secodn test", "Test")};
        FileDataAdapter fda = new FileDataAdapter(fs,this);
        mRv.setAdapter(fda);
    }

    private void findViews(){
        mRv = (RecyclerView) findViewById(R.id.rv_files);
        mTvError = (TextView) findViewById(R.id.tv_error);
        mPb = (ProgressBar) findViewById(R.id.pb_load);
    }

}
