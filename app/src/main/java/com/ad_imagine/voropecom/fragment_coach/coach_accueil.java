package com.ad_imagine.voropecom.fragment_coach;


import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import com.ad_imagine.voropecom.R;

/**
 * coach_accueil.java show a listview
 * for listview, also use
 * coach_accueil_adapter.java    coach_accueil_Item.java
 * coach_accueil_listview.xml    fragment_coach_accueil.xml
 */
public class coach_accueil extends Fragment {


    private List<coach_accueil_Item> listSliding=new ArrayList<coach_accueil_Item>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_coach_accueil, container, false);


        //Init component
        ListView listViewSliding= (ListView) rootView.findViewById(R.id.listview);

        //Add 10 item for list
        for(int i = 0;i<10;i++) {
            //Random  0--9
            float f = new Random().nextFloat() * 9;
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(2);
            df.setDecimalSeparatorAlwaysShown(true);
            //add item

                listSliding.add(new coach_accueil_Item(R.drawable.arrow, "Jean Voi", "jean ne va pas bien", ""+Float.parseFloat(df.format(f).replace(',','.'))));

            }

//        listSliding.add(new coach_accueil_Item(R.drawable.arrow, "Jean Voi","jean ne va pas bien","2"));
//        listSliding.add(new coach_accueil_Item(R.drawable.arrow, "Jean Voi","jean ne va pas bien","3"));
//        listSliding.add(new coach_accueil_Item(R.drawable.arrow, "Jean Voi","jean ne va pas bien","4"));
//        listSliding.add(new coach_accueil_Item(R.drawable.arrow, "Jean Voi","jean ne va pas bien","5"));
//        listSliding.add(new coach_accueil_Item(R.drawable.arrow, "Jean Voi","jean ne va pas bien","6"));
//        listSliding.add(new coach_accueil_Item(R.drawable.arrow, "Jean Voi","jean ne va pas bien","7"));
//        listSliding.add(new coach_accueil_Item(R.drawable.arrow, "Jean Voi","jean ne va pas bien","8"));
//        listSliding.add(new coach_accueil_Item(R.drawable.arrow, "Jean Voi","jean ne va pas bien","9"));

        coach_accueil_adapter adapter = new coach_accueil_adapter(getActivity(), R.layout.coach_accueil_listview,listSliding);

        listViewSliding.setAdapter(adapter);
        //Hanlde on item click
        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), twoNote.class);
                intent.putExtra("extra_data",listSliding.get(position).getTitle3().toString());
                startActivity(intent);

            }
        });

        return rootView;
    }


}
