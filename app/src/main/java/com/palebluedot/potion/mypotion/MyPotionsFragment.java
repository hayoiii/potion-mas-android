package com.palebluedot.potion.mypotion;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.palebluedot.potion.R;
import com.palebluedot.potion.db.DbContract;
import com.palebluedot.potion.db.DbHelper;

/**
 * A fragment representing a list of Items.
 */
public class MyPotionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MyPotionsAdapter adapter;
    private DbHelper dbHelper;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyPotionsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyPotionsFragment newInstance(int columnCount) {
        MyPotionsFragment fragment = new MyPotionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_potions_list, container, false);
        ListView potionList = view.findViewById(R.id.my_potion_list);
        mSwipeRefreshLayout = view.findViewById(R.id.my_potion__swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        dbHelper = DbHelper.getInstance(this.getContext());
        Cursor cursor = dbHelper.getReadableDatabase()
                .query(DbContract.MyPotionEntry.TABLE_NAME, null,null,null,null,null,null);
        adapter = new MyPotionsAdapter(this.getContext(), cursor, 1);
        potionList.setAdapter(adapter);
        potionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                String serialNo = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MyPotionEntry.COLUMN_NAME_SERIALNO));
                String product = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MyPotionEntry.COLUMN_NAME_PRODUCT));
                String factory = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MyPotionEntry.COLUMN_NAME_FACTORY));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MyPotionEntry.COLUMN_NAME_DATE));
                String memo = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MyPotionEntry.COLUMN_NAME_MEMO));
                int days = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.MyPotionEntry.COLUMN_NAME_DAYS));
                int times = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.MyPotionEntry.COLUMN_NAME_TIMES));

                MyPotionDialog dialog = MyPotionDialog.newInstance(product, factory, date, memo, days, times, serialNo);
                dialog.show(getActivity().getSupportFragmentManager(), "my_potion_dialog");
            }


        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.swapCursor(dbHelper.getReadableDatabase()
                .query(DbContract.MyPotionEntry.TABLE_NAME, null,null,null,null,null,null));

    }

    @Override
    public void onRefresh() {
        adapter.swapCursor(dbHelper.getReadableDatabase()
                .query(DbContract.MyPotionEntry.TABLE_NAME, null,null,null,null,null,null));
        mSwipeRefreshLayout.setRefreshing(false);
    }
}