package com.szentesi.david.cyclingpalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.szentesi.david.cyclingpalapp.activities.HomeScreenActivity;
import com.szentesi.david.cyclingpalapp.activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // creating shared preferences variable that's reading the app's shared preference file
        SharedPreferences sharedPreferences = this.getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
        // if shared preferences file isn't empty, go to home screen
        if(sharedPreferences.getAll().size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("emailContainer", sharedPreferences.getString("emailContainer", null));
            Intent intent = new Intent(this, HomeScreenActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        // otherwise go to login
        else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
