package com.szentesi.david.cyclingpalapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.szentesi.david.cyclingpalapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private SQLiteDatabase cyclingPalDB = null;
    private Spinner weightViewSpinner;
    private LineChart lineChart;
    private Button refreshButton;

    //sqls for getting a week
    private static final String sqliteSelectWeight7Days = "select weight " +
            "from userWeightRecord " +
            "where date > (select datetime('now', '-8 day'))";

    private static final String sqliteSelectDate7Days = "select date " +
            "from userWeightRecord " +
            "where date > (select datetime('now', '-8 day'))";


    //sqls for getting a month
    private static final String sqliteSelectWeight30Days = "select weight " +
            "from userWeightRecord " +
            "where date > (select datetime('now', '-31 day'))";

    private static final String sqliteSelectDate30Days = "select date " +
            "from userWeightRecord " +
            "where date > (select datetime('now', '-31 day'))";

    private List<String> weightChoicesList;

    public ProgressFragment() {
        // Required empty public constructor
    }

    public static ProgressFragment newInstance() {
        ProgressFragment fragment = new ProgressFragment();
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
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        lineChart = (LineChart)view.findViewById(R.id.progressLineChart);
        refreshButton = (Button)view.findViewById(R.id.refresh_weight_graph);
        weightViewSpinner = (Spinner)view.findViewById(R.id.weightTimeSpinner);
        //creating list for spinner
        weightChoicesList = new ArrayList<String>();
        weightChoicesList.add("7 days");
        weightChoicesList.add("30 days");
        //creating adapter to connect list to spinner
        ArrayAdapter<String> weightAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.sex_spinner_row, weightChoicesList);
        //setting the adapter onto our spinner widget
        weightViewSpinner.setAdapter(weightAdapter);
        weightViewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //checking if selected spinner item is 7 days
                if(weightViewSpinner.getSelectedItem().toString().equals("7 days")) {
                    createLineChart(lineChart, sqliteSelectWeight7Days, sqliteSelectDate7Days, "7 Days");
                }
                // checking if 30 days
                if(weightViewSpinner.getSelectedItem().toString().equals("30 days")) {
                    createLineChart(lineChart, sqliteSelectWeight30Days, sqliteSelectDate30Days, "30 days");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });
        // refresh chart
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // refresh linechart
                lineChart.invalidate();
            }
        });

        return view;
    }

    private void createLineChart(LineChart lineChart, String sqliteSelectWeight, String sqliteSelectDate, String chartLabel) {
        // arrayslist to store chart data and dates
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> dateEntries = new ArrayList<>();
        //open db
        cyclingPalDB = getActivity().openOrCreateDatabase("CyclingPal", Context.MODE_PRIVATE, null);
        // showing weight in the last week

        // storing the results of our sqls in these 2 cursors
        Cursor weightCursor = cyclingPalDB.rawQuery(sqliteSelectWeight, null);
        Cursor dateCursor = cyclingPalDB.rawQuery(sqliteSelectDate, null);

        int count = 0;
        //collect the weight values
        while (weightCursor.moveToNext()) {
                entries.add(new Entry(weightCursor.getInt(0), count));
            count++;
        }
        count = 0;
        //collects the dates
        while (dateCursor.moveToNext()) {
            // assigning dateCursor.getString(0) to dateToTrim
            String dateToTrim = dateCursor.getString(0) ;
            //Using subString to remove the year ie the first 5 characters
            dateEntries.add(dateToTrim.substring(5));
            count++;
        }
        LineDataSet lineDataSet = new LineDataSet(entries, chartLabel);
        // setting chart color
        lineDataSet.setFillColor(Color.GREEN);
        lineDataSet.setFillAlpha(10);
        LineData lineData = new LineData(dateEntries, lineDataSet);
        //add the data to the linechart
        lineChart.setData(lineData);
        lineChart.setDescription("Blah");
        lineChart.invalidate();
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
}
