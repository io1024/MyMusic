package com.zero.music.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 音乐播放  服务
 * 音频播放是一种耗时操作，为什么我们没有开启线程操作播放，却没有出问题呢？
 * 原因：播放音频的代码，底层代码已开过线程了。
 * ......Java并不能使设备发声，播放音乐的代码底层是使用JNI调用C的函数执行，C语言可以使底层音频驱动发出声音。
 */
public class MusicPlayService extends Service {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TimerTask mTask;
    private Timer mTimer;
    private int mDuration;

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBindImpl();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer == null) {
            //1.初始化 MediaPlayer
            mediaPlayer = new MediaPlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTask != null) {
            mTask.cancel();
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //解决 SeekBar 的问题（当播放界面退出再打开的时候，保证服务内的进度条显示正常）
    private void initSeekBar(SeekBar seek) {
        seekBar = seek;
        seekBar.setMax(mDuration);
        if (mediaPlayer.isPlaying()) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            //把进度设置给SeekBar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                seekBar.setProgress(currentPosition, true);
            } else {
                seekBar.setProgress(currentPosition);
            }
        }
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    //继续播放
    private void rePlay() {
        mediaPlayer.start();
    }

    //暂停播放
    private void pause() {
        mediaPlayer.pause();
    }

    //开始播放
    private void play(String source) {
        try {
            //2.重置播放器
            mediaPlayer.reset();
            //3.设置播放路径
            mediaPlayer.setDataSource(source);
            //4.MediaPlayer 准备
            mediaPlayer.prepare();
            //5.开始播放
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            //更新进度
            updateMusicProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //播放网络路径的文件方式
        //mediaPlayer.reset();
        //播放asset
        //setDataSource(AssetFileDescriptor afd)
        //setDataSource(Context context, Uri uri)
        //mediaPlayer.setDataSource(url);
        //播放网络音频需要以下调用（异步准备）
        //mediaPlayer.prepareAsync();
        //准备完成的监听回调方法
//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                //准备完成后再开始播放
//                mediaPlayer.start();
//            }
//        });
    }

    //播放完成监听
    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (mTimer != null) {
                mTimer.cancel();
            }
            if (mTask != null) {
                mTask.cancel();
            }
            if (seekBar != null) {
                seekBar.setProgress(0);
            }
        }
    };

    //SeekBar的进度监听
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //当进度条开始被拖拽的时候执行
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            //进度条被拖拽中执行
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //当进度条被拖拽结束的时候执行
            //获取当前的进度
            int progress = seekBar.getProgress();
            //让音乐开始从当前位置开始播放
            mediaPlayer.seekTo(progress);
        }
    };

    //更新音乐的播放进度
    private void updateMusicProcess() {
        //获取音乐的总时长
        mDuration = mediaPlayer.getDuration();
        seekBar.setMax(mDuration);
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        //创建一个定时器任务
        mTask = new TimerTask() {
            @Override
            public void run() {
                //获取音乐当前进度
                int currentPosition = mediaPlayer.getCurrentPosition();
                //把进度设置给SeekBar
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    seekBar.setProgress(currentPosition, true);
                } else {
                    seekBar.setProgress(currentPosition);
                }
            }
        };
        mTimer = new Timer();
        //定时器调度一个任务：TimerTask任务。delay第一次延迟多少毫秒。period每隔多少毫秒再次执行
        //schedule(TimerTask task, long delay, long period)
        mTimer.schedule(mTask, 10, 300);
    }


    //中间人对象
    public class MusicBindImpl extends Binder implements IMusicInterface {

        @Override
        public void callPlayer(String path) {
            play(path);
        }

        @Override
        public void callRePlayer() {
            rePlay();
        }

        @Override
        public void callPause() {
            pause();
        }

        @Override
        public void setSeekBar(SeekBar seekBar) {
            initSeekBar(seekBar);
        }
    }
}
