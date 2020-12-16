package com.palebluedot.potion.search;

import android.content.Intent;
import android.os.Bundle;

import com.palebluedot.potion.R;
import com.palebluedot.potion.api.OpenDataApiUtil;
import com.palebluedot.potion.api.model.PotionDetail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.palebluedot.potion.mypotion.AddPotionActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private TextView effectText;
    private TextView shapeText;
    private TextView cautionText;
    private TextView takeWayText;
    private TextView storeWayText;
    private TextView materialText;
    private TextView productText;
    private TextView factoryText;
    private String product;
    private String factory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        String serialNo = intent.getStringExtra("serialNo");
        Boolean addBlocking = intent.getBooleanExtra("addBlocking", false);

        productText = findViewById(R.id.detail_product_text);
        factoryText = findViewById(R.id.detail_factory_text);

        effectText = findViewById(R.id.effect_text);
        shapeText = findViewById(R.id.shape_text);
        cautionText = findViewById(R.id.caution_text);
        takeWayText = findViewById(R.id.take_way_text);
        storeWayText = findViewById(R.id.store_way_text);
        materialText = findViewById(R.id.material_text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), AddPotionActivity.class);
            intent1.putExtra("isNew", true);
            intent1.putExtra("product", product);
            intent1.putExtra("serialNo", serialNo);
            intent1.putExtra("factory", factory);
            startActivity(intent1);
            finish();
        });

        Call<PotionDetail> res = OpenDataApiUtil.getInstance().getApi().getDetail(serialNo);
        res.enqueue(new Callback<PotionDetail>() {
            @Override
            public void onResponse(Call<PotionDetail> call, Response<PotionDetail> response) {
                if (response.body() != null) {
                    PotionDetail potionDetail = (PotionDetail) response.body();
                    if (potionDetail.getC003().getRESULT().getCODE().equals("INFO-000")) {
                        product = potionDetail.getC003().getRow().get(0).getPRDLST_NM();
                        factory = potionDetail.getC003().getRow().get(0).getBSSH_NM();
                        String shape = potionDetail.getC003().getRow().get(0).getDISPOS();
                        String takeWay = potionDetail.getC003().getRow().get(0).getNTK_MTHD();
                        String effect = potionDetail.getC003().getRow().get(0).getPRIMARY_FNCLTY();
                        String caution = potionDetail.getC003().getRow().get(0).getIFTKN_ATNT_MATR_CN();
                        String storeWay = potionDetail.getC003().getRow().get(0).getCSTDY_MTHD();
                        String materialResult = potionDetail.getC003().getRow().get(0).getRAWMTRL_NM();

                        String[] materials = materialResult.split(",");

                        String material = "";
                        for(int i=0; i<materials.length; i++){
                            material += materials[i]+"\n";
                        }
                        productText.setText(product);
                        factoryText.setText(factory);

                        shapeText.setText(shape);
                        takeWayText.setText(takeWay);
                        effectText.setText(effect);
                        cautionText.setText(caution);
                        storeWayText.setText(storeWay);
                        materialText.setText(material);

                        if(!addBlocking){
                            fab.setVisibility(View.VISIBLE);
                        }
                    }
                    else{
                        /*오류 코드 시 처리 */
                        productText.setText(potionDetail.getC003().getRESULT().getMSG());
                    }
                }
            }

            @Override
            public void onFailure(Call<PotionDetail> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });

    }


}