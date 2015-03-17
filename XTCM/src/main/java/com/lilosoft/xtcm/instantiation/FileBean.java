package com.lilosoft.xtcm.instantiation;

import android.graphics.Bitmap;

/**
 * @category 文件附件
 * @author William Liu
 *
 */
public class FileBean {

    private String fName;
    private String fType;
    private String fData;
    private Bitmap bitmap;
    private String path;

    public FileBean(String fName, String fType, String fData) {
        this.fName = fName;
        this.fType = fType;
        this.fData = fData;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    public String getfData() {
        return fData;
    }

    public void setfData(String fData) {
        this.fData = fData;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
