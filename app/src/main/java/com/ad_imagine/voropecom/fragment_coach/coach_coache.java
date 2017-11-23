package com.ad_imagine.voropecom.fragment_coach;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ad_imagine.voropecom.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * add relativelayout of each coache to fragment
 * use coache_example.xml  as relativelayout.
 */
public class coach_coache extends Fragment implements
        View.OnClickListener {

    Button Bdebut, Bfin;
    private int mYear, mMonth, mDay, mYear2, mMonth2, mDay2,mYear_debut,mDay_debut,mMonth_debut;

    TextView text_note;


    public coach_coache() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_coach_coache, container, false);

        //***choose date***

        Bdebut=(Button)rootView.findViewById(R.id.coache_button_debut);
        Bfin=(Button)rootView.findViewById(R.id.coache_button_fin);


        /***add view similair for coache***/
//   example to add view:
//        RelativeLayout relativeLayout1=new RelativeLayout(getActivity());
//        TextView mTextView = new TextView(getActivity());
//        mTextView.setText("3.0");
//        mTextView.setBackgroundColor(Color.parseColor("#25ac39"));
//        mTextView.setTextColor(Color.parseColor("#ffffff"));
//        relativeLayout1.addView(mTextView);


        RelativeLayout relativeLayout=(RelativeLayout)rootView.findViewById(R.id.relativeLayout);

        ArrayList<RelativeLayout.LayoutParams > LP=new ArrayList<>();
        ArrayList<View > child=new ArrayList<>();  // each child can only be added one time
        //View child = getActivity().getLayoutInflater().inflate(R.layout.coache_example, null);
        //RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        float sum=0;


        // use "for" to add 10 viewa
        for(int i = 0;i<10;i++) {
           //change note and color of coache_example
           float f = new Random().nextFloat() * 9;
            sum=sum+f;
           DecimalFormat df=new DecimalFormat();
           df.setMaximumFractionDigits(2);
           df.setMinimumFractionDigits(2);
           df.setDecimalSeparatorAlwaysShown(true);
           /******important****/
           View view2 = getActivity().getLayoutInflater().inflate(R.layout.coache_example, null) ;
           text_note=(TextView)view2.findViewById(R.id.coache_0);
           TextView text_number=(TextView)view2.findViewById(R.id.coache0);
          /******important******/
           text_note.setText(""+Float.parseFloat(df.format(f).replace(',', '.')));
            text_number.setText("Coache"+i);
           if (f<= 3) {
             text_note.setBackgroundResource(R.drawable.coach_coache_raduis_green);
           }
           if (f<=6 && f > 3) {
               text_note.setBackgroundResource(R.drawable.coach_coache_raduis_orange);
           }
           if (f > 6) {
                text_note.setBackgroundResource(R.drawable.coach_coache_raduis_red);
           }

            //click listenner
            text_note.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {


                    Intent intent = new Intent(getActivity(), coache_historique.class);


                    startActivity(intent);

                }
            });
         //set parametre to add coache view

           child.add( view2);
           LP.add(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
           if(i%2==0) {
               LP.get(i).leftMargin = 25 * (int) (Math.round(getActivity().getResources().getDisplayMetrics().density));//change 25dp to px and change float to int
           }
           else{
               LP.get(i).leftMargin = 200 * (int) (Math.round(getActivity().getResources().getDisplayMetrics().density));
           }
            LP.get(i).topMargin = (200+150*(i/2)) * (int) (Math.round(getActivity().getResources().getDisplayMetrics().density));
            relativeLayout.addView(child.get(i), LP.get(i));

       }   // "for" end

        // moyon note  and  color
        float moyennote=sum/10;
        TextView Notemoyen=(TextView)rootView.findViewById(R.id.coache_note);
        TextView comment=(TextView)rootView.findViewById(R.id.coache_coaching);
        DecimalFormat df2=new DecimalFormat();
        df2.setMaximumFractionDigits(1);
        df2.setMinimumFractionDigits(1);
        Notemoyen.setText(""+Float.parseFloat(df2.format(moyennote).replace(',', '.')));

        if(Float.valueOf(Notemoyen.getText().toString())<=3){
            Notemoyen.setBackgroundColor(Color.parseColor("#cc40b83d"));

        }
        if((Float.valueOf(Notemoyen.getText().toString())<=6)&&(Float.valueOf(Notemoyen.getText().toString())>3)){
            Notemoyen.setBackgroundColor(Color.parseColor("#ccf57f20"));

        }
        if(Float.valueOf(Notemoyen.getText().toString())>6){
            Notemoyen.setBackgroundColor(Color.parseColor("#cced282b"));

        }

/*********date************/

//set default date (now-1week,now)
        final Calendar c = Calendar.getInstance();//now
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DAY_OF_YEAR, -7);//last week
        mYear_debut = c.get(Calendar.YEAR);
        mMonth_debut = c.get(Calendar.MONTH);
        mDay_debut = c.get(Calendar.DAY_OF_MONTH);
        Bfin.setText(mDay+ "/" + (mMonth + 1) + "/" + mYear);
        Bdebut.setText(mDay_debut + "/" + (mMonth_debut + 1) + "/" + mYear_debut);
        Bdebut.setOnClickListener(this);
        Bfin.setOnClickListener(this);

        return rootView;
    }
// date pick listening
    public void onClick(View v) {

        if (v == Bdebut) {




            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            Bdebut.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear_debut, mMonth_debut, mDay_debut);
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

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
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
}
