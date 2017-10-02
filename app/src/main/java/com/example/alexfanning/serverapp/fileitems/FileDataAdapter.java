package com.example.alexfanning.serverapp.fileitems;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.alexfanning.serverapp.R;

/**
 * Created by alex.fanning on 02/10/2017.
 */

public class FileDataAdapter extends RecyclerView.Adapter<FileDataAdapter.FileViewHolder> {
    private static final String TAG = FileDataAdapter.class.getSimpleName();
    private File[] mFiles;
    private Context mContext;

    public FileDataAdapter(File[] files, Context context){
        mFiles = files;
        mContext = context;
    }

    @Override
    public FileDataAdapter.FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForGridItem = R.layout.file_grid_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutIdForGridItem,parent,false);
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

        public FileViewHolder(View itemView){
            super(itemView);

            mtvName = (TextView) itemView.findViewById(R.id.tv_file_name);
            mBtn = (Button) itemView.findViewById(R.id.btn_download_file);
        }

        void bind(File f){
            mtvName.setText(f.getName());

        }
    }


}
