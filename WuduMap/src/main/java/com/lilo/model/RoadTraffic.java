package com.lilo.model;

/**
 * 道路交通部件
 * @author Administrator
 *
 */
public class RoadTraffic extends Part{
    private String stopName; //站名 (0203)

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    @Override
    public String toString() {
        return super.toString() + "::RoadTraffic [stopName=" + stopName + "]";
    }


}
