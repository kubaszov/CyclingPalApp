package com.szentesi.david.cyclingpalapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.szentesi.david.cyclingpalapp.R;
import com.szentesi.david.cyclingpalapp.helpers.MyDateFormatter;
import com.szentesi.david.cyclingpalapp.helpers.UnitConvertion;

import java.util.ArrayList;
import java.util.List;

public class CalorieFragment extends Fragment {

    private String userEmail;
    private TextView userCalorieTextView;
    private TextView caloriesBurnedTodayTextview;
    private TextView userCalorieBurnedTextView;
    private TextView previousWorkoutTextview;
    private View calorieFragmentView;
    private CardView caloriedsBurnedTodayCardview;
    private AlertDialog.Builder submitWorkoutDialogBuilder;

    private static final double CALORIES_PER_MET_PER_KG_PER_MINUTE = 0.0175;
    private static final double LIGHT_INTENSITY = 3.0;
    private static final double MEDIUM_INTENSITY= 4.0;
    private static final double HEAVY_INTENSITY = 5.5;

    private SQLiteDatabase cyclingPalDB = null;
    private SharedPreferences sharedPreferences;

    private OnFragmentInteractionListener mListener;

    public CalorieFragment() {
        // Required empty public constructor
    }

    public static CalorieFragment newInstance() {
        CalorieFragment fragment = new CalorieFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        calorieFragmentView = inflater.inflate(R.layout.fragment_calorie, container, false);
        Context context = calorieFragmentView.getContext();
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("emailContainer", null);
        userCalorieTextView = (TextView)calorieFragmentView.findViewById(R.id.userCalorieTextView);
        caloriedsBurnedTodayCardview = (CardView)calorieFragmentView.findViewById(R.id.calorie_burned_today_card);
        caloriesBurnedTodayTextview = (TextView)calorieFragmentView.findViewById(R.id.calorieBurnedMessageTextView);
        userCalorieBurnedTextView = (TextView)calorieFragmentView.findViewById(R.id.userCalorieBurnedTextView);
        previousWorkoutTextview = (TextView)calorieFragmentView.findViewById(R.id.last_workout_textview_value);
        calculateInitialCaloriIntake();
        displayInitialCaloriesBurnedToday();
        displayLastWorkout();

        caloriedsBurnedTodayCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSubmitWorkoutDialog();
            }
        });
        return calorieFragmentView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void calculateInitialCaloriIntake() {
        String date = MyDateFormatter.retrieveDateFormat();
        cyclingPalDB = getActivity().openOrCreateDatabase("CyclingPal", Context.MODE_PRIVATE, null);
        Cursor cursor = cyclingPalDB.rawQuery("select weight, height, sex, age " +
                "from userFitnessInfo " +
                "where email = " + "'" + userEmail + "'", null);
        // setting cursor to the first value of our results
        cursor.moveToFirst();
        // getting values in order of cursor index from the database
        int weight = cursor.getInt(0);
        int height = cursor.getInt(1);
        String sex = cursor.getString(2);
        int age = cursor.getInt(3);
        //If weight has been updated
        String sqlIfUSerWeightRecordExists = "SELECT weight FROM userWeightRecord " +
                "WHERE email = '" + userEmail + "' " +
                "AND date = '" + date + "'";
        try {
            cursor = cyclingPalDB.rawQuery(sqlIfUSerWeightRecordExists, null);
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                // todo sort whether weight is a double or int
                weight = cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            Log.e("CALORIEFRAGMENT" , e.getMessage());
        }
        double calorie = UnitConvertion.calculateInitialCaloriIntake(age, weight, height, sex);
        userCalorieTextView.setText(String.valueOf(calorie));
    }

    // method to creat submit workout dialog
    private void callSubmitWorkoutDialog() {
        final Cursor weightCursor = cyclingPalDB.rawQuery("SELECT weight from userWeightRecord WHERE date = '" + MyDateFormatter.retrieveDateFormat() + "'", null);
        weightCursor.moveToFirst();
        // If there is no weight a toast will fire to promt the user to esubmit a new weight
        if(weightCursor.getCount() > 0) {
            submitWorkoutDialogBuilder = new AlertDialog.Builder(calorieFragmentView.getContext());
            LayoutInflater submitDialogInflator = getActivity().getLayoutInflater();
            View dialogView = submitDialogInflator.inflate(R.layout.submit_workout_dialog, null);
            submitWorkoutDialogBuilder.setView(dialogView);
            final EditText timeTextView = (EditText) dialogView.findViewById(R.id.submitted_time_editText);
            final Spinner workoutIntensitySpinner = (Spinner) dialogView.findViewById(R.id.workout_intensity_spinner);
            List<String> intensityList = new ArrayList<>();
            intensityList.add("1.  Light Cycle less than 12kmph average");
            intensityList.add("2.  Medium effort ie. work commute > 14kmph");
            intensityList.add("3.  Heavy > 18kmph inclined cycle");
            ArrayAdapter<String> intensityAdapter = new ArrayAdapter<>(calorieFragmentView.getContext(), R.layout.intensity_spinner_row, intensityList);
            workoutIntensitySpinner.setAdapter(intensityAdapter);
            submitWorkoutDialogBuilder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int weight = 0;
                    if (weightCursor.getCount() > 0) {
                        weight = weightCursor.getInt(0);
                        int time = Integer.parseInt(timeTextView.getText().toString());
                        int intensity = workoutIntensitySpinner.getSelectedItemPosition();
                        calculateCaloriesBurned(weight, time, intensity);
                        weightCursor.close();
                    }
                }
            });
            submitWorkoutDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog submitWorkoutDialog = submitWorkoutDialogBuilder.show();
            submitWorkoutDialog.show();
        }
        else {
            Toast.makeText(getContext(), R.string.submmit_weight_first, Toast.LENGTH_LONG);
        }
    }

    private void calculateCaloriesBurned(double weight, int time, int metIntensity) {
        double caloriesBurned = CALORIES_PER_MET_PER_KG_PER_MINUTE * metIntensity * weight * time;
        double totalCaloriesToday = 0.0;
        String date = MyDateFormatter.retrieveDateFormat();
        Cursor previousCaloriesCursor = cyclingPalDB.rawQuery("SELECT caloriesBurnedToday FROM userWeightRecord WHERE date == '" + date + "'", null);
        if(previousCaloriesCursor.getCount() > 0) {
            previousCaloriesCursor.moveToFirst();
            totalCaloriesToday =  caloriesBurned + previousCaloriesCursor.getDouble(0);
        } else {
            totalCaloriesToday =  caloriesBurned;
        }
        //insert updated calories into db
        String sql = "UPDATE userWeightRecord" +
                " SET caloriesBurnedToday = " + totalCaloriesToday +
                " WHERE date == '" + date + "' AND email == '" + userEmail +"'";
        cyclingPalDB.execSQL(sql);
        userCalorieBurnedTextView.setText(String.format("%.2f", totalCaloriesToday));
        previousCaloriesCursor.close();
    }

    private void displayLastWorkout() {
        String date = MyDateFormatter.retrieveDateFormat();
        Cursor previousCaloriesCursor = cyclingPalDB.rawQuery("SELECT caloriesBurnedToday FROM userWeightRecord WHERE date >= date('now', '-1 day')", null);
        if(previousCaloriesCursor.getCount() > 0) {
            previousCaloriesCursor.moveToFirst();
            previousWorkoutTextview.setText(previousCaloriesCursor.getString(0));
        }
        else {
            previousWorkoutTextview.setText(R.string.no_previous_days_workout_found);
        }
        previousCaloriesCursor.close();
    }

    private void displayInitialCaloriesBurnedToday() {
        String date = MyDateFormatter.retrieveDateFormat();
        Cursor previousCaloriesCursor = cyclingPalDB.rawQuery("SELECT caloriesBurnedToday FROM userWeightRecord WHERE date == '" + date + "'", null);
        previousCaloriesCursor.moveToFirst();
        if(previousCaloriesCursor.getCount() > 0) {
            userCalorieBurnedTextView.setText(String.format("%.2f", previousCaloriesCursor.getDouble(0)));
        }
        previousCaloriesCursor.close();
    }
}
