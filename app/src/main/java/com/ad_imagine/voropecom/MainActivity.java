package com.ad_imagine.voropecom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import android.widget.EditText;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private EditText etEmail;
    private EditText etPassword;
    String email;
    String password;
    ProgressDialog pdLoading;

    private String string_token;
    private String string_json_info;

    private String string_json_emotion;
    private String string_json_his;
    private String string_json_entre;
    private String string_json_team;
    private String string_json_depart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// page connection
        Button button=(Button)findViewById(R.id.button);
        etEmail = (EditText) findViewById(R.id.editText);
        etPassword = (EditText) findViewById(R.id.editText2);


       // underline and lien for inscrive
        TextView tv_inscire=(TextView)findViewById(R.id.textView_inscrire);
        tv_inscire.setClickable(true);
        tv_inscire.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://www.google.com'> Inscrivez-vous!</a>";
        tv_inscire.setText(Html.fromHtml(text));
        //SpannableString content = new SpannableString(tv_inscire.getText().toString());
       // content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        //content.setSpan(new URLSpan("http://www.google.com"), 0, content.length(), 0);
       // tv_inscire.setText(content);

//valide
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get text from email and passord field
                email = etEmail.getText().toString();
                 password = etPassword.getText().toString();

                // Initialize  AsyncLogin() class with email and password
                //#########POST########if responsecode=200,return token#######
                new AsyncLogin().execute(email,password);



            }
        });
//delete coach
        Button button2=(Button)findViewById(R.id.button_coach);
        button2.setVisibility(View.GONE);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Coach.class);
                 startActivity(intent);
            }
        });


    }
    //######Fonction for POST#######
    private class AsyncLogin extends AsyncTask<String, String, String>
    {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading = new ProgressDialog(MainActivity.this);
            //this method will be running on UI thread
            pdLoading.setMessage("\tChargement...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL( getResources().getString(R.string.url1)+"/api/login_check");
                System.out.println("MainActivity-post-url:"+getResources().getString(R.string.url1)+"/api/login_check");

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
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("_username", params[0])
                        .appendQueryParameter("_password", params[1]);
                String query = builder.build().getEncodedQuery();
                System.out.println("MainActivity-post:"+query);
                //  String data = "_username=lorem@ipsum.fr";
                // data += "&_password=aze";

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

                System.out.println("MainActivity-post-0:"+query);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            String txtResult = "";

            try {

                int response_code = conn.getResponseCode();
                System.out.println("MainActivity-post-response_code:"+response_code);//200

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {     //200



                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                        txtResult = result.toString();
                        System.out.println("MainActivity-post-result1 : " + txtResult);//token
                        string_token=txtResult;
                    }



                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("MainActivity-post-result2 : " + txtResult);//token
                    return(result.toString());

                }else{

                    return("unsuccessful");
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

           //pdLoading.hide();
            System.out.println("MainActivity-post-result3 : " + result);//token

            if(result.equalsIgnoreCase(string_token))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */

                saveLoginInfo(MainActivity.this, email, password, string_token);
                //Get json to emotion
                new GetClassEmotion().execute();
                new GetClassHis().execute();
                //Get json to choose type /give nome to page utilisateur
                //###get role###
                int type=2;//chef entreprise
                if(type==4){
                    new GetClassChefEntr().execute();
                }else if(type==2){
                    new GetClassChefTeam().execute();
                }else if(type==3){
                    new GetClassChefDepart().execute();
                }
                new GetClassProfile().execute();
              //  pdLoading.dismiss();



            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MainActivity.this, "wrong username or password ", Toast.LENGTH_LONG).show();

            }
        }

    }
    //################GET profile################
    private class GetClassProfile extends AsyncTask<String, String, String>
    {
     //  ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            //this method will be running on UI thread
//           pdLoading.setMessage("\tLoading...");
//           pdLoading.setCancelable(false);
//            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL( getResources().getString(R.string.url1)+"/api/user/info");

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


                get_token=string_token.replace("\"token\":\"","").replace("\"","");
                System.out.println("mainactivity-get-token:"+get_token);

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

                System.out.println("mainactivity-get:"+response_code);//200 or 401

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

                        string_json_info=txtResult;

                    }



                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("Profil-get-json: " + txtResult);//get json
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

        @Override
        protected void onPostExecute(String result) {


            pdLoading.dismiss();//#################

            System.out.println("Profil: " + result);//get json

            if(result.equalsIgnoreCase(string_json_info))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                try {
                    JSONObject jsonObject = new JSONObject(string_json_info);
                    String nom = jsonObject.optString("nom").toString();
                    String prenom = jsonObject.optString("prenom").toString();


                    savename(MainActivity.this, nom, prenom, string_json_info);
                    //next page after data saves
                    int i=0;
                    if(i==0) {
                        Intent intent = new Intent(MainActivity.this, Utilisateur.class);
                        startActivity(intent);
                    }
                    if(i==1) {
                        Intent intent = new Intent(MainActivity.this, Utilisateur.class);// to page coach
                        startActivity(intent);
                    }
//                if(i==2) {
//                    Intent intent = new Intent(MainActivity.this, ChefDepartment.class);
//                    startActivity(intent);
//                }


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }




            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MainActivity.this, "get wrong", Toast.LENGTH_LONG).show();

            }
        }

    }
    //save username password token
    public static void saveLoginInfo(Context context,String username,String password,String token){
        // get SharedPreferences object
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(context);;
        //get Editor object
        SharedPreferences.Editor editor=sharedPre.edit();

        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("token",token);//editor.putString("token", token);

        editor.commit();
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
    //save string_json_emotion
    public static void savejsonemotion(Context context,String json){
        // get SharedPreferences object
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(context);;
        //get Editor object
        SharedPreferences.Editor editor=sharedPre.edit();

        editor.putString("string_json_emotion", json);



        editor.commit();
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
    //save string_json_entre
    public static void savejsonentre(Context context,String json){
        // get SharedPreferences object
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(context);;
        //get Editor object
        SharedPreferences.Editor editor=sharedPre.edit();

        editor.putString("string_json_entre", json);



        editor.commit();
    }
    //save string_json_entre
    public static void savejsonteam(Context context,String json){
        // get SharedPreferences object
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(context);;
        //get Editor object
        SharedPreferences.Editor editor=sharedPre.edit();

        editor.putString("string_json_team", json);



        editor.commit();
    }
    //save string_json_entre
    public static void savejsondepart(Context context,String json){
        // get SharedPreferences object
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(context);;
        //get Editor object
        SharedPreferences.Editor editor=sharedPre.edit();

        editor.putString("string_json_depart", json);



        editor.commit();
    }
    //################GET data emotion################
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
                        string_json_emotion=txtResult;

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

            if(result.equalsIgnoreCase(string_json_emotion))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                savejsonemotion(MainActivity.this, string_json_emotion);
//####################get json/button valide/ post####################
               // getjson();
//############################################


            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MainActivity.this, "get wrong", Toast.LENGTH_LONG).show();

            }
        }

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

            // pdLoading.dismiss();
            System.out.println("his-get : " + result);//get return (json or exception)

            if(result.equalsIgnoreCase(string_json_his))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                savejsonhis(MainActivity.this, string_json_his);
//####################get json/button valide/ post####################
                // getjson();
//############################################


            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MainActivity.this, "get wrong", Toast.LENGTH_LONG).show();

            }
        }

    }
    //################GET data chef Entreprise################
    private class GetClassChefEntr extends AsyncTask<String, String, String>
    {
        // ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            System.out.println("entr-onPreExecute : ");
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
                System.out.println("entr-21 : ");
                // Enter URL address where your php file resides
                url = new URL( getResources().getString(R.string.url1)+"/api/entreprises");

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

                System.out.println("entr-get-token1:"+get_token);
                get_token=string_token.replace("\"token\":\"","").replace("\"","");
                System.out.println("entr--get-token2:" + get_token);

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

                System.out.println("entr-get:"+response_code);//200 or 401

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
                        System.out.println("entr- get: " + txtResult);
                        string_json_entre=txtResult;

                    }



                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("entr-get: " + txtResult);//get json
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
            System.out.println("entr-get : " + result);//get return (json or exception)

            if(result.equalsIgnoreCase(string_json_entre))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                savejsonentre(MainActivity.this, string_json_entre);



            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MainActivity.this, "get wrong", Toast.LENGTH_LONG).show();

            }
        }

    }
    //################GET data chef Team################
    private class GetClassChefTeam extends AsyncTask<String, String, String>
    {
        // ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            System.out.println("Team-onPreExecute : ");
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
                System.out.println("Team-21 : ");
                // Enter URL address where your php file resides
                url = new URL( getResources().getString(R.string.url1)+"/api/teams");

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

                System.out.println("Team-get-token1:"+get_token);
                get_token=string_token.replace("\"token\":\"","").replace("\"","");
                System.out.println("Team--get-token2:" + get_token);

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

                System.out.println("Team-get:"+response_code);//200 or 401

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
                        System.out.println("Team- get: " + txtResult);
                        string_json_team=txtResult;

                    }



                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("Team-get: " + txtResult);//get json
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
            System.out.println("Team-get : " + result);//get return (json or exception)

            if(result.equalsIgnoreCase(string_json_team))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                savejsonteam(MainActivity.this, string_json_team);



            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MainActivity.this, "get wrong", Toast.LENGTH_LONG).show();

            }
        }

    }
    //################GET data chef Department################
    private class GetClassChefDepart extends AsyncTask<String, String, String>
    {
        // ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            System.out.println("depart-onPreExecute : ");
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
                System.out.println("depart-21 : ");
                // Enter URL address where your php file resides
                url = new URL( getResources().getString(R.string.url1)+"/api/departements");

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

                System.out.println("depart-get-token1:"+get_token);
                get_token=string_token.replace("\"token\":\"","").replace("\"","");
                System.out.println("depart--get-token2:" + get_token);

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

                System.out.println("depart-get:"+response_code);//200 or 401

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
                        System.out.println("depart- get: " + txtResult);
                        string_json_depart=txtResult;

                    }



                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("depart-get: " + txtResult);//get json
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
            System.out.println("depart-get : " + result);//get return (json or exception)

            if(result.equalsIgnoreCase(string_json_depart))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                savejsondepart(MainActivity.this, string_json_depart);



            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MainActivity.this, "get wrong", Toast.LENGTH_LONG).show();

            }
        }

    }
}

