package com.kali.xvideoplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import com.kali.xvideoplayer.R;
import com.kali.xvideoplayer.adapter.VideoFolderAdapter;
import com.kali.xvideoplayer.model.VideoFiles;

import java.util.ArrayList;

import static com.kali.xvideoplayer.activity.MainActivity.folderLiset;

public class VideoFolderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    VideoFolderAdapter videoFolderAdapter;
    String myFolderName;
    ArrayList<VideoFiles> arrayListVideoFolder=new ArrayList<>();
    int pos;
    Toolbar toolbar;
    TextView toolbarTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);
        setStatusBarColor();
       // myFolderName=getIntent().getStringExtra("folderName");
        pos=getIntent().getIntExtra("position",0);
        myFolderName=folderLiset.get(pos);

        initView();
    }


    private void initView() {
        //Finding ID
        toolbar=findViewById(R.id.toolbar2);
        toolbarTextView=findViewById(R.id.toolbarTextView);


        toolbarTextView.setText(getFolderNameOnly(myFolderName));
        recyclerView=findViewById(R.id.video_folder_recycler_viev);
        if (myFolderName!=null){
            arrayListVideoFolder=getAllVideo(this,myFolderName);
        }
        if (arrayListVideoFolder.size()>0){
            videoFolderAdapter=new VideoFolderAdapter(this,arrayListVideoFolder);
            recyclerView.setAdapter(videoFolderAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        }
    }

    private String getFolderNameOnly(String myFolderN) {
        int index = myFolderN.lastIndexOf("/");
        String folderNameOnly=myFolderN.substring(index+1);
        return folderNameOnly;
    }

    private ArrayList<VideoFiles> getAllVideo(Context context,String folderPath){
        ArrayList<VideoFiles> tempVideoFiles=new ArrayList<>();
        Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection={
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME

        };
        String selection=MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs=new String[]{"%" + folderPath + "%"};
        Cursor cursor=context.getContentResolver().query(uri,projection,selection,selectionArgs,null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                String id=cursor.getString(0);
                String path=cursor.getString(1);
                String title=cursor.getString(2);
                String size=cursor.getString(3);
                String dataAddad=cursor.getString(4);
                String duration=cursor.getString(5);
                String fileName=cursor.getString(6);
                String bucketName=cursor.getString(7);

                VideoFiles videoFiles = new VideoFiles(id, path, title, fileName, size, dataAddad, duration);


                if (folderLiset.get(pos).endsWith(bucketName)) {



                    Log.e("xxxx", title);

                    tempVideoFiles.add(videoFiles);
                }
            }
            cursor.close();
        }
        return tempVideoFiles;
    }
    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.back_them, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.back_them));
        }
    }

}