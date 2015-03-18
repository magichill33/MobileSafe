package com.lilo.model;

/**
 * 共公设施类
 *
 * @author Administrator
 *
 */
public class CommonFacility extends Part {
    private String material; //材质,不适合于部件编号为(0120~0121，0124~0126,0131~0137)
    private String tradeScope; //经营范围 适合于(0125)
    private int num; //健身设施数目 适合于(0126)
    public String getMaterial() {
        return material;
    }
    public void setMaterial(String material) {
        this.material = material;
    }
    public String getTradeScope() {
        return tradeScope;
    }
    public void setTradeScope(String tradeScope) {
        this.tradeScope = tradeScope;
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    @Override
    public String toString() {
        return super.toString() + "::CommonFacility [material=" + material + ", tradeScope="
                + tradeScope + ", num=" + num + "]";
    }


}
