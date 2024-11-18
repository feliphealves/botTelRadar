package com.botTelRadar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.botTelRadar.model.User;

@Service
public class AuthService {
    private final RestTemplate restTemplate;
    private final String loginUrl;

    @Autowired
    public AuthService(RestTemplate restTemplate, @Value("${app.login.url}") String loginUrl) {
        this.restTemplate = restTemplate;
        this.loginUrl = loginUrl;
    }

    public String authenticate(String username, String password) {
    	User authRequest = new User(username, password);
        HttpEntity<User> request = new HttpEntity<>(authRequest);
        ResponseEntity<String> response = restTemplate.exchange(
            loginUrl,
            HttpMethod.POST,
            request,
            String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return null;
    }

}
