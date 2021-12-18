package com.example.productcrud_javafx;

public class Product {
    private int id;
    private String name;
    private String code;
    private float price;
    private int remain;

    public Product(int id, String name, String code, float price, int remain) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.price = price;
        this.remain = remain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
