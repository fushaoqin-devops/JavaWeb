package com.shaoqin.fruit.io;

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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ClassPathXmlApplicationContext
 * Package: com.shaoqin.fruit.io
 * Description:
 * Author Shaoqin
 * Create 6/17/23 5:31 PM
 * Version 1.0
 */
public class ClassPathXmlApplicationContext implements BeanFactory{

    private Map<String, Object> beanMap = new HashMap<>();

    public ClassPathXmlApplicationContext() {
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
                    String beanId = bean.getAttribute("id");
                    Class<?> beanClass = Class.forName(bean.getAttribute("class"));
                    Object beanObj = beanClass.newInstance();
                    beanMap.put(beanId, beanObj);
                }
            }
            // create dependencies
            for (int i = 0; i < beanList.getLength(); i++) {
                Node node = beanList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    // Loop through each Bean defined in the xml file
                    Element bean = (Element) node;
                    NodeList beanChildNodeList = bean.getChildNodes();
                    for (int j = 0; j < beanChildNodeList.getLength(); j++) {
                        // Find the property tag
                        Node beanChildNode = beanChildNodeList.item(j);
                        if (beanChildNode.getNodeType() == Node.ELEMENT_NODE && "property".equals(beanChildNode.getNodeName())) {
                            Element property = (Element) beanChildNode;
                            String propertyName = property.getAttribute("name");
                            String propertyRef = property.getAttribute("ref");
                            Object refObj = beanMap.get(propertyRef);
                            Object beanObj = beanMap.get(bean.getAttribute("id"));
                            Field declaredField = beanObj.getClass().getDeclaredField(propertyName);
                            declaredField.setAccessible(true);
                            declaredField.set(beanObj, refObj);
                        }
                    }
                    // Set dependency if there is one
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException | ClassNotFoundException |
                 InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }

}
