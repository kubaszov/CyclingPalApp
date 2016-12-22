package com.szentesi.david.cyclingpalapp;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// to implement fragment need to do the following
// http://stackoverflow.com/questions/24777985/how-to-implement-onfragmentinteractionlistener
public class HomeScreenActivity extends AppCompatActivity implements BMIFragment.OnFragmentInteractionListener {

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        bundle = getIntent().getExtras();
        // calling BMIFragment
        // http://stackoverflow.com/questions/5159982/how-do-i-add-a-fragment-to-an-activity-with-a-programmatically-created-content-v
        if(savedInstanceState == null) {
            Fragment bmiFragment = new BMIFragment();
            // sending the bundle to the BMIFragment
            // http://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android
            bmiFragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.activity_home_screen, bmiFragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
