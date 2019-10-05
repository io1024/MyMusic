package com.zero.music.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;

import com.zero.music.BaseActivity;
import com.zero.music.R;
import com.zero.music.adapter.MusicAdapter;
import com.zero.music.bean.MediaMusicBean;
import com.zero.music.database.MyQueryHandler;
import com.zero.music.service.IMusicConnection;
import com.zero.music.service.MusicPlayService;
import com.zero.music.service.MusicServiceConnection;

import java.util.ArrayList;

/**
 * 音乐播放 界面
 * 切记：安卓8.0之后必须动态设置权限，否则File获取的数据为null
 */
public class MusicPlayActivity extends BaseActivity implements IMusicConnection {


    //@BindView(R.id.seekBar)
    //SeekBar seekBar;
    //private final String musicPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "music";
    //private RecyclerView rcView;
    private ListView listView;
    private SeekBar seekBar;
    private int currentPos = 0;
    private Intent musicIntent;
    private MusicServiceConnection mscc;
    private ArrayList<MediaMusicBean> itemsFromCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        //ButterKnife.bind(this);
        seekBar = findViewById(R.id.seekBar);
        //rcView = findViewById(R.id.rcView);
        listView = findViewById(R.id.listView);
        initData();
        startMusicService();
    }

    private void initData() {
//        //保证RecyclerView的数据显示
//        rcView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
//        MusicPlayAdapter playAdapter = new MusicPlayAdapter(this, musicNamePaths);
//        playAdapter.setOnItemClickListener(new MusicPlayAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//                currentPos = position;
//                playMusic();
//            }
//        });
//        rcView.setAdapter(playAdapter);

        final MusicAdapter musicAdapter = new MusicAdapter(this, null);
        listView.setAdapter(musicAdapter);
        ContentResolver resolver = this.getContentResolver();
        MyQueryHandler queryHandler = new MyQueryHandler(resolver);
        queryHandler.startQuery(100, musicAdapter, Media.EXTERNAL_CONTENT_URI,
                new String[]{Media._ID, Media.TITLE, Media.SIZE, Media.DURATION, Media.DATA}, null, null, null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = musicAdapter.getCursor();
                itemsFromCursor = getItemsFromCursor(cursor);
                currentPos = position;
                playMusic();
            }
        });

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
        if (itemsFromCursor.size() > currentPos) {
            MediaMusicBean musicBean = itemsFromCursor.get(currentPos);
            mscc.getMusicBindImpl().callPlayer(musicBean.getPath());
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


    /**
     * @param cursor 游标
     * @return 获取所有的 视频 数据
     */
    private ArrayList<MediaMusicBean> getItemsFromCursor(Cursor cursor) {
        ArrayList<MediaMusicBean> items = new ArrayList<>();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            MediaMusicBean bean = MediaMusicBean.getMediaMusicBean(cursor);
            items.add(bean);
        }
        return items;
    }
}
