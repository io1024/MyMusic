package com.zero.music.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 定义服务连接对象,接收中间帮助类对象
 */
public class MusicServiceConnection implements ServiceConnection {

    private IMusicConnection listener;
    private MusicPlayService.MusicBindImpl musicBind;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        //获取：服务绑定成功后，返回的中间帮助类对象
        this.musicBind = (MusicPlayService.MusicBindImpl) service;
        if (listener != null) {
            listener.OnConnectionListener();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    //暴露方法：获取中间帮助类对象
    public MusicPlayService.MusicBindImpl getMusicBindImpl() {
        return musicBind;
    }

    public void setMusicConnection(IMusicConnection listener) {
        this.listener = listener;
    }
}
