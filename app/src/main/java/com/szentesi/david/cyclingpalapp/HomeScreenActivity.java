package com.szentesi.david.cyclingpalapp;

import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

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

}
