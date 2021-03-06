package com.szentesi.david.cyclingpalapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.szentesi.david.cyclingpalapp.R;
import com.szentesi.david.cyclingpalapp.helpers.MyDateFormatter;
import com.szentesi.david.cyclingpalapp.helpers.UnitConvertion;

import java.util.ArrayList;
import java.util.Date;

public class BMIFragment extends Fragment {

    private String userEmail;
    private int userHeight;
    private int userWeight;
    private TextView bmiTextView;
    private View bmiFragmentView;
    private BarChart bmiBarChart;

    private SQLiteDatabase cyclingPalDB = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static String TAG = "BMIFRAGMENT";

    private OnFragmentInteractionListener mListener;

    public BMIFragment() {
        // Required empty public constructor
    }

    public static BMIFragment newInstance() {
        BMIFragment fragment = new BMIFragment();
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
        bmiFragmentView = inflater.inflate(R.layout.fragment_bmi, container, false);
        Context context = bmiFragmentView.getContext();
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userEmail = sharedPreferences.getString("emailContainer", null);
        bmiTextView = (TextView)bmiFragmentView.findViewById(R.id.bmiTextView);
        bmiBarChart = (BarChart)bmiFragmentView.findViewById(R.id.bmiBarChart);
        userBMIData();

        return bmiFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        userBMIData();
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

    private void userBMIData() {
        // on how to open DB in fragment
        // http://stackoverflow.com/questions/24511031/openorcreatedatabase-undefined-in-the-fragment-class
        cyclingPalDB = getActivity().openOrCreateDatabase("CyclingPal", Context.MODE_PRIVATE, null);

        String date = MyDateFormatter.retrieveDateFormat();
        // If weight has been updated
            String sqlIfUSerWeightRecordExists = "SELECT weight FROM userWeightRecord " +
                    "WHERE email = '" + userEmail + "' " +
                    "AND date = '" + date + "'";
        Cursor cursor = cyclingPalDB.rawQuery("select weight, height " +
                "from userFitnessInfo " +
                "where email = " + "'" + userEmail + "'", null);
        cursor.moveToFirst();
        userWeight = cursor.getInt(0);
        userHeight = cursor.getInt(1);
        try {
            //now initialize to see if weight was updated
            Cursor weightUpdatedCursor = cyclingPalDB.rawQuery(sqlIfUSerWeightRecordExists, null);
            if (weightUpdatedCursor.getCount() != 0) {
                weightUpdatedCursor.moveToFirst();
                //use updated weight
                userWeight = weightUpdatedCursor.getInt(0);
            }
        } catch (SQLiteException e) {
            Log.e("BMIFragment", e.getMessage());
        }
        double bmi = UnitConvertion.calculateBMI(userWeight, userHeight);
        bmiTextView.setText(String.valueOf(bmi));
        // creating Bar chart values
        ArrayList<BarEntry> bmiBarEntries = new ArrayList<>();
        bmiBarEntries.add(new BarEntry(18f, 0));
        bmiBarEntries.add(new BarEntry(24f, 1));
        bmiBarEntries.add(new BarEntry(((float) bmi), 2));
        bmiBarEntries.add(new BarEntry(30f, 3));
        // creating Bar chart labels
        ArrayList<String> chartLabels = new ArrayList<>();
        chartLabels.add("Underweight");
        chartLabels.add("Normal");
        chartLabels.add("Actual");
        chartLabels.add("Overweight");
        // creating Bar data set
        BarDataSet bmiBarDataset = new BarDataSet(bmiBarEntries, "BMI Chart");
        // creating Bar chart colours
        bmiBarDataset.setColors(new int[] {Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED});
        BarData bmiBarData = new BarData(chartLabels, bmiBarDataset);
        bmiBarChart.setData(bmiBarData);
        bmiBarChart.setClickable(false);
        bmiBarChart.setTouchEnabled(false);
        bmiBarChart.animateY(1000);
        bmiBarChart.invalidate();
    }

}
