package com.tjun.asw.androidpro.model;

import java.util.List;

/**
 * Created by asw on 2017/12/28.
 */

public class MusicFirst {
    private int code;
    private MusicSecond playlist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MusicSecond getPlaylist() {
        return playlist;
    }

    public void setPlaylist(MusicSecond playlist) {
        this.playlist = playlist;
    }
}
