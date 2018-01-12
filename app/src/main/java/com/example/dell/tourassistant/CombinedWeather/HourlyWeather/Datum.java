
package com.example.dell.tourassistant.CombinedWeather.HourlyWeather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("wind_cdir")
    @Expose
    private String windCdir;
    @SerializedName("rh")
    @Expose
    private Double rh;
    @SerializedName("wind_spd")
    @Expose
    private double windSpd;
    @SerializedName("pop")
    @Expose
    private Integer pop;
    @SerializedName("wind_cdir_full")
    @Expose
    private String windCdirFull;
    @SerializedName("app_temp")
    @Expose
    private Double appTemp;
    @SerializedName("snow6h")
    @Expose
    private double snow6h;
    @SerializedName("pod")
    @Expose
    private String pod;
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
    @SerializedName("snow_depth")
    @Expose
    private double snowDepth;
    @SerializedName("dhi")
    @Expose
    private Double dhi;
    @SerializedName("precip6h")
    @Expose
    private double precip6h;
    @SerializedName("precip")
    @Expose
    private double precip;
    @SerializedName("pres")
    @Expose
    private Double pres;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("temp")
    @Expose
    private Double temp;
    @SerializedName("slp")
    @Expose
    private double slp;
    @SerializedName("clouds")
    @Expose
    private Integer clouds;
    @SerializedName("vis")
    @Expose
    private double vis;

    public String getWindCdir() {
        return windCdir;
    }

    public void setWindCdir(String windCdir) {
        this.windCdir = windCdir;
    }

    public Double getRh() {
        return rh;
    }

    public void setRh(Double rh) {
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

    public Double getAppTemp() {
        return appTemp;
    }

    public void setAppTemp(Double appTemp) {
        this.appTemp = appTemp;
    }

    public double getSnow6h() {
        return snow6h;
    }

    public void setSnow6h(Integer snow6h) {
        this.snow6h = snow6h;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
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

    public double getSnowDepth() {
        return snowDepth;
    }

    public void setSnowDepth(Integer snowDepth) {
        this.snowDepth = snowDepth;
    }

    public Double getDhi() {
        return dhi;
    }

    public void setDhi(Double dhi) {
        this.dhi = dhi;
    }

    public double getPrecip6h() {
        return precip6h;
    }

    public void setPrecip6h(Integer precip6h) {
        this.precip6h = precip6h;
    }

    public double getPrecip() {
        return precip;
    }

    public void setPrecip(Integer precip) {
        this.precip = precip;
    }

    public Double getPres() {
        return pres;
    }

    public void setPres(Double pres) {
        this.pres = pres;
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

    public double getSlp() {
        return slp;
    }

    public void setSlp(Integer slp) {
        this.slp = slp;
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

}
