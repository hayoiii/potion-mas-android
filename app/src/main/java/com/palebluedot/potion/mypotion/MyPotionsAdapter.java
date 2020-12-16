package com.palebluedot.potion.mypotion;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.palebluedot.potion.R;
import com.palebluedot.potion.db.DbContract;

public class MyPotionsAdapter extends CursorAdapter {
    public MyPotionsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context)
                .inflate(R.layout.item_my_potion, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView aliasText = view.findViewById(R.id.alias_text);
        TextView memoText = view.findViewById(R.id.memo_text);

        String alias;
        if(!cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MyPotionEntry.COLUMN_NAME_ALIAS)).equals("")){
            alias = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MyPotionEntry.COLUMN_NAME_ALIAS));
        }
        else
            alias = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MyPotionEntry.COLUMN_NAME_PRODUCT));
        String memo = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MyPotionEntry.COLUMN_NAME_MEMO));

        aliasText.setText(alias);
        memoText.setText(memo);
    }
}
