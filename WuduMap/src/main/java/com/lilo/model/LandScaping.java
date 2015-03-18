package com.lilo.model;

/**
 * 园林绿化类
 * @author Administrator
 *
 */
public class LandScaping extends Part{
    private String spec; //规格开状和尺寸(0403)
    private String sculptName; //雕塑名称(0406)

    public String getSpec() {
        return spec;
    }
    public void setSpec(String spec) {
        this.spec = spec;
    }
    public String getSculptName() {
        return sculptName;
    }
    public void setSculptName(String sculptName) {
        this.sculptName = sculptName;
    }
    @Override
    public String toString() {
        return super.toString() + "::LandScaping [spec=" + spec + ", sculptName=" + sculptName + "]";
    }


}
