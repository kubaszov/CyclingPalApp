package com.szentesi.david.cyclingpalapp;

import android.content.Intent;
import android.database.Cursor;
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
    private String emailContainer;
    private Bundle bundle;

    private SQLiteDatabase cyclingPalDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        userAge = (EditText)findViewById(R.id.ageEditText);
        userWeight = (EditText)findViewById(R.id.weightEditText);
        userHeight = (EditText)findViewById(R.id.heightEditText);
        userSex = (EditText)findViewById(R.id.sexEditText);
        submitButton = (Button)findViewById(R.id.submitButton);
        // initialise bundle that was created within LoginActivity
        bundle = getIntent().getExtras();
        emailContainer = bundle.getString("emailContainer");

        cyclingPalDB = openOrCreateDatabase("CyclingPal", MODE_PRIVATE, null);

        initialiseUsersPersonalDatabase();
        parseUserFitnessInfo();
    }

    private void parseUserFitnessInfo() {

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAgeInt = Integer.parseInt(userAge.getText().toString());
                userWeightInt = Integer.parseInt(userWeight.getText().toString());
                userHeightInt = Integer.parseInt(userHeight.getText().toString());
                userSexString = userSex.getText().toString();

                populateUsersPersonalDatabase(userAgeInt, userWeightInt, userHeightInt, userSexString);
                updateFirstLogin(emailContainer);
                Intent homeScreenActivityIntent = new Intent(v.getContext(), HomeScreenActivity.class);
                homeScreenActivityIntent.putExtras(bundle);
                startActivity(homeScreenActivityIntent);
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
        cyclingPalDB.execSQL(sqlStatement);
    }

    private void populateUsersPersonalDatabase( int ageInt, int weightInt, int heightInt, String sexText ) {

        String sqlInsert = "INSERT INTO userFitnessInfo ( age, weight, height, sex, email ) " +
                "VALUES ( " + "'" + ageInt + "', '" + weightInt + "', '" + heightInt + "', '" + sexText + "', '" + emailContainer + "')";
        cyclingPalDB.execSQL(sqlInsert);
    }

    // method to set firstLogin value to 1 after entering FirstLoginActivity
    private void updateFirstLogin(String email) {
        String sqlUpdate = "UPDATE registrations SET firstLogin = '1' " +
                "WHERE email = " + "'" + email + "'";
        cyclingPalDB.execSQL(sqlUpdate);
    }


}
