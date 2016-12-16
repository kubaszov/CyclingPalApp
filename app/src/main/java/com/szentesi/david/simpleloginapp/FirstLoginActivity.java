package com.szentesi.david.simpleloginapp;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstLoginActivity extends AppCompatActivity {

    private EditText userAge;
    private EditText userWeight;
    private EditText userHeight;
    private EditText userSex;
    private Button submitButton;

    private int userAgeInt;
    private int userWeightInt;
    private int userHeightInt;
    private String userSexString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        userAge = (EditText)findViewById(R.id.ageEditText);
        userWeight = (EditText)findViewById(R.id.weightEditText);
        userHeight = (EditText)findViewById(R.id.heightEditText);
        userSex = (EditText)findViewById(R.id.sexEditText);
        submitButton = (Button)findViewById(R.id.submitButton);

        //  needs to be integer

        initialiseUsersPersonalDatabase();
    }

    private void parseUserFitnessInfo() {

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAgeInt = Integer.parseInt(userAge.getText().toString());
                // SQLite statement to insert into database
            }
        });
    }

    private void initialiseUsersPersonalDatabase() {

        SQLiteDatabase cyclingPalDataBase = openOrCreateDatabase("CyclingPal", MODE_PRIVATE, null);
        String sqlStatement = "create table if not exists userFitnessInfo(" +
                "age integer not null, " +
                "weight integer not null, " +
                "height integer not null, " +
                "sex text not null, " +
                "email text not null, " +
                    "FOREIGN KEY (email) REFERENCES registrations(email));";
        cyclingPalDataBase.execSQL(sqlStatement);
        //test
    }
}
