package com.palebluedot.potion.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    //singleton
    private static DbHelper sInstance;

    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "db.db";

    //create TABLE
    private static final String SQL_CREATE_ENTRIES=
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT)",
                    DbContract.MyPotionEntry.TABLE_NAME,
                    DbContract.MyPotionEntry._ID,
                    DbContract.MyPotionEntry.COLUMN_NAME_PRODUCT,
                    DbContract.MyPotionEntry.COLUMN_NAME_FACTORY,
                    DbContract.MyPotionEntry.COLUMN_NAME_SERIALNO,
                    DbContract.MyPotionEntry.COLUMN_NAME_ALIAS,
                    DbContract.MyPotionEntry.COLUMN_NAME_DATE,
                    DbContract.MyPotionEntry.COLUMN_NAME_DAYS,
                    DbContract.MyPotionEntry.COLUMN_NAME_TIMES,
                    DbContract.MyPotionEntry.COLUMN_NAME_MEMO);
    //delete TABLE
    private static final String SQL_DELETE_ENTRIES=
            "DROP TABLE IF EXISTS "+ DbContract.MyPotionEntry.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db 스키마 변경 시 데이터 백업
        //테이블 삭제 후 재생성 및 데이터 복원 등
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public static synchronized DbHelper getInstance(Context context){
        //memory leak 발생 가능성 -> 액티비티의 context 대신 application context 사용하기
        if(sInstance ==null){
            sInstance = new DbHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    private DbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
}
