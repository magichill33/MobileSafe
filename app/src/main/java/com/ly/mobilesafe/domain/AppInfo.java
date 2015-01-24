package com.ly.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by magichill33 on 2015/1/24.
 */
public class AppInfo {
    private Drawable icon;
    private String name;
    private String packname;
    private boolean inRom;
    private boolean userApp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public boolean isInRom() {
        return inRom;
    }

    public void setInRom(boolean inRom) {
        this.inRom = inRom;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public Drawable getIcon() {

        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", packname='" + packname + '\'' +
                ", inRom=" + inRom +
                ", userApp=" + userApp +
                '}';
    }
}
