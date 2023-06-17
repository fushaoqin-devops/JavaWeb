package com.shaoqin.fruit.servlets;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

public class ViewBaseServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private JakartaServletWebApplication application;

    @Override
    public void init() throws ServletException {
        // get application context
        ServletContext servletContext = this.getServletContext();
        application = JakartaServletWebApplication.buildApplication(servletContext);

        // get resolver
        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);

        // set resolver attribute
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        String viewPrefix = servletContext.getInitParameter("view-prefix");
        templateResolver.setPrefix(viewPrefix);
        String viewSuffix = servletContext.getInitParameter("view-suffix");
        templateResolver.setSuffix(viewSuffix);
        templateResolver.setCacheTTLMs(60000L);
        templateResolver.setCacheable(true);

        // create template
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

    }

    protected void processTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");

        // create web context
        IServletWebExchange webExchange = this.application.buildExchange(req, resp);
        WebContext webContext = new WebContext(webExchange);

        // process template
        templateEngine.process(templateName, webContext, resp.getWriter());
    }
}