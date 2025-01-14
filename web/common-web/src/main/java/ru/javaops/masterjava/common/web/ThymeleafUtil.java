package ru.javaops.masterjava.common.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;


public class ThymeleafUtil {

    private ITemplateEngine templateEngine;
    private JakartaServletWebApplication application;
    private static final ThymeleafUtil thymeleafUtil = new ThymeleafUtil();

    private ThymeleafUtil() {
    }

    public void init(final ServletContext context) {
        this.application =
                JakartaServletWebApplication.buildApplication(context);
        this.templateEngine = buildTemplateEngine(this.application);
    }


    public ITemplateEngine buildTemplateEngine(final IWebApplication application) {
        final WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(1000L);
        final TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver);
        return engine;
    }

    public static ITemplateEngine getEngine(final ServletContext context) {
        thymeleafUtil.init(context);
        return thymeleafUtil.templateEngine;
    }

    public static IWebExchange getIWebExchange(final HttpServletRequest request, final HttpServletResponse response) {
        return thymeleafUtil.application.buildExchange(request, response);
    }
}
