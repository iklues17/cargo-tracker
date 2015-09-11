package com.sds.fsf.auth.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.sds.fsf.auth.security.AjaxLogoutSuccessHandler;
import com.sds.fsf.cmm.security.Http401UnauthorizedEntryPoint;
import com.sds.fsf.auth.security.oauth2.ScopeFilteredAccessTokenConverter;

@Configuration
public class OAuth2ServerConfiguration {

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Inject
        private Http401UnauthorizedEntryPoint authenticationEntryPoint;

        @Inject
        private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

		@Override
		public void configure(ResourceServerSecurityConfigurer resources)
				throws Exception {
			resources.authenticationEntryPoint(authenticationEntryPoint);
		}
		
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
	            .requestMatchers()
	            	.antMatchers("/api/**")
	            	.and()
	            .exceptionHandling()
	            	.authenticationEntryPoint(authenticationEntryPoint)
	            	.and()
	            .logout()
		            .logoutUrl("/api/logout")
		            .logoutSuccessHandler(ajaxLogoutSuccessHandler)
		            .and()
                .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                .disable()
                .headers()
                .frameOptions().disable()
                .sessionManagement()
                	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                	.and()
                .authorizeRequests()
        			.antMatchers("/api/logout").permitAll()
        			.antMatchers("/api/register").permitAll()
        			.antMatchers("/api/activate").permitAll()
        			.antMatchers("/api/t2/register/**").permitAll()
        			.antMatchers("/api/t2/activate").permitAll()   
        			.antMatchers("/api/send_login_by_email").permitAll()  
        			.antMatchers("/api/send_onetime_link_by_email_to_reset_password").permitAll()  
        			.antMatchers("/api/get_login").hasRole("AUTH_USER")
        			.antMatchers("/api/randomize_password").hasRole("AUTH_USER")     			
        			.antMatchers("/api/authorities/**").hasRole("AUTH_USER")
        			.antMatchers("/api/admin/authorities/**").hasRole("AUTH_USER")
        			.antMatchers(HttpMethod.GET, "/api/admin/clients/**").hasRole("AUTH_USER")
        			.antMatchers(HttpMethod.GET, "/api/admin/users/**").hasRole("AUTH_USER")
        			.antMatchers("/api/admin/**").hasRole("AUTH")        			
        			.antMatchers("/api/**").authenticated()
	            	.anyRequest().hasRole("AUTH_USER")
	            	.and()
                ;

        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {



        @Inject
        private DataSource dataSource;

        @Bean
        public TokenStore tokenStore() {
            return new JdbcTokenStore(dataSource);
        }

        @Inject
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {

            endpoints
                    .tokenStore(tokenStore())
                    .authenticationManager(authenticationManager)
                    .accessTokenConverter(accessTokenConverter());
        }

        @Bean
        public AccessTokenConverter accessTokenConverter() {
			return new ScopeFilteredAccessTokenConverter();
		}
        
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        	clients.jdbc(dataSource);
        }

		@Override
		public void configure(AuthorizationServerSecurityConfigurer security)
				throws Exception {
			
			security
				.realm("oauth2/client")
				.checkTokenAccess("permitAll()");
		}

    }
}
