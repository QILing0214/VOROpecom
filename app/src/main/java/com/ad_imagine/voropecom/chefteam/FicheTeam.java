package com.ad_imagine.voropecom.chefteam;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ad_imagine.voropecom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FicheTeam extends Fragment {


    public FicheTeam() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.team_ficheteam, container, false);
        return rootView;
    }


}
