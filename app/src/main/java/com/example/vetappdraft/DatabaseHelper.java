package com.example.vetappdraft;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "VetUsers.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE users ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "phone_number TEXT NOT NULL, "
                + "branch TEXT NOT NULL);";

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
    public long insertUser(String phoneNumber, String branch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone_number", phoneNumber);
        values.put("branch", branch);

        long userId = db.insert("users", null, values);
        db.close();
        return userId;
    }
}
