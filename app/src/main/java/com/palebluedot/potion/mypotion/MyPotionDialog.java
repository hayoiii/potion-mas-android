package com.palebluedot.potion.mypotion;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import com.palebluedot.potion.search.DetailActivity;
import com.palebluedot.potion.R;
import com.palebluedot.potion.db.DbContract;
import com.palebluedot.potion.db.DbHelper;

public class MyPotionDialog extends DialogFragment implements View.OnClickListener {
    static private String mProduct, mFactory, mDate, mMemo, mSerialNo;
    static private int mDays, mTimes;

    public static MyPotionDialog newInstance(String product, String factory, String date, String memo, int days, int times, String serialNo) {
        mProduct = product;
        mFactory = factory;
        mDate = date;
        mMemo = memo;
        mDays = days;
        mTimes = times;
        mSerialNo = serialNo;

        MyPotionDialog e = new MyPotionDialog();
        return e;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_my_potion, container);
        ImageButton btnBack = view.findViewById(R.id.back_btn);
        Button btnModify = view.findViewById(R.id.modify_btn);
        Button btnDelete = view.findViewById(R.id.delete_btn);
        Button btnDetail = view.findViewById(R.id.detail_btn);

        btnBack.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnDetail.setOnClickListener(this);

        TextView product_text = view.findViewById(R.id.name_detail);
        TextView factory_text = view.findViewById(R.id.effect_detail);
        TextView date_text = view.findViewById(R.id.caution_detail);
        TextView memo_text = view.findViewById(R.id.remark_detail);
        TextView days_text = view.findViewById(R.id.low_detail);
        TextView times_text = view.findViewById(R.id.high_detail);

        product_text.setText(mProduct);
        factory_text.setText(mFactory);
        date_text.setText(mDate);
        memo_text.setText(mMemo);
        days_text.setText(String.valueOf(mDays));
        times_text.setText(String.valueOf(mTimes));

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_btn:
                dismiss();
                break;
            case R.id.modify_btn:
                Intent modifyIntent = new Intent(getActivity().getApplicationContext(), AddPotionActivity.class);
                modifyIntent.putExtra("isNew", false);
                modifyIntent.putExtra("product", mProduct);
                modifyIntent.putExtra("factory", mFactory);
                modifyIntent.putExtra("serialNo", mSerialNo);

                modifyIntent.putExtra("date", mDate);
                modifyIntent.putExtra("memo", mMemo);

                startActivity(modifyIntent);
                dismiss();

                break;
            case R.id.delete_btn:
                deleteItem();

                break;
            case R.id.detail_btn:
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("serialNo", mSerialNo);
                intent.putExtra("addBlocking", true);
                startActivity(intent);
                break;
        }
    }

    private void deleteItem(){
        DbHelper dbHelper = DbHelper.getInstance(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int deleteCount = db.delete(DbContract.MyPotionEntry.TABLE_NAME, DbContract.MyPotionEntry.COLUMN_NAME_SERIALNO+"="+mSerialNo, null);
        Toast.makeText(getActivity().getApplicationContext(), deleteCount+"개 항목 삭제", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
