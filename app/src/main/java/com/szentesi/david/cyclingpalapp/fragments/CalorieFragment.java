package com.szentesi.david.cyclingpalapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szentesi.david.cyclingpalapp.R;
import com.szentesi.david.cyclingpalapp.helpers.MyDateFormatter;
import com.szentesi.david.cyclingpalapp.helpers.UnitConvertion;

public class CalorieFragment extends Fragment {

    private String userEmail;
    private TextView userCalorieTextView;
    private View calorieFragmentView;

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
        calculateInitialCaloriIntake();
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
}
