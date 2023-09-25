package com.seitov.chatgptrest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig
public class AuthenticationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    public void testRegistrationAndLogin() {
        // Registration
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        String registrationRequestBody = "username=testuser&password=testpassword";
        HttpEntity<String> registrationRequestEntity = new HttpEntity<>(registrationRequestBody, headers);

        ResponseEntity<String> registrationResponse = restTemplate.exchange(
                baseUrl + "/registration",
                HttpMethod.POST,
                registrationRequestEntity,
                String.class
        );
        assertEquals(HttpStatus.FOUND, registrationResponse.getStatusCode());

        // Login
        headers.clear();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        String loginRequestBody = "username=testuser&password=testpassword";
        HttpEntity<String> loginRequestEntity = new HttpEntity<>(loginRequestBody, headers);

        ResponseEntity<String> loginResponse = restTemplate.exchange(
                baseUrl + "/login",
                HttpMethod.POST,
                loginRequestEntity,
                String.class
        );
        assertEquals(HttpStatus.FOUND, loginResponse.getStatusCode());

        // Add additional assertions as needed
    }
}