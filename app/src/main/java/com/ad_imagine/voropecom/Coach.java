package com.ad_imagine.voropecom;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;


import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ad_imagine.voropecom.fragment_coach.coach_accueil;
import com.ad_imagine.voropecom.fragment_coach.coach_coache;
import com.ad_imagine.voropecom.fragment_coach.coach_requetage;

/**Sliding menu for Coach**/
public class Coach extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//Toolbar likes actionbar,define in app_bar_coach.xml
        setSupportActionBar(toolbar);
        tv_title=(TextView)toolbar.findViewById(R.id.toolbar_title);

        StartAccueil();//start coach accueil for defaut view


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //three lines for open and close drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.coach, menu);
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
            Intent i = new Intent(this,MainActivity.class);        //change page to connection
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            System.exit(0);//clear all activity

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

        if (id == R.id.nav_accueil) {
            // change to frsgment accueil
            StartAccueil();

        } else if (id == R.id.nav_coache) {
            // change to frsgment coache
            StartCoache();

        } else if (id == R.id.nav_requetage) {
            // change to frsgment requetage
            StartRequetage();

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // fonction for change the fragment


    private void changeToFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_coach, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void StartAccueil() {
        changeToFragment(new coach_accueil());
        try {

            getSupportActionBar().setTitle(null);
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText("Accueil");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private void changeToFragment2(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_coach, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void StartCoache() {
        changeToFragment2(new coach_coache());
        try {
            getSupportActionBar().setTitle(null);
            //tv_title.setVisibility(View.VISIBLE);
            tv_title.setText("VOR de mes coaches");


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private void changeToFragment3(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_coach, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void StartRequetage() {
        changeToFragment3(new coach_requetage());
        try {
            getSupportActionBar().setTitle(null);
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText("Requetage");

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
