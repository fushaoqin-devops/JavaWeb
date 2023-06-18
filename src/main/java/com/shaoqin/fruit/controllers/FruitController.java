package com.shaoqin.fruit.controllers;

import com.shaoqin.fruit.service.FruitService;
import com.shaoqin.fruit.pojo.Fruit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * ClassName: FruitController
 * Package: com.shaoqin.fruit.controllers
 * Description:
 * Author Shaoqin
 * Create 6/16/23 5:52 PM
 * Version 1.0
 */
public class FruitController {

    private final FruitService fruitService = null;

    private String index(String keyword, Integer pageNumber, HttpServletRequest req) {
        HttpSession session = req.getSession();

        int currPage = pageNumber == null ? 1 : pageNumber;
        if (keyword == null) {
            Object existingKeyword = session.getAttribute("keyword");
            if (existingKeyword != null) keyword = (String) existingKeyword;
        }

        Integer[] paginationIndex = fruitService.getPageNationIndex(keyword, currPage);
        Integer[] pageNumbers = fruitService.getPaginationNumbers(paginationIndex);

        currPage = paginationIndex[1];
        List<Fruit> fruitList = fruitService.getFruitList(keyword, currPage, fruitService.PAGE_SIZE);

        session.setAttribute("keyword", keyword);
        session.setAttribute("fruitList", fruitList);
        session.setAttribute("pageNumbers", pageNumbers);
        session.setAttribute("currPageNumber", currPage);
        session.setAttribute("totalPage", paginationIndex[3]);

        return "fruit/index";
    }

    private String add (String name, Double price, Integer count, String remark) {
        fruitService.addFruit(new Fruit(1, name, price, count, remark));
        return "redirect:fruit";
    }

    private String del(Integer id) {
        if (id != null) {
            fruitService.delFruit(id);
            return "redirect:fruit";
        }
        return "error";
    }

    private String edit(Integer id, HttpServletRequest req) {
        if(id != null) {
            Fruit fruit = fruitService.getFruitById(id);
            req.setAttribute("fruit", fruit);
            return "fruit/edit";
        } else {
            return "fruit/add";
        }
    }

    private String update(Integer id, String name, Double price, Integer count, String remark) {
        fruitService.updateFruit(new Fruit(id, name, price, count, remark));
        return "redirect:fruit";
    }

}
