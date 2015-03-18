package com.lilo.model;

/**
 * 其它设施
 * @author Administrator
 *
 */
public class OtherFacility extends Part{
    private String siteName; //工地名称(0602)

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    @Override
    public String toString() {
        return super.toString() + "::OtherFacility [siteName=" + siteName + "]";
    }


}
