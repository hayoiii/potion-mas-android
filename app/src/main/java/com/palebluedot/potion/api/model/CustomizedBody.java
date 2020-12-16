package com.palebluedot.potion.api.model;

import com.google.gson.annotations.SerializedName;

public class CustomizedBody {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("factory")
    private String factory;
    @SerializedName("wayToTake")
    private String wayToTake;
    @SerializedName("effect")
    private String effect;
    @SerializedName("remark")
    private String remark;

    @Override
    public String toString() {
        return "CustomizedBody{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", factory='" + factory + '\'' +
                ", wayToTake='" + wayToTake + '\'' +
                ", effect='" + effect + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getWayToTake() {
        return wayToTake;
    }

    public void setWayToTake(String wayToTake) {
        this.wayToTake = wayToTake;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CustomizedBody(Integer id, String name, String factory, String wayToTake, String effect, String remark) {
        this.id = id;
        this.name = name;
        this.factory = factory;
        this.wayToTake = wayToTake;
        this.effect = effect;
        this.remark = remark;
    }
}
