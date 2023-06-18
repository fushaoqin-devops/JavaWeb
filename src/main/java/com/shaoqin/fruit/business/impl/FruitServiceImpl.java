package com.shaoqin.fruit.business.impl;

import com.shaoqin.fruit.business.FruitService;
import com.shaoqin.fruit.dao.FruitDAO;
import com.shaoqin.fruit.dao.impl.FruitDaoImpl;
import com.shaoqin.fruit.pojo.Fruit;
import com.shaoqin.fruit.utils.StringUtil;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * ClassName: FruitServiceImpl
 * Package: com.shaoqin.fruit.business.impl
 * Description:
 * Author Shaoqin
 * Create 6/17/23 3:57 PM
 * Version 1.0
 */
public class FruitServiceImpl implements FruitService {

    private FruitDAO fruitDAO = new FruitDaoImpl();

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo, Integer pageSize) {
        List<Fruit> fruitList = keyword == null ? fruitDAO.getFruitList(pageNo, pageSize) : fruitDAO.getFruitList(keyword, pageNo, pageSize);
        return fruitList;
    }

    @Override
    public void addFruit(Fruit fruit) {
        fruitDAO.addFruit(fruit);
    }

    @Override
    public Fruit getFruitById(Integer id) {
        return fruitDAO.getFruitById(id);
    }

    @Override
    public void delFruit(Integer id) {
        fruitDAO.deleteFruit(id);
    }

    @Override
    public void updateFruit(Fruit fruit) {
        fruitDAO.updateFruit(fruit);
    }

    @Override
    public Integer getPageCount(Integer fruitCount) {
        Integer pageCount = fruitCount % PAGE_SIZE == 0 ? fruitCount / PAGE_SIZE : fruitCount / PAGE_SIZE + 1;
        return pageCount;
    }

    @Override
    public Integer[] getPageNationIndex(String keyword, Integer currPage) {
        // handle pagination
        Integer fruitCount = getFruitCount(keyword);
        int maxPage = getPageCount(fruitCount);
        int startPage, endPage;
        int MAX_VISIBLE_PAGE = 5;
        if (currPage < 0) {
            // handling last page as last page sets page index to -1
            endPage = maxPage;
            currPage = endPage;
            startPage = Math.max(1, endPage - MAX_VISIBLE_PAGE + 1);
        } else {
            currPage = Math.min(currPage, maxPage);
            startPage = Math.max(currPage - MAX_VISIBLE_PAGE / 2, 1);
            endPage = startPage + MAX_VISIBLE_PAGE - 1;

            // handle endPage where it exceeds max page
            if (endPage * PAGE_SIZE > fruitCount) {
                endPage = maxPage;
                startPage = Math.max(1, endPage - MAX_VISIBLE_PAGE + 1);    // handle page number smaller than 1
            }
        }
        return new Integer[] {startPage, currPage, endPage, maxPage};
    }

    @Override
    public Integer[] getPaginationNumbers(Integer[] paginationIndex) {
        int endPage = paginationIndex[2];
        int startPage = paginationIndex[0];
        Integer[] pageNumbers = new Integer[endPage - startPage + 1];
        for (int i = 0; i < endPage - startPage + 1; i++) {
            pageNumbers[i] = i + startPage;
        }
        return pageNumbers;
    }

    @Override
    public Integer getFruitCount(String keyword) {
        return keyword != null ? fruitDAO.getFruitCount(keyword) : fruitDAO.getFruitCount();
    }

}
