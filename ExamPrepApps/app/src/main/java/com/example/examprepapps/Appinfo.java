package com.example.examprepapps;

public class Appinfo {
    String name, price;
    String images;

    public Appinfo() {
        this.name = name;
        this.price = price;
        this.images = images;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }


    @Override
    public String toString() {
        return "Appinfo{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", images=" + images +
                '}';
    }
}