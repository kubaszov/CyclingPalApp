package com.szentesi.david.cyclingpalapp;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

public class HomeScreenActivity extends AppCompatActivity implements HomeScreenFragment1.OnFragmentInteractionListener {

    // to implement fragment need to do the following
    // http://stackoverflow.com/questions/24777985/how-to-implement-onfragmentinteractionlistener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        // calling HomeScreenFragment1
        // http://stackoverflow.com/questions/5159982/how-do-i-add-a-fragment-to-an-activity-with-a-programmatically-created-content-v
        if(savedInstanceState == null) {
            Fragment newFragment = new HomeScreenFragment1();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.activity_home_screen, newFragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
