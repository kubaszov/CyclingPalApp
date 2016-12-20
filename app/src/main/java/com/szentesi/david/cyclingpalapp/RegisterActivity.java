package com.szentesi.david.cyclingpalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName;
    private EditText email;
    private EditText password;
    private Button registerButton;

    private String userNameString;
    private String emailString;
    private String passwordString;

    private SQLiteDatabase cyclingPalDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText)findViewById(R.id.userName);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
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

                register(userNameString, emailString, passwordString);

                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                Toast.makeText(RegisterActivity.this, R.string.user_creation_toast_prompt, Toast.LENGTH_SHORT).show();
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
        String sqlInsert = "INSERT INTO registrations ( userName, email, password, firstLogin ) VALUES (" + "'" + userNameInsert + "', '" + emailInsert + "', '" + passwordInsert + "', '0' )";
        cyclingPalDB.execSQL(sqlInsert);
    }

//    private static boolean doesDatabaseExist(Context context, String dbName) {
//        File dbFile = context.getDatabasePath(dbName);
//        return dbFile.exists();
//    }



}
