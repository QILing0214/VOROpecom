package com.ad_imagine.voropecom.chefteam;



        import android.app.DatePickerDialog;
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.app.Fragment;
        import android.preference.PreferenceManager;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.ad_imagine.voropecom.R;
        import com.ad_imagine.voropecom.utilisateur.Note;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.text.DecimalFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.Random;

/**
 * add relativelayout of each team to fragment
 * use coache_example.xml  as relativelayout.
 */
public class Chefteams extends Fragment {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private String string_token;
    private String string_json_team, string_json_teamid;
    private String string_id;

    TextView text_note;


    public Chefteams() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.team_chefteams, container, false);
// read SharedPreferences data(token)
        SharedPreferences sharedPre = PreferenceManager.getDefaultSharedPreferences(getActivity());

        string_token = sharedPre.getString("token", "");

        string_json_team = sharedPre.getString("string_json_team", "");
        System.out.println("Chefteam : " + string_token);
        System.out.println("Chefteam : " + string_json_team);
        // get
        //       dojson();
        //   new GetClass().execute();


        /***add view similair for chefteams***/
//   example to add view:
//        RelativeLayout relativeLayout1=new RelativeLayout(getActivity());
//        TextView mTextView = new TextView(getActivity());
//        mTextView.setText("3.0");
//        mTextView.setBackgroundColor(Color.parseColor("#25ac39"));
//        mTextView.setTextColor(Color.parseColor("#ffffff"));
//        relativeLayout1.addView(mTextView);


        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.relativeLayout);


        try {
            final JSONArray jsonArrayObject = new JSONArray(string_json_team);


            // use "for" to add 10 viewa
            for (int i = 0; i < jsonArrayObject.length(); i++) {
                JSONObject jsonObject = jsonArrayObject.getJSONObject(i);//{json object}
                String name = jsonObject.optString("nom").toString();
                final String json_id = jsonObject.optString("id").toString();


                View view2 = getActivity().getLayoutInflater().inflate(R.layout.vorteam_listviewcontent, null);

                TextView text_note = (TextView) view2.findViewById(R.id.tv_note);
                text_note.setText(name);


                //click listenner
                text_note.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {

                        //##### GET json_note for Team ID#############
                        new GetClassNote().execute(json_id);


                    }
                });
                //set parametre to add coache view


                RelativeLayout.LayoutParams LP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                if (i % 2 == 0) {
                    LP.leftMargin = 25 * (int) (Math.round(getActivity().getResources().getDisplayMetrics().density));//change 25dp to px and change float to int
                } else {
                    LP.leftMargin = 200 * (int) (Math.round(getActivity().getResources().getDisplayMetrics().density));
                }
                LP.topMargin = (20 + 150 * (i / 2)) * (int) (Math.round(getActivity().getResources().getDisplayMetrics().density));
                relativeLayout.addView(view2, LP);

            }   // "for" end

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Chefteam: exception json");
        }


        return rootView;
    }

    //################GET json Note################
    private class GetClassNote extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            System.out.println("TeamId-get-onPreExecute : ");
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                System.out.println("TeamId-get-doInBackground : ");
                // Enter URL address where your php file resides
                url = new URL(getResources().getString(R.string.url1) + "/api/teams/" + params[0]);
                System.out.println("Test-URL-Id:" + getResources().getString(R.string.url_vor) + "/api/teams/" + params[0]);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception1";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                String get_token = string_token;//token

                System.out.println("TeamId--get-token1:" + get_token);
                get_token = string_token.replace("\"token\":\"", "").replace("\"", "");
                System.out.println("TeamId--get-token2:" + get_token);

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

                System.out.println("TeamId--get-11:" + response_code);//200 or 401

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
                        System.out.println("TeamId-get-12 get json: " + txtResult);
                        string_json_teamid = txtResult;

                    }


                    // Pass data to onPostExecute method
                    //return(result.toString());
                    System.out.println("TeamId-get-13 : " + txtResult);//get json
                    return (result.toString());

//                }else if (response_code == 401){//########token invalide#############
//                    //post login password again
//                    postagain();
//
//                    return getagain();//try new token and return json to excute
//
                } else {
                    return ("unsuccessful 403 404 500...");
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
            System.out.println("TeamId-get-14 : " + result);//get json

            if (result.equalsIgnoreCase(string_json_teamid)) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Intent intent = new Intent(getActivity(), VorTeam_activity.class);
                intent.putExtra("string_json_teamid", string_json_teamid);
                startActivity(intent);

            } else if (result.equalsIgnoreCase("false")) {

                // If username and password does not match display a error message
                //Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).Show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getActivity(), "get wrong", Toast.LENGTH_LONG).show();

            }
        }

    }
}
