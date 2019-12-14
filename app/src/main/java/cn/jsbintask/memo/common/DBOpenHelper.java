package cn.jsbintask.memo.common;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import cn.jsbintask.memo.MemoApplication;



public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = DBOpenHelper.class.getSimpleName();
    private static final int VERSION = 1;
    private static final String DB_NAME = "memo.db";

    public DBOpenHelper() {
        super(MemoApplication.getContext(), DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ColumnContacts.EVENT_TABLE_NAME + "( "
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ColumnContacts.EVENT_TITLE_COLUMN + " text, "
                    + ColumnContacts.EVENT_CONTENT_COLUMN + " text, "
                    + ColumnContacts.EVENT_CREATED_TIME_COLUMN + " datetime, "
                    + ColumnContacts.EVENT_UPDATED_TIME_COLUMN + " datetime, "
                    + ColumnContacts.EVENT_REMIND_TIME_COLUMN + " datetime, "
                    + ColumnContacts.EVENT_IS_IMPORTANT_COLUMN + " INTEGER, "
                    + ColumnContacts.EVENT_IS_CLOCKED + " INTEGER"
        + ")");

        String sql = "INSERT INTO " + ColumnContacts.EVENT_TABLE_NAME + " VALUES(NULL, ?, ?, ?, ?, ?, ?, ?)";
        db.beginTransaction();
        db.execSQL(sql, new Object[]{"memo", "메모는 메모입니다,하고싶은일을 적으세요\n" + "2017103945"+ "컴퓨터공학과"+ "이영일", 0});
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
