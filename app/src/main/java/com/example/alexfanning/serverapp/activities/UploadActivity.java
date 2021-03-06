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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class UploadActivity extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = UploadActivity.class.getSimpleName();
    private Button mButtonSelect;
    private Button mButtonUpload;
    private TextView mTvUpload;
    private EditText mEtFileName;
    private static String sPath;
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
                        uploadFile(sPath);
                        return null;
                    }
                }.execute();

            }
        });

    }


//    private void uploadFile(){
//        final String url = "http://10.0.0.78:5000/upload";
//        final String charset = "UTF-8";
//        File newFile = new File(mTvUpload.getText().toString());
//        String boundary = Long.toHexString(System.currentTimeMillis());
//        String CRLF = "\r\n";
//        try{
//            URLConnection connection = new URL(url).openConnection();
//            connection.setDoOutput(true);
//            connection.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);
//            try(
//                    OutputStream output = connection.getOutputStream();
//                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output,charset),true);
//            ){
//                writer.append("--" + boundary).append(CRLF);
//                writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + newFile.getName() + "\"").append(CRLF);
//            }
//        }catch(Exception e){
//
//        }
//
//    }
public int uploadFile(String sourceFileUri) {


    String fileName = sourceFileUri;

    HttpURLConnection conn = null;
    DataOutputStream dos = null;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1024 * 1024;
    File sourceFile = new File(sourceFileUri);

    if (!sourceFile.isFile()) {


        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(UploadActivity.this, "Unable to find file", Toast.LENGTH_SHORT).show();
            }
        });

        return 0;

    }
    else
    {
        int serverResponseCode =0;
        try {

            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL("http://192.168.1.10:5000/upload");

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("file", fileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadfile\";filename=\""
                    + mTvUpload.getText().toString() + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();


            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200){

                runOnUiThread(new Runnable() {
                    public void run() {
                        String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                +" C:/xamp/wamp/fileupload/uploads";
                        Toast.makeText(UploadActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {


            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(UploadActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                }
            });

            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {


            runOnUiThread(new Runnable() {
                public void run() {

                    Toast.makeText(UploadActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                }
            });

        }

        return serverResponseCode;

    } // End else block
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
                    sPath = path;
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }





}
