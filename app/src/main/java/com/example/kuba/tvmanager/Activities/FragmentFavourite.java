package com.example.kuba.tvmanager.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kuba.tvmanager.R;

/**
 * Created by Kuba on 12/16/2017.
 */

public class FragmentFavourite extends Fragment {
    private static final String TAG = "FragmentFavourite";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_favourite_list, container, false);

        return view;
    }
}
