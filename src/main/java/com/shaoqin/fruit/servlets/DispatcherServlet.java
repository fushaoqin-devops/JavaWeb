package com.shaoqin.fruit.servlets;

import com.shaoqin.fruit.utils.StringUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: DispacherServlet
 * Package: com.shaoqin.fruit.controllers
 * Description:
 * Author Shaoqin
 * Create 6/16/23 5:53 PM
 * Version 1.0
 */
@WebServlet("/fruit/*")
public class DispatcherServlet extends HttpServlet {
    private final Map<String, Object> beanMap = new HashMap<>();

    @Override
    public void init() {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("ApplicationContext.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList beanList = document.getElementsByTagName("bean");

            for (int i = 0; i < beanList.getLength(); i++) {
                Node node = beanList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element bean = (Element) node;
                    Class<?> controllerClass = Class.forName(bean.getAttribute("class"));
                    Object beanObj = controllerClass.newInstance();
                    String beanId = bean.getAttribute("id");
                    Method setServletContext = controllerClass.getDeclaredMethod("setServletContext", ServletContext.class);
                    setServletContext.invoke(beanObj, this.getServletContext());

                    beanMap.put(beanId, beanObj);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException | ClassNotFoundException |
                 InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        // Get controller
        String servletPath = req.getServletPath();
        servletPath = servletPath.substring(1);
        Object controllerObject = beanMap.get(servletPath);

        String operate = req.getParameter("operate");
        if (StringUtil.isEmpty(operate)) operate = "index";

        try {
            Method method = controllerObject.getClass().getDeclaredMethod(operate, HttpServletRequest.class, HttpServletResponse.class);
            method.setAccessible(true);
            method.invoke(controllerObject, req, resp);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
