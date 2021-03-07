package com.kali.xvideoplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kali.xvideoplayer.R;
import com.kali.xvideoplayer.activity.VideoFolderActivity;

import com.kali.xvideoplayer.model.VideoFiles;

import java.util.ArrayList;

import static com.kali.xvideoplayer.activity.MainActivity.folderLiset;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<VideoFiles> videoFiles;
    private ArrayList<String> folderFiles;


    public FolderAdapter(Context context, ArrayList<VideoFiles> videoFiles,ArrayList<String> folderFiles) {
        this.context = context;
        this.videoFiles = videoFiles;
        this.folderFiles=folderFiles;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        int index = folderFiles.get(position).lastIndexOf("/");
        String folderNameOnly=folderFiles.get(position).substring(index+1);
        holder.folderName.setText(folderNameOnly);
         holder.numberOfFileInFolder.setText(String.valueOf(numberOfFile(folderFiles.get(position))));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoFolderActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return folderFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView folderName, numberOfFileInFolder;
        ImageView folderImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            numberOfFileInFolder = itemView.findViewById(R.id.total_video_in_folder);
            folderName = itemView.findViewById(R.id.folder_name);
            folderImage = itemView.findViewById(R.id.folder_image);
        }
    }

    int numberOfFile(String folderName){
        int counter=0;
        for (VideoFiles videoFiles : videoFiles){
            if (videoFiles.getPath().substring(0,videoFiles.getPath().lastIndexOf("/")).endsWith(folderName)){
                counter++;
            }
        }
        return counter;
    }
}


