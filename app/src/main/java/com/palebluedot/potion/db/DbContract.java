package com.palebluedot.potion.db;

import android.provider.BaseColumns;

public final class DbContract {
    private DbContract() {}

    public static class MyPotionEntry implements BaseColumns{
        public static final String TABLE_NAME = "myPotion";
        public static final String COLUMN_NAME_PRODUCT = "product";
        public static final String COLUMN_NAME_FACTORY = "factory";
        public static final String COLUMN_NAME_ALIAS = "alias";
        public static final String COLUMN_NAME_MEMO = "memo";
        public static final String COLUMN_NAME_SERIALNO = "serial_no";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_DAYS = "days";
        public static final String COLUMN_NAME_TIMES = "times";
    }
}

