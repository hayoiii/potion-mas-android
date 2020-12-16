package com.palebluedot.potion.customized;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.palebluedot.potion.R;
import com.palebluedot.potion.api.PotionApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomizedDialog extends DialogFragment {
    static private String mName, mFactory, mEffect, mRemark, mWay;
    static private int mId;

    public static CustomizedDialog newInstance(int id, String name, String factory, String effect, String way, String remark){
        mName = name;
        mFactory = factory;
        mEffect = effect;
        mWay = way;
        mRemark = remark;
        mId = id;

        CustomizedDialog e = new CustomizedDialog();
        return e;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.dialog_customized, container);
        TextView nameText = view.findViewById(R.id.customized_name_detail);
        TextView effectText = view.findViewById(R.id.customized_effect_detail);
        TextView factoryText = view.findViewById(R.id.customized_factory_detail);
        TextView wayText = view.findViewById(R.id.customized_way_detail);
        TextView remarkText = view.findViewById(R.id.customized_remark_detail);
        ImageButton btnBack = view.findViewById(R.id.customized_back_btn);
        Button btnModify = view.findViewById(R.id.customized_modify_btn);
        Button btnDelete = view.findViewById(R.id.customized_delete_btn);

        nameText.setText(mName);
        effectText.setText(mEffect);
        factoryText.setText(mFactory);
        wayText.setText(mWay);
        remarkText.setText(mRemark);

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deleteCustomizedByApi(mId);
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddCustomizedActivity.class);
                intent.putExtra("isNew", false);
                intent.putExtra("target", mId);
                startActivity(intent);
                dismiss();
            }
        });
        return view;

    }

    private void deleteCustomizedByApi(int targetId) {
        Call<Integer> res = PotionApiUtil.getInstance().getApi().deleteCustomized(targetId);
        res.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d("onResponse","start");
                Log.d("response.code", String.valueOf(response.code()));

                if(response.code() == 201) {
                    Integer result = response.body();

                    Toast.makeText(getActivity().getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
                    Log.e("new id", result.toString());
                }

                else{
                    Toast.makeText(getActivity().getApplicationContext(), "FAILED: Error on MongoDb operation", Toast.LENGTH_LONG).show();
                }
                dismiss();
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("ACCESS API", "failed on deleteCustomizedByApi");
                Log.e("throwable", t.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), "Failed to connect API", Toast.LENGTH_LONG).show();
            }
        });
    }
}
