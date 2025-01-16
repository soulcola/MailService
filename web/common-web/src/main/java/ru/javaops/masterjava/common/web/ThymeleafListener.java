package ru.javaops.masterjava.common.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.thymeleaf.ITemplateEngine;


@WebListener
public class ThymeleafListener implements ServletContextListener {

    public static ITemplateEngine engine;

    public void contextInitialized(ServletContextEvent sce) {
        engine = ThymeleafUtil.getEngine(sce.getServletContext());
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
