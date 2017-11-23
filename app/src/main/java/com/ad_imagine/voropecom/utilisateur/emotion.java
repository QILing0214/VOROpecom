package com.ad_imagine.voropecom.utilisateur;

/**
 * Created by chu on 2016/6/7.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ad_imagine.voropecom.utilisateur.Note;
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

public class emotion extends Fragment {
    public emotion() {
    }
    int start=0;
    int restart=0;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private String string_token;
    private String string_json;
    private String string_json_his;
    private String string_json_new;
    private String responseResult;
    private String string_login;
    private String string_password;
    SeekBar seekbar;
    LinearLayout linearLayout;
    Button button_valide;
    TextView facteur;
    ProgressDialog pdLoading;
    View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.emotion, container, false);
//hide view before data is loaded
        //rootView.setVisibility(View.GONE);

        linearLayout=(LinearLayout)rootView.findViewById(R.id.linearLayout_content);

        button_valide=(Button)rootView.findViewById(R.id.button8);
        // read SharedPreferences data(token)
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String token = sharedPre.getString("token", "");
        string_token=token;
        string_login=sharedPre.getString("username", "");
        string_password=sharedPre.getString("password", "");
        string_json=sharedPre.getString("string_json_emotion", "");
        System.out.println("Emotion sharepre token : " + token);
        System.out.println("Emotion sharepre login: " + string_login);
        System.out.println("Emotion sharepre password: " + string_password);
        System.out.println("Emotion sharepre json: " + string_json);
        if(restart==1) {
            // get
            rootView.setVisibility(View.GONE);
            new GetClass().execute();//get new emotion id
        }
        else {
            getjson();
        }
        System.out.println("Emotion-string_json-in main : " + string_json);




        return rootView;
    }
    //################GET not use################
    private class GetClass extends AsyncTask<String, String, String>
    {
       ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            System.out.println("Emotion-onPreExecute : ");
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading. Emotion..");
            pdLoading.setCancelable(false);

            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {

            try {
                System.out.println("Test-21 : ");
                // Enter URL address where your php file resides
                url = new URL( getResources().getString(R.string.url_vor)+"/api/vor/new");

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

                System.out.println("Emotion-get-token1:"+get_token);
                get_token=string_token.replace("\"token\":\"","").replace("\"","");
                System.out.println("Emotion--get-token2:" + get_token);

                conn.setRequestProperty("Authorization", "Bearer " +get_token);//conn.setRequestProperty("Authorization", "Bearer " + get_token);


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

                System.out.println("Emotion-get:"+response_code);//200 or 401

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
                        System.out.println("Emotion- get: " + txtResult);
                        string_json=txtResult;

                    }



                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("Emotion-get: " + txtResult);//get json
                    return(result.toString());

                }else if (response_code == 401){//########token invalide#############
                    //post login password again
                    postagain();

                    return getagain();//try new token and return json to excute

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
            //show fragment
            rootView.setVisibility(View.VISIBLE);
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            Fragment f = getActivity().getFragmentManager().findFragmentByTag("tag_emotion");
//            ft.show(f);
//            ft.commit();
            pdLoading.dismiss();
            System.out.println("Emotion-get : " + result);//get return (json or exception)

            if(result.equalsIgnoreCase(string_json))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */

//####################get json/button valide/ post####################
                restart=0;
             getjson();
//############################################


            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getActivity(), "get wrong", Toast.LENGTH_LONG).show();

            }
        }

    }
    //##########POST new json###########
    private class Postjson extends AsyncTask<String, String, String>
    {
      //  ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            //hide fragment
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            Fragment f = getActivity().getFragmentManager().findFragmentByTag("tag_emotion");
//            ft.hide(f);
//            ft.commit();

            //this method will be running on UI thread
            pdLoading = new ProgressDialog(getActivity());
            pdLoading.setMessage("\tChargement...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL(getResources().getString(R.string.url1)+"/api/vors");
                System.out.println("Emotion-Post-URL:"+getResources().getString(R.string.url_vor)+"/api/vors");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                String get_token=string_token;//token

                System.out.println("Emotion-Post-token1:" + get_token);
                get_token=string_token.replace("\"token\":\"","").replace("\"","");
                System.out.println("Emotion-Post-token2:" + get_token);
                conn.setRequestProperty("Authorization", "Bearer " +get_token);//conn.setRequestProperty("Authorization", "Bearer " + get_token);

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL


                //  String data = "_username=lorem@ipsum.fr";
                // data += "&_password=aze";

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(string_json_new);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

                System.out.println("Emotion-Post-0 string_json_new:"+string_json_new);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }


            try {

                int response_code = conn.getResponseCode();
                System.out.println("Emotion-Post-1:response_code:"+response_code);//200
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {



                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                        responseResult = result.toString();
                        System.out.println("Emotion-Post-2 :respose Result " + responseResult);//json add note for Note.java

                    }



                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("Emotion-Post-3 : " + responseResult);//token
                    return(result.toString());

                }else if (response_code == 401){//########token invalide#############
                    //post login password again
                    postagain();

                    return postagain2();//try new token and return json to excute

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


           // pdLoading.dismiss();
            System.out.println("Emotion-Post-4 :result " + result);//json add note
            System.out.println("Emotion-Post-5:string_json adddata " + string_json);
            System.out.println("Emotion-Post-6:responseResult " +responseResult);

            if(result.equalsIgnoreCase(responseResult))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                //change emotion id
                new GetClassEmotion().execute();
                new GetClassHis().execute();
//                Intent intent = new Intent(getActivity(), Note.class);
//                intent.putExtra("string_json_note",responseResult );//send json to note
//                System.out.println("string_json_note send to next page : " + responseResult);
//                startActivity(intent);
                savejsonnote(getActivity(), responseResult);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_utilisateur, new note_fragment());
                transaction.addToBackStack(null);
                transaction.commit();

            }else if (result.equalsIgnoreCase("exception")){
                Toast.makeText(getActivity(), "Invalid email or password.", Toast.LENGTH_LONG).show();

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else  {

                Toast.makeText(getActivity(), "unsuccessful....", Toast.LENGTH_LONG).show();

            }
        }

    }

    //save string_json_his
    public static void savejsonhis(Context context,String json){
        // get SharedPreferences object
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(context);;
        //get Editor object
        SharedPreferences.Editor editor=sharedPre.edit();

        editor.putString("string_json_his", json);



        editor.commit();
    }
    //save string_json_his
    public static void savejsonnote(Context context,String json){
        // get SharedPreferences object
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(context);;
        //get Editor object
        SharedPreferences.Editor editor=sharedPre.edit();

        editor.putString("string_json_note", json);



        editor.commit();
    }

    //################GET data Historique################
    private class GetClassHis extends AsyncTask<String, String, String>
    {
        // ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            System.out.println("his-onPreExecute : ");
            super.onPreExecute();

//            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading. his..");
//            pdLoading.setCancelable(false);
//
//            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {

            try {
                System.out.println("his-21 : ");
                // Enter URL address where your php file resides
                url = new URL( getResources().getString(R.string.url1)+"/api/vors");

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

                System.out.println("Emotion-get-token1:"+get_token);
                get_token=string_token.replace("\"token\":\"","").replace("\"","");
                System.out.println("Emotion--get-token2:" + get_token);

                conn.setRequestProperty("Authorization", "Bearer " +get_token);//conn.setRequestProperty("Authorization", "Bearer " + get_token);


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

                System.out.println("his-get:"+response_code);//200 or 401

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
                        System.out.println("his- get: " + txtResult);
                        string_json_his=txtResult;

                    }



                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("his-get: " + txtResult);//get json
                    return(result.toString());

                }else {
                    return("unsuccessful 401403 404 500...");
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
            //show fragment
            //  rootView.setVisibility(View.VISIBLE);

            pdLoading.dismiss();
            System.out.println("his-get : " + result);//get return (json or exception)

            if(result.equalsIgnoreCase(string_json_his))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                savejsonhis(getActivity(), string_json_his);

//####################get json/button valide/ post####################
                // getjson();
//############################################


            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getActivity(), "get wrong", Toast.LENGTH_LONG).show();

            }
        }

    }
    //get json
    public void getjson(){
        try {
            final   JSONObject  jsonRootObject = new JSONObject(string_json);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArrayDomain = jsonRootObject.optJSONArray("domaines");//[json Array]


            //create LinearLayout for domains facteurs

            //     LinearLayout linearLayout=(LinearLayout)rootView.findViewById(R.id.linearLayout_content);

            final JSONObject newjsonObjectVOR = new JSONObject();//create new json1
            final JSONObject newjsonObjectdomaines = new JSONObject();//create new json2
            JSONArray newjsonArraydomain = new JSONArray();//create new json3
            // use "for" to add domains
            for(int i = 0;i<jsonArrayDomain.length();i++) {
                //domain nom
                JSONObject jsonObject = jsonArrayDomain.getJSONObject(i);//{json object}
                String name = jsonObject.optString("nom").toString();
                JSONObject newjsonObjectundomain = new JSONObject();//create new json4
                final JSONArray newjsonArrayfacteur = new JSONArray();//create new json5

                //
                TextView mTextView = new TextView(getActivity());
                mTextView.setText(name);

                mTextView.setTypeface(null, Typeface.BOLD);
                mTextView.setTextColor(Color.parseColor("#000000"));
                float pixels_8dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
                mTextView.setTextSize(pixels_8dp);
                LinearLayout.LayoutParams LP=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LP.leftMargin = 5 * (int) (Math.round(getActivity().getResources().getDisplayMetrics().density));
                LP.topMargin = 20 * (int) (Math.round(getActivity().getResources().getDisplayMetrics().density));
                linearLayout.addView(mTextView, LP);

                //add facteur
                JSONArray jsonArrayFacteur = jsonObject.optJSONArray("facteurs");

                for(int j = 0;j<jsonArrayFacteur.length();j++) {

                    View view2 = getActivity().getLayoutInflater().inflate(R.layout.onefacteur, null);

                    seekbar=(SeekBar)view2.findViewById(R.id.seekBar);
                    seekbar.setThumb(getResources().getDrawable(R.drawable.graythumb));
                    seekbar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbarnew));
                    //   System.out.println("Emotion-0 : " + seekbar.getId());

                    final TextView tv_result=(TextView)view2.findViewById(R.id.txt_note);//######important
                    facteur=(TextView)view2.findViewById(R.id.txt_facteur);
                    //facteur nom
                    final JSONObject jsonObjectFacteur = jsonArrayFacteur.getJSONObject(j);
                    String nameFacteur = jsonObjectFacteur.optString("nom").toString();
                    facteur.setText(nameFacteur);
                    jsonObjectFacteur.put("note", 0);//facteur note defaut 1

                    final JSONObject newjsonObjectfacteur = new JSONObject();//create new json6
                    try {

                        newjsonObjectfacteur.put("id", Integer.parseInt(jsonObjectFacteur.optString("id").toString()));
                        newjsonObjectfacteur.put("note",0);
                        newjsonArrayfacteur.put(newjsonObjectfacteur);//add json 1


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    final  JSONObject jsonObjectFacteur_j = newjsonArrayfacteur.getJSONObject(j);//###important
                    //
                    LinearLayout.LayoutParams LP1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LP1.leftMargin =1 * (int) (Math.round(getActivity().getResources().getDisplayMetrics().density));
                    LP1.topMargin = 3 * (int) (Math.round(getActivity().getResources().getDisplayMetrics().density));


                    System.out.println("Emotion-1 : ");
                    seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                        public void onProgressChanged(SeekBar seekBar, int progress,
                                                      boolean fromUser) {

                            if(progress<1){
                                seekBar.setProgress(1);
                                progress=1;
                            }
                            //   System.out.println("Emotion-2 : " + progress);

                            // change progress text label with current see_kbar value
                            tv_result.setText("" + progress);
                            //change json data "name" value
                            try {
                                jsonObjectFacteur.put("note", progress);
                            } catch (JSONException e) {
                            }

                            try {

                                // newjsonObjectfacteur.put("id", jsonObjectFacteur.optString("id").toString());
                                jsonObjectFacteur_j.put("note", progress);//###important
                                //add json 1

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            //change thumb color and note color

                            if (progress == 0) {

                                seekBar.setThumb(getResources().getDrawable(R.drawable.graythumb));
                                tv_result.setBackgroundResource(R.drawable.graynumber);

                            }
                            if (progress == 1 || progress == 2 || progress == 3) {

                                seekBar.setThumb(getResources().getDrawable(R.drawable.greenthumb));
                                tv_result.setBackgroundResource(R.drawable.greennumber);
                                //    System.out.println("Emotion-3 : " + seekBar.getId());

                            }
                            if (progress == 4 || progress == 5 || progress == 6) {
                                seekBar.setThumb(getResources().getDrawable(R.drawable.orangethumb));
                                tv_result.setBackgroundResource(R.drawable.orangenumber);
                            }
                            if (progress == 7 || progress == 8 || progress == 9) {
                                seekBar.setThumb(getResources().getDrawable(R.drawable.redthumb));
                                tv_result.setBackgroundResource(R.drawable.rednumber);
                            }


                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                            // ("starting to track touch");

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                            seekBar.setSecondaryProgress(seekBar.getProgress());
                            // ("ended tracking touch");
                            string_json=jsonRootObject.toString();//string_json add note
                            buttonvalidation();//########button? note==0######
                        }
                    });
                    linearLayout.addView(view2, LP1);

                } //end if facteurs
                newjsonObjectundomain.put("id",Integer.parseInt(jsonObject.optString("id").toString()));//add json2
                newjsonObjectundomain.put("facteurs",newjsonArrayfacteur);//add json3
                newjsonArraydomain.put(newjsonObjectundomain);//add json4
            }// end if domaines
            newjsonObjectdomaines.put("id",Integer.parseInt(jsonRootObject.optString("id").toString()));//add json5
            newjsonObjectdomaines.put("addedAt",0000);//add  "addedAt":
            newjsonObjectdomaines.put("domaines",newjsonArraydomain);//add json6
            newjsonObjectVOR.put("VOR", newjsonObjectdomaines);//add json7
            System.out.println("Emotion--json post to server before seekbar : " + newjsonObjectVOR.toString());//POST json


//start activity  Note.class
            string_json=jsonRootObject.toString();//string_json add note
            buttonvalidation();//########button? note==0######
            button_valide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String date = df.format(Calendar.getInstance().getTime());
                    try {
                    newjsonObjectdomaines.put("addedAt",date);//add  "addedAt":
                    string_json_new=newjsonObjectVOR.toString();// JSON new for POST
                        System.out.println("Emotion--json post to server when valide (string_json_new) : " +string_json_new);

                    //######POST new json#######
                    new Postjson().execute();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    //   getActivity().finish();// #######update page when return
                }
            });
        } catch (JSONException e) {e.printStackTrace();
            System.out.println("Emotion-get: exception json");}

    }
    //if there is one note==0, myButton.setEnabled(false);  unclickable
    public void buttonvalidation(){

        try {
            JSONObject jsonRootObject = new JSONObject(string_json);
            JSONArray jsonArrayDomain = jsonRootObject.optJSONArray("domaines");
            float multiply_note = 1;
            for (int i = 0; i < jsonArrayDomain.length(); i++) {
                for (int j = 0; j < jsonArrayDomain.getJSONObject(i).optJSONArray("facteurs").length(); j++) {
                    multiply_note = multiply_note * Float.valueOf(jsonArrayDomain.getJSONObject(i).optJSONArray("facteurs").getJSONObject(j).optString("note").toString());
                }

            }
            if(multiply_note==0){
                button_valide.setEnabled(false);
                System.out.println("multiply_note : " + multiply_note);
                button_valide.setBackgroundResource(R.drawable.emotion_radius_novalide);

            }

            else if(multiply_note!=0) {
                button_valide.setEnabled(true);
                System.out.println("multiply_note : " + multiply_note);
                button_valide.setBackgroundResource(R.drawable.emotion_radius_valide);
            }
        }catch (JSONException e) {
        }


    }
    //################change data emotion when post################
    private class GetClassEmotion extends AsyncTask<String, String, String>
    {
        // ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            System.out.println("Emotion-onPreExecute : ");
            super.onPreExecute();

//            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading. Emotion..");
//            pdLoading.setCancelable(false);
//
//            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {

            try {
                System.out.println("Test-21 : ");
                // Enter URL address where your php file resides
                url = new URL( getResources().getString(R.string.url_vor)+"/api/vor/new");

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

                System.out.println("Emotion-get-token1:"+get_token);
                get_token=string_token.replace("\"token\":\"","").replace("\"","");
                System.out.println("Emotion--get-token2:" + get_token);

                conn.setRequestProperty("Authorization", "Bearer " +get_token);//conn.setRequestProperty("Authorization", "Bearer " + get_token);


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

                System.out.println("Emotion-get:"+response_code);//200 or 401

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
                        System.out.println("Emotion- get: " + txtResult);
                        string_json=txtResult;

                    }



                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("Emotion-get: " + txtResult);//get json
                    return(result.toString());

                }else {
                    return("unsuccessful 401403 404 500...");
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
            //show fragment
            //  rootView.setVisibility(View.VISIBLE);

            // pdLoading.dismiss();
            System.out.println("Emotion-get : " + result);//get return (json or exception)

            if(result.equalsIgnoreCase(string_json))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                savejsonemotion(getActivity(), string_json);
//####################get json/button valide/ post####################
                // getjson();
//############################################


            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getActivity(), "get wrong", Toast.LENGTH_LONG).show();

            }
        }

    }
    //save string_json_emotion
    public static void savejsonemotion(Context context,String json){
        // get SharedPreferences object
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(context);;
        //get Editor object
        SharedPreferences.Editor editor=sharedPre.edit();

        editor.putString("string_json_emotion", json);



        editor.commit();
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
                System.out.println("Emotion-postagain response_code:"+response_code);//200

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
                        System.out.println("Emotion-postagain token : " + txtResult);//token
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
            url3 = new URL( getResources().getString(R.string.url_vor)+"/api/vor/new");

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
            System.out.println("Emotion-getagin sharepre token : " + token);


            String get_token=string_token;//token
            get_token=string_token.replace("\"token\":\"","").replace("\"","");
            System.out.println("Emotion-getagain-token2:" + get_token);

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

            System.out.println("Emotion-getagain response_code:"+response_code);//200 or 401

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
                    System.out.println("Emotion-getagain json: " + txtResult);
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
    public String postagain2(){

        HttpURLConnection conn;
        URL url = null;
        try {

            // Enter URL address where your php file resides
            url = new URL(getResources().getString(R.string.url1)+"/api/vors");


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "exception";
        }
        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            String get_token=string_token;//token

            System.out.println("Emotion-postagin2 get_token:" + get_token);
            get_token=string_token.replace("\"token\":\"","").replace("\"","");
            System.out.println("Emotion-postagin2 get_token:" + get_token);
            conn.setRequestProperty("Authorization", "Bearer " +get_token);//conn.setRequestProperty("Authorization", "Bearer " + get_token);

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL


            //  String data = "_username=lorem@ipsum.fr";
            // data += "&_password=aze";

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(string_json_new);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            System.out.println("Emotion-Postagain2 string_json_new:"+string_json_new);

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return "exception";
        }

        String txtResult = "";

        try {

            int response_code = conn.getResponseCode();
            System.out.println("Emotion-Postagain2:response_code:"+response_code);//200
            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {



                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    txtResult = result.toString();
                    System.out.println("Emotion-Postagain2 :respose Result " + txtResult);//token?

                }



                // Pass data to onPostExecute method

                return(result.toString());

            }else {
                return("unsuccessful 401 403 404 500...");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        } finally {
            conn.disconnect();
        }
    }
    // for analyse fragment life and reload fragment data when back
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
        Log.d("TAG", "----------Fragment onStart----------start="+start);
        if(start==1){
            restart=1;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
            //To add to Rajdeep's answer, you'll notice that when a Fragment is detached, its onPause, onStop and onDestroyView methods are called only (in that order). On the other hand, when a Fragment is removed, its onPause, onStop, onDestroyView, onDestroy and onDetach methods are called (in that order).
            // Similarly, when attaching, the Fragment's onCreateView, onStart and onResume methods are called only; and when adding, the Fragment's onAttach, onCreate, onCreateView, onStart and onResume methods are called (in that order).
        }
        start=0;
        Log.d("TAG", "----------Fragment set Start=0----------start="+start);
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "----------Fragment onResume----------");
    }
    @Override
    public void onPause() {
        super.onPause();
        start=1;
        Log.d("TAG", "----------Fragment onPause---------start="+start);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TAG", "----------Fragment< emotion > onStop----------");
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
