package com.marliao.intelligenttransportation.db.dao;

public class GetAllSense {
    private Integer pm_2_5;
    private Integer co_2;
    private Integer lightIntensity;
    private Integer humidity;
    private Integer temperature;


    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getPm_2_5() {
        return pm_2_5;
    }

    public void setPm_2_5(Integer pm_2_5) {
        this.pm_2_5 = pm_2_5;
    }

    public Integer getCo_2() {
        return co_2;
    }

    public void setCo_2(Integer co_2) {
        this.co_2 = co_2;
    }

    public Integer getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(Integer lightIntensity) {
        this.lightIntensity = lightIntensity;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }
}
