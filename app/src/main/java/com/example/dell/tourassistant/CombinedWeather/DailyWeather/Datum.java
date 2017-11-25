
package com.example.dell.tourassistant.CombinedWeather.DailyWeather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("wind_cdir")
    @Expose
    private String windCdir;
    @SerializedName("rh")
    @Expose
    private Integer rh;
    @SerializedName("wind_spd")
    @Expose
    private double windSpd;
    @SerializedName("pop")
    @Expose
    private Integer pop;
    @SerializedName("wind_cdir_full")
    @Expose
    private String windCdirFull;
    @SerializedName("slp")
    @Expose
    private Double slp;
    @SerializedName("app_max_temp")
    @Expose
    private Double appMaxTemp;
    @SerializedName("pres")
    @Expose
    private Double pres;
    @SerializedName("dewpt")
    @Expose
    private Double dewpt;
    @SerializedName("snow")
    @Expose
    private double snow;
    @SerializedName("uv")
    @Expose
    private Integer uv;
    @SerializedName("ts")
    @Expose
    private Integer ts;
    @SerializedName("wind_dir")
    @Expose
    private Integer windDir;
    @SerializedName("weather")
    @Expose
    private Weather weather;
    @SerializedName("app_min_temp")
    @Expose
    private Double appMinTemp;
    @SerializedName("max_temp")
    @Expose
    private Double maxTemp;
    @SerializedName("snow_depth")
    @Expose
    private double snowDepth;
    @SerializedName("precip")
    @Expose
    private double precip;
    @SerializedName("max_dhi")
    @Expose
    private Double maxDhi;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("temp")
    @Expose
    private Double temp;
    @SerializedName("min_temp")
    @Expose
    private double minTemp;
    @SerializedName("clouds")
    @Expose
    private Integer clouds;
    @SerializedName("vis")
    @Expose
    private double vis;
    @SerializedName("dhi")
    @Expose
    private Double dhi;

    public String getWindCdir() {
        return windCdir;
    }

    public void setWindCdir(String windCdir) {
        this.windCdir = windCdir;
    }

    public Integer getRh() {
        return rh;
    }

    public void setRh(Integer rh) {
        this.rh = rh;
    }

    public double getWindSpd() {
        return windSpd;
    }

    public void setWindSpd(Integer windSpd) {
        this.windSpd = windSpd;
    }

    public Integer getPop() {
        return pop;
    }

    public void setPop(Integer pop) {
        this.pop = pop;
    }

    public String getWindCdirFull() {
        return windCdirFull;
    }

    public void setWindCdirFull(String windCdirFull) {
        this.windCdirFull = windCdirFull;
    }

    public Double getSlp() {
        return slp;
    }

    public void setSlp(Double slp) {
        this.slp = slp;
    }

    public Double getAppMaxTemp() {
        return appMaxTemp;
    }

    public void setAppMaxTemp(Double appMaxTemp) {
        this.appMaxTemp = appMaxTemp;
    }

    public Double getPres() {
        return pres;
    }

    public void setPres(Double pres) {
        this.pres = pres;
    }

    public Double getDewpt() {
        return dewpt;
    }

    public void setDewpt(Double dewpt) {
        this.dewpt = dewpt;
    }

    public double getSnow() {
        return snow;
    }

    public void setSnow(Integer snow) {
        this.snow = snow;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Integer getTs() {
        return ts;
    }

    public void setTs(Integer ts) {
        this.ts = ts;
    }

    public Integer getWindDir() {
        return windDir;
    }

    public void setWindDir(Integer windDir) {
        this.windDir = windDir;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Double getAppMinTemp() {
        return appMinTemp;
    }

    public void setAppMinTemp(Double appMinTemp) {
        this.appMinTemp = appMinTemp;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getSnowDepth() {
        return snowDepth;
    }

    public void setSnowDepth(Integer snowDepth) {
        this.snowDepth = snowDepth;
    }

    public double getPrecip() {
        return precip;
    }

    public void setPrecip(Integer precip) {
        this.precip = precip;
    }

    public Double getMaxDhi() {
        return maxDhi;
    }

    public void setMaxDhi(Double maxDhi) {
        this.maxDhi = maxDhi;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Integer minTemp) {
        this.minTemp = minTemp;
    }

    public Integer getClouds() {
        return clouds;
    }

    public void setClouds(Integer clouds) {
        this.clouds = clouds;
    }

    public double getVis() {
        return vis;
    }

    public void setVis(Integer vis) {
        this.vis = vis;
    }

    public Double getDhi() {
        return dhi;
    }

    public void setDhi(Double dhi) {
        this.dhi = dhi;
    }

}
