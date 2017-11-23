package com.ad_imagine.voropecom.fragment_coach;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.ad_imagine.voropecom.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * this activity show a view average note and listview for history
 * for listview, also use
 * coache_historique_adapter.java    coache_historique_item.java
 * coache_historique.xml    coache_listview.xml
 */

public class coache_historique extends AppCompatActivity  implements
        View.OnClickListener {
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private String string_token;
    private String string_json_userid;
    private List<coache_historique_item> listSliding=new ArrayList<>();
    Button Bdebut, Bfin;
    private int mYear, mMonth, mDay, mYear2, mMonth2, mDay2,mYear_debut,mDay_debut,mMonth_debut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coache_historique);
        // read SharedPreferences data(token)
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPre.getString("token", "");

        string_token=token;
        string_json_userid=sharedPre.getString("string_json_userid", "");
        System.out.println("Historique-token : " + token);
        System.out.println("Historique  json: " + string_json_userid);

        //action bar set return button and background and title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.wallpaper_toolbar));
        actionBar.setTitle("VOR mes coaches");

        //Init component
        ListView listViewSliding= (ListView) findViewById(R.id.listview2);
        //Add 13 item for list
        float sum=0;
        for(int i = 0;i<13;i++) {
            //Random  0--9
            float f = new Random().nextFloat() * 9;
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(2);
            df.setDecimalSeparatorAlwaysShown(true);

            sum=sum+f;
            //add item

            listSliding.add(new coache_historique_item(R.drawable.angle_arrow_pointing_to_right, ""+i, ""+Float.parseFloat(df.format(f).replace(',','.'))));



        }
        //Moyen note et text
        float moyennote=sum/13;
        TextView Notemoyen=(TextView)findViewById(R.id.txt_coache_note);
        TextView comment=(TextView)findViewById(R.id.txt_coache_comment);
        DecimalFormat df2=new DecimalFormat();
        df2.setMaximumFractionDigits(2);
        df2.setMinimumFractionDigits(2);
        Notemoyen.setText(""+Float.parseFloat(df2.format(moyennote).replace(',','.')));

        if(Float.valueOf(Notemoyen.getText().toString())<=3){
            Notemoyen.setBackgroundColor(Color.parseColor("#cc40b83d"));
            comment.setText("très détendu");
        }
        if((Float.valueOf(Notemoyen.getText().toString())<=6)&&(Float.valueOf(Notemoyen.getText().toString())>3)){
            Notemoyen.setBackgroundColor(Color.parseColor("#ccf57f20"));
            comment.setText("tendu");
        }
        if(Float.valueOf(Notemoyen.getText().toString())>6){
            Notemoyen.setBackgroundColor(Color.parseColor("#cced282b"));
            comment.setText("très tendu");
        }


        Bdebut=(Button)findViewById(R.id.but_coache_debut);
        Bfin=(Button)findViewById(R.id.but_coache_fin);
        //set default date (now-1week,now)
        final Calendar c = Calendar.getInstance();//now
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DAY_OF_YEAR, -7);//last week
        mYear_debut = c.get(Calendar.YEAR);
        mMonth_debut = c.get(Calendar.MONTH);
        mDay_debut = c.get(Calendar.DAY_OF_MONTH);
        Bfin.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        Bdebut.setText(mDay_debut + "/" + (mMonth_debut + 1) + "/" + mYear_debut);

        //choose date

        Bdebut.setOnClickListener(this);
        Bfin.setOnClickListener(this);

        coache_historique_adapter adapter = new coache_historique_adapter(coache_historique.this, R.layout.coache_listview,listSliding);

        listViewSliding.setAdapter(adapter);

        //Hanlde on listview item click
        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(coache_historique.this, twoNote.class);
                intent.putExtra("extra_data", listSliding.get(position).getTitle2().toString());
                startActivity(intent);

            }
        });
    }
    @Override
    public void onClick(View v) {

        if (v == Bdebut) {




            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            Bdebut.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    },mYear_debut, mMonth_debut, mDay_debut);
            // min date 1/3/2016
            Calendar c1 = Calendar.getInstance();
            c1.set(2016, 2, 1);//Year,Mounth -1,Day
            datePickerDialog.getDatePicker().setMinDate(c1.getTimeInMillis());
            System.out.println("mindate:" + c1.getTimeInMillis());
            //max date from button_fin
            String strThatDay = Bfin.getText().toString();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date d = null;
            try {
                d = formatter.parse(strThatDay);//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Calendar fin = Calendar.getInstance();
            fin.setTime(d);


            datePickerDialog.getDatePicker().setMaxDate(fin.getTimeInMillis());
            datePickerDialog.show();
        }
        if (v == Bfin) {


            final Calendar c = Calendar.getInstance();
            mYear2 = c.get(Calendar.YEAR);
            mMonth2 = c.get(Calendar.MONTH);
            mDay2 = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            Bfin.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear2, mMonth2, mDay2);
            // max date now
            Calendar c2 = Calendar.getInstance();

            datePickerDialog.getDatePicker().setMaxDate(c2.getTimeInMillis());
            //min date from button_debut
            String strThatDay = Bdebut.getText().toString();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date d2 = null;
            try {
                d2 = formatter.parse(strThatDay);//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Calendar debut = Calendar.getInstance();
            debut.setTime(d2);
            datePickerDialog.getDatePicker().setMinDate(debut.getTimeInMillis());
            datePickerDialog.show();
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
