package com.palebluedot.potion.search;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.palebluedot.potion.R;
import com.palebluedot.potion.api.model.Potion;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class   SearchActivity extends AppCompatActivity {
    public static final int REQUEST_DETAIL = 1000;
    private TextInputEditText mSearchView;
    private ListView mListView;
    private Button mSearchButton;
    private Button mNextButton;
    private Button mPreviousButton;
    private TextView mCurrpageNo;
    private TextView mMaxpageNo;
    static int pageNo = 1;
    static int maxPageNo = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mCurrpageNo = findViewById(R.id.curr_pageNo);
        mCurrpageNo.setText("");
        mMaxpageNo = findViewById(R.id.max_pageNo);
        mMaxpageNo.setText("");
        mSearchView = findViewById(R.id.search_text);
        mListView = findViewById(R.id.result_list_view);
        mSearchButton = findViewById(R.id.search_button);
        mPreviousButton = findViewById(R.id.previous_button);
        mNextButton = findViewById(R.id.next_button);

        mSearchButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = mSearchView.getText().toString();
                pageNo = 1;
                HtfsInfoServiceAPI task = new HtfsInfoServiceAPI(keyword, pageNo, SearchActivity.this);
                task.execute();
            }
        });

        mNextButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                if (pageNo+1 <= maxPageNo){
                    pageNo++;
                    String keyword = mSearchView.getText().toString();
                    HtfsInfoServiceAPI task = new HtfsInfoServiceAPI(keyword, pageNo, SearchActivity.this);
                    task.execute();
                }
            }
        });

        mPreviousButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if(pageNo>1){
                    pageNo--;
                    String keyword = mSearchView.getText().toString();
                    HtfsInfoServiceAPI task = new HtfsInfoServiceAPI(keyword, pageNo, SearchActivity.this);
                    task.execute();
                }
            }
        });
    }



    /*건강기능식품 서비스 api*/
    private class HtfsInfoServiceAPI extends AsyncTask<Void, Void, List<Potion>> {
        private final String BASE_URI = "http://apis.data.go.kr/1470000/HtfsInfoService/getHtfsList?ServiceKey=s%2BNvTjaKtsyT1%2BlP6yGS%2FCfxqzWMgdGie6yOQr3dJLHAYG9q0sQmYdrDrrMCnS3797H9TBiHJBI8%2BFy3ex9A0A%3D%3D";
        private String url;
        private Context mContext = null;

        HtfsInfoServiceAPI(String product, int pageNo, Context context){
            if(product == null){
                this.url = this.BASE_URI+"&pageNo="+pageNo;
            }
            else
                this.url = this.BASE_URI + "&Prduct="+product+"&pageNo="+pageNo;
            mContext = context;
        }

        /*백그라운드 스레드가 실행되기 전, 메인 스레드에 의해 호출되는 메서드
        * 주로 UI 초기화 */
        @Override
        protected void onPreExecute() {

        }

        /*실질적인 비동기 작업이 실행
        * UI를 직접 제어하면 X*/
        @Override
        protected List<Potion> doInBackground(Void... voids) {
            List<Potion> potions = new ArrayList<Potion>();
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            try {
                dBuilder = dbFactoty.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document doc = null;
            try {
                doc = dBuilder.parse(this.url);
            } catch (IOException | SAXException e) {
                Log.d("Exception", "on Document");
                e.printStackTrace();
            }

            // root tag
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName()); // Root element: result

            //page 정보
            NodeList nHeaderList = doc.getElementsByTagName("header");
            Node nHeader = nHeaderList.item(0);
            org.w3c.dom.Element eHeader = (org.w3c.dom.Element) nHeader;
            String sCode = getTagValue("resultCode", eHeader);

            if(!sCode.equals("00")){
                Log.e("API error", "error code-"+sCode);
                return null;
            }

            // 페이지 정보 추출
            NodeList nBodyList = doc.getElementsByTagName("body");
            Node nBody = nBodyList.item(0);
            org.w3c.dom.Element eBody = (org.w3c.dom.Element) nBody;
            String sPageNo = getTagValue("pageNo", eBody);
            String sTotal = getTagValue("totalCount", eBody);
            pageNo = Integer.parseInt(sPageNo);
            maxPageNo = (int)Math.ceil( Integer.parseInt(sTotal)/10.0 );

            //건강기능식품 리스트 받아오기
            NodeList nList = doc.getElementsByTagName("item");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
                    String product = getTagValue("PRDUCT", eElement);
                    String factory = getTagValue("ENTRPS", eElement);
                    String serialNo = getTagValue("STTEMNT_NO", eElement);

                    Potion item = new Potion(product, factory, serialNo);
                    potions.add(item);
                }    // for end
            }    // if end
            return potions;
        }

        /*doInBackground의 결과값을 받음*/
        @Override
        protected void onPostExecute(List<Potion> potions) {
            if (potions != null) {
                SearchListAdapter adapter = new SearchListAdapter(potions);
                mListView.setAdapter(adapter);
                mCurrpageNo.setText(Integer.toString(pageNo));
                mMaxpageNo.setText(Integer.toString(maxPageNo));
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                        Potion potion = (Potion)parent.getItemAtPosition(position);
                        intent.putExtra("serialNo", potion.getSerialNo());
                        startActivity(intent);
                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "ERROR: onPostExecute()", Toast.LENGTH_SHORT);
            }
        }

        private String getTagValue(String tag, org.w3c.dom.Element eElement) {
            NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
            Node nValue = (Node) nlList.item(0);
            if(nValue == null)
                return null;
            return nValue.getNodeValue();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}