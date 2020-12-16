package com.palebluedot.potion.customized;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.palebluedot.potion.R;
import com.palebluedot.potion.api.PotionApiUtil;
import com.palebluedot.potion.api.model.CustomizedBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCustomizedActivity extends AppCompatActivity {
    private EditText nameEdit, factoryEdit, effectEdit, wayEdit, remarkEdit;
    private Button btn;
    private static boolean isNew = true;
    private static int target=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isNew = getIntent().getBooleanExtra("isNew", true);
        target = getIntent().getIntExtra("target", -1);
        setContentView(R.layout.activity_add_customized);

        nameEdit = findViewById(R.id.name_edit);
        factoryEdit = findViewById(R.id.factory_edit);
        effectEdit = findViewById(R.id.effect_edit);
        wayEdit = findViewById(R.id.way_edit);
        remarkEdit = findViewById(R.id.remark_edit);
        btn = findViewById(R.id.customized_finish_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,factory,effect,way,remark;
                name = nameEdit.getText().toString();
                factory = factoryEdit.getText().toString();
                effect = effectEdit.getText().toString();
                way = wayEdit.getText().toString();
                remark = remarkEdit.getText().toString();


                if(isNew) {
                    CustomizedBody newItem = new CustomizedBody(null, name, factory, way, effect, remark);
                    postCustomizedByApi(newItem);
                }
                else {
                    if(name.equals(""))
                        name = null;
                    if(factory.equals(""))
                        factory = null;
                    if(effect.equals(""))
                        effect = null;
                    if(way.equals(""))
                        way = null;
                    if(remark.equals(""))
                        remark = null;
                    CustomizedBody updatedItem = new CustomizedBody(null, name, factory, way, effect, remark);
                    patchCustomizedByApi(target, updatedItem);
                }

            }
        });
    }
    public void postCustomizedByApi(CustomizedBody newItem) {
        Log.e("newItem", newItem.toString());
        Call<Integer> res = PotionApiUtil.getInstance().getApi().postCustomized(newItem);
        res.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d("onResponse","start");
                Log.d("response.code", String.valueOf(response.code()));

                if(response.code() == 201) {
                    Integer result = response.body();

                    Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_LONG).show();
                    Log.e("new id", result.toString());
                }

                else{
                    Toast.makeText(getApplicationContext(), "FAILED: Error on MongoDb operation", Toast.LENGTH_LONG).show();
                }
                finish();
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("ACCESS API", "failed on postCustomizedByApi");
                Log.e("throwable", t.getMessage());
                Toast.makeText(getApplicationContext(), "Failed to connect API", Toast.LENGTH_LONG).show();
            }
        });

        Log.d("onResponse", "end");
    }

    public void patchCustomizedByApi(int target, CustomizedBody newItem) {
        Log.e("target item", newItem.toString());
        Call<Integer> res = PotionApiUtil.getInstance().getApi().patchCustomized(target, newItem);
        res.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d("onResponse","start");
                Log.d("response.code", String.valueOf(response.code()));

                if(response.code() == 201) {
                    Integer result = response.body();

                    Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_LONG).show();
                    Log.e("new id", result.toString());
                }

                else{
                    Toast.makeText(getApplicationContext(), "FAILED: Error on MongoDb operation", Toast.LENGTH_LONG).show();
                }
                finish();
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("ACCESS API", "failed on patchCustomizedByApi");
                Log.e("throwable", t.getMessage());
                Toast.makeText(getApplicationContext(), "Failed to connect API", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }


}