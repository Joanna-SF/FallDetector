package org.falldetectives.falldetector;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

        import androidx.annotation.Nullable;

        import java.util.ArrayList;
        import java.util.List;

public class UserDatabase extends SQLiteOpenHelper {

    public static final String USER_TABLE = "User_Table";
    public static final String COLUMN_USER_NAME = "USER_NAME";
    public static final String COLUMN_USER_AGE = "USER_AGE";
    public static final String COLUMN_USER_BLOOD_TYPE = "USER_BLOOD_TYPE";
    public static final String COLUMN_USER_EMERGENCY_CONTACT = "USER_EMERGENCY_CONTACT";
    public static final String COLUMN_ID = "ID";
    public static final String FALL_HISTORY_TABLE = "Fall_History_Table";
    public static final String COLUMN_FALL_TIMESTAMP = "FALL_TIMESTAMP";
    public static final String COLUMN_FALL_IS_FALSE_ALARM = "IS_FALSE_ALARM";
    public static final String COLUMN_FALL_USER_NAME = "USER_NAME";
    public UserDatabase(@Nullable Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + USER_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_AGE + " TEXT, " +
                COLUMN_USER_BLOOD_TYPE + " TEXT, " +
                COLUMN_USER_EMERGENCY_CONTACT + " TEXT)";
        db.execSQL(createTableStatement);

        String createFallHistoryTableStatement = "CREATE TABLE " + FALL_HISTORY_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FALL_TIMESTAMP + " INTEGER, " +
                COLUMN_FALL_IS_FALSE_ALARM + " INTEGER, " +
                COLUMN_FALL_USER_NAME + " TEXT)";
        db.execSQL(createFallHistoryTableStatement);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME, userModel.getName());
        cv.put(COLUMN_USER_AGE, userModel.getAge());
        cv.put(COLUMN_USER_BLOOD_TYPE, userModel.getBlood_type());
        cv.put(COLUMN_USER_EMERGENCY_CONTACT, userModel.getEmergency_contact());

        long insert = db.insert(USER_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }

    }
    public List<UserModel> getEveryone(){
        List<UserModel> returnList = new ArrayList<>();

        //get data from database
        String queryString= "SELECT * FROM " + USER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()){
            // loop through the results and create new user objects
            do{
                int userID=cursor.getInt(0);
                String userName=cursor.getString(1);
                String userAge=cursor.getString(2);
                String userBloodType=cursor.getString(3);
                int userEmergencyContact=cursor.getInt(4);


                UserModel newUser=new UserModel(userID, userName,userAge,userBloodType,userEmergencyContact);
                returnList.add(newUser);
            }while(cursor.moveToNext());
        }
        else{
            // do not add anything to the list
        }
        cursor.close();
        db.close();
        return returnList;
    }
    public boolean addFallEvent(FallData fallData, String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FALL_TIMESTAMP, fallData.getTimestamp());
        cv.put(COLUMN_FALL_IS_FALSE_ALARM, fallData.isFalseAlarm() ? 1 : 0);
        cv.put(COLUMN_FALL_USER_NAME, userName);
        long insert = db.insert(FALL_HISTORY_TABLE, null, cv);
        db.close();
        return insert != -1;
    }

    public List<FallData> getFallHistory(String userName) {
        List<FallData> fallHistory = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + FALL_HISTORY_TABLE + " WHERE " + COLUMN_USER_NAME + " = ?";
        Cursor cursor = db.rawQuery(queryString, new String[]{userName});

        if (cursor.moveToFirst()) {
            do {
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return fallHistory;
    }
}