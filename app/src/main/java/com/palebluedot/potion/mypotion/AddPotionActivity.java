package com.palebluedot.potion.mypotion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.palebluedot.potion.R;
import com.palebluedot.potion.db.DbContract;
import com.palebluedot.potion.db.DbHelper;

public class AddPotionActivity extends AppCompatActivity implements View.OnClickListener {
    private Button nextBtn;
    private AddStep1Fragment firstFragment;
    private AddStep2Fragment secondFragment;
    private TextView step1Text;
    private TextView step2Text;

    private String serialNo;
    private String product;
    private String factory;

    private String inputAlias;
    private String inputDate;
    private String inputMemo;
    private int inputDays;
    private int inputTimes;

    private DbHelper dbHelper;
    private boolean isNew = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_potion);

        Intent intent = getIntent();
        isNew = intent.getBooleanExtra("isNew", true);
        serialNo = intent.getStringExtra("serialNo");
        product = intent.getStringExtra("product");
        factory = intent.getStringExtra("factory");


        if(!isNew){
            String date, memo;
            date = intent.getStringExtra("date");
            memo = intent.getStringExtra("memo");

            firstFragment = AddStep1Fragment.newInstance(date, memo);
        }
        else{
            firstFragment = AddStep1Fragment.newInstance(null, null);
        }

        step1Text = findViewById(R.id.textStep1);
        step2Text = findViewById(R.id.textStep2);
        step2Text.setVisibility(View.INVISIBLE);

        TextView productText = findViewById(R.id.new_product);
        productText.setText(product);
        TextView factoryText = findViewById(R.id.new_factory);
        factoryText.setText(factory);


        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment)
                .commit();

        nextBtn = findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(!nextBtn.getText().equals("FINISH")) {
            String[] first_input = firstFragment.toNext();
            inputAlias = first_input[0];
            inputDate = first_input[1];
            inputMemo = first_input[2];

            secondFragment = new AddStep2Fragment();

            AddStep2Fragment secondFragment = new AddStep2Fragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, secondFragment)
                    .commit();

            nextBtn.setText("FINISH");
            step2Text.setVisibility(View.VISIBLE);
        }

        else if(nextBtn.getText().equals("FINISH")){
            int[] second_input = secondFragment.toNext();
            inputDays = second_input[0];
            inputTimes = second_input[1];

            dbHelper = DbHelper.getInstance(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DbContract.MyPotionEntry.COLUMN_NAME_PRODUCT, product);
            values.put(DbContract.MyPotionEntry.COLUMN_NAME_FACTORY, factory);
            values.put(DbContract.MyPotionEntry.COLUMN_NAME_ALIAS, inputAlias);
            values.put(DbContract.MyPotionEntry.COLUMN_NAME_SERIALNO, serialNo);
            values.put(DbContract.MyPotionEntry.COLUMN_NAME_MEMO, inputMemo);
            values.put(DbContract.MyPotionEntry.COLUMN_NAME_DATE, inputDate);
            values.put(DbContract.MyPotionEntry.COLUMN_NAME_DAYS, inputDays);
            values.put(DbContract.MyPotionEntry.COLUMN_NAME_TIMES, inputTimes);

            //insert
            if(isNew) {
                long newRowId = db.insert(DbContract.MyPotionEntry.TABLE_NAME, null, values);
                if(newRowId>0) {
                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "저장 실패.", Toast.LENGTH_SHORT).show();
                }
            }
            //update
            else{
                db.update(DbContract.MyPotionEntry.TABLE_NAME, values, DbContract.MyPotionEntry.COLUMN_NAME_SERIALNO+"="+ serialNo, null);
                Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
            }
            //TODO: 저장 성공/ 실패 등 메시지 띄워주기
            finish();
        }
    }
}