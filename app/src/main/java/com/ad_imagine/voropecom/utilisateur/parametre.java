package com.ad_imagine.voropecom.utilisateur;

/**
 * Created by chu on 2016/6/7.
 */
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ad_imagine.voropecom.R;

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

public class parametre extends Fragment {
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private String string_token;
    private String string_json;
    private String string_json_new;
    private String string_login;
    private String string_password;
    String txtResult;
    int permission=0;
    Switch switch1,switch2,switch3,switch4;
    public parametre() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.parametre, container, false);
        // read SharedPreferences data(token)
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String token = sharedPre.getString("token", "");
        string_token=token;
        string_login=sharedPre.getString("username", "");
        string_password=sharedPre.getString("password", "");

        Button button=(Button)rootView.findViewById(R.id.button9);
        switch1 = (Switch)rootView. findViewById(R.id.switch1);
        switch2 = (Switch)rootView. findViewById(R.id.switch2);
        switch3 = (Switch)rootView. findViewById(R.id.switch3);
        switch4 = (Switch)rootView. findViewById(R.id.switch4);
        //Set a CheckedChange Listener for Switch Button
        /*if switch1 off, 2 unclickable  if3 off, 4 unclickable*/
        switch2.setEnabled(false);//switch2 unclickable for initial
        switch4.setEnabled(false);//switch2 unclickable
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                if (on) {
                    switch2.setEnabled(true);//switch2 clickable

                } else {
                    switch2.setChecked(false);
                    switch2.setEnabled(false);//switch2 unclickable

                }
            }
        });
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                if (on) {
                    switch4.setEnabled(true);//switch2 clickable

                } else {
                    switch4.setChecked(false);
                    switch4.setEnabled(false);//switch2 unclickable

                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //permission:0000-->0   0010-->2  1111-->15
              permission=8*changeboolean(switch1.isChecked())+4*changeboolean(switch2.isChecked())+2*changeboolean(switch3.isChecked())+changeboolean(switch4.isChecked());
                System.out.println("permission:"+permission);
                try {
                JSONObject newjsonObjectVOR = new JSONObject();
                JSONObject newjsonObjectVOR2 = new JSONObject();
                newjsonObjectVOR2.put("permission",permission);
                newjsonObjectVOR.put("permission",newjsonObjectVOR2);
                string_json_new=newjsonObjectVOR.toString();
                    System.out.println("json post  : " + newjsonObjectVOR.toString());//POST json
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

             new postpermission().execute();

            }
        });
        return rootView;
    }
    public int changeboolean(boolean b){
        if(b){
            return 1;
        }else  {
            return 0;
        }
    }
    //##########POST new json###########
    private class postpermission extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //hide fragment
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment f = getActivity().getFragmentManager().findFragmentByTag("tag_emotion");
            ft.hide(f);
            ft.commit();

            //this method will be running on UI thread
            pdLoading.setMessage("\tChargement...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL(getResources().getString(R.string.url1)+"/api/permissions");


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

                System.out.println("Test-token1:" + get_token);
                get_token=string_token.replace("\"token\":\"","").replace("\"","");
                System.out.println("Test-token2:" + get_token);
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

                System.out.println("Post-0 string_json_new:"+string_json_new);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            txtResult = "";

            try {

                int response_code = conn.getResponseCode();
                System.out.println("Post-1:response_code:"+response_code);//200
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
                        System.out.println("Post-2 :respose Result " + txtResult);

                    }



                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("Post-3 : " + txtResult);//token
                    return(result.toString());

                }else if (response_code == 401){//########token invalide#############
                    //post login password again
                   // postagain();

                  //  return postagain2();//try new token and return json to excute
                    return("unsuccessful 401...");

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


            pdLoading.dismiss();
            System.out.println("Post-4 :result " + result);//json

            if(result.equalsIgnoreCase( txtResult))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
              //  Toast.makeText(getActivity(), "successful....", Toast.LENGTH_LONG).show();
//                //change page to emotion(page accueil)
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.content_utilisateur, new emotion());
//                transaction.addToBackStack(null);
//                transaction.commit();

                 dialog_changeparametre();

            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getActivity(), "OOPs! Something went wrong.", Toast.LENGTH_LONG).show();

            }
        }

    }
    private void dialog_changeparametre(){
        final Dialog d2 = new Dialog(getActivity());


        d2.setContentView(R.layout.dialog_changeparametre);

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
}

