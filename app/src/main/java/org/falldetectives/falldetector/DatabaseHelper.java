package org.falldetectives.falldetector;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fall_data.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_FALL_DATA = "fall_data";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_IS_FALSE_ALARM = "is_false_alarm";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_FALL_DATA + " (" +
                    COLUMN_TIMESTAMP + " INTEGER, " +
                    COLUMN_IS_FALSE_ALARM + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FALL_DATA);
        onCreate(db);
    }
}