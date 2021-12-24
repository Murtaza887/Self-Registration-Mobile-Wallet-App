package com.murtaza.simplifiedselfregistrationonmobilewallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Wallet.db";
    public static final int DB_VER = 1;
    Context context;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE USERS (_id INTEGER PRIMARY KEY AUTOINCREMENT, id INTEGER, number TEXT, amount INTEGER)";
        sqLiteDatabase.execSQL(create);

        insertData(1, "+92 334 5820814", 50, sqLiteDatabase);
        insertData(7,"+92 310 5376009", 50, sqLiteDatabase);
        insertData(8, "+92 336 0533100", 50, sqLiteDatabase);
        insertData(9, "+92 331 5251219", 50, sqLiteDatabase);
        insertData(10, "+92 312 5894810", 50, sqLiteDatabase);
        insertData(11, "+92 309 9727077", 50, sqLiteDatabase);
    }

    public Boolean insertData(Integer id, String number, Integer amount, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("number", number);
        values.put("amount", amount);
        double result = database.insert("USERS", null, values);
        return result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
