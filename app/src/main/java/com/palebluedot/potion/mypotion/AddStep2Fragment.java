package com.palebluedot.potion.mypotion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.palebluedot.potion.R;

import nl.dionsegijn.steppertouch.OnStepCallback;
import nl.dionsegijn.steppertouch.StepperTouch;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddStep2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddStep2Fragment extends Fragment {
    private static int days = 0;
    private static int times = 0;
    private StepperTouch stepperDays;
    private StepperTouch stepperTimes;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddStep2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddStep2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddStep2Fragment newInstance(String alias, String date, String memo) {
        AddStep2Fragment fragment = new AddStep2Fragment();
        Bundle args = new Bundle();
        /*
        args.putString(ALIAS, alias);
        args.putString(DATE, date);
        args.putString(MEMO, memo);

        fragment.setArguments(args);
        */
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_step2, container, false);
        Log.d("AddStep2Fragment","start");
        stepperDays = view.findViewById(R.id.stepper_days);
        stepperDays.setMinValue(0);
        stepperDays.setMaxValue(7);
        stepperDays.setSideTapEnabled(true);
        stepperDays.addStepCallback(new OnStepCallback() {
            @Override
            public void onStep(int value, boolean positive) {
                days = value;
            }
        });

        stepperTimes = view.findViewById(R.id.stepper_times);
        stepperTimes.setMinValue(1);
        stepperTimes.setSideTapEnabled(true);
        stepperTimes.addStepCallback(new OnStepCallback() {
            @Override
            public void onStep(int value, boolean positive) {
                times = value;
            }
        });

        return view;
    }

    public int[] toNext(){
        int[] input = new int[2];
        input[0] = days;
        input[1] = times;

        return input;
    }
}