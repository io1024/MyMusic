package com.zero.music.database;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import com.zero.music.adapter.MusicAdapter;


/**
 * 自定义一个异步查询帮助类
 */
public class MyQueryHandler extends AsyncQueryHandler {

    public MyQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        //100代表是视频，200代表是音乐
        if (token == 100) {
            //查询结束后，执行该方法
            MusicAdapter videoAdapter = (MusicAdapter) cookie;
            //刷新界面（该方法会使用新的游标替换旧的游标，且会关闭旧游标）
            videoAdapter.changeCursor(cursor);
            //刷新界面（该方法会使用新的游标替换旧的游标，且不会关闭旧游标）
            //游标不关闭容易造成内存泄漏，界面退出的时候，需要手动关闭旧游标
            //videoAdapter.swapCursor(cursor);
        }
    }
}
