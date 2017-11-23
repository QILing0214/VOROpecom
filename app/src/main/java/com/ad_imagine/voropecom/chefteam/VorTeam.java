package com.ad_imagine.voropecom.chefteam;




        import android.app.ListFragment;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.app.Fragment;
        import android.preference.PreferenceManager;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import android.widget.AdapterView;
        import android.widget.GridView;
        import android.widget.ListView;


        import java.text.DecimalFormat;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Random;


        import com.ad_imagine.voropecom.R;
        import com.ad_imagine.voropecom.fragment_coach.coache_historique;

/**
 * coach_accueil.java show a listview
 * for listview, also use
 * coach_accueil_adapter.java    coach_accueil_Item.java
 * coach_accueil_listview.xml    fragment_coach_accueil.xml
 */
public class VorTeam extends Fragment {

    private String string_token;
    private String string_json_teamid;

    private List<VorTeam_item> listSliding=new ArrayList<VorTeam_item>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.vorteam_main, container, false);
        // read SharedPreferences data(token)
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String token = sharedPre.getString("token", "");
        string_token=token;

        string_json_teamid=sharedPre.getString("string_json_team", "");
        System.out.println("Vorteam sharepre token : " + token);

        System.out.println("Vorteam sharepre json: " + string_json_teamid);

        //Init component
        GridView listViewSliding= (GridView)rootView.findViewById(R.id.grid);

        //Add 10 item for list
        for(int i = 0;i<10;i++) {
            //Random  0--9
            float f = new Random().nextFloat() * 9;
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(2);
            df.setDecimalSeparatorAlwaysShown(true);
            //add item

            listSliding.add(new VorTeam_item( ""+Float.parseFloat(df.format(f).replace(',','.')),"jean ne va pas bien"));

        }



        VorTeam_adapter adapter = new VorTeam_adapter(getActivity(), R.layout.vorteam_listviewcontent,listSliding);

        listViewSliding.setAdapter(adapter);
        //Hanlde on item click
        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), coache_historique.class);
                intent.putExtra("extra_data",listSliding.get(position).getTitle().toString());
                startActivity(intent);

            }
        });

        return rootView;
    }


}
