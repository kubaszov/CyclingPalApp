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
import com.szentesi.david.cyclingpalapp.helpers.MyDateFormatter;

import java.util.Date;

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
    private SQLiteDatabase cyclingPalDB = null;
    private String myDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cyclingPalDB = openOrCreateDatabase("CyclingPal", MODE_PRIVATE, null);
        myDate = MyDateFormatter.retrieveDateFormat();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        sharedPreferences = this.getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        email = sharedPreferences.getString("emailContainer", null);
        bundle = getIntent().getExtras();
        if(getIntent().getExtras() == null) {
            bundle = new Bundle();
        }
        bundle.putString("emailContainer", email);
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
        createUserWeightRecordTable();
        initialiseUserWeightRecordTable();

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
                BMIFragment bmiFragment = BMIFragment.newInstance();
                bundle = getIntent().getExtras();
                bmiFragment.setArguments(bundle);
                return bmiFragment;
            }
            else if (position == 1) {
                CalorieFragment calorieFragment = new CalorieFragment();
                return calorieFragment;
            }
            else if (position == 2) {
                ProgressFragment progressFragment = new ProgressFragment();
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
                        .setBackgroundDrawable(R.drawable.blue_selector)
                        .setLayoutParams(floatingMenuLayout)
                        .build();
        // creating the sub-menu of the action button
        SubActionButton.Builder subMenuBuilder = new SubActionButton.Builder(this);
        subMenuBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_selector));
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
                                .getDrawable(R.drawable.yellow_selector)).build())
                .attachTo(floatingActionButton)
                .build();
        // adding listener to weight icon
        weightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sqlCheckIfWeightSubmittedAlready = "select weightSubmitted " +
                        "from userWeightRecord " +
                        "where date = '" + myDate + "'";
                Cursor cursor = cyclingPalDB.rawQuery(sqlCheckIfWeightSubmittedAlready, null);
                final Intent intent = new Intent(view.getContext(), RecordWeightActivity.class);
                if(cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    if (cursor.getInt(0) == 1) {
                        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                                .setTitle("Weight Already Submitted")
                                .setMessage(R.string.confirm_update_weight)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        floatingActionHomeMenu.close(true);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).show();
                    }
                }
                else {
                    floatingActionHomeMenu.close(true);
                    startActivity(intent);
                }
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
                floatingActionButton.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(floatingActionButton, pvhR);
                animation.start();
            }
        });
    }
    //set Up userWeightRecord table
    private void createUserWeightRecordTable() {
        String sqlStatement = "create table if not exists userWeightRecord(" +
                "weight integer not null, " +
                "date text not null, " +
                "weightSubmitted integer not null, " +
                "caloriesBurnedToday real, " +
                "email text not null," +
                "unique ( date ) " +
                "FOREIGN KEY (email) REFERENCES registrations(email));";
        cyclingPalDB.execSQL(sqlStatement);
    }

        private void initialiseUserWeightRecordTable() {
            String sqlSelect = "select weight from userFitnessInfo where email = '" + email + "'";
            Cursor cursor = cyclingPalDB.rawQuery(sqlSelect, null);
            Cursor checkIfTableIsPopulated = cyclingPalDB.rawQuery("select weight from userWeightRecord where email = '" + email + "'", null);
            cursor.moveToFirst();
            int weight = 0;
            if(checkIfTableIsPopulated.getCount() == 0) {
                weight = cursor.getInt(0);
                String sqlInsert = "INSERT INTO userWeightRecord (weight, date, weightSubmitted, caloriesBurnedToday, email)" +
                        "VALUES ( " + "'" + weight + "','" + myDate + "','1','0.0','" + email + "')";
                cyclingPalDB.execSQL(sqlInsert);
            }
            cursor.close();
        }
}
