package com.shaoqin.fruit.business;

import com.shaoqin.fruit.pojo.Fruit;

import java.util.List;

/**
 * ClassName: FruitService
 * Package: com.shaoqin.fruit.business
 * Description:
 * Author Shaoqin
 * Create 6/17/23 3:55 PM
 * Version 1.0
 */
public interface FruitService {
    Integer PAGE_SIZE = 8;

    List<Fruit> getFruitList(String keyword, Integer pageNo, Integer pageSize);

    void addFruit(Fruit fruit);

    Fruit getFruitById(Integer id);

    void delFruit(Integer id);

    void updateFruit(Fruit fruit);

    Integer getPageCount(Integer fruitCount);

    Integer[] getPageNationIndex(String keyword, Integer currPage);

    Integer[] getPaginationNumbers(Integer[] paginationIndex);

    Integer getFruitCount(String keyword);

}
