package com.sds.fsf.auth.security;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security logout handler, specialized for Ajax requests.
 */
@Component
public class AjaxLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
        implements LogoutSuccessHandler, ApplicationEventPublisherAware {

    public static final String BEARER_AUTHENTICATION = "Bearer ";
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private ApplicationEventPublisher applicationEventPublisher;
    
    @Inject
    private TokenStore tokenStore;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {

    	int responseStatus = HttpServletResponse.SC_BAD_REQUEST;
    	Map<String, String> responseBodyMap = new HashMap<String,String>();
    	
        // Request the token
        String token = request.getHeader("authorization");
        if (token != null && token.startsWith(BEARER_AUTHENTICATION)) {
            final OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(StringUtils.substringAfter(token, BEARER_AUTHENTICATION));

            if (oAuth2AccessToken != null) {
            	OAuth2Authentication authentcation = tokenStore.readAuthentication(oAuth2AccessToken);
                tokenStore.removeAccessToken(oAuth2AccessToken);
                
                if(tokenStore.readAuthentication(oAuth2AccessToken) != null) {
                	responseStatus = HttpServletResponse.SC_BAD_REQUEST;
                	responseBodyMap.put("desciption", "Cannot remove access_token.");           	
                } else {
                    applicationEventPublisher.publishEvent(new LogoutSuccessEvent(authentcation));
                    responseStatus = HttpServletResponse.SC_OK;
                }
            } else {
            	responseStatus = HttpServletResponse.SC_BAD_REQUEST;
            	responseBodyMap.put("desciption", "Invalid access_token.");
            }
        } else {
        	responseStatus = HttpServletResponse.SC_BAD_REQUEST;
        	responseBodyMap.put("desciption", "Authorization header is required.");       	
        }
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, link"); 
        response.setHeader("Access-Control-Expose-Headers", "link");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(responseStatus);
        if(responseStatus != HttpServletResponse.SC_OK) {
        	response.getOutputStream().print(new ObjectMapper().writeValueAsString(responseBodyMap));   
        }
    }

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
		
	}
}
