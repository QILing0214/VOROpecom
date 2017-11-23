package com.ad_imagine.voropecom.utilisateur;

/**
 * Created by chu on 2016/6/7.
 */
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ad_imagine.voropecom.R;
import com.ad_imagine.voropecom.Utilisateur;

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

public class profile extends Fragment {
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private String string_token;
    private String string_json,string_json_new,string_json_newget;
    private String string_login;
    private String string_password,string_json_profile;
    View rootView;
    private EditText etnom;
    private EditText etprenom;
    private EditText etemail,ettel,etpwd1,etpwd2;
    Button button;
    public profile() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.profile, container, false);
        //hide view before data is loaded
     //   rootView.setVisibility(View.GONE);

        etnom = (EditText) rootView.findViewById(R.id.editText3);
        etprenom = (EditText)rootView. findViewById(R.id.editText4);
        etemail = (EditText)rootView. findViewById(R.id.editText5);
        ettel = (EditText)rootView. findViewById(R.id.editText6);
        etpwd1 = (EditText)rootView. findViewById(R.id.editText7);
        etpwd2 = (EditText)rootView. findViewById(R.id.editText8);
// read SharedPreferences data(token)
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String token = sharedPre.getString("token", "");
        string_login=sharedPre.getString("username", "");
        string_password=sharedPre.getString("password", "");
        string_token=token;
        string_json_profile=sharedPre.getString("string_json_profile", "");
        System.out.println("Profile : " + token);
       // get
        dojson();
     //   new GetClass().execute();


        return rootView;
    }
    public void dojson(){
        try {
            JSONObject  jsonObject = new JSONObject(string_json_profile);

            int id = Integer.parseInt(jsonObject.optString("id").toString());
            String email = jsonObject.optString("email").toString();
            String nom = jsonObject.optString("nom").toString();
            String prenom = jsonObject.optString("prenom").toString();
            String tel = jsonObject.optString("numerotel").toString();
            etnom.setText(nom);
            etprenom.setText(prenom);
            etemail.setText(email);
            ettel.setText(tel);
            //valide
            button=(Button)rootView.findViewById(R.id.button11);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check 2 pwd same
                    if(etpwd1.getText().toString().equals(etpwd2.getText().toString())) {
                        //change json
                        try {
                            JSONObject jsonObject_new = new JSONObject();
                            JSONObject jsonObject_new2 = new JSONObject(string_json_profile);
                            jsonObject_new2.put("nom", etnom.getText().toString());
                            jsonObject_new2.put("prenom", etprenom.getText().toString());
                            jsonObject_new2.put("numerotel", ettel.getText().toString());
                            jsonObject_new.put("profil",jsonObject_new2);
                            string_json_new = jsonObject_new.toString();
                            System.out.println("Profile-get-string_json_new : " + string_json_new);//newjson
                            //#########POST########post changed profile without psw######
                            new Postjson().execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        dialog();
                    }


                }
            });




        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
//    //################GET################
//    private class GetClass extends AsyncTask<String, String, String>
//    {
//        ProgressDialog pdLoading = new ProgressDialog(getActivity());
//        HttpURLConnection conn;
//        URL url = null;
//
//        @Override
//        protected void onPreExecute() {
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
//
//                // Enter URL address where your php file resides
//                url = new URL( getResources().getString(R.string.url1)+"/api/user/info");
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
//                System.out.println("Profile-get-token1:"+get_token);
//                get_token=string_token.replace("\"token\":\"","").replace("\"","");
//                System.out.println("Profile-get-token2:" + get_token);
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
//                System.out.println("Profile-get:"+response_code);//200 or 401
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
//
//                        string_json=txtResult;
//
//                    }
//
//
//
//                    // Pass data to onPostExecute method
//                    //return(result.toString());
//                    System.out.println("Profile-get: " + txtResult);//get json
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
//            rootView.setVisibility(View.VISIBLE);
//            pdLoading.dismiss();
//            System.out.println("Profile-get: " + result);//get json
//
//            if(result.equalsIgnoreCase(string_json))
//            {
//                /* Here launching another activity when login successful. If you persist login state
//                use sharedPreferences of Android. and logout button to clear sharedPreferences.
//                 */
//                try {
//                    JSONObject  jsonObject = new JSONObject(string_json);
//
//                    int id = Integer.parseInt(jsonObject.optString("id").toString());
//                    String email = jsonObject.optString("email").toString();
//                    String nom = jsonObject.optString("nom").toString();
//                    String prenom = jsonObject.optString("prenom").toString();
//                    String tel = jsonObject.optString("numerotel").toString();
//                    etnom.setText(nom);
//                    etprenom.setText(prenom);
//                    etemail.setText(email);
//                    ettel.setText(tel);
//                    //valide
//                    button=(Button)rootView.findViewById(R.id.button11);
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //check 2 pwd same
//                            if(etpwd1.getText().toString().equals(etpwd2.getText().toString())) {
//                                //change json
//                                try {
//                                    JSONObject jsonObject_new = new JSONObject(string_json);
//                                    jsonObject_new.put("nom", etnom.getText().toString());
//                                    jsonObject_new.put("prenom", etprenom.getText().toString());
//                                    jsonObject_new.put("numerotel", ettel.getText().toString());
//                                    string_json_new = jsonObject_new.toString();
//                                    System.out.println("Profile-get-string_json_new : " + string_json_new);//newjson
//                                    //#########POST########post changed profile without psw######
//                                    new Postjson().execute();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }else {
//                                dialog();
//                            }
//
//
//                        }
//                    });
//
//
//
//
//                } catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }
//
//
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
    private void dialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Attention");
        builder.setMessage("Vous devez saisir un mot de passe identique");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.create().show();
    }
    //##########POST new json###########
    private class Postjson extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tChargement...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL(getResources().getString(R.string.url1)+"/api/users/infos");
                System.out.println("Profile-post-url:"+getResources().getString(R.string.url1)+"/api/user/info");

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

                get_token=string_token.replace("\"token\":\"","").replace("\"","");
                System.out.println("profile-post-token:" + get_token);
                conn.setRequestProperty("Authorization", "Bearer " +get_token);//conn.setRequestProperty("Authorization", "Bearer " + get_token);

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(string_json_new);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

                System.out.println("profile-post- string_json_new:" + string_json_new);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            String txtResult = "";

            try {

                int response_code = conn.getResponseCode();
                System.out.println("profile-post-:response_code:"+response_code);//200
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
                        System.out.println("profile-post :respose Result " + txtResult);//json add note
                        string_json_newget=txtResult;
                    }



                    // Pass data to onPostExecute method

                    return(result.toString());

                }else if (response_code == 401){//########token invalide#############
                    //post login password again
                  //  postagain();

                   // return postagain2();//try new token and return json to excute
                    return("unsuccessful 401...");
                }else {
                    return("unsuccessful 403 404 405 500...");
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


            pdLoading.dismiss();
            System.out.println("profile-post :result " + result);//json add note

            if(result.equalsIgnoreCase(string_json_newget))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */

        //        Toast.makeText(getActivity(), "successful....", Toast.LENGTH_LONG).show();
//save new nom for page utilisateur
                try {
                    JSONObject jsonObject = new JSONObject(string_json_newget);


                    String nom = jsonObject.optString("nom").toString();
                    String prenom = jsonObject.optString("prenom").toString();


                    savename(getActivity(), nom, prenom, string_json_newget);
//                    //change page to emotion(page accueil)
//                    FragmentManager fragmentManager = getFragmentManager();
//                    FragmentTransaction transaction = fragmentManager.beginTransaction();
//                    transaction.replace(R.id.content_utilisateur, new emotion());
//                    transaction.addToBackStack(null);
//                    transaction.commit();
                    dialog_changeprofil();

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }else if (result.equalsIgnoreCase("exception")){
                Toast.makeText(getActivity(), "Invalid email or password.", Toast.LENGTH_LONG).show();

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else  {

                Toast.makeText(getActivity(), "unsuccessful....", Toast.LENGTH_LONG).show();

            }
        }

    }
    //save username password token
    public static void savename(Context context,String nom,String prenom,String json){
        // get SharedPreferences object
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(context);;
        //get Editor object
        SharedPreferences.Editor editor=sharedPre.edit();

        editor.putString("nom", nom);
        editor.putString("prenom",prenom);
        editor.putString("string_json_profile",json);


        editor.commit();
    }
    private void dialog_changeprofil(){
        final Dialog d2 = new Dialog(getActivity());


        d2.setContentView(R.layout.dialog_changeprofil);

        d2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv_ok= (TextView) d2.findViewById(R.id.tv_ok);


        d2.setCancelable(false);
        d2.show();


        tv_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                d2.dismiss();
            }
        });




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
                SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(getActivity());;
                //get Editor object
                SharedPreferences.Editor editor = sharedPre.edit();
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
            url3 = new URL( getResources().getString(R.string.url1)+"/api/user/info");

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
            System.out.println("getagin sharepre token : " + token);


            String get_token=string_token;//token
            get_token=string_token.replace("\"token\":\"","").replace("\"","");
            System.out.println("getagain-token2:" + get_token);

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

            System.out.println("getagain response_code:"+response_code);//200 or 401

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
                    System.out.println("getagain json: " + txtResult);
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

}

