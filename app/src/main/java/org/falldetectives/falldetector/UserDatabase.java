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
    public boolean updateUser(int userId, String updatedName, int updatedAge, String updatedBloodType, String updatedEmergencyContact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME, updatedName);
        cv.put(COLUMN_USER_AGE, updatedAge);
        cv.put(COLUMN_USER_BLOOD_TYPE, updatedBloodType);
        cv.put(COLUMN_USER_EMERGENCY_CONTACT, updatedEmergencyContact);

        int update = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();

        return update != 0;
    }
    public void deleteUser(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE, COLUMN_ID + " = ?", new String[]{String.valueOf(user.getID())});
        db.close();
    }

}
