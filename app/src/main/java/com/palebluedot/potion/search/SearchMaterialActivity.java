package com.palebluedot.potion.search;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.palebluedot.clientsdk.model.Material;
import com.palebluedot.clientsdk.model.MaterialResultItem;
import com.palebluedot.potion.R;
import com.palebluedot.potion.api.PotionApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMaterialActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final int REQUEST_DETAIL = 1000;
    public static final int BY_NAME = 1;
    public static final int BY_EFFECT=2;


    private TextInputEditText mSearchView;
    private ListView mListView;
    private Button mSearchButton;
    private RadioButton mRadioName;
    private SearchMaterialAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_material);

        mSearchView = findViewById(R.id.search_text);
        mListView = findViewById(R.id.result_list_view);
        mSearchButton = findViewById(R.id.search_button);
        mRadioName = findViewById(R.id.radio_name);

        mSearchButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = mSearchView.getText().toString();

                //이름으로 검색하기
                if(mRadioName.isChecked()) {
                    getMaterialFromApi(BY_NAME, keyword);
                }
                //효능으로 검색하
                else{
                    getMaterialFromApi(BY_EFFECT, keyword);
                }
            }
        });

    }



    private void getMaterialFromApi(int type, String keyword){
        Call<Material> res = null;
        if(type == BY_NAME) {
            res = PotionApiUtil.getInstance().getApi().getMaterialsByName(keyword);
        }
        if(type == BY_EFFECT){
            res = PotionApiUtil.getInstance().getApi().getMaterialsByEffect(keyword);
        }
        res.enqueue(new Callback<Material>() {
            @Override
            public void onResponse(Call<Material> call, Response<Material> response) {
                if (response.isSuccessful()) {
                    Material materialList = response.body();
                    if (materialList.getStatusCode() == 201) {
                        adapter = new SearchMaterialAdapter(materialList);
                        mListView.setAdapter(adapter);
                        mListView.setOnItemClickListener(SearchMaterialActivity.this::onItemClick);
                        Toast.makeText(getApplicationContext(), "검색 완료", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "FAILED: Error on MongoDb operation", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "FAILED: Http Connection error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Material> call, Throwable t) {
                Log.e("ACCESS API", "failed on getMaterialByNameFromApi");
                Toast.makeText(getApplicationContext(), "Failed to connect API", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MaterialResultItem item = adapter.getItem(position);
        MaterialDialog dialog = MaterialDialog.newInstance(
                item.getRawMaterial(), item.getName(),item.getEffect(), item.getCaution(), item.getRemark(),
                item.getDayHighLimit(), item.getDayLowLimit(), item.getLastUpdate());
        dialog.show(getSupportFragmentManager(), "material_dialog");
    }
}