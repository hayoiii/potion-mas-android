package com.palebluedot.potion.customized;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.palebluedot.clientsdk.model.Customized;
import com.palebluedot.clientsdk.model.CustomizedResultItem;
import com.palebluedot.potion.R;
import com.palebluedot.potion.api.PotionApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 */
public class CustomizedFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private CustomizedAdapter adapter;
    private ListView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CustomizedFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CustomizedFragment newInstance(int columnCount) {
        CustomizedFragment fragment = new CustomizedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customized_list, container, false);
        mListView = view.findViewById(R.id.recycler_list);
        mListView.setOnItemClickListener(this::onItemClick);

        mSwipeRefreshLayout = view.findViewById(R.id.customized_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        getCustomizedList();
        return view;
    }

    private void getCustomizedList() {
        //getCustomizedById(param): if param == -1, show all
        Call<Customized> res = PotionApiUtil.getInstance().getApi().getCustomizedById(-1);
        res.enqueue(new Callback<Customized>() {
            @Override
            public void onResponse(Call<Customized> call, Response<Customized> response) {
                if (response.isSuccessful()) {
                    Customized customizedList = response.body();
                    //TODO:
                    if(customizedList.getStatusCode() == 201) {
                        adapter = new CustomizedAdapter(customizedList.getResult());
                        mListView.setAdapter(adapter);
                        Toast.makeText(getActivity().getApplicationContext(), "사용자 정의 포션 목록 가져오기 성공", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "FAILED: Error on MongoDb operation", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "FAILED: Http Connection error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Customized> call, Throwable t) {
                Log.e("ACCESS API", "failed on getCustomizedList");
                Log.e("throwable", t.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), "Failed to connect API", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CustomizedResultItem item = (CustomizedResultItem) parent.getItemAtPosition(position);
        String name = item.getName();
        String factory = item.getFactory();
        String effect = item.getEffect();
        String way = item.getWayToTake();
        String remark = item.getRemark();
        int item_id = Integer.parseInt(item.getId());

        CustomizedDialog dialog = CustomizedDialog.newInstance(item_id, name, factory,effect, way, remark);
        dialog.showNow(getActivity().getSupportFragmentManager(), "customized_dialog");
    }

    @Override
    public void onRefresh() {
        getCustomizedList();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}