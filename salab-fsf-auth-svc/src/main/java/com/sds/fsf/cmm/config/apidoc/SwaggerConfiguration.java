package com.sds.fsf.cmm.config.apidoc;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.paths.SwaggerPathProvider;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

/**
 * Swagger configuration.
 *
 * Warning! When having a lot of REST endpoints, Swagger can become a performance issue. In that
 * case, you can use a specific Spring profile for this class, so that only front-end developers
 * have access to the Swagger view.
 */
@Configuration
@EnableSwagger
@PropertySources({
	@PropertySource(value = "classpath:/config/swagger-default.properties", ignoreResourceNotFound = true),
	@PropertySource("classpath:/config/swagger.properties")
})
@Profile("API")
public class SwaggerConfiguration {

    private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

    @Inject
    private Environment env;
    
    @Inject
    private ServletContext servletContext;
    
    /**
     * Swagger Spring MVC configuration.
     */
    @Bean
    public SwaggerSpringMvcPlugin swaggerSpringMvcPlugin(SpringSwaggerConfig springSwaggerConfig) {
        log.info("Starting Swagger");
        log.debug("documentation.services.basePath : " + env.getProperty("documentation.services.basePath"));
        StopWatch watch = new StopWatch();
        watch.start();
        
        SwaggerSpringMvcPlugin swaggerSpringMvcPlugin = new SwaggerSpringMvcPlugin(springSwaggerConfig)
            .apiInfo(apiInfo())
            .genericModelSubstitutes(ResponseEntity.class)
            .includePatterns(env.getProperty("include.pattern")) 
            .pathProvider(swaggerPathProvider())
            ;

        swaggerSpringMvcPlugin.build();
        watch.stop();
        log.info("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return swaggerSpringMvcPlugin;
    }

    private SwaggerPathProvider swaggerPathProvider() {
		SwaggerPathProvider provider = new SwaggerPathProvider() {
			
			@Override
			protected String getDocumentationPath() {
				return "/";
			}
			
			@Override
			protected String applicationPath() {
				String applicationPath = "/";
				if(StringUtils.isEmpty(env.getProperty("documentation.services.basePath"))){
					try {
						applicationPath = "http://" + InetAddress.getLocalHost().getHostAddress();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				} else {
					applicationPath = env.getProperty("documentation.services.basePath");
				}
			
				return applicationPath + servletContext.getContextPath();
			}
		};
		//provider.setApiResourcePrefix(env.getProperty("api.resource.prefix"));
		return provider;
	}

	/**
     * API Info as it appears on the swagger-ui page.
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
        		env.getProperty("apiinfo.title"),//title
        		env.getProperty("apiinfo.description"),//description
        		env.getProperty("apiinfo.termsOfServiceUrl"),//termsOfServiceUrl
        		env.getProperty("apiinfo.contact"),//contact
        		env.getProperty("apiinfo.license"),//license
        		env.getProperty("apiinfo.licenseUrl")//licenseUrl        		    	
        		);
    }
}
