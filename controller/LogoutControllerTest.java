package com.example.diploma1.controller;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LogoutControllerTest {
    @Autowired
    TestRestTemplate template1;
    public static GenericContainer<?> app = new GenericContainer("app").withExposedPorts(29999);

    @BeforeAll
    public static void setUp() {
        app.start();
    }


}