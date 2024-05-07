package com.skillstorm.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of(new AuthInterceptor()));

        return restTemplate;
    }

    // Needed so that our RestTemplate can use the authorization already stored in the request headers.
    // Without it, we'd effectively be trying to log in with either no password or the hashed password.
    private static class AuthInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("\n\nAuthentication: " + authentication.toString());
            System.out.println("Request headers prior to auth: " + request.getHeaders().toString());
            if(authentication != null && authentication.isAuthenticated()) {
                HttpHeaders headers = request.getHeaders();
                headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + authentication.getCredentials());
            }

            System.out.println("Request headers with auth: " + request.getHeaders().toString() + "\n\n");
            return execution.execute(request, body);
        }
    }
}
