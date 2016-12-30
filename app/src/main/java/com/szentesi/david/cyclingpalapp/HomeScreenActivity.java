package com.szentesi.david.cyclingpalapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

// to implement fragment need to do the following
// http://stackoverflow.com/questions/24777985/how-to-implement-onfragmentinteractionlistener
public class HomeScreenActivity extends AppCompatActivity implements
        BMIFragment.OnFragmentInteractionListener, CalorieFragment.OnFragmentInteractionListener {

    private Bundle bundle;
    private static int NUMBER_OF_FRAGMENTS = 2;
    private ViewPager homeScreenViewPager;
    private PagerAdapter pa;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        // TODO: 29/12/2016  for later if have time
        //SharedPreferences sharedPreferences = null;
        //SharedPreferences.Editor = sharedPreferences.edit();
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        // naming the Tabs
        tabLayout.addTab(tabLayout.newTab().setText("BMI"));
        tabLayout.addTab(tabLayout.newTab().setText("Calories"));
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
            else {
                CalorieFragment calorieFragment = new CalorieFragment();
                bundle = getIntent().getExtras();
                calorieFragment.setArguments(bundle);
                return calorieFragment;
            }
        }

        @Override
        public int getCount() {
            return NUMBER_OF_FRAGMENTS;
        }
    }

    private void floatingActionMenu(Context context) {
        // creating floating menu icon
        ImageView floatingMenuIcon = new ImageView(this);
        floatingMenuIcon.setImageResource(R.drawable.ic_action_new);
        int floatingMenuSize = getResources().getDimensionPixelSize(R.dimen.floating_action_button_size);
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
        ImageView weightIcon = new ImageView(this);
        ImageView logOutIcon = new ImageView(this);
        // setting sub-menu icons
        weightIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new_light));
        logOutIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_important));
        // adding the two buttons to our sub-menu
        final FloatingActionMenu floatingActionHomeMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(subMenuBuilder
                        .setContentView(weightIcon)
                        .setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.button_action_yellow)).build())
                .addSubActionView(subMenuBuilder.setContentView(logOutIcon).build())
                .attachTo(floatingActionButton)
                .build();
        // adding listener to weight icon
        weightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RecordWeight.class);
                intent.putExtras(bundle);
                startActivity(intent);
                floatingActionHomeMenu.close(true);
            }
        });
    }
}
