package com.szentesi.david.cyclingpalapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.szentesi.david.cyclingpalapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProgressFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private SQLiteDatabase cyclingPalDB = null;

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
        LineChart lineChart = (LineChart)view.findViewById(R.id.progressLineChart);
        ArrayList<Entry> entries = new ArrayList<>();
        cyclingPalDB = getActivity().openOrCreateDatabase("CyclingPal", Context.MODE_PRIVATE, null);
        String sqliteSelectWeight7Days = "select weight " +
                "from userWeightRecord " +
                "where date > (select datetime('now', '-8 day'))";
        String sqliteSelectDate7Days = "select date " +
                "from userWeightRecord " +
                "where date > (select datetime('now', '-8 day'))";
        String sqliteSelect30Days = "select weight, date " +
                "from userWeightRecord " +
                "where date > (select datetime('now', '-31 day'))";
        Cursor weightCursor = cyclingPalDB.rawQuery(sqliteSelectWeight7Days, null);
        Cursor dateCursor = cyclingPalDB.rawQuery(sqliteSelectDate7Days, null);
        ArrayList<String> dateEntries = new ArrayList<>();
        int count = 0;
        weightCursor.moveToFirst();
        dateCursor.moveToFirst();
        while (weightCursor.moveToNext()) {
                entries.add(new Entry(weightCursor.getInt(0), count));
            count++;
        }
        count = 0;
        while (dateCursor.moveToNext()) {
            dateEntries.add(dateCursor.getString(0));
            count++;
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Last 5 days");
        LineData lineData = new LineData(dateEntries, lineDataSet);
        lineChart.setData(lineData);
        lineChart.setDescription("Blah");

        return view;
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
