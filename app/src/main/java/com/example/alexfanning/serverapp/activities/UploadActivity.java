package com.example.alexfanning.serverapp.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexfanning.serverapp.R;
import com.example.alexfanning.serverapp.utils.PathUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;

public class UploadActivity extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = UploadActivity.class.getSimpleName();
    private Button mButtonSelect;
    private Button mButtonUpload;
    private TextView mTvUpload;
    private EditText mEtFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ActionBar ab = this.getSupportActionBar();
        if (ab!= null)
            ab.setDisplayHomeAsUpEnabled(true);
        findViews();
    }

    private void findViews(){
        mButtonSelect = (Button)findViewById(R.id.btn_select);
        mButtonUpload = (Button)findViewById(R.id.btn_upload);
        mTvUpload = (TextView) findViewById(R.id.tv_file_name);
        mEtFileName = (EditText) findViewById(R.id.et_file_name);
        mButtonSelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        mButtonUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {
                        uploadFile();
                        return null;
                    }
                }.execute();

            }
        });

    }


    private void uploadFile(){
        final String url = "http://10.0.0.78:5000/upload";
        File newFile = new File(mTvUpload.getText().toString());
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(url);

            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(newFile), -1);
            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(true); // Send in multiple parts if needed
            httppost.setEntity(reqEntity);
            httpclient.execute(httppost);
            Log.d(TAG, "SUCCESS: ");
            //Do something with response...

        } catch (Exception e) {
            // show error
            Log.d(TAG, "ERROR: " + e.getMessage());
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    // Get the path
                    String path = PathUtil.getPath(this,uri);
                    mTvUpload.setText(path);

                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



//    public String getPath(Uri uri) {
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            String[] projection = { "_data" };
//            Cursor cursor = null;
//
//            try {
//                cursor = getContentResolver().query(uri, projection, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow("_data");
//                if (cursor.moveToFirst()) {
//                    return cursor.getString(column_index);
//                }
//            } catch (Exception e) {
//                // Eat it
//            }
//        }
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }


}
