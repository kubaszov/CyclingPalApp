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

                Bundle bundle = new Bundle();

                cyclingPalDB = openOrCreateDatabase("CyclingPal", MODE_PRIVATE, null);
                /**********************************************
                * Need to handle three cases:
                * 1: When the registrations table does not exist and throws an sqlite exception
                * 2: When user name or password is incorrect
                * 3: When user name does not exist
                 *********************************************/
                try {
                     Cursor cursor = cyclingPalDB.rawQuery("select userName, password from registrations", null);
                    while(cursor.moveToNext()) {
                        if (username.equals(cursor.getString(0)) && password.equals(cursor.getString(1))) {
                            // code for launching first login activity
                            cursor = cyclingPalDB.rawQuery("select firstLogin, email " +
                                    "from registrations " +
                                    "where userName = " + "'" + username + "'" + " and password = " + "'" + password + "'", null);
                            cursor.moveToFirst();
                            // this is used to pass values between two activities
                            bundle.putString("emailContainer", cursor.getString(1));
                            if (cursor.getInt(0) == 0) {
                                // launching FirstLoginActivity
                                Intent firstLoginActivityIntent = new Intent(v.getContext(), FirstLoginActivity.class);
                                // creating bundle that can be called in FirstLoginActivity
                                firstLoginActivityIntent.putExtras(bundle);
                                startActivity(firstLoginActivityIntent);
                                finish();
                            }
                            else if (cursor.getInt(0) == 1) {
                                // launching HomeScreenActivity
                                Intent homeScreenActivityIntent = new Intent(v.getContext(), HomeScreenActivity.class);
                                // creating bundle that can be called in HomeScreenActivity
                                homeScreenActivityIntent.putExtras(bundle);
                                startActivity(homeScreenActivityIntent);
                                finish();
                            }
                                break;
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.incorrect_credentials_toast_prompt, Toast.LENGTH_SHORT).show();
                            usernameInput.setText("");
                            passwordInput.setText("");
                            usernameInput.requestFocus();
                        }
                     }
                   // Need to paste the below to the correct location in the code
                    // message pops up after successful login...
                    // Toast.makeText(LoginActivity.this, R.string.incorrect_credentials_toast_prompt, Toast.LENGTH_LONG).show();
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
        finish();
    }
}
