package org.anyframe.filter.auth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonJsonParser;
import org.springframework.boot.json.JsonSimpleJsonParser;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class CheckAccessTokenFilter extends ZuulFilter {

	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private AuthServerConfiguration authServerConfig; 
			
	@Override
	public Object run() {
		Object result;
		RequestContext ctx = RequestContext.getCurrentContext();
		String auth_server_uri = authServerConfig.getBaseUrl() + authServerConfig.getCheckTokenUrl();
		
		try {
			String authHeader = ctx.getRequest().getHeader("Authorization");
			if(authHeader == null) {
				authHeader = "bearer ";
			}
			String[] bearer_token = authHeader.trim().split(" ");
			String accessToken = bearer_token[bearer_token.length-1];
			ResponseEntity<String> exchange = restTemplate.exchange(auth_server_uri, HttpMethod.GET, null, String.class, accessToken);
			result = exchange.hasBody();
			
			if(exchange != null && exchange.hasBody()) {
				Map<String, Object> userInfoMap = new JsonJsonParser().parseMap(exchange.getBody());
				if(userInfoMap.get("user_name") != null) {
					ctx.addZuulRequestHeader("userName", userInfoMap.get("user_name").toString());
				}
			}
			
		} catch (HttpClientErrorException e) {
			ctx.getResponse().setStatus(e.getStatusCode().value());
			ctx.setResponseBody(e.getResponseBodyAsString());
			throw e;
		} catch (Exception e) {
			ctx.getResponse().setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			throw e;
		}
		
		return result;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		if(request.getRequestURI().equals("/auth"+authServerConfig.getGetTokenUrl().split("\\?")[0])){
			return false;
		}
		return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return "pre";
	}
}
