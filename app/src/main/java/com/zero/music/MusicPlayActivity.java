package com.zero.music;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.SeekBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zero.music.service.IMusicConnection;
import com.zero.music.service.MusicPlayService;
import com.zero.music.service.MusicServiceConnection;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐播放 界面
 * 切记：安卓8.0之后必须动态设置权限，否则File获取的数据为null
 */
public class MusicPlayActivity extends BaseActivity implements IMusicConnection {


    //@BindView(R.id.seekBar)
    //SeekBar seekBar;
    private final String musicPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "music";
    private List<String> musicNamePaths;
    private RecyclerView rcView;
    private SeekBar seekBar;
    private int currentPos = 0;
    private Intent musicIntent;
    private MusicServiceConnection mscc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        //ButterKnife.bind(this);
        seekBar = findViewById(R.id.seekBar);
        rcView = findViewById(R.id.rcView);
        initData();
        startMusicService();
    }

    private void initData() {
        musicNamePaths = new ArrayList<>();
        File file = new File(musicPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                //自定义过滤器：筛选MP3
                return pathname.getName().toLowerCase().endsWith(".mp3");
            }
        });
        if (files != null && files.length > 0) {
            for (File f : files) {
                String name = f.getName();
                musicNamePaths.add(musicPath + File.separator + name);
            }
        }
        //保证RecyclerView的数据显示
        rcView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MusicPlayAdapter playAdapter = new MusicPlayAdapter(this, musicNamePaths);
        playAdapter.setOnItemClickListener(new MusicPlayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                currentPos = position;
                playMusic();
            }
        });
        rcView.setAdapter(playAdapter);
    }

    //混合方式开启服务
    private void startMusicService() {
        //1. 使用start方式开启服务
        musicIntent = new Intent(this, MusicPlayService.class);
        this.startService(musicIntent);
        //2. 通过bind方式绑定该服务（同一个Intent对象）
        if (mscc == null) {
            mscc = new MusicServiceConnection();
        }
        bindService(musicIntent, mscc, Context.BIND_AUTO_CREATE);
        mscc.setMusicConnection(this);
    }

    @Override
    public void OnConnectionListener() {
        //当中间人对象绑定成功后，该方法别调用
        mscc.getMusicBindImpl().setSeekBar(seekBar);
    }

    private void playMusic() {
        if (musicNamePaths.size() > currentPos) {
            String source = musicNamePaths.get(currentPos);
            mscc.getMusicBindImpl().callPlayer(source);
        }
    }

    //开始播放
    public void clickStart(View view) {
        playMusic();
    }

    //暂停播放
    public void clickPause(View view) {
        mscc.getMusicBindImpl().callPause();
    }

    //继续播放
    public void clickReStart(View view) {
        mscc.getMusicBindImpl().callRePlayer();
    }

    //退出播放
    public void clickOut(View view) {
        mscc.getMusicBindImpl().callPause();
        if (mscc != null) {
            this.unbindService(mscc);
            mscc = null;
        }
        if (musicIntent != null) {
            stopService(musicIntent);
            musicIntent = null;
        }
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mscc != null) {
            this.unbindService(mscc);
            mscc = null;
        }
    }


}
