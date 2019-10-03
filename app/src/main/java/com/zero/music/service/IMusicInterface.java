package com.zero.music.service;

import android.widget.SeekBar;

interface IMusicInterface {

    void callPlayer(String path);

    void callRePlayer();

    void callPause();

    void setSeekBar(SeekBar seekBar);
}
