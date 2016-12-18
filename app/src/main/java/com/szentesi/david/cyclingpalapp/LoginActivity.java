package com.szentesi.david.cyclingpalapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;

    private EditText usernameInput;
    private EditText passwordInput;

    private String username;
    private String password;

    private SQLiteDatabase cyclingPalDB = null;

    TextView loginAttempts;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button)findViewById(R.id.loginButton);
        usernameInput = (EditText)findViewById(R.id.userNameEditText);
        passwordInput = (EditText)findViewById(R.id.passwordEditText);

        registerButton = (Button)findViewById(R.id.registerButton);
        loginAttempts = (TextView)findViewById(R.id.attemptsCounterTextView);
        loginAttempts.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();

                cyclingPalDB = openOrCreateDatabase("CyclingPal", MODE_PRIVATE, null);
                /**********************************************
                * Need to handle three cases:
                * 1: When the registrations table does not exist and throws an sqlite exception
                * 2: When user name or password is incorrect
                * 3 :When user name does not exist
                 *********************************************/
                try {
                     Cursor cursor = cyclingPalDB.rawQuery("select userName, password from registrations", null);
                     while(cursor.moveToNext()) {
                        if (username.equals(cursor.getString(0)) && password.equals(cursor.getString(1))) {
                            // code for launch first activity
                            cursor = cyclingPalDB.rawQuery("select firstLogin " +
                                    "from registrations " +
                                    "where userName = " + "'" + username + "'" + " and password = " + "'" + password + "'", null);
                            cursor.moveToFirst();
                            if (cursor.getInt(0) == 0) {
                                // code for launching first login activity
                                Intent intent = new Intent(v.getContext(), FirstLoginActivity.class);
                                startActivity(intent);
                            }
                            else
                                // code to launch home screen activity
                                break;
                        }
                     }
                    Toast.makeText(LoginActivity.this, R.string.incorrect_credentials_toast_prompt, Toast.LENGTH_LONG).show();
                } catch (SQLiteException e) {
                    Toast.makeText(LoginActivity.this, R.string.registration_toast_prompt, Toast.LENGTH_SHORT).show();
                    launchRegistrationActivity(v);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRegistrationActivity(v);
            }
        });
    }

    private void launchRegistrationActivity(View v) {
        Intent intent = new Intent(v.getContext(), RegisterActivity.class);
        startActivity(intent);
    }
}
