package com.palebluedot.potion.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.palebluedot.potion.R;

public class MaterialDialog extends DialogFragment {
    static private String mRaw, mName, mEffect, mCaution, mRemark, mHigh, mLow, mDate;

    public static MaterialDialog newInstance(String raw, String name, String effect, String caution, String remark, String high, String low, String date) {
        mName = name;
        mEffect = effect;
        mCaution = caution;
        mRemark = remark;
        mHigh = high;
        mLow = low;
        mDate = date;
        mRaw = raw;

        MaterialDialog e= new MaterialDialog();
        return e;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_material, container);

        TextView nameText = view.findViewById(R.id.name_detail);
        TextView effectText = view.findViewById(R.id.effect_detail);
        TextView rawText = view.findViewById(R.id.raw_detail);
        TextView cautionText = view.findViewById(R.id.caution_detail);
        TextView remarkText = view.findViewById(R.id.remark_detail);
        TextView lowText = view.findViewById(R.id.low_detail);
        TextView highText = view.findViewById(R.id.high_detail);
        TextView dateText = view.findViewById(R.id.update_date_detail);
        ImageButton btnBack = view.findViewById(R.id.back_btn);

        nameText.setText(mName);
        effectText.setText(mEffect);
        rawText.setText(mRaw);
        cautionText.setText(mCaution);
        remarkText.setText(mRemark);
        lowText.setText(mLow);
        highText.setText(mHigh);
        dateText.setText(mDate);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
