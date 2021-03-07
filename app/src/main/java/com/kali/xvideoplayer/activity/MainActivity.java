package com.kali.xvideoplayer.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.StatusBarManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kali.xvideoplayer.R;

import com.kali.xvideoplayer.fragment.File;
import com.kali.xvideoplayer.fragment.Folder;

import com.kali.xvideoplayer.model.VideoFiles;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    public static ArrayList<VideoFiles> videoFileList = new ArrayList<>();
    public static ArrayList<String> folderLiset = new ArrayList<>();
    FragmentTransaction fragmentTransaction;
    TextView toobarText;
    ViewPager viewPager;
    MenuItem prevMenuItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColor();
        checkPermissions();
        initView();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            videoFileList = getAllVideo(getApplicationContext());
            Log.e("xxxx", String.valueOf(videoFileList));
            Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        viewPager=findViewById(R.id.view_pager);
        toobarText=findViewById(R.id.toolbarTextView2);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragement(new File(),"file");
        viewPagerAdapter.addFragement(new Folder(),"folder");
        viewPager.setAdapter(viewPagerAdapter);
       // bottomNavigationView.setupWithViewPager(viewPager);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.file_list:
                                viewPager.setCurrentItem(0);

                                break;
                            case R.id.folder_list:
                                viewPager.setCurrentItem(1);

                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position==0){
                    toobarText.setText("All Video");
                }else {
                    toobarText.setText("All Folder");
                }

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page",""+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                videoFileList = getAllVideo(getApplicationContext());
                initView();
                Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
            } else {
                checkPermissions();
            }
        }
    }

    private ArrayList<VideoFiles> getAllVideo(Context context) {
        ArrayList<VideoFiles> tempVideoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME

        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String dataAddad = cursor.getString(4);
                String duration = cursor.getString(5);
                String fileName = cursor.getString(6);


                VideoFiles videoFiles = new VideoFiles(id, path, title, fileName, size, dataAddad, duration);
                Log.e("xxxx", title);
                int slashFirstIndex = path.lastIndexOf("/");
                String subsString = path.substring(0, slashFirstIndex);

                if (!folderLiset.contains(subsString)) {
                    folderLiset.add(subsString);
                }

                tempVideoFiles.add(videoFiles);
            }
            cursor.close();
        }

        return tempVideoFiles;
    }
    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.back_them, this.getTheme()));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.back_them));
        }
    }


    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }
        void addFragement(Fragment fragment ,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}