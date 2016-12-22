package com.szentesi.david.cyclingpalapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BMIFragment extends Fragment {

    private String userEmail;
    private int userHeight;
    private int userWeight;
    private TextView bmiTextView;
    private View bmiFragmentView;

    private SQLiteDatabase cyclingPalDB = null;

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
        userEmail = getArguments().getString("emailContainer");
        bmiFragmentView = inflater.inflate(R.layout.fragment_bmi, container, false);
        bmiTextView = (TextView)bmiFragmentView.findViewById(R.id.bmiTextView);
        userFitnessData();

        return bmiFragmentView;
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

    private void userFitnessData() {
        // on how to open DB in fragment
        // http://stackoverflow.com/questions/24511031/openorcreatedatabase-undefined-in-the-fragment-class
        cyclingPalDB = getActivity().openOrCreateDatabase("CyclingPal", Context.MODE_PRIVATE, null);
        Cursor cursor = cyclingPalDB.rawQuery("select weight, height " +
                "from userFitnessInfo " +
                "where email = " + "'" + userEmail + "'", null);
        cursor.moveToFirst();
        userWeight = cursor.getInt(0);
        userHeight = cursor.getInt(1);
        double bmi = UnitConvertion.calculateBMI(userWeight, userHeight);
        bmiTextView.setText(String.valueOf(bmi));
    }

}
