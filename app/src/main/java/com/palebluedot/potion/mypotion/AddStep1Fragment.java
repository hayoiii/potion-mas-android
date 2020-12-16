package com.palebluedot.potion.mypotion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.palebluedot.potion.R;

public class AddStep1Fragment extends Fragment {
    private EditText mAliasInput;
    private EditText mDateInput;
    private EditText mMemoInput;
    private ProgressBar mProgressBar;
    private static String mDate = null, mMemo = null;

    public static AddStep1Fragment newInstance(String date, String memo){
        mDate = date;
        mMemo = memo;

        AddStep1Fragment e = new AddStep1Fragment();
        return e;
    }

    public AddStep1Fragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_step1, container, false);
        mAliasInput = view.findViewById(R.id.input_product);
        mDateInput = view.findViewById(R.id.input_effect);
        mMemoInput = view.findViewById(R.id.input_wayToTake);

        mDateInput.setText("");
        mMemoInput.setText("");

        if(mDate != null){
            mDateInput.setText(mDate);
        }
        if(mMemo != null){
            mMemoInput.setText(mMemo);
        }
        return view;
    }

    public String[] toNext(){
        String[] input = new String[3];
        String alias = mAliasInput.getText().toString();
        String date = mDateInput.getText().toString();
        String memo = mMemoInput.getText().toString();

        input[0] = alias;
        input[1] = date;
        input[2] = memo;
        return input;
    }


}