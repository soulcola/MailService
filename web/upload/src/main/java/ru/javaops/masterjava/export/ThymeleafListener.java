package ru.javaops.masterjava.export;

import org.thymeleaf.TemplateEngine;
import ru.javaops.masterjava.xml.util.ThymeLeafUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ThymeleafListener implements ServletContextListener {
    public static TemplateEngine engine;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
            engine = ThymeLeafUtil.getEngine(sce.getServletContext());
    }
}
