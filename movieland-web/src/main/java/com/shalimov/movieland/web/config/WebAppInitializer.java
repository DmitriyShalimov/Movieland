package com.shalimov.movieland.web.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class WebAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        //root context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfig.class);

        //root context listener
        servletContext.addListener(new ContextLoaderListener(rootContext));

        //api context
        AnnotationConfigWebApplicationContext apiContext = new AnnotationConfigWebApplicationContext();
        apiContext.register(ApiConfig.class);

        //dispatcher for api
        DispatcherServlet apiDispatcher = new DispatcherServlet(apiContext);
        ServletRegistration.Dynamic apiRegister = servletContext.addServlet("Api Servlet", apiDispatcher);
        apiRegister.setLoadOnStartup(1);
        apiRegister.addMapping("/v1/*");

        //web context
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(WebConfig.class);

        //dispatcher for web
        DispatcherServlet webDispatcher = new DispatcherServlet(webContext);
        ServletRegistration.Dynamic webRegister = servletContext.addServlet("Web Servlet", webDispatcher);
        webRegister.setLoadOnStartup(1);
        webRegister.addMapping("/v1/*");

    }
}