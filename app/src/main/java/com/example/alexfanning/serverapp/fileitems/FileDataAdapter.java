package com.example.alexfanning.serverapp.fileitems;

import android.app.DownloadManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by alex.fanning on 02/10/2017.
 */

public class FileDataAdapter extends RecyclerView.Adapter<FileDataAdapter.FileViewHolder> {
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
        ContextWrapper c = new ContextWrapper(mContext);
        final String root = c.getFilesDir().toString();

        new AsyncTask<File, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(File... voids) {
                try {

                    java.io.File f = new java.io.File(Environment.DIRECTORY_DOWNLOADS + java.io.File.separator + selectedFile.getName());
                    f.createNewFile();
                    NetworkUtilities.downloadFile(selectedFile.getPath(), f);
                    return "1";
                } catch (Exception e) {
                    return "0";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            }
        }.execute(selectedFile);


    }


}
