package com.kali.xvideoplayer.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kali.xvideoplayer.R;
import com.kali.xvideoplayer.adapter.FolderAdapter;

import static com.kali.xvideoplayer.activity.MainActivity.folderLiset;
import static com.kali.xvideoplayer.activity.MainActivity.videoFileList;


public class Folder extends Fragment {


    FolderAdapter folderAdapter;
    RecyclerView recyclerView;
    public Folder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_folder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycler_view_for_folder);
        if (folderLiset!=null && folderLiset.size()>0 && videoFileList!=null){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
            folderAdapter=new FolderAdapter(getContext(),videoFileList,folderLiset);

            recyclerView.setAdapter(folderAdapter);
        }

    }
}