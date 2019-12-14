package cn.jsbintask.memo.common;


import android.database.Cursor;


public interface DBCallback <T> {

    T cursorToInstance(Cursor cursor);
}
