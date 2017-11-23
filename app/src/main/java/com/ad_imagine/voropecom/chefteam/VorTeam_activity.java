package com.ad_imagine.voropecom.chefteam;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ad_imagine.voropecom.MainActivity;
import com.ad_imagine.voropecom.R;
import com.ad_imagine.voropecom.fragment_coach.coache_historique;
import com.ad_imagine.voropecom.utilisateur.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VorTeam_activity extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private String string_token;
    private String string_json_teamid;
    private String string_json_userid;
    ArrayList<String>string_userid= new ArrayList<String>();

    private List<VorTeam_item> listSliding=new ArrayList<VorTeam_item>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vorteam_main);
        // read SharedPreferences data(token)
        SharedPreferences sharedPre= PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPre.getString("token", "");
        string_token=token;

        System.out.println("Vorteam sharepre token : " + token);

        /***get note from last page and change note note color and comment***/
        Intent intent = getIntent();
        string_json_teamid = intent.getStringExtra("string_json_teamid");
        System.out.println("Vorteam string_json_teamid : " + string_json_teamid);

        //Init component
        GridView listViewSliding= (GridView)findViewById(R.id.grid);
        try {
            final JSONObject jsonRootObject = new JSONObject(string_json_teamid);
            JSONArray jsonArrayUser = jsonRootObject.optJSONArray("users");

            //Add 10 item for list
        for(int i = 0;i<jsonArrayUser.length();i++) {
            //Random  0--9
            float f = new Random().nextFloat() * 9;
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(2);
            df.setDecimalSeparatorAlwaysShown(true);
            //add item
            String string_nom=jsonArrayUser.getJSONObject(i).optString("nom").toString();
            String string_prenom=jsonArrayUser.getJSONObject(i).optString("prenom").toString();
           string_userid.add(jsonArrayUser.getJSONObject(i).optString("id").toString());

            listSliding.add(new VorTeam_item("" + Float.parseFloat(df.format(f).replace(',', '.')), string_prenom + " " + string_nom));

        }



        VorTeam_adapter adapter = new VorTeam_adapter(this, R.layout.vorteam_listviewcontent,listSliding);

        listViewSliding.setAdapter(adapter);
        //Hanlde on item click
        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("OnClick", "position = " + position);
                System.out.println("Userid: " + string_userid.get(position));

                new GetUserHis().execute(string_userid.get(position));//user history by id



            }
        });


        //action bar set return button and background and title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.wallpaper_toolbar));
        actionBar.setTitle("Team");


        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Chefteam: exception json");
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    //################GET json Note################
    private class GetUserHis extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(VorTeam_activity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            System.out.println("UserbyId-onPreExecute : ");
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {
                System.out.println("UserbyId-get-doInBackground : ");
                // Enter URL address where your php file resides
                url = new URL( getResources().getString(R.string.url1)+"/api/users/"+params[0]+"/vors");
                System.out.println("Test-URL-Id:"+getResources().getString(R.string.url_vor)+"/api/users/"+params[0]+"/vors");
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

                System.out.println("UserbyId-get-token1:"+get_token);
                get_token=string_token.replace("\"token\":\"","").replace("\"","");
                System.out.println("UserbyId-get-token2:"+get_token);

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

                System.out.println("UserbyId-get-11:"+response_code);//200 or 401

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
                        System.out.println("UserbyIdget-12 get json: " + txtResult);
                        string_json_userid=txtResult;

                    }



                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("UserbyId-get-Result: " + txtResult);//get json
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
            System.out.println("UserbyIdget-14 : " + result);//get json

            if(result.equalsIgnoreCase(string_json_userid))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Intent intent = new Intent(VorTeam_activity.this,coache_historique.class);
                intent.putExtra("string_json_userid",string_json_userid);
                startActivity(intent);

            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(VorTeam_activity.this, "get wrong", Toast.LENGTH_LONG).show();

            }
        }

    }

}
