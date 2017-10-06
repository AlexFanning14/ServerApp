package com.example.alexfanning.serverapp.fileitems;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexfanning.serverapp.R;
import com.example.alexfanning.serverapp.utils.NetworkUtilities;

import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by alex.fanning on 02/10/2017.
 */

public class FileDataAdapter extends RecyclerView.Adapter<FileDataAdapter.FileViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = FileDataAdapter.class.getSimpleName();
    private File[] mFiles;
    private Context mContext;

    public FileDataAdapter(File[] files, Context context) {
        mFiles = files;
        mContext = context;
    }


    @Override
    public FileDataAdapter.FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForGridItem = R.layout.file_grid_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutIdForGridItem, parent, false);
        FileViewHolder viewHolder = new FileViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FileDataAdapter.FileViewHolder holder, int position) {
        holder.bind(mFiles[position]);
    }

    @Override
    public int getItemCount() {
        return mFiles.length;
    }



    class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView mtvName;
        private Button mBtn;

        public FileViewHolder(View itemView) {
            super(itemView);

            mtvName = (TextView) itemView.findViewById(R.id.tv_file_name);
            mBtn = (Button) itemView.findViewById(R.id.btn_download_file);
            mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isStoragePermissionGranted())
                        downloadSelectedFile(getAdapterPosition());
                }
            });
        }


        void bind(File f) {
            mtvName.setText(f.getName());
        }
    }

    private void downloadSelectedFile(int pos) {
        final File selectedFile = mFiles[pos];
        String path = NetworkUtilities.DOWNLOAD_FILE_ENPOINT + selectedFile.getId();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, selectedFile.getName());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); // to notify when download is complete
        request.allowScanningByMediaScanner();// if you want to be available from media players
        DownloadManager manager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (mContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");

                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}
