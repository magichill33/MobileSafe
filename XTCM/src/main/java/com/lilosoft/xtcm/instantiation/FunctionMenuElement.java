package com.lilosoft.xtcm.instantiation;

import android.content.Intent;

/**
 * @category 菜单元素
 * @author William Liu
 *
 */

public class FunctionMenuElement {

    /**
     * @category 标志位
     */
    private String tag;
    /**
     * @category 字段名
     */
    private int resLabel;
    /**
     * @category 图标
     */
    private int resIcon;
    /**
     * @category 响应事件
     */
    private Intent content;

    /**
     * @category 菜单元素构造方法
     * @param tag
     * @param resLabel
     * @param resIcon
     * @param content
     */
    public FunctionMenuElement(String tag, int resLabel, int resIcon,
                               Intent content) {
        this.tag = tag;
        this.resLabel = resLabel;
        this.resIcon = resIcon;
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getResLabel() {
        return resLabel;
    }

    public void setResLabel(int resLabel) {
        this.resLabel = resLabel;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Intent getContent() {
        return content;
    }

    public void setContent(Intent content) {
        this.content = content;
    }

}
