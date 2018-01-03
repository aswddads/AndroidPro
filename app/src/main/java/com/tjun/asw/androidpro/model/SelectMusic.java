package com.tjun.asw.androidpro.model;

import java.util.List;

/**
 * Created by asw on 2017/12/28.
 */

public class SelectMusic {
    private String code;
    private List<MusicPlay> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<MusicPlay> getData() {
        return data;
    }

    public void setData(List<MusicPlay> data) {
        this.data = data;
    }
}
