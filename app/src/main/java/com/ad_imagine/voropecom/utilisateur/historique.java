package com.ad_imagine.voropecom.utilisateur;

/**
 * Created by chu on 2016/6/7.
 * json data example
 * [{"id":58,"nom":"Professionnel","is_process":false,"added_at":"07\/07\/2016 11:56","updated_at":"07\/07\/2016 11:56","type":"Pendant","moyenne":5.3},{"id":50,"nom":"Professionnel","is_process":false,"added_at":"06\/07\/2016 15:15","updated_at":"06\/07\/2016 15:15","type":"Pendant","moyenne":3.59},{"id":48,"nom":"Professionnel","is_process":false,"added_at":"06\/07\/2016 14:59","updated_at":"06\/07\/2016 14:59","type":"Descendant","moyenne":7.67},{"id":47,"nom":"Professionnel","is_process":false,"added_at":"06\/07\/2016 14:58","updated_at":"06\/07\/2016 14:58","type":"Pendant","moyenne":2.52},{"id":46,"nom":"Professionnel","is_process":false,"added_at":"06\/07\/2016 14:57","updated_at":"06\/07\/2016 14:57","type":"Montant","moyenne":3.77},{"id":45,"nom":"Professionnel","is_process":false,"added_at":"06\/07\/2016 14:53","updated_at":"06\/07\/2016 14:53","type":"Pendant","moyenne":5.32},{"id":44,"nom":"Professionnel","is_process":false,"added_at":"06\/07\/2016 12:40","updated_at":"06\/07\/2016 12:40","type":"Pendant","moyenne":5.3},{"id":42,"nom":"Professionnel","is_process":false,"added_at":"06\/07\/2016 11:19","updated_at":"06\/07\/2016 11:19","type":"Pendant","moyenne":5.3},{"id":41,"nom":"Professionnel","is_process":false,"added_at":"06\/07\/2016 09:09","updated_at":"06\/07\/2016 09:09","type":"Pendant","moyenne":5.3}]
 */

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ad_imagine.voropecom.Coach;
import com.ad_imagine.voropecom.R;
import com.ad_imagine.voropecom.Utilisateur;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.text.ParseException;

public class historique extends Fragment{

    int start=1;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private String string_token;
    private String string_json;
    private String string_login;
    private String string_password;

    View rootView;
    LinearLayout linearLayout;
    TextView Notemoyen;
    TextView comment;
    TextView tvnumberhistorique;
  //  LineChart lineChart;

    TextView historique_date,historique_note;
    Button Bdebut, Bfin;
    String last_Bdebut,last_Bfin;
    private int mYear, mMonth, mDay, mYear2, mMonth2, mDay2,mYear_debut,mDay_debut,mMonth_debut;

//   // TextView of note0-9 evaluation0-9;
//    ArrayList<TextView> evaluation = new ArrayList<TextView>();
//    ArrayList<TextView> note = new ArrayList<TextView>();


   //colors.add(Color.rgb(0,0,0));
    public static final int[] COLORS = {
            Color.rgb(0,0,0), Color.rgb(0,0,0), Color.rgb(0,0,0),
            Color.rgb(0,0,0), Color.rgb(0,0,0)
    };





    public historique() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.historique, container, false);
        //hide view before data is loaded
    //    rootView.setVisibility(View.GONE);

        Bdebut=(Button)rootView.findViewById(R.id.button12);
        Bfin=(Button)rootView.findViewById(R.id.button13);
        linearLayout=(LinearLayout)rootView.findViewById(R.id.linearlayout_historique);
        Notemoyen=(TextView)rootView.findViewById(R.id.textView7);
        comment=(TextView)rootView.findViewById(R.id.textView61);
        tvnumberhistorique=(TextView)rootView.findViewById(R.id.textView60);
      //  lineChart = (LineChart)rootView. findViewById(R.id.chart);
        // read SharedPreferences data(token)
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String token = sharedPre.getString("token", "");
        string_login=sharedPre.getString("username", "");
        string_password=sharedPre.getString("password", "");
        string_token=token;
        string_json=sharedPre.getString("string_json_his", "");
        System.out.println("Historique-token : " + token);
        // get

         dojson();

      // new GetClass(getActivity(), rootView).execute();
        System.out.println("Historique  json: "+string_json);



        return rootView;
    }



////compare two dates
//
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//
//                Date date_debut = sdf.parse(Bdebut.getText().toString());
//
//                Date date_fin = sdf.parse(Bfin.getText().toString());
//
//                int result = date_debut.compareTo(date_fin);
//                if(result>0){
//                    dialog();
//                }
//
//            }catch (ParseException e){
//                e.printStackTrace();
//            }

    public void dojson(){
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

        //choose date
        Bdebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                last_Bdebut=Bdebut.getText().toString();
                                last_Bfin=Bfin.getText().toString();
                                System.out.println(" last_Bdebut: " +  last_Bdebut);
                                Bdebut.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);//set date for text which is choosed
                                System.out.println(" Bdebut: " +Bdebut.getText().toString());
                                loadviewdata(1);//#################

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
                    d = formatter.parse(strThatDay);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Calendar fin = Calendar.getInstance();
                fin.setTime(d);
                datePickerDialog.getDatePicker().setMaxDate(fin.getTimeInMillis());


                datePickerDialog.show();


            }
        });
        Bfin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear2 = c.get(Calendar.YEAR);
                mMonth2 = c.get(Calendar.MONTH);
                mDay2 = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                last_Bdebut=Bdebut.getText().toString();
                                last_Bfin=Bfin.getText().toString();
                                Bfin.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                loadviewdata(1);//################


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
        });

        loadviewdata(0);//first view
    }
    private void closefragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }
    private void dialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Attention");
        builder.setMessage("Vous n'avez pas saisie de VOR");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                closefragment();
                Intent intent = new Intent(getActivity(), Utilisateur.class);// to page coach
                startActivity(intent);

            }
        });

        builder.create().show();
    }
    private void dialog2(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Attention");
        builder.setMessage("Aucune données sur cette période");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Bdebut.setText(last_Bdebut);
                Bfin.setText(last_Bfin);
                loadviewdata(1);

            }
        });

        builder.create().show();
    }
//    //################GET################
//    private class GetClass extends AsyncTask<String, String, String>
//
//    {   private Context mContext;
//        private View rootView;
//
//
//        ProgressDialog pdLoading = new ProgressDialog(getActivity());
//        HttpURLConnection conn;
//        URL url = null;
//        public GetClass(Context context, View rootView) {
//            this.mContext = context;
//            this.rootView = rootView;
//        }
//        @Override
//        protected void onPreExecute() {
//            System.out.println("Historique-get-onPreExecute : ");
//            super.onPreExecute();
//
//            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading...");
//            pdLoading.setCancelable(false);
//            pdLoading.show();
//
//        }
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                System.out.println("Historique-get-doInBackground : ");
//                // Enter URL address where your php file resides
//                url = new URL( getResources().getString(R.string.url1)+"/api/vors");
//
//            } catch (MalformedURLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                return "exception1";
//            }
//            try {
//                // Setup HttpURLConnection class to send and receive data from php and mysql
//                conn = (HttpURLConnection)url.openConnection();
//                conn.setReadTimeout(READ_TIMEOUT);
//                conn.setConnectTimeout(CONNECTION_TIMEOUT);
//                conn.setRequestMethod("GET");
//                String get_token=string_token;//token
//
//                System.out.println("Historique-token1:"+get_token);
//                get_token=string_token.replace("\"token\":\"","").replace("\"","");
//                System.out.println("Historique-token2:"+get_token);
//
//                conn.setRequestProperty("Authorization", "Bearer " + get_token);// conn.setRequestProperty("Authorization", "Bearer " + get_token);
//
//
//                conn.setDoInput(true);
//
//                conn.connect();
////
//
//
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//                return "exception";
//            }
//
//            String txtResult = "";
//
//            try {
//
//                int response_code = conn.getResponseCode();
//
//                System.out.println("Historique-response_code:"+response_code);//200 or 401
//
//                // Check if successful connection made
//                if (response_code == HttpURLConnection.HTTP_OK) {
//
//
//
//                    // Read data sent from server
//                    InputStream input = conn.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//                    StringBuilder result = new StringBuilder();
//                    String line;
//
//                    while ((line = reader.readLine()) != null) {
//                        result.append(line);
//                        txtResult = result.toString();         //get json
//                        System.out.println("Historique-get-Result: " + txtResult);
//                        string_json=txtResult;
//
//                    }
//
//
//
//                    // Pass data to onPostExecute method
//                    //return(result.toString());
//                    System.out.println("Historique-Result: " + txtResult);//get json
//                    return(result.toString());
//
//                }else if (response_code == 401){//########token invalide#############
//                    //post login password again
//                    postagain();
//
//                    return getagain();//try new token and return json to excute
//
//                }else {
//                    return("unsuccessful 403 404 500...");
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                return "exception";
//            } finally {
//                conn.disconnect();
//            }
//
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            //this method will be running on UI thread
//            //show view
//            rootView.setVisibility(View.VISIBLE);
//
//            pdLoading.dismiss();
//            System.out.println("Historique-onPostExecute-Result: " + result);//get json
//
//            if(result.equalsIgnoreCase(string_json))
//            {
//                /* Here launching another activity when login successful. If you persist login state
//                use sharedPreferences of Android. and logout button to clear sharedPreferences.
//                 */
//                /*********date************/
//
////set default date (now-1week,now)
//                final Calendar c = Calendar.getInstance();//now
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//                c.add(Calendar.DAY_OF_YEAR, -7);//last week
//                mYear_debut = c.get(Calendar.YEAR);
//                mMonth_debut = c.get(Calendar.MONTH);
//                mDay_debut = c.get(Calendar.DAY_OF_MONTH);
//                Bfin.setText(mDay+ "/" + (mMonth + 1) + "/" + mYear);
//                Bdebut.setText(mDay_debut + "/" + (mMonth_debut + 1) + "/" + mYear_debut);
//
//                //choose date
//                Bdebut.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Get Current Date
//
//
//
//                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
//                                new DatePickerDialog.OnDateSetListener() {
//
//                                    @Override
//                                    public void onDateSet(DatePicker view, int year,
//                                                          int monthOfYear, int dayOfMonth) {
//                                        last_Bdebut=Bdebut.getText().toString();
//                                        last_Bfin=Bfin.getText().toString();
//                                        System.out.println(" last_Bdebut: " +  last_Bdebut);
//                                        Bdebut.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);//set date for text which is choosed
//                                        System.out.println(" Bdebut: " +Bdebut.getText().toString());
//                                        loadviewdata(1);//#################
//
//                                    }
//                                }, mYear_debut, mMonth_debut, mDay_debut);
//                        // min date 1/3/2016
//                        Calendar c1 = Calendar.getInstance();
//                        c1.set(2016, 2, 1);//Year,Mounth -1,Day
//                        datePickerDialog.getDatePicker().setMinDate(c1.getTimeInMillis());
//                        System.out.println("mindate:" + c1.getTimeInMillis());
//                        //max date from button_fin
//                        String strThatDay = Bfin.getText().toString();
//                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//                        Date d = null;
//                        try {
//                            d = formatter.parse(strThatDay);
//                        } catch (ParseException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//
//                        Calendar fin = Calendar.getInstance();
//                        fin.setTime(d);
//
//
//                        datePickerDialog.getDatePicker().setMaxDate(fin.getTimeInMillis());
//
//
//                        datePickerDialog.show();
//
//
//                    }
//                });
//                Bfin.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        final Calendar c = Calendar.getInstance();
//                        mYear2 = c.get(Calendar.YEAR);
//                        mMonth2 = c.get(Calendar.MONTH);
//                        mDay2 = c.get(Calendar.DAY_OF_MONTH);
//
//                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
//                                new DatePickerDialog.OnDateSetListener() {
//
//                                    @Override
//                                    public void onDateSet(DatePicker view, int year,
//                                                          int monthOfYear, int dayOfMonth) {
//                                        last_Bdebut=Bdebut.getText().toString();
//                                        last_Bfin=Bfin.getText().toString();
//                                        Bfin.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                                        loadviewdata(1);//################
//
//
//                                    }
//                                }, mYear2, mMonth2, mDay2);
//                        // max date now
//                        Calendar c2 = Calendar.getInstance();
//
//                        datePickerDialog.getDatePicker().setMaxDate(c2.getTimeInMillis());
//                        //min date from button_debut
//                        String strThatDay = Bdebut.getText().toString();
//                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//                        Date d2 = null;
//                        try {
//                            d2 = formatter.parse(strThatDay);//catch exception
//                        } catch (ParseException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//
//                        Calendar debut = Calendar.getInstance();
//                        debut.setTime(d2);
//                        datePickerDialog.getDatePicker().setMinDate(debut.getTimeInMillis());
//
//
//                        datePickerDialog.show();
//
//                    }
//                });
//
//                loadviewdata(0);//first view
//
//
//            }else if (result.equalsIgnoreCase("false")){
//
//                // If username and password does not match display a error message
//                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();
//
//            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
//
//                Toast.makeText(getActivity(), "get wrong", Toast.LENGTH_LONG).show();
//
//            }
//        }
//
//    }

    public void loadviewdata(int t){
        try {

            linearLayout.removeAllViews();


            JSONArray jsonRootArray = new JSONArray(string_json);
            //first view
            if(t==0) {
                //first login non json
                if(jsonRootArray.length()==0){
                    dialog();
                }
                //first_date<date_debut------->change date_fin=date_first
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    Date date_debut = sdf.parse(Bdebut.getText().toString());


                    Date date_first = sdf.parse(jsonRootArray.getJSONObject(0).optString("added_at").toString());
                    int result12 = date_first.compareTo(date_debut);


                    if (result12 < 0) {

                        Calendar c = Calendar.getInstance();
                        c.setTime(date_first);
                        int mYear_first = c.get(Calendar.YEAR);
                        int mMonth_first = c.get(Calendar.MONTH);
                        int mDay_first = c.get(Calendar.DAY_OF_MONTH);
                        Date date_first_debut = new Date(date_first.getTime() - 604800000L);//7 days before
                        c.setTime(date_first_debut);
                        int mYear_first_debut = c.get(Calendar.YEAR);
                        int mMonth_first_debut = c.get(Calendar.MONTH);
                        int mDay_first_debut = c.get(Calendar.DAY_OF_MONTH);
                        Bfin.setText(mDay_first + "/" + (mMonth_first + 1) + "/" + mYear_first);
                        Bdebut.setText(mDay_first_debut + "/" + (mMonth_first_debut + 1) + "/" + mYear_first_debut);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                last_Bdebut=Bdebut.getText().toString();
                last_Bfin=Bfin.getText().toString();

            }
            float sum=0;
            int historiquenumber=0;
            for(int i = 0;i<jsonRootArray.length();i++)
            {
                JSONObject jsonObject = jsonRootArray.getJSONObject(i);
                String json_date = jsonObject.optString("added_at").toString();
                String json_moyenne= jsonObject.optString("moyenne").toString();
                final String json_id= jsonObject.optString("id").toString();

                View view2 = getActivity().getLayoutInflater().inflate(R.layout.historique_example, null);
                historique_date=(TextView)view2.findViewById(R.id.textView64);
                historique_date.setText(json_date);
                historique_note=(TextView)view2.findViewById(R.id.textView65);
                historique_note.setText(String.format("%.2f", Float.valueOf(json_moyenne)).replace(',','.'));
                LinearLayout.LayoutParams LP1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LP1.leftMargin =1 * (int) (Math.round(getActivity().getResources().getDisplayMetrics().density));
                LP1.topMargin = 4 * (int) (Math.round(getActivity().getResources().getDisplayMetrics().density));

                //compare date:  date_debut<=date_historique<=date_fin

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    Date date_debut = sdf.parse(Bdebut.getText().toString());

                    Date date_fin = sdf.parse(Bfin.getText().toString());

                    Date date_historique=sdf.parse(json_date);
                    int result1 = date_historique.compareTo(date_debut);
                    int result2 = date_fin.compareTo(date_historique);

                    if((result1>=0)&&(result2>=0)){
                        linearLayout.addView(view2, LP1);
                        sum=sum+Float.valueOf(json_moyenne);
                        historiquenumber++;
                        //change color
                        if(Float.valueOf(json_moyenne)<=3){
                            historique_date.setBackgroundColor(Color.parseColor("#40b83d"));

                        }
                        if((Float.valueOf(json_moyenne)<=6)&&(Float.valueOf(json_moyenne)>3)){
                            historique_date.setBackgroundColor(Color.parseColor("#f57f20"));

                        }
                        if(Float.valueOf(json_moyenne)>6){
                            historique_date.setBackgroundColor(Color.parseColor("#ed282b"));

                        }
                    }

                }catch (ParseException e){
                    e.printStackTrace();
                }







                historique_date.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {

                        //##### GET json_note#############
                        new GetClassNote().execute(json_id);


                        //###send data note to note.class###
                        // String snote=historique_note.getText().toString();



                    }
                });
            }
//not choose data    end create view
            if(historiquenumber==0){
                dialog2();
                return;
            }
//Moyen note et text
            float moyennote=sum/historiquenumber;
            tvnumberhistorique.setText("Nombre d'évaluations:"+historiquenumber);

            DecimalFormat df2=new DecimalFormat();
            df2.setMaximumFractionDigits(2);
            df2.setMinimumFractionDigits(2);
            Notemoyen.setText(""+Float.parseFloat(df2.format(moyennote).replace(',', '.')));//####important#### change virgule
            System.out.println("la valeur generer3:" +df2.format(moyennote));

            if(Float.valueOf(Notemoyen.getText().toString())<=3){
                Notemoyen.setBackgroundResource(R.drawable.coach_coache_raduis_green);
                if(Float.valueOf(Notemoyen.getText().toString())<2) {
                    comment.setText("Très détendu");
                }else {
                    comment.setText("Détendu");
                }
            }
            if((Float.valueOf(Notemoyen.getText().toString())<=6)&&(Float.valueOf(Notemoyen.getText().toString())>3)){
                Notemoyen.setBackgroundResource(R.drawable.coach_coache_raduis_orange);
                if(Float.valueOf(Notemoyen.getText().toString())<4) {
                    comment.setText("Légèrement tendu");
                }else {
                    comment.setText("Assez tendu");
                }
            }
            if(Float.valueOf(Notemoyen.getText().toString())>6){
                Notemoyen.setBackgroundResource(R.drawable.coach_coache_raduis_red);
                if(Float.valueOf(Notemoyen.getText().toString())<7) {
                    comment.setText("Tendu");
                }else if(Float.valueOf(Notemoyen.getText().toString())<8){
                    comment.setText("Très tendu");
                } else {
                    comment.setText("Extrêmement tendu");
                }
            }
            /*******insert graph chart data********/

            LineChart lineChart = (LineChart)rootView.findViewById(R.id.chart);
            lineChart.removeAllViews();//##############important
            System.out.println("Historique-chart");
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<Integer> colors=new ArrayList<>();// set circle colors
            int chartitemnumber=0;
            for(int i = 0;i<jsonRootArray.length();i++) {

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    Date date_debut = sdf.parse(Bdebut.getText().toString());

                    Date date_fin = sdf.parse(Bfin.getText().toString());

                    Date date_historique=sdf.parse(jsonRootArray.getJSONObject(jsonRootArray.length()-1-i).optString("added_at").toString());//before-->now
                    int result1 = date_historique.compareTo(date_debut);
                    int result2 = date_fin.compareTo(date_historique);

                    if((result1>=0)&&(result2>=0)){
                        entries.add(new Entry(Float.parseFloat(jsonRootArray.getJSONObject(jsonRootArray.length()-1-i).optString("moyenne").toString()), chartitemnumber));

                        labels.add("v"+chartitemnumber);
                        chartitemnumber++;
                        //add different colors point for chart
                        if(Float.valueOf(jsonRootArray.getJSONObject(jsonRootArray.length()-1-i).optString("moyenne").toString())<=3){

                            colors.add(Color.rgb(37,172, 57));//add different colors point for chart
                        }
                        if((Float.valueOf(jsonRootArray.getJSONObject(jsonRootArray.length()-1-i).optString("moyenne").toString())<=6)&&(Float.valueOf(jsonRootArray.getJSONObject(jsonRootArray.length()-1-i).optString("moyenne").toString())>3)){

                            colors.add(Color.rgb(245, 127, 32));
                        }
                        if(Float.valueOf(jsonRootArray.getJSONObject(jsonRootArray.length()-1-i).optString("moyenne").toString())>6){

                            colors.add(Color.rgb(237, 40, 43));
                        }
                    }

                }catch (ParseException e){
                    e.printStackTrace();
                }


            }
            LineDataSet dataset = new LineDataSet(entries, "");
            LineData data = new LineData(labels, dataset);
            lineChart.setData(data); // set the data and list of lables into chart
            lineChart.setDescription("");  // set the description
            dataset.setDrawCubic(true);
            dataset.setCircleRadius(8);
            dataset.setDrawCircleHole(false);
            dataset.setCircleColors(colors);
            dataset.setColors(COLORS);//black line
            //set non legend
            Legend l = lineChart.getLegend();
            l.setEnabled(false);
            System.out.println("Historique-chart-end");
/*********end chart************/
//                //add chart2
//                    View view3 = getActivity().getLayoutInflater().inflate(R.layout.chart, null);
//                    LineChart lineChart2 = (LineChart)view3.findViewById(R.id.chart2);
//                    ArrayList<Entry> entries2 = new ArrayList<>();
//                    entries2.add(new Entry(0, 0));
//                    entries2.add(new Entry(1, 1));
//                    entries2.add(new Entry(2, 2));
//                    entries2.add(new Entry(3, 3));
//                    entries2.add(new Entry(4, 4));
//                    entries2.add(new Entry(5, 5));
//                    entries2.add(new Entry(6, 6));
//                    entries2.add(new Entry(7, 7));
//                    entries2.add(new Entry(8, 8));
//                    entries2.add(new Entry(9, 9));
//                    LineDataSet dataset2 = new LineDataSet(entries2, "");
//                    ArrayList<String> labels2 = new ArrayList<String>();
//                    labels2.add("v0");
//                    labels2.add("v1");
//                    labels2.add("v2");
//                    labels2.add("v3");
//                    labels2.add("v4");
//                    labels2.add("v5");
//                    labels2.add("v6");
//                    labels2.add("v7");
//                    labels2.add("v8");
//                    labels2.add("v9");
//
//                    LineData data2 = new LineData(labels2, dataset2);
//                    lineChart2.setData(data2); // set the data and list of lables into chart
//                    lineChart2.setDescription("");  // set the description
//                    dataset2.setDrawCubic(true);
//                    dataset2.setCircleRadius(8);
//                    dataset2.setDrawCircleHole(false);
//                    dataset2.setCircleColors(colors);
//                    dataset2.setColors(COLORS);//black line
//                    linearLayout.addView(view3);
//
//
//               //end chart2
          //  rootView.setVisibility(View.VISIBLE);
        } catch (JSONException e) {e.printStackTrace();
            System.out.println("Historique-12 : exception json");}

    }
    //################GET json Note################
    private class GetClassNote extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            System.out.println("Note-get-onPreExecute : ");
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tChargement...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {
                System.out.println("Note-get-doInBackground : ");
                // Enter URL address where your php file resides
                url = new URL( getResources().getString(R.string.url1)+"/api/vors/"+params[0]);
                System.out.println("Test-URL-Id:"+getResources().getString(R.string.url_vor)+"/api/vors/"+params[0]);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception1";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                String get_token=string_token;//token

                System.out.println("Note-get-token1:"+get_token);
                get_token=string_token.replace("\"token\":\"","").replace("\"","");
                System.out.println("Note-get-token2:"+get_token);

                conn.setRequestProperty("Authorization", "Bearer " + get_token);//conn.setRequestProperty("Authorization", "Bearer " + get_token);


                conn.setDoInput(true);

                conn.connect();
//


            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            String txtResult = "";

            try {

                int response_code = conn.getResponseCode();

                System.out.println("Note-get-11:"+response_code);//200 or 401

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {



                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                        txtResult = result.toString();         //get json
                        System.out.println("Note-get-12 get json: " + txtResult);
                        string_json=txtResult;

                    }



                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("Note-get-13 : " + txtResult);//get json
                    return(result.toString());

//                }else if (response_code == 401){//########token invalide#############
//                    //post login password again
//                    postagain();
//
//                    return getagain();//try new token and return json to excute
//
               }else {
                    return("unsuccessful 403 404 500...");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
           // scrollview.setVisibility(View.VISIBLE);
            pdLoading.dismiss();
            System.out.println("Note-get-14 : " + result);//get json

            if(result.equalsIgnoreCase(string_json))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Intent intent = new Intent(getActivity(), Note.class);
                intent.putExtra("string_json_note", string_json);
                startActivity(intent);
                /***add domain and facteur and change text color***/
                //add domain
//#######json##################
        //        getjson();
//#############################
            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getActivity(), "get wrong", Toast.LENGTH_LONG).show();

            }
        }

    }

    public void postagain(){
        HttpURLConnection conn2=null;
        URL url2 = null;
        try {

            // Enter URL address where your php file resides
            url2 = new URL( getResources().getString(R.string.url1)+"/api/login_check");


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getActivity(), "wrong username or password ", Toast.LENGTH_LONG).show();
        }
        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn2 = (HttpURLConnection)url2.openConnection();
            conn2.setReadTimeout(READ_TIMEOUT);
            conn2.setConnectTimeout(CONNECTION_TIMEOUT);
            conn2.setRequestMethod("POST");
            conn2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn2.setDoInput(true);
            conn2.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("_username",string_login)
                    .appendQueryParameter("_password", string_password);
            String query = builder.build().getEncodedQuery();

            //  String data = "_username=lorem@ipsum.fr";
            // data += "&_password=aze";

            // Open connection for sending data
            OutputStream os = conn2.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn2.connect();



        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            Toast.makeText(getActivity(), "wrong username or password ", Toast.LENGTH_LONG).show();
        }

        String txtResult = "";

        try {

            int response_code = conn2.getResponseCode();
            System.out.println("historique- postagain response_code:"+response_code);//200

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {



                // Read data sent from server
                InputStream input = conn2.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    txtResult = result.toString();
                    System.out.println("historique-postagain token : " + txtResult);//token
                    string_token=txtResult;
                }
                //#####save new token#####
                // get SharedPreferences object
                SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(getActivity());;
                //get Editor object
                SharedPreferences.Editor editor=sharedPre.edit();
                editor.putString("token", string_token);
                editor.commit();
                //#####save new token#####
            }else{

                Toast.makeText(getActivity(), "wrong username or password ", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "wrong username or password ", Toast.LENGTH_LONG).show();
        } finally {
            conn2.disconnect();
        }



    }
    public String getagain(){
        HttpURLConnection conn3=null;
        URL url3 = null;
        try {

            // Enter URL address where your php file resides
            url3 = new URL( getResources().getString(R.string.url1)+"/api/vors");

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "exception1";
        }
        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn3 = (HttpURLConnection)url3.openConnection();
            conn3.setReadTimeout(READ_TIMEOUT);
            conn3.setConnectTimeout(CONNECTION_TIMEOUT);
            conn3.setRequestMethod("GET");

            // read SharedPreferences data(token)
            SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(getActivity());
            String token = sharedPre.getString("token", "");
            string_token=token;
            System.out.println("historique-getagin sharepre token : " + token);


            String get_token=string_token;//token
            get_token=string_token.replace("\"token\":\"","").replace("\"","");
            System.out.println("Historique-getagain-token2:" + get_token);

            conn3.setRequestProperty("Authorization", "Bearer " + get_token);//conn.setRequestProperty("Authorization", "Bearer " + get_token);


            conn3.setDoInput(true);

            conn3.connect();
//


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return "exception";
        }

        String txtResult = "";

        try {

            int response_code = conn3.getResponseCode();

            System.out.println("historique-getagain response_code:"+response_code);//200 or 401

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {



                // Read data sent from server
                InputStream input = conn3.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    txtResult = result.toString();         //get json
                    System.out.println("historique-getagain json: " + txtResult);
                    string_json=txtResult;

                }



                // Pass data to onPostExecute method
                //get json
                return(result.toString());

            }else if (response_code == 401){//token invalide
                return("unsuccessful 401...");
            }else {
                return("unsuccessful 403 404 500...");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        } finally {
            conn3.disconnect();
        }


    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("TAG", "----------Fragment onAttach----------");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "----------Fragment onCreate----------");
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("TAG", "----------Fragment onActivityCreated----------");
    }
    @Override
    public void onStart() {

        super.onStart();
        Log.d("TAG", "----------Fragment historique onStart----------start="+start);

    }
    @Override
    public void onPause() {
        super.onPause();

        Log.d("TAG", "----------Fragment onPause---------start="+start);

    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("TAG", "----------Fragment historique onStop----------");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("TAG", "----------Fragment onDestroyView----------");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "----------Fragment onDestroy----------");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("TAG", "----------Fragment onDetach----------");
    }
}



