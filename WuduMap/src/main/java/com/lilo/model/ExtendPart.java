package com.lilo.model;


/**
 * 扩展部件
 * @author Administrator
 *
 */
public class ExtendPart extends Part{
    private String material; //材料(2101)

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return super.toString() + "::ExtendPart [material=" + material + "]";
    }


}
