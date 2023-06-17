package com.shaoqin.fruit.controllers;

import com.shaoqin.fruit.dao.FruitDAO;
import com.shaoqin.fruit.dao.impl.FruitDaoImpl;
import com.shaoqin.fruit.pojo.Fruit;
import com.shaoqin.fruit.servlets.ViewBaseServlet;
import com.shaoqin.fruit.utils.StringUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
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

    private final FruitDAO fruitDAO = new FruitDaoImpl();

    private String index(HttpServletRequest req) {
        FruitDAO fruitDao = new FruitDaoImpl();
        HttpSession session = req.getSession();

        // get parameters
        String currPageStr = req.getParameter("pageNumber");
        String keyword = req.getParameter("keyword");

        // set current page, defaults to 1 when user first visit the page
        int currPage = 1;
        if (StringUtil.isNotEmpty(currPageStr)) {
            currPage = Integer.parseInt(currPageStr);
            if (currPage == 0) currPage = 1;    // handling negative page
        }

        // determine if the request has keyword
        List<Fruit> fruitList;
        Integer fruitCount;
        boolean isSearch = false;
        if (StringUtil.isNotEmpty(keyword)) {
            // user clicks the search button
            currPage = 1;
            isSearch = true;
            session.setAttribute("keyword", keyword);
        } else if (keyword != null && keyword.equals("")) {
            // search of empty string
            session.setAttribute("keyword", null);  // reset session keyword
        } else {
            // user clicks on the pagination section
            // need to decide if there's existing keyword
            Object existingKeyword = session.getAttribute("keyword");
            if (existingKeyword != null) {
                // there's a keyword
                isSearch = true;
                keyword = (String) existingKeyword;
            }
        }
        fruitCount = isSearch ? fruitDao.getFruitCount(keyword) : fruitDao.getFruitCount();

        // handle pagination
        Integer PAGE_SIZE = 8;
        int maxPage = fruitCount % PAGE_SIZE == 0 ? fruitCount / PAGE_SIZE : fruitCount / PAGE_SIZE + 1;
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

        // populate page numbers displayed in pagination
        Integer[] pageNumbers = new Integer[endPage - startPage + 1];
        for (int i = 0; i < endPage - startPage + 1; i++) {
            pageNumbers[i] = i + startPage;
        }

        // if there's keyword, return fruits that contains the keyword
        // if there isn't, then return all fruits
        fruitList = isSearch ? fruitDao.getFruitList(keyword, currPage, PAGE_SIZE) : fruitDao.getFruitList(currPage, PAGE_SIZE);

        session.setAttribute("fruitList", fruitList);
        session.setAttribute("pageNumbers", pageNumbers);
        session.setAttribute("currPageNumber", currPage);
        session.setAttribute("totalPage", maxPage);

        // super.processTemplate("fruit/index", req, resp);
        return "fruit/index";
    }

    private String add (HttpServletRequest req) {
        String name = req.getParameter("name");
        Double price = Double.parseDouble(req.getParameter("price"));
        Integer count = Integer.parseInt(req.getParameter("count"));
        String remark = req.getParameter("remark");
        fruitDAO.addFruit(new Fruit(1, name, price, count, remark));
        // resp.sendRedirect("fruit");
        return "redirect:fruit";
    }


    private String del(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        if (StringUtil.isNotEmpty(idStr)) {
            int id = Integer.parseInt(idStr);
            fruitDAO.deleteFruit(id);

            // resp.sendRedirect("fruit");
            return "redirect:fruit";
        }
        return "error";
    }

    private String edit(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        if(StringUtil.isNotEmpty(idStr)) {
            int id = Integer.parseInt(idStr);
            Fruit fruit = fruitDAO.getFruitById(id);
            req.setAttribute("fruit", fruit);
            // super.processTemplate("fruit/edit", req, resp);
            return "fruit/edit";
        } else {
            // super.processTemplate("fruit/add", req, resp);
            return "fruit/add";
        }
    }

    private String update(HttpServletRequest req) {
        Integer id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        Double price = Double.parseDouble(req.getParameter("price"));
        Integer count = Integer.parseInt(req.getParameter("count"));
        String remark = req.getParameter("remark");
        fruitDAO.updateFruit(new Fruit(id, name, price, count, remark));
        // resp.sendRedirect("fruit");
        return "redirect:fruit";
    }

}
