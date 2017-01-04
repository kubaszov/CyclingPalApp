package com.szentesi.david.cyclingpalapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.szentesi.david.cyclingpalapp.R;

import java.util.ArrayList;
import java.util.List;

public class FirstLoginActivity extends AppCompatActivity {

    private EditText userAge;
    private EditText userWeight;
    private EditText userHeight;
    private Spinner userSexSpinner;
    private Button submitButton;

    private int userAgeInt;
    private int userWeightInt;
    private int userHeightInt;
    private String userSexString;
    private String emailContainer;
    private Bundle bundle;
    private List<String> sexArray;

    private SQLiteDatabase cyclingPalDB = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);
        sharedPreferences = this.getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        // set up List for spinner
        // http://stackoverflow.com/questions/11920754/android-fill-spinner-from-java-code-programmatically
        sexArray = new ArrayList<String>();
        sexArray.add("Male");
        sexArray.add("Female");
        // created an ArrayAdapter to store the male and female values in sexArray list
        ArrayAdapter<String> sexAdapter = new ArrayAdapter<String>(this, R.layout.sex_spinner_row, sexArray);

        userAge = (EditText)findViewById(R.id.ageEditText);
        userWeight = (EditText)findViewById(R.id.weightEditText);
        userHeight = (EditText)findViewById(R.id.heightEditText);
        userSexSpinner = (Spinner) findViewById(R.id.sexEditSpinner);
        submitButton = (Button)findViewById(R.id.submitButton);
        // setAdapter method is adding my ArrayAdapter (sexAdapter) to our spinner widget
        userSexSpinner.setAdapter(sexAdapter);
        // initialise bundle that was created within LoginActivity
        bundle = getIntent().getExtras();
        emailContainer = bundle.getString("emailContainer");

        cyclingPalDB = openOrCreateDatabase("CyclingPal", MODE_PRIVATE, null);

        initialiseUserFitnessInfoTable();
        parseUserFitnessInfo();
    }

    private void parseUserFitnessInfo() {

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAgeInt = Integer.parseInt(userAge.getText().toString());
                userWeightInt = Integer.parseInt(userWeight.getText().toString());
                editor.putInt("weight",0);
                userHeightInt = Integer.parseInt(userHeight.getText().toString());
                // how to get text to String using spinner
                // http://stackoverflow.com/questions/10331854/how-to-get-spinner-selected-item-value-to-string
                userSexString = userSexSpinner.getSelectedItem().toString();

                populateUserFitnessInfoTable(userAgeInt, userWeightInt, userHeightInt, userSexString);
                updateFirstLogin(emailContainer);
                Intent homeScreenActivityIntent = new Intent(v.getContext(), HomeScreenActivity.class);
                homeScreenActivityIntent.putExtras(bundle);
                startActivity(homeScreenActivityIntent);
                finish();
            }
        });
    }

    private void initialiseUserFitnessInfoTable() {

        String sqlStatement = "create table if not exists userFitnessInfo(" +
                "age integer not null, " +
                "weight integer not null, " +
                "height integer not null, " +
                "sex text not null, " +
                "email text not null, " +
                "FOREIGN KEY (email) REFERENCES registrations(email));";
        cyclingPalDB.execSQL(sqlStatement);
    }


    private void populateUserFitnessInfoTable(int ageInt, int weightInt, int heightInt, String sexText ) {

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
