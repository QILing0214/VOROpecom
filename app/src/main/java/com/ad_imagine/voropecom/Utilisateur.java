package com.ad_imagine.voropecom;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;


import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ad_imagine.voropecom.chefteam.Chefteams;
import com.ad_imagine.voropecom.chefteam.FicheTeam;
import com.ad_imagine.voropecom.chefteam.GestionTeam;
import com.ad_imagine.voropecom.chefteam.VorTeam;
import com.ad_imagine.voropecom.utilisateur.coach;
import com.ad_imagine.voropecom.utilisateur.emotion;
import com.ad_imagine.voropecom.utilisateur.historique;
import com.ad_imagine.voropecom.utilisateur.parametre;
import com.ad_imagine.voropecom.utilisateur.process;
import com.ad_imagine.voropecom.utilisateur.profile;

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

/**Sliding menu
 * - Dans le menu mobile: renommer "VOR Autre" en "Paramètres",
 * Faire passer "Historique" dans "VOR Emotion" et "VOR Process",
 * Remplacer "Paramètre" par "Confidentialité"
 * **/
public class Utilisateur extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private String string_token;
    private String string_json;
    private String string_login;
    private String string_password;
    private String string_nom;
    private String string_prenom;
    Toolbar toolbar;
    TextView tv_title;
    ImageView toolbar_image;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utilisateur);
        // read SharedPreferences data(token)
        SharedPreferences sharedPre = PreferenceManager.getDefaultSharedPreferences(this);
        string_token = sharedPre.getString("token", "");
        string_login=sharedPre.getString("username", "");
        string_password=sharedPre.getString("password", "");
        string_nom=sharedPre.getString("nom", "");
        string_prenom=sharedPre.getString("prenom", "");
        string_json=sharedPre.getString("string_json_emotion", "");

        System.out.println("Utilisateur-1 : " + string_token);
        // get
     //   new GetClass().execute();


//set  Toolbar likes actionbar,define in app_bar_coach.xml
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_image = (ImageView) toolbar.findViewById(R.id.toolbar_image);

        StartEmotion();//start or change coach accueil for defaut view





    //############   change  navigation menu   normally type=0  ##############
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        int type=0;
        if(type==1)
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.utilisateur_noconfidentialite);
        } else if(type==2)
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.utilisateur_chefteam);
        }
//navigation process or emotion
        try {
            JSONObject jsonRootObject = new JSONObject(string_json);
            String isprocess = jsonRootObject.optString("is_process").toString();
            System.out.println("Utilisateur : isprocess:" + isprocess);

            if (Boolean.valueOf(isprocess) == true) {
                //change menu emotion---->process if(is_process==true)
                //Menu menuNav = navigationView.getMenu();
                MenuItem menu_emotion = navigationView.getMenu().findItem(R.id.nav_emotion_title);
                menu_emotion.setTitle("Process");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//    navigation header layout   normally type=0
       View headerLayout = navigationView.getHeaderView(0);
       final TextView tv_headername = (TextView) headerLayout.findViewById(R.id.name);
        tv_headername.setText(string_prenom+" "+string_nom);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //##three lines for open and close drawer##
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
                SharedPreferences sharedPre = PreferenceManager.getDefaultSharedPreferences(Utilisateur.this);

                string_nom=sharedPre.getString("nom", "");
                string_prenom=sharedPre.getString("prenom", "");
                tv_headername.setText(string_prenom+" "+string_nom);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        // get menu from navigationView and change munu style
        Menu menu = navigationView.getMenu();

        MenuItem nav_emotion = menu.findItem(R.id.nav_emotion_title);
        SpannableString spanString = new SpannableString(nav_emotion.getTitle().toString());
        int end = spanString.length();
        spanString.setSpan(new RelativeSizeSpan(1.2f), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0);
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        spanString.setSpan(bss, 0,end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        nav_emotion.setTitle(spanString);


        MenuItem nav_parametre = menu.findItem(R.id.nav_parametre_title);
        SpannableString spanString2 = new SpannableString(nav_parametre.getTitle().toString());
        int end2 = spanString2.length();
        spanString2.setSpan(new RelativeSizeSpan(1.2f), 0, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString2.length(), 0);
        final StyleSpan bss2 = new StyleSpan(android.graphics.Typeface.BOLD);
        spanString2.setSpan(bss2, 0,end2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        nav_parametre.setTitle(spanString2);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    //right menu（like setting）to return to connection
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.utilisateur, menu);

        return true;
    }

    @Override
    //menu listenner to change page to Mainactivity
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, MainActivity.class);        //change page to connection
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
           // System.exit(0);//clear all other activity
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_emotion) {
            // change to frsgment emotion
            StartEmotion();

        }  else if (id == R.id.nav_historique) {
            // change to frsgment historique
            StartHistorique();

        } else if (id == R.id.nav_parametre) {
            // change to frsgment parametre
            StartParametre();

        } else if (id == R.id.nav_profil) {
            // change to frsgment profil
            StartProfil();

        } else if (id == R.id.nav_coach) {
            // change to frsgment coach
            StartCoach();

        } else if (id == R.id.nav_vorteam) {
            // change to frsgment parametre
            StartVorTeam();

        } else if (id == R.id.nav_ficheTeam) {
            // change to frsgment profil
            StartFicheTeam();

        } else if (id == R.id.nav_gestionTeam) {
            // change to frsgment coach
            StartGestionTeam();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // fonction for change the fragment


    private void changeToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_utilisateur, fragment,"tag_emotion");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void StartEmotion() {
        changeToFragment(new emotion());
        try {
            getSupportActionBar().setTitle(null);
            tv_title.setVisibility(View.GONE);
            toolbar_image.setVisibility(View.VISIBLE);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void changeToFragment2(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_utilisateur, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void StartProcess() {
        changeToFragment2(new process());
        try {
            getSupportActionBar().setTitle(null);
            tv_title.setVisibility(View.GONE);
            toolbar_image.setVisibility(View.VISIBLE);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void changeToFragment3(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_utilisateur, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void StartHistorique() {
        changeToFragment3(new historique());
        try {
            getSupportActionBar().setTitle(null);
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText("Historique");
            toolbar_image.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void changeToFragment4(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_utilisateur, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void StartParametre() {
        changeToFragment4(new parametre());
        try {
            getSupportActionBar().setTitle(null);
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText("Confidentialité");
            toolbar_image.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void changeToFragment5(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_utilisateur, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void StartProfil() {
        changeToFragment5(new profile());
        try {
            getSupportActionBar().setTitle(null);
            tv_title.setVisibility(View.GONE);
            toolbar_image.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void changeToFragment6(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_utilisateur, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void StartCoach() {
        changeToFragment6(new coach());
        try {
            getSupportActionBar().setTitle(null);
            tv_title.setVisibility(View.GONE);
            toolbar_image.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private void changeToFragment7(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_utilisateur, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void StartVorTeam() {
        changeToFragment7(new Chefteams());//or new VorTeam()
        try {
            getSupportActionBar().setTitle(null);
            tv_title.setVisibility(View.VISIBLE);
            toolbar_image.setVisibility(View.GONE);
            tv_title.setText("VOR de la Team");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private void changeToFragment8(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_utilisateur, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void StartFicheTeam() {
        changeToFragment8(new FicheTeam());
        try {
            getSupportActionBar().setTitle(null);
            tv_title.setVisibility(View.VISIBLE);
            toolbar_image.setVisibility(View.GONE);
            tv_title.setText("VOR de la Team");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private void changeToFragment9(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_utilisateur, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void StartGestionTeam() {
        changeToFragment9(new GestionTeam());
        try {
            getSupportActionBar().setTitle(null);
            tv_title.setVisibility(View.VISIBLE);
            toolbar_image.setVisibility(View.GONE);
            tv_title.setText("Gestion de la Team");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    //    //################GET################
//    private class GetClass extends AsyncTask<String, String, String> {
//        ProgressDialog pdLoading = new ProgressDialog(Utilisateur.this);
//        HttpURLConnection conn;
//        URL url = null;
//
//        @Override
//        protected void onPreExecute() {
//
//            super.onPreExecute();
//
//            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading Utilisateur...");
//            pdLoading.setCancelable(false);
//            pdLoading.show();
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//
//                // Enter URL address where your php file resides
//                url = new URL(getResources().getString(R.string.url_vor) + "/api/vor/new");
//
//            } catch (MalformedURLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                return "exception1";
//            }
//            try {
//                // Setup HttpURLConnection class to send and receive data from php and mysql
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(READ_TIMEOUT);
//                conn.setConnectTimeout(CONNECTION_TIMEOUT);
//                conn.setRequestMethod("GET");
//                String get_token = string_token;//token
//
//                System.out.println("Utilisateur-token1:" + get_token);
//                get_token = string_token.replace("\"token\":\"", "").replace("\"", "");
//                System.out.println("Utilisateur-token2:" + get_token);
//
//                conn.setRequestProperty("Authorization", "Bearer " + get_token);
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
//                System.out.println("Utilisateur1:" + response_code);//200 or 401
//
//                // Check if successful connection made
//                if (response_code == HttpURLConnection.HTTP_OK) {
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
//                        System.out.println("Utilisateur2 get: " + txtResult);
//                        string_json = txtResult;
//
//                    }
//
//
//                    // Pass data to onPostExecute method
//                    //return(result.toString());
//                    System.out.println("Utilisateur3 : " + txtResult);//get json
//                    return (result.toString());
//
//                } else if (response_code == 401){//########token invalide#############
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
//
//            pdLoading.dismiss();
//            System.out.println("Utilisateur4 : " + result);//get json
//
//            if (result.equalsIgnoreCase(string_json)) {
//                /* Here launching another activity when login successful. If you persist login state
//                use sharedPreferences of Android. and logout button to clear sharedPreferences.
//                 */
//                try {
//                    JSONObject jsonRootObject = new JSONObject(string_json);
//                    String isprocess = jsonRootObject.optString("is_process").toString();
//                    System.out.println("Utilisateur : isprocess:" + isprocess);
//
//                    if (Boolean.valueOf(isprocess) == true) {
//                        //change menu emotion---->process if(is_process==true)
//                        //Menu menuNav = navigationView.getMenu();
//                        MenuItem menu_emotion = navigationView.getMenu().findItem(R.id.nav_emotion_title);
//                        menu_emotion.setTitle("Process");
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            } else if (result.equalsIgnoreCase("false")) {
//
//                // If username and password does not match display a error message
//                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();
//
//            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
//
//                Toast.makeText(Utilisateur.this, "get wrong", Toast.LENGTH_LONG).show();
//
//            }
//        }
//    }
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
            System.out.println("Utilisateur-postagain response_code:"+response_code);//200

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
                    System.out.println("Utilisateur-postagain token : " + txtResult);//token
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
            url = new URL(getResources().getString(R.string.url_vor) + "/api/vor/new");

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

            System.out.println("Utilisateur-getagain-token1:"+get_token);
            get_token=string_token.replace("\"token\":\"","").replace("\"","");
            System.out.println("Utilisateur-getagain-token2:"+get_token);

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

            System.out.println("Utilisateur-getagain:"+response_code);//200 or 401

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
                    System.out.println("Utilisateur-getagain get json: " + txtResult);
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
