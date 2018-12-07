package com.marliao.intelligenttransportation.db.dao;

public class GetTrafficLightConfigAction {
    private Integer RoadId;
    private Integer YellowTime;
    private Integer GreenTime;
    private Integer RedTime;

    public Integer getRoadId() {
        return RoadId;
    }

    public void setRoadId(Integer roadId) {
        RoadId = roadId;
    }

    public Integer getYellowTime() {
        return YellowTime;
    }

    public void setYellowTime(Integer yellowTime) {
        YellowTime = yellowTime;
    }

    public Integer getGreenTime() {
        return GreenTime;
    }

    public void setGreenTime(Integer greenTime) {
        GreenTime = greenTime;
    }

    public Integer getRedTime() {
        return RedTime;
    }

    public void setRedTime(Integer redTime) {
        RedTime = redTime;
    }
}
