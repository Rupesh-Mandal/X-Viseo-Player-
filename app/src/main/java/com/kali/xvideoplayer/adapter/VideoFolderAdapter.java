package com.kali.xvideoplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kali.xvideoplayer.R;
import com.kali.xvideoplayer.activity.PlayerActivity;
import com.kali.xvideoplayer.model.VideoFiles;

import java.util.ArrayList;

public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.MyViewHolder> {

    private Context context;
    public static ArrayList<VideoFiles> videoFoldeoFiles=new ArrayList<>();

    public VideoFolderAdapter(Context context, ArrayList<VideoFiles> videoFoldeoFiles) {
        this.context = context;
        this.videoFoldeoFiles = videoFoldeoFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.video_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideoFiles videoFiles=videoFoldeoFiles.get(position);
        holder.videoName.setText(videoFiles.getTitle());
        holder.duration.setText(getDurationForma(videoFiles.getDuration()));
        Glide.with(context).load(videoFiles.getPath()).into(holder.thumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PlayerActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("sender","folder");
                context.startActivity(intent);
            }
        });
    }

    private String getDurationForma(String duration) {
        int dur=Integer.parseInt(duration)/1000;
        int min=dur/60;
        int sec=dur%60;
        String formadeDuration=(min +":" +sec);
        return formadeDuration;
    }

    @Override
    public int getItemCount() {
        return videoFoldeoFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView duration;
        TextView videoName;
        //Button moreOption;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail=itemView.findViewById(R.id.video_thumbail);
            duration=itemView.findViewById(R.id.video_duration);
            videoName=itemView.findViewById(R.id.video_title);
           // moreOption=itemView.findViewById(R.id.more_btn);
        }
    }
}
