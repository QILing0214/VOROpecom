package com.ad_imagine.voropecom.utilisateur;

import android.app.Dialog;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;


import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.ad_imagine.voropecom.Coach;
import com.ad_imagine.voropecom.R;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Note extends AppCompatActivity {
   // View section1, section2, section3;
    ImageButton imagebutton_avant,imagebutton_now,imagebutton_apres;
   // ImageView imageview4,imageview5,imageview6;
   ScrollView scrollview;
    TextView nomtemps;
    int i;//i=1,2,3 check what button icon is clicked

    private String string_token;
    private String string_json;
    private String string_id;
    private String string_login;
    private String string_password;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_activity_note);
        //hide view before loading data
       scrollview=(ScrollView)findViewById(R.id.scrollview_note);
      //  scrollview.setVisibility(View.GONE);
        /***get note from last page and change note note color and comment***/
        Intent intent = getIntent();
        string_json = intent.getStringExtra("string_json_note");
        System.out.println("Note get string_json_note : " + string_json);

        // read SharedPreferences data(token)
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPre.getString("token", "");
        string_login=sharedPre.getString("username", "");
        string_password=sharedPre.getString("password", "");
        string_token=token;
        System.out.println("Note-token : " + token);
        //#######json##################
        getjson();
//#############################
//        //##### GET #############
//        new GetClass().execute();
//        System.out.println("Note-string_json-main : " + string_json);



        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("VOR");//getSupportActionBar().setTitle(null);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

//        //coordion
//        section1=findViewById(R.id.relativeLayout);
//        section2=findViewById(R.id.relativeLayout2);
//        section3=findViewById(R.id.relativeLayout3);

//        imageview4=(ImageView)findViewById(R.id.imageButton4);
//        imageview5=(ImageView)findViewById(R.id.imageButton5);
//        imageview6=(ImageView)findViewById(R.id.imageButton6);
//

//        section1.setVisibility(View.GONE);
//        section2.setVisibility(View.GONE);
//        section3.setVisibility(View.GONE);
//
//        tv_domain.get(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (section1.getVisibility() == View.GONE) {
//                    section1.setVisibility(View.VISIBLE);
//                    imageview4.setImageResource(R.drawable.angle_arrow_pointing_down);
//
//                } else {
//                    section1.setVisibility(View.GONE);
//                    imageview4.setImageResource(R.drawable.angle_arrow_pointing_to_right_small);
//                }
//            }
//        });
//       tv_domain.get(1).setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               if (section2.getVisibility() == View.GONE) {
//                   section2.setVisibility(View.VISIBLE);
//                   imageview5.setImageResource(R.drawable.angle_arrow_pointing_down);
//               } else {
//                   section2.setVisibility(View.GONE);
//                   imageview5.setImageResource(R.drawable.angle_arrow_pointing_to_right_small);
//               }
//           }
//       });
//       tv_domain.get(2).setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               if (section3.getVisibility() == View.GONE) {
//                   section3.setVisibility(View.VISIBLE);
//                   imageview6.setImageResource(R.drawable.angle_arrow_pointing_down);
//               } else {
//                   section3.setVisibility(View.GONE);
//                   imageview6.setImageResource(R.drawable.angle_arrow_pointing_to_right_small);
//               }
//           }
//       });
        /****choose time*******/
        imagebutton_avant=(ImageButton)findViewById(R.id.imageButton_avant);
        imagebutton_now=(ImageButton)findViewById(R.id.imageButton_now);
        imagebutton_apres=(ImageButton)findViewById(R.id.imageButton_apres);
        nomtemps=(TextView)findViewById(R.id.textView_nomtemps);

        //avant icon click--> dialog
        imagebutton_avant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1;
                dialog_avant();
            }
        });
        //now icon click-->dialog
        imagebutton_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=2;
                dialog_evenementexistant();
            }
        });
       //apres icon click-->dialog
        imagebutton_apres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=3;
                dialog_evenementexistant();
            }
        });

    }  //onCreate end

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, emotion.class));
//    }
//toorbar menu
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here(BACK)
        if (item.getItemId() == android.R.id.home) {
            System.out.println("action bar clicked");

            finish(); // close this activity and return to preview activity (if there is any)

        }

        return super.onOptionsItemSelected(item);
    }

    // choose time
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
               if(i==1) {
                   imagebutton_avant.setImageResource(R.drawable.arrow_up_white);
                   imagebutton_avant.setBackgroundResource(R.drawable.blackcircle);
                   imagebutton_now.setImageResource(R.drawable.plus);
                   imagebutton_now.setBackgroundResource(R.drawable.whitecircle);
                   imagebutton_apres.setImageResource(R.drawable.arrowdown);
                   imagebutton_apres.setBackgroundResource(R.drawable.whitecircle);
               }
               if(i==2){
                   imagebutton_now.setImageResource(R.drawable.plus_white);
                   imagebutton_now.setBackgroundResource(R.drawable.blackcircle);
                   imagebutton_avant.setImageResource(R.drawable.arrowup);
                   imagebutton_avant.setBackgroundResource(R.drawable.whitecircle);
                   imagebutton_apres.setImageResource(R.drawable.arrowdown);
                   imagebutton_apres.setBackgroundResource(R.drawable.whitecircle);
               }
               if(i==3){
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
//    //################GET################
//    private class GetClass extends AsyncTask<String, String, String>
//    {
//        ProgressDialog pdLoading = new ProgressDialog(Note.this);
//        HttpURLConnection conn;
//        URL url = null;
//
//        @Override
//        protected void onPreExecute() {
//            System.out.println("Note-get-onPreExecute : ");
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
//                System.out.println("Note-get-doInBackground : ");
//                // Enter URL address where your php file resides
//                url = new URL( getResources().getString(R.string.url1)+"/api/vors/"+string_id);
//                System.out.println("Test-URL-Id:"+getResources().getString(R.string.url_vor)+"/api/vors/"+string_id);
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
//                System.out.println("Note-get-token1:"+get_token);
//                get_token=string_token.replace("\"token\":\"","").replace("\"","");
//                System.out.println("Note-get-token2:"+get_token);
//
//                conn.setRequestProperty("Authorization", "Bearer " + get_token);//conn.setRequestProperty("Authorization", "Bearer " + get_token);
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
//                System.out.println("Note-get-11:"+response_code);//200 or 401
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
//                        System.out.println("Note-get-12 get json: " + txtResult);
//                        string_json=txtResult;
//
//                    }
//
//
//
//                    // Pass data to onPostExecute method
//                    //return(result.toString());
//                    System.out.println("Note-get-13 : " + txtResult);//get json
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
//            scrollview.setVisibility(View.VISIBLE);
//            pdLoading.dismiss();
//            System.out.println("Note-get-14 : " + result);//get json
//
//            if(result.equalsIgnoreCase(string_json))
//            {
//                /* Here launching another activity when login successful. If you persist login state
//                use sharedPreferences of Android. and logout button to clear sharedPreferences.
//                 */
//
//                /***add domain and facteur and change text color***/
//                //add domain
////#######json##################
//               getjson();
////#############################
//            }else if (result.equalsIgnoreCase("false")){
//
//                // If username and password does not match display a error message
//                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();
//
//            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
//
//                Toast.makeText(Note.this, "get wrong", Toast.LENGTH_LONG).show();
//
//            }
//        }
//
//    }
    public void getjson(){
        try {
            final JSONObject jsonRootObject = new JSONObject(string_json);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArrayDomain = jsonRootObject.optJSONArray("domaines");//[json Array]
            for(int i = 0;i<jsonArrayDomain.length();i++) {
                //domain nom
                JSONObject jsonObject = jsonArrayDomain.getJSONObject(i);//{json object}
                String name = jsonObject.optString("nom").toString();

                View view1 = getLayoutInflater().inflate(R.layout.note_example_domain, null);
                LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearlayout1);
                LinearLayout.LayoutParams LP1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LP1.leftMargin =1 * (int) (Math.round(Note.this.getResources().getDisplayMetrics().density));
                LP1.topMargin = 5 * (int) (Math.round(Note.this.getResources().getDisplayMetrics().density));
                linearLayout.addView(view1, LP1);
                final LinearLayout LL = new LinearLayout(Note.this);
                LL.setOrientation(LinearLayout.VERTICAL);

                //add facteur
                JSONArray jsonArrayFacteur = jsonObject.optJSONArray("facteurs");

                for(int j= 0;j<jsonArrayFacteur.length();j++) {
                    //facteur nom
                    JSONObject jsonObjectFacteur = jsonArrayFacteur.getJSONObject(j);
                    String nameFacteur = jsonObjectFacteur.optString("nom").toString();
                    String noteFacteur = jsonObjectFacteur.optString("note").toString();
                    View view2 = getLayoutInflater().inflate(R.layout.note_example_facteur, null);

                    LinearLayout.LayoutParams LP2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LP1.leftMargin =1 * (int) (Math.round(Note.this.getResources().getDisplayMetrics().density));
                    LP1.topMargin =5* (int) (Math.round(Note.this.getResources().getDisplayMetrics().density));
                    LL.addView(view2);
                    TextView tvfacteur=(TextView)view2.findViewById(R.id.textView_facteur);
                    TextView tvfacteurnote=(TextView)view2.findViewById(R.id.textView_facteur_note);
                    tvfacteur.setText(nameFacteur);
                    tvfacteurnote.setText(noteFacteur);

                    //change color of facteur
                    if (Float.valueOf(tvfacteurnote.getText().toString()) <= 3) {
                        tvfacteurnote.setBackgroundResource(R.drawable.greennumber);
                    }
                    if ((Float.valueOf(tvfacteurnote.getText().toString()) <=6) && (Float.valueOf(tvfacteurnote.getText().toString()) > 3)) {
                        tvfacteurnote.setBackgroundResource(R.drawable.orangenumber);
                    }
                    if (Float.valueOf(tvfacteurnote.getText().toString()) > 6) {
                        tvfacteurnote.setBackgroundResource(R.drawable.rednumber);;
                    }


                }//end if facteur
                linearLayout.addView(LL);
                //domain note nom
                TextView tvdomainnote=(TextView)view1.findViewById(R.id.txt_domain_note);
                String noteDomain = jsonObject.optString("moyenne").toString();
                tvdomainnote.setText(String.format("%.2f", Float.valueOf(noteDomain)).replace(',','.'));////#####the format float x.xx   2 decimal#####

                LL.setVisibility(View.GONE);
                TextView tvdomain=(TextView)view1.findViewById(R.id.textView_domain);
                tvdomain.setText(name);// get json
                //change color of domain
                if (Float.valueOf(tvdomainnote.getText().toString()) <= 3) {
                    tvdomain.setBackgroundColor(Color.parseColor("#40b83d"));
                }
                if ((Float.valueOf(tvdomainnote.getText().toString()) <=6) && (Float.valueOf(tvdomainnote.getText().toString()) > 3)) {
                    tvdomain.setBackgroundColor(Color.parseColor("#f57f20"));
                }
                if (Float.valueOf(tvdomainnote.getText().toString()) > 6) {
                    tvdomain.setBackgroundColor(Color.parseColor("#ed282b"));
                }


                final ImageView im_arrow=(ImageView)view1.findViewById(R.id.imageButton_arrow_right);
                tvdomain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (LL.getVisibility() == View.GONE) {
                            LL.setVisibility(View.VISIBLE);
                            im_arrow.setImageResource(R.drawable.angle_arrow_pointing_down);

                        } else {
                            LL.setVisibility(View.GONE);
                            im_arrow.setImageResource(R.drawable.angle_arrow_pointing_to_right_small);
                        }
                    }
                });


            }//end if domian

            //date and moyenne note

            String strThatDay =jsonRootObject.optString("added_at").toString();

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date d = null;
            try {
                d = formatter.parse(strThatDay);//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Calendar c = Calendar.getInstance();
            c.setTime(d);

            TextView tvdate=(TextView)findViewById(R.id.textView_date);

            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            String stringdate="";
            switch (mMonth){
                case 0:
                    stringdate="janvier";
                    break;
                case 1:
                    stringdate="février";
                    break;
                case 2:
                    stringdate="mars";
                    break;
                case 3:
                    stringdate="avril";
                    break;
                case 4:
                    stringdate="mai";
                    break;
                case 5:
                    stringdate="juin";
                    break;
                case 6:
                    stringdate="juillet";
                    break;
                case 7:
                    stringdate="août";
                    break;
                case 8:
                    stringdate="septembre";
                    break;
                case 9:
                    stringdate="octobre";
                    break;
                case 10:
                    stringdate="novembre";
                    break;
                case 11:
                    stringdate="décembre";
                    break;
                default:

            }

            tvdate.setText("Le "+mDay+" "+stringdate+" "+mYear);

            TextView note=(TextView)findViewById(R.id.textView_note);
            note.setText(jsonRootObject.optString("moyenne").toString());
            TextView comment=(TextView)findViewById(R.id.textView_comment);

            if(Float.valueOf(note.getText().toString())<=3){
                note.setBackgroundResource(R.drawable.coach_coache_raduis_green);
                if(Float.valueOf(note.getText().toString())<2) {
                    comment.setText("Très détendu");
                }else {
                    comment.setText("Détendu");
                }
            }
            if((Float.valueOf(note.getText().toString())<=6)&&(Float.valueOf(note.getText().toString())>3)){
                note.setBackgroundResource(R.drawable.coach_coache_raduis_orange);
                if(Float.valueOf(note.getText().toString())<4) {
                    comment.setText("Légèrement tendu");
                }else {
                    comment.setText("Assez tendu");
                }
            }
            if(Float.valueOf(note.getText().toString())>6){
                note.setBackgroundResource(R.drawable.coach_coache_raduis_red);
                if(Float.valueOf(note.getText().toString())<7) {
                    comment.setText("Tendu");
                }else if(Float.valueOf(note.getText().toString())<8){
                    comment.setText("Très tendu");
                } else {
                    comment.setText("Extrêmement tendu");
                }
            }
        } catch (JSONException e) {e.printStackTrace();
            System.out.println("Note-get-12 : exception json");}


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
            Toast.makeText(this, "wrong username or password ", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "wrong username or password ", Toast.LENGTH_LONG).show();
        }

        String txtResult = "";

        try {

            int response_code = conn2.getResponseCode();
            System.out.println("postagain response_code:"+response_code);//200

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
                    System.out.println("postagain token : " + txtResult);//token
                    string_token=txtResult;
                }
                //#####save new token#####
                // get SharedPreferences object
                SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(this);
                //get Editor object
                SharedPreferences.Editor editor=sharedPre.edit();
                editor.putString("token", string_token);
                editor.commit();
                //#####save new token#####
            }else{

                Toast.makeText(this, "wrong username or password ", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "wrong username or password ", Toast.LENGTH_LONG).show();
        } finally {
            conn2.disconnect();
        }



    }
    public String getagain(){
        HttpURLConnection conn;
        URL url = null;
        try {

            // Enter URL address where your php file resides
            url = new URL( getResources().getString(R.string.url1)+"/api/vors/"+string_id);

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

            System.out.println("Note-getagain-token1:"+get_token);
            get_token=string_token.replace("\"token\":\"","").replace("\"","");
            System.out.println("Note-getagain-token2:"+get_token);

            conn.setRequestProperty("Authorization", "Bearer " + get_token);


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

            System.out.println("Note-getagain:"+response_code);//200 or 401

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
                    System.out.println("Note-getagain get json: " + txtResult);
                    string_json=txtResult;

                }



                // Pass data to onPostExecute method

                return(result.toString());

            }else {
                return("unsuccessful401 403 404 500...");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        } finally {
            conn.disconnect();
        }
    }
}
