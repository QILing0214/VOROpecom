package com.ad_imagine.voropecom.fragment_coach;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ad_imagine.voropecom.R;

import java.util.ArrayList;

//two note means note of emotion and process

public class twoNote extends AppCompatActivity  {
    View section1, section2, section3,section1_, section2_, section3_;
    ImageView imageview1,imageview2,imageview3,imageview4,imageview5,imageview6;
    ImageButton imagebutton_avant,imagebutton_now,imagebutton_apres;
    TextView nomtemps;
    int i;//i=1,2,3 check what button icon is clicked
    ArrayList<TextView> tv_domain = new ArrayList<TextView>();
    ArrayList<TextView> tv_facteur = new ArrayList<TextView>();
    ArrayList<TextView> tv_domainnote = new ArrayList<TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_note);

        /***change domain and facteur text color***/
        tv_domain.add((TextView) findViewById(R.id.txt_emotion_domain0));
        tv_domain.add((TextView) findViewById(R.id.txt_emotion_domain1));
        tv_domain.add((TextView) findViewById(R.id.txt_emotion_domain2));
        tv_domain.add((TextView) findViewById(R.id.txt_process_domain0));
        tv_domain.add((TextView) findViewById(R.id.txt_process_domain1));
        tv_domain.add((TextView) findViewById(R.id.txt_process_domain2));
        tv_domainnote.add((TextView) findViewById(R.id.txt_emotion_domain0_note));
        tv_domainnote.add((TextView) findViewById(R.id.txt_emotion_domain1_note));
        tv_domainnote.add((TextView) findViewById(R.id.txt_emotion_domain2_note));
        tv_domainnote.add((TextView) findViewById(R.id.txt_process_domain0_note));
        tv_domainnote.add((TextView) findViewById(R.id.txt_process_domain1_note));
        tv_domainnote.add((TextView) findViewById(R.id.txt_process_domain2_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_emotion_domain0_fac1_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_emotion_domain0_fac2_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_emotion_domain0_fac3_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_emotion_domain1_fac1_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_emotion_domain1_fac2_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_emotion_domain1_fac3_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_emotion_domain2_fac1_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_emotion_domain2_fac2_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_emotion_domain2_fac3_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_process_domain0_fac1_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_process_domain0_fac2_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_process_domain0_fac3_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_process_domain1_fac1_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_process_domain1_fac2_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_process_domain1_fac3_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_process_domain2_fac1_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_process_domain2_fac2_note));
        tv_facteur.add((TextView) findViewById(R.id.txt_process_domain2_fac3_note));
        nomtemps=(TextView)findViewById(R.id.txt_nom);
        for(int i = 0;i<6;i++)
        {
            //change color of domain
            if (Float.valueOf(tv_domainnote.get(i).getText().toString()) <= 3) {
                tv_domain.get(i).setBackgroundColor(Color.parseColor("#40b83d"));
            }
            if ((Float.valueOf(tv_domainnote.get(i).getText().toString()) <=6) && (Float.valueOf(tv_domainnote.get(i).getText().toString()) > 3)) {
                tv_domain.get(i).setBackgroundColor(Color.parseColor("#f57f20"));
            }
            if (Float.valueOf(tv_domainnote.get(i).getText().toString()) > 6) {
                tv_domain.get(i).setBackgroundColor(Color.parseColor("#ed282b"));
            }

        }
        for(int i = 0;i<18;i++)
        {
            //change color of facteur
            if (Float.valueOf(tv_facteur.get(i).getText().toString()) <= 3) {
                tv_facteur.get(i).setBackgroundResource(R.drawable.greennumber);
            }
            if ((Float.valueOf(tv_facteur.get(i).getText().toString()) <=6) && (Float.valueOf(tv_facteur.get(i).getText().toString()) > 3)) {
                tv_facteur.get(i).setBackgroundResource(R.drawable.orangenumber);
            }
            if (Float.valueOf(tv_facteur.get(i).getText().toString()) > 6) {
                tv_facteur.get(i).setBackgroundResource(R.drawable.rednumber);;
            }

        }
        //###get note from historique and change note note color and comment###
        Intent intent = getIntent();
        String data = intent.getStringExtra("extra_data");

        TextView note=(TextView)findViewById(R.id.txt_moyennote);
        note.setText(data);
        TextView comment=(TextView)findViewById(R.id.txt_comment);

        if(Float.valueOf(note.getText().toString())<=3){
            note.setBackgroundResource(R.drawable.coach_coache_raduis_green);;
            comment.setText("très détendu");
        }
        if((Float.valueOf(note.getText().toString())<=6)&&(Float.valueOf(note.getText().toString())>3)){
            note.setBackgroundResource(R.drawable.coach_coache_raduis_orange);
            comment.setText("tendu");
        }
        if(Float.valueOf(note.getText().toString())>6){
            note.setBackgroundResource(R.drawable.coach_coache_raduis_red);
            comment.setText("très tendu");
        }

        //action bar set return button and background and title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.wallpaper_toolbar));
        actionBar.setTitle("Accueil");

       //coordion
        section1=findViewById(R.id.relativeLayout_emotion_domain0);
        section2=findViewById(R.id.relativeLayout_emotion_domain1);
        section3=findViewById(R.id.relativeLayout_emotion_domain2);

        imageview1=(ImageView)findViewById(R.id.imageButton_emotion_domain0);
        imageview2=(ImageView)findViewById(R.id.imageButton_emotion_domain1);
        imageview3=(ImageView)findViewById(R.id.imageButton_emotion_domain2);
        imageview4=(ImageView)findViewById(R.id.imageButton_process_domain0);
        imageview5=(ImageView)findViewById(R.id.imageButton_process_domain1);
        imageview6=(ImageView)findViewById(R.id.imageButton_process_domain2);

        imagebutton_avant=(ImageButton)findViewById(R.id.imageButton_avant);
        imagebutton_now=(ImageButton)findViewById(R.id.imageButton_now);
        imagebutton_apres=(ImageButton)findViewById(R.id.imageButton_apres);

//        t1=(TextView)findViewById(R.id.txt_emotion_domain0);
//        t2=(TextView)findViewById(R.id.txt_emotion_domain1);
//        t3=(TextView)findViewById(R.id.txt_emotion_domain2);
        section1.setVisibility(View.GONE);
        section2.setVisibility(View.GONE);
        section3.setVisibility(View.GONE);
        tv_domain.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (section1.getVisibility() == View.GONE) {
                    section1.setVisibility(View.VISIBLE);
                    imageview1.setImageResource(R.drawable.angle_arrow_pointing_down);
                } else {
                    section1.setVisibility(View.GONE);
                    imageview1.setImageResource(R.drawable.angle_arrow_pointing_to_right_small);
                }
            }
        });
        tv_domain.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (section2.getVisibility() == View.GONE) {
                    section2.setVisibility(View.VISIBLE);
                    imageview2.setImageResource(R.drawable.angle_arrow_pointing_down);
                } else {
                    section2.setVisibility(View.GONE);
                    imageview2.setImageResource(R.drawable.angle_arrow_pointing_to_right_small);
                }
            }
        });
        tv_domain.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (section3.getVisibility() == View.GONE) {
                    section3.setVisibility(View.VISIBLE);
                    imageview3.setImageResource(R.drawable.angle_arrow_pointing_down);
                } else {
                    section3.setVisibility(View.GONE);
                    imageview3.setImageResource(R.drawable.angle_arrow_pointing_to_right_small);
                }
            }
        });
        section1_=findViewById(R.id.relativeLayout_process_domain0);
        section2_=findViewById(R.id.relativeLayout_process_domain1);
        section3_=findViewById(R.id.relativeLayout_process_domain2);

//        t1_=(TextView)findViewById(R.id.txt_process_domain0);
//        t2_=(TextView)findViewById(R.id.txt_process_domain1);
//        t3_=(TextView)findViewById(R.id.txt_process_domain2);
        section1_.setVisibility(View.GONE);
        section2_.setVisibility(View.GONE);
        section3_.setVisibility(View.GONE);
        tv_domain.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (section1_.getVisibility() == View.GONE) {
                    section1_.setVisibility(View.VISIBLE);
                    imageview4.setImageResource(R.drawable.angle_arrow_pointing_down);
                } else {
                    section1_.setVisibility(View.GONE);
                    imageview4.setImageResource(R.drawable.angle_arrow_pointing_to_right_small);
                }
            }
        });
        tv_domain.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (section2_.getVisibility() == View.GONE) {
                    section2_.setVisibility(View.VISIBLE);
                    imageview5.setImageResource(R.drawable.angle_arrow_pointing_down);
                } else {
                    section2_.setVisibility(View.GONE);
                    imageview5.setImageResource(R.drawable.angle_arrow_pointing_to_right_small);
                }
            }
        });
        tv_domain.get(5).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (section3_.getVisibility() == View.GONE)
                {
                    section3_.setVisibility(View.VISIBLE);
                    imageview6.setImageResource(R.drawable.angle_arrow_pointing_down);
                }
                else
                {
                    section3_.setVisibility(View.GONE);
                    imageview6.setImageResource(R.drawable.angle_arrow_pointing_to_right_small);
                }
            }
        });



        /****choose time*******/
        //avant icon click--> dialog
        imagebutton_avant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1;
                dialog_avant();
            }
        });
        //now icon click-->dialog
        imagebutton_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 2;
                dialog_evenementexistant();
            }
        });
        //apres icon click-->dialog
        imagebutton_apres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 3;
                dialog_evenementexistant();
            }
        });
    }//onCreate end

    private void dialog_avant(){
        final Dialog d1 = new Dialog(this);


        d1.setContentView(R.layout.dialog1);
        d1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv1 = (TextView) d1.findViewById(R.id.tv_exis);
        TextView tv2 = (TextView) d1.findViewById(R.id.tv_newele);
        TextView tv3 = (TextView) d1.findViewById(R.id.tv_annu);

        d1.setCancelable(false);
        d1.show();
        tv1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                d1.dismiss();
                dialog_evenementexistant();


            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                d1.dismiss();
                dialog_nouvelevenement();


            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                d1.dismiss();



            }
        });
    }

    private void dialog_evenementexistant(){
        final Dialog d = new Dialog(this);


        d.setContentView(R.layout.name_picker);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button valider = (Button) d.findViewById(R.id.btn_valider);
        Button annuler= (Button) d.findViewById(R.id.btn_annuler);

        d.setCancelable(false);





        final NumberPicker namePick = (NumberPicker) d
                .findViewById(R.id.inchpicker);
        final String[] city = {"n1","n2","n3","n4","n5","n6"};
        namePick.setDisplayedValues(city);
        namePick.setMinValue(0);
        namePick.setMaxValue(city.length - 1);
        namePick.setValue(0);


        d.show();




        valider.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                d.dismiss();
                if (i == 1) {
                    imagebutton_avant.setImageResource(R.drawable.arrow_up_white);
                    imagebutton_avant.setBackgroundResource(R.drawable.blackcircle);
                    imagebutton_now.setImageResource(R.drawable.plus);
                    imagebutton_now.setBackgroundResource(R.drawable.whitecircle);
                    imagebutton_apres.setImageResource(R.drawable.arrowdown);
                    imagebutton_apres.setBackgroundResource(R.drawable.whitecircle);
                }
                if (i == 2) {
                    imagebutton_now.setImageResource(R.drawable.plus_white);
                    imagebutton_now.setBackgroundResource(R.drawable.blackcircle);
                    imagebutton_avant.setImageResource(R.drawable.arrowup);
                    imagebutton_avant.setBackgroundResource(R.drawable.whitecircle);
                    imagebutton_apres.setImageResource(R.drawable.arrowdown);
                    imagebutton_apres.setBackgroundResource(R.drawable.whitecircle);
                }
                if (i == 3) {
                    imagebutton_apres.setImageResource(R.drawable.arrow_down_white);
                    imagebutton_apres.setBackgroundResource(R.drawable.blackcircle);
                    imagebutton_now.setImageResource(R.drawable.plus);
                    imagebutton_now.setBackgroundResource(R.drawable.whitecircle);
                    imagebutton_avant.setImageResource(R.drawable.arrowup);
                    imagebutton_avant.setBackgroundResource(R.drawable.whitecircle);
                }
                String name = city[namePick.getValue()];

                nomtemps.setText(name);


            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                d.dismiss();




            }
        });
    }

    private void dialog_nouvelevenement(){
        final Dialog d2 = new Dialog(this);


        d2.setContentView(R.layout.dialog2);

        d2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv_annuler = (TextView) d2.findViewById(R.id.tv_annuler);
        TextView tv_valider= (TextView) d2.findViewById(R.id.tv_valider);
        final EditText edittext=(EditText)d2.findViewById(R.id.editText9);
        d2.setCancelable(false);
        d2.show();

//        final EditText edittext = new EditText(this);
//        alert.setMessage("Saisir un nouveau nom d'événement");
//        alert.setTitle("Nouvel événement");
//
//        alert.setView(edittext);

        tv_valider.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String newname = edittext.getText().toString();
                nomtemps.setText(newname);
                imagebutton_avant.setImageResource(R.drawable.arrow_up_white);
                imagebutton_avant.setBackgroundResource(R.drawable.blackcircle);
                imagebutton_now.setImageResource(R.drawable.plus);
                imagebutton_now.setBackgroundResource(R.drawable.whitecircle);
                imagebutton_apres.setImageResource(R.drawable.arrowdown);
                imagebutton_apres.setBackgroundResource(R.drawable.whitecircle);
                d2.dismiss();
            }
        });

        tv_annuler.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                d2.dismiss();
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}

