package br.com.wifi.chat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KayO on 29/12/2016.
 */
public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, DbConstants.DB_NAME, null, DbConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DbConstants.CONTACT.CREATE_SCRIPT);
        sqLiteDatabase.execSQL(DbConstants.MESSAGE.CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion == DbConstants.DB_VERSION) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + DbConstants.CONTACT.TABLE_NAME + "'");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + DbConstants.MESSAGE.TABLE_NAME + "'");
            onCreate(sqLiteDatabase);
        }
    }
}
