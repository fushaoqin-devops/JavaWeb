package com.shaoqin.fruit.servlets;

import com.shaoqin.fruit.utils.StringUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
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
public class DispatcherServlet extends ViewBaseServlet {
    private final Map<String, Object> beanMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Get beans from configuration file
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("ApplicationContext.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList beanList = document.getElementsByTagName("bean");

            // Put each bean in a map
            for (int i = 0; i < beanList.getLength(); i++) {
                Node node = beanList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element bean = (Element) node;
                    Class<?> controllerClass = Class.forName(bean.getAttribute("class"));
                    Object beanObj = controllerClass.newInstance();
                    String beanId = bean.getAttribute("id");
                    beanMap.put(beanId, beanObj);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException | ClassNotFoundException |
                 InstantiationException | IllegalAccessException e) {
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
            Method method = controllerObject.getClass().getDeclaredMethod(operate, HttpServletRequest.class);
            method.setAccessible(true);
            Object returnObj = method.invoke(controllerObject, req);
            String methodReturnStr = (String) returnObj;
            if (methodReturnStr.startsWith("redirect:")) {
                // Redirect to other servlet
                String redirectStr = methodReturnStr.substring("redirect:".length());
                resp.sendRedirect(redirectStr);
            } else {
                // Render Thymeleaf template
                super.processTemplate(methodReturnStr, req, resp);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
