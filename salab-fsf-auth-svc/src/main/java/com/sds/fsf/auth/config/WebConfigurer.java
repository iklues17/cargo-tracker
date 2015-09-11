package com.sds.fsf.auth.config;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration

public class WebConfigurer implements ServletContextInitializer {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    @Inject
    private Environment env;


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("Web application configuration, using profiles: {}", Arrays.toString(env.getActiveProfiles()));
        
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);

//        initCORSFilter(servletContext, disps);
        if (env.acceptsProfiles("boot")) {
            initH2Console(servletContext);
        }

    }

    /**
     * Initializes the CORS filter.
     */
    private void initCORSFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        log.debug("Registering CORS Filter");
        FilterRegistration.Dynamic simpleCORSFilter = servletContext.addFilter("simpleCORSFilter", new SimpleCORSFilter());
        Map<String, String> parameters = new HashMap<>();
        simpleCORSFilter.setInitParameters(parameters);
        simpleCORSFilter.addMappingForUrlPatterns(disps, true, "*");
        simpleCORSFilter.setAsyncSupported(true);
    }
    

    /**
     * Initializes H2 console
     */
    private void initH2Console(ServletContext servletContext) {
        log.debug("Initialize H2 console");
        ServletRegistration.Dynamic h2ConsoleServlet = servletContext.addServlet("H2Console", new org.h2.server.web.WebServlet());
        h2ConsoleServlet.addMapping("/console/*");
        h2ConsoleServlet.setInitParameter("-properties", "src/main/resources");
        h2ConsoleServlet.setLoadOnStartup(1);
    }
}
