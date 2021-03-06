package com.szentesi.david.cyclingpalapp.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.szentesi.david.cyclingpalapp.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button registerButton;

    private String userNameString;
    private String emailString;
    private String passwordString;
    private String confirmPasswordString;
    private Boolean userIsCreated = false;

    private SQLiteDatabase cyclingPalDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText)findViewById(R.id.userName);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword);
        registerButton = (Button)findViewById(R.id.registerButton);

        createDatabase();
        parseRegistrationForm();
    }

    private void parseRegistrationForm() {

        // After registration, user will be redirected to the Login screen
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // capturing the value of user input
                userNameString = userName.getText().toString();
                emailString = email.getText().toString();
                passwordString = password.getText().toString();
                confirmPasswordString = confirmPassword.getText().toString();

                if ( passwordString.equals(confirmPasswordString) ) {
                    register(userNameString, emailString, passwordString);

                    if (userIsCreated == true) {
                        Intent intent = new Intent(view.getContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(RegisterActivity.this, R.string.user_creation_toast_prompt, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, R.string.passwords_do_not_match_toast_prompt, Toast.LENGTH_SHORT).show();
                    password.setText("");
                    confirmPassword.setText("");
                    password.requestFocus();
                }
            }
        });
    }

    private void createDatabase() {
        cyclingPalDB = openOrCreateDatabase("CyclingPal", MODE_PRIVATE, null);
        cyclingPalDB.setForeignKeyConstraintsEnabled(true);
        String sqlStatement = "create table if not exists registrations (" +
                "_id integer primary key autoincrement, " +
                "userName text not null unique, " +
                "email text not null unique, " +
                "password text not null, " +
                "firstLogin integer not null);";
        cyclingPalDB.execSQL(sqlStatement);
    }

    private void register(String userNameInsert, String emailInsert, String passwordInsert) {
        try {
            String sqlInsert = "INSERT INTO registrations ( userName, email, password, firstLogin ) VALUES (" + "'" + userNameInsert + "', '" + emailInsert + "', '" + passwordInsert + "', '0' )";
            cyclingPalDB.execSQL(sqlInsert);
            userIsCreated = true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(RegisterActivity.this, R.string.duplicate_user_toast_prompt, Toast.LENGTH_SHORT).show();
            userName.setText("");
            email.setText("");
            password.setText("");
            confirmPassword.setText("");
            // return cursor on screen to userName field
            // http://www.concretepage.com/forum/thread?qid=409
            userName.requestFocus();
        }
    }

//    private static boolean doesDatabaseExist(Context context, String dbName) {
//        File dbFile = context.getDatabasePath(dbName);
//        return dbFile.exists();
//    }



}
