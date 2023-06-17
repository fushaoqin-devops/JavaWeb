package com.shaoqin.fruit.pojo;

/**
 * ClassName: Fruit
 * Package: com.shaoqin.pojo
 * Description:
 * Author Shaoqin
 * Create 6/14/23 1:37 PM
 * Version 1.0
 */
public class Fruit {

    private Integer id;
    private String name;
    private Double price;
    private Integer count;
    private String remark;

    public Fruit() {
    }

    public Fruit(Integer id, String name, Double price, Integer count, String remark) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.remark = remark;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", remark='" + remark + '\'' +
                '}';
    }

}
