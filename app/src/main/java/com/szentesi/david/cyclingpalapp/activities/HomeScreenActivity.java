package com.szentesi.david.cyclingpalapp.activities;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.szentesi.david.cyclingpalapp.fragments.BMIFragment;
import com.szentesi.david.cyclingpalapp.fragments.CalorieFragment;
import com.szentesi.david.cyclingpalapp.R;
import com.szentesi.david.cyclingpalapp.fragments.ProgressFragment;

import java.util.Date;
import java.util.Locale;

// to implement fragment need to do the following
// http://stackoverflow.com/questions/24777985/how-to-implement-onfragmentinteractionlistener
public class HomeScreenActivity extends AppCompatActivity implements
        BMIFragment.OnFragmentInteractionListener,
        CalorieFragment.OnFragmentInteractionListener,
        ProgressFragment.OnFragmentInteractionListener {

    private Bundle bundle;
    private static int NUMBER_OF_FRAGMENTS = 3;
    private ViewPager homeScreenViewPager;
    private PagerAdapter pa;
    private TabLayout tabLayout;
    private String email;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        sharedPreferences = this.getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        email = sharedPreferences.getString("emailContainer", null);
        bundle = getIntent().getExtras();
        bundle.putString("emailContainer", email);
        // if userWeightTable already has been initialised on the first run
        if(sharedPreferences.getAll().size() < 0) {
            initialiseUserWeightRecordTable();
        }
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        // naming the Tabs
        tabLayout.addTab(tabLayout.newTab().setText("BMI"));
        tabLayout.addTab(tabLayout.newTab().setText("Calories"));
        tabLayout.addTab(tabLayout.newTab().setText("Progress"));
        // adding colour to the Tabs
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setTabTextColors(Color.BLACK, Color.WHITE);
        // UI widget to allow users to swipe between fragments on the screen
        homeScreenViewPager = (ViewPager)findViewById(R.id.homeScreenViewPager);
        pa = new FragmentHomeScreenPagerAdapter(getSupportFragmentManager(), 0);
        homeScreenViewPager.setAdapter(pa);
        // addOnPageChangeListener to update selected Tab names when swiping between pages
        homeScreenViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // listen to user interaction on Tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                homeScreenViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        floatingActionMenu(this);
        }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    // how to set up fragmentsstatepageradapter
    // http://www.truiton.com/2013/05/android-fragmentstatepageradapter-example/

    private class FragmentHomeScreenPagerAdapter extends FragmentStatePagerAdapter {
        public FragmentHomeScreenPagerAdapter(android.support.v4.app.FragmentManager fm, int tabCounter) {
            super(fm);
        }

        // method to return the fragments created
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                BMIFragment bmiFragment = new BMIFragment();
                bundle = getIntent().getExtras();
                bmiFragment.setArguments(bundle);
                return bmiFragment;
            }
            else if (position == 1) {
                CalorieFragment calorieFragment = new CalorieFragment();
                bundle = getIntent().getExtras();
                calorieFragment.setArguments(bundle);
                return calorieFragment;
            }
            else if (position == 2) {
                ProgressFragment progressFragment = new ProgressFragment();
                bundle = getIntent().getExtras();
                progressFragment.setArguments(bundle);
                return progressFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUMBER_OF_FRAGMENTS;
        }
    }

    private void floatingActionMenu(Context context) {
        // creating floating menu icon
        final ImageView floatingMenuIcon = new ImageView(this);
        floatingMenuIcon.setImageResource(R.drawable.ic_action_new);
        int floatingMenuSize = getResources().getDimensionPixelSize(R.dimen.floating_action_button_size);
        int floatingMenuIconSize = getResources().getDimensionPixelSize(R.dimen.floating_action_menu_button_size);
        // adding a layout to the button which allows us to put an image on top of it
        com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.LayoutParams floatingMenuLayout =
                new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.LayoutParams(floatingMenuSize, floatingMenuSize);
        floatingMenuIcon.setLayoutParams(floatingMenuLayout);
        // adding the floating menu action button widget
        // taken from https://github.com/oguzbilgener/CircularFloatingActionMenu/blob/master/samples/src/main/java/com/oguzdev/circularfloatingactionmenu/samples/MenuWithFABActivity.java
        final com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton floatingActionButton =
                new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(this)
                        .setContentView(floatingMenuIcon)
                        .setBackgroundDrawable(R.drawable.button_action_blue)
                        .setLayoutParams(floatingMenuLayout)
                        .build();
        // creating the sub-menu of the action button
        SubActionButton.Builder subMenuBuilder = new SubActionButton.Builder(this);
        subMenuBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_red));
        FrameLayout.LayoutParams menuLayout = new FrameLayout.LayoutParams(floatingMenuIconSize, floatingMenuIconSize);
        subMenuBuilder.setLayoutParams(menuLayout);
        ImageView weightIcon = new ImageView(this);
        ImageView logOutIcon = new ImageView(this);
        // setting sub-menu icons
        weightIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new_light));
        logOutIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_important));
        // adding the two buttons to our sub-menu
        final FloatingActionMenu floatingActionHomeMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(subMenuBuilder.setContentView(logOutIcon).build())
                .addSubActionView(subMenuBuilder
                        .setContentView(weightIcon)
                        .setBackgroundDrawable(getResources()
                                .getDrawable(R.drawable.button_action_yellow)).build())
                .attachTo(floatingActionButton)
                .build();
        // adding listener to weight icon
        weightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RecordWeightActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                floatingActionHomeMenu.close(true);
                finish();
            }
        });
        // logout button listener
        logOutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Logout")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // initialise shared preferences for the app
                                SharedPreferences sharedPreferences = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                // removes user email from shared preferences upon logout
                                editor.remove("emailContainer");
                                editor.commit();
                                // creating new intent to go to login screen
                                Intent intent = new Intent(HomeScreenActivity.this, LoginActivity.class);
                                // using above intent
                                startActivity(intent);
                                // removing this activity from the backstack
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });
        // Setting animation on floating action button
        floatingActionHomeMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                floatingMenuIcon.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(floatingActionButton, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                floatingActionButton.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(floatingActionButton, pvhR);
                animation.start();
            }
        });
    }
            private void initialiseUserWeightRecordTable() {
                String sqlStatement = "create table if not exists userWeightRecord(" +
                        "weight integer not null, " +
                        "date text not null, " +
                        "weightSubmitted integer not null, " +
                        "email text not null," +
                        "unique ( date ) " +
                        "FOREIGN KEY (email) REFERENCES registrations(email));";
                SQLiteDatabase cyclingPalDB = openOrCreateDatabase("CyclingPal", MODE_PRIVATE, null);
                cyclingPalDB.execSQL(sqlStatement);
                String sqlSelect = "select weight from userFitnessInfo where email = '" + email + "'";
                Cursor cursor = cyclingPalDB.rawQuery(sqlSelect, null);
                cursor.moveToFirst();
                int weight = cursor.getInt(0);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String myDate = dateFormat.format(new Date());
                String sqlInsert = "INSERT INTO userWeightRecord (weight, date, weightSubmitted, email)" +
                        "VALUES ( " + "'" + weight + "','" +  myDate + "','1'" + ",'" + email + "')";
                cyclingPalDB.execSQL(sqlInsert);
            }
}
