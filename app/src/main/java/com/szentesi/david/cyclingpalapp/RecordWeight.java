package com.szentesi.david.cyclingpalapp;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;

public class RecordWeight extends AppCompatActivity {

    private EditText userWeight;
    private TextView currentDateTextView;
    private Button submitWeight;
    private int userWeightEntry;
    private SQLiteDatabase cyclingPalDB = null;
    private Bundle bundle;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_weight);
        // creating simple date format object
        // http://stackoverflow.com/questions/33316186/getting-the-current-date-time-on-an-android-device
        // using Java version of SimpleDateFormat as the Android version is only compatible with
        // API version 24 and above
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String myDate = dateFormat.format(new Date());
        Toolbar toolbar = (Toolbar)findViewById(R.id.recordWeightToolbar);
        toolbar.setTitle("Record Weight");
        bundle = getIntent().getExtras();
        email = bundle.getString("emailContainer");

        userWeight = (EditText) findViewById(R.id.enterWeightEditText);
        currentDateTextView = (TextView) findViewById(R.id.currentDateValueTextView);
        currentDateTextView.setText(myDate);
        submitWeight = (Button) findViewById(R.id.recordWeightButton);
        submitWeight.setEnabled(false);
        // creating edit text listener if the box is filled in
        // http://stackoverflow.com/questions/8225245/enable-and-disable-button-according-to-the-text-in-edittext-in-android
        userWeight.addTextChangedListener(new TextWatcher() {
            // auto-generated
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            // auto-generated
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            // checking for value entered
            @Override
            public void afterTextChanged(Editable editable) {
                boolean isFilledIn = userWeight.getText().toString().length() > 0;
                submitWeight.setEnabled(isFilledIn);
            }
        });

        cyclingPalDB = openOrCreateDatabase("CyclingPal", MODE_PRIVATE, null);
        initialiseUserWeightRecordTable();

        submitWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // take the value of userWeight EditText and convert it to an integer
                userWeightEntry = Integer.parseInt(userWeight.getText().toString());
                populateUserWeightRecordTable(userWeightEntry);
            }
        });
    }

    private void initialiseUserWeightRecordTable() {
        String sqlStatement = "create table if not exists userWeightRecord(" +
                "weight integer not null, " +
                "date text not null, " +
                "weightSubmitted integer not null, " +
                "email text not null, " +
                "FOREIGN KEY (email) REFERENCES registrations(email));";
        cyclingPalDB.execSQL(sqlStatement);
    }

    private void populateUserWeightRecordTable(int userWeightParam) {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String myDate = dateFormat.format(new Date());
        String sqlInsert = "INSERT INTO userWeightRecord (weight, date, weightSubmitted, email)" +
                "VALUES ( " + "'" + userWeightParam + "','" +  myDate + "','1'" + ",'" + email + "')";
        cyclingPalDB.execSQL(sqlInsert);
    }
}
