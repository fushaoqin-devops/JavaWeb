package com.shaoqin.fruit.dao;

import com.shaoqin.fruit.pojo.Fruit;

import java.util.List;

/**
 * ClassName: FruitDAO
 * Package: com.shaoqin.dao
 * Description:
 * Author Shaoqin
 * Create 6/14/23 1:38 PM
 * Version 1.0
 */
public interface FruitDAO {

    /**
     * Get all fruits
     * @return all fruits
     */
    List<Fruit> getFruitList();

    /**
     * Get all fruits on the specific page
     * @param pageNo page number
     * @param pageSize number of fruits per page
     * @return fruits on the page
     */
    List<Fruit> getFruitList(Integer pageNo, Integer pageSize);

    /**
     * Get all fruits that matches the keyword on the specific page
     * @param keyword user search keyword
     * @param pageNo number of fruits per page
     * @param pageSize number of fruits per page
     * @return fruits on the page
     */
    List<Fruit> getFruitList(String keyword, Integer pageNo, Integer pageSize);

    /**
     * get a single fruit that matches the id
     * @param id fruit id
     * @return fruit
     */
    Fruit getFruitById(Integer id);

    /**
     * update a fruit entry
     * @param fruit Fruit object
     */
    void updateFruit(Fruit fruit);

    /**
     * delete a fruit entry
     * @param id fruit id
     */
    void deleteFruit(Integer id);

    /**
     * add a fruit entry
     * @param fruit fruit object
     */
    void addFruit(Fruit fruit);

    /**
     * get count of all fruits in table
     * @return count of fruits
     */
    Integer getFruitCount();

    /**
     * get count of all fruits that matches the keyword
     * @param keyword user search keyword
     * @return  all fruits that matches the keyword
     */
    Integer getFruitCount(String keyword);

}
