package com.ly.kumi.bean;

/**
 * Created by magichill33 on 2015/3/13.
 */
public class Music {
    //标题
    private String title;
    //演唱者
    private String artist;
    //ID
    private String id;
    //在SDcard上的播放路径
    private String path;
    //时长
    private String duration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
