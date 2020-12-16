package com.palebluedot.potion.api.model;

public class Potion {
    private String product;
    private String factory;
    private String serialNo;

    public Potion(String product, String factory, String serialNo) {
        this.product = product;
        this.factory = factory;
        this.serialNo = serialNo;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    public String toString() {
        return "Potion{" +
                "product='" + product + '\'' +
                ", factory='" + factory + '\'' +
                ", serialNo='" + serialNo + '\'' +
                '}';
    }
}
