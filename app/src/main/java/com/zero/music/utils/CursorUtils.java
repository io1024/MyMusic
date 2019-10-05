package com.zero.music.utils;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

public class CursorUtils {

    public static void printCursorVideo(Cursor cursor) {
        if (cursor == null) {
            Log.e("Cursor", "Music" + "  无数据  ");
            return;
        }
        Log.e("Cursor", "Music" + " 一共查到 " + cursor.getCount() + " 条数据");
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            Log.e("Cursor", "Music" + MediaStore.Audio.Media.TITLE + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            Log.e("Cursor", "Music" + MediaStore.Audio.Media.DURATION + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            Log.e("Cursor", "Music" + MediaStore.Audio.Media.DATA + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            Log.e("Cursor", "Music" + MediaStore.Audio.Media.SIZE + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
            Log.e("Cursor", "Music" + MediaStore.Audio.Media.ARTIST + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            Log.e("Cursor", "=========================");

        }
    }
}
