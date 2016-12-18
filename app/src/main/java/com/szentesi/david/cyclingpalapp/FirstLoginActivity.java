package com.szentesi.david.cyclingpalapp;

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

    private SQLiteDatabase cyclingPalDataBase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        userAge = (EditText)findViewById(R.id.ageEditText);
        userWeight = (EditText)findViewById(R.id.weightEditText);
        userHeight = (EditText)findViewById(R.id.heightEditText);
        userSex = (EditText)findViewById(R.id.sexEditText);
        submitButton = (Button)findViewById(R.id.submitButton);

        cyclingPalDataBase = openOrCreateDatabase("CyclingPal", MODE_PRIVATE, null);

        initialiseUsersPersonalDatabase();
        parseUserFitnessInfo();
    }

    private void parseUserFitnessInfo() {

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAgeInt = Integer.parseInt(userAge.getText().toString());
                userWeightInt = Integer.parseInt(userWeight.getText().toString());
                userHeightInt = Integer.parseInt(userHeight.getText().toString());
                userSexString = userSex.getText().toString();

                populateUsersPersonalDatabase(userAgeInt, userWeightInt, userHeightInt, userSexString);
            }
        });
    }

    private void initialiseUsersPersonalDatabase() {

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

    private void populateUsersPersonalDatabase( int ageInt, int weightInt, int heightInt, String sexText ) {

        String sqlInsert = "INSERT INTO userFitnessInfo ( age, weight, height, sex ) " +
                "VALUES ( " + "'" + ageInt + "', '" + weightInt + "', '" + heightInt + "', '" + sexText + "')";
        cyclingPalDataBase.execSQL(sqlInsert);
    }
}
