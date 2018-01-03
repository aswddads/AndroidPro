package com.tjun.asw.androidpro.model;

import java.util.List;

/**
 * Created by asw on 2017/12/28.
 */

public class Music {
    private String name;
    private long id;
    private MusicPic al;

    private List<MusicImg> artists;

    public List<MusicImg> getArtists() {
        return artists;
    }

    public void setArtists(List<MusicImg> artists) {
        this.artists = artists;
    }

    public MusicPic getAl() {
        return al;
    }

    public void setAl(MusicPic al) {
        this.al = al;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
