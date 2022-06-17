package com.dkkim.springsecurity.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HealthCheckControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("antMatchers 등록된 API 인증 없이 성공")
    void success_antMatchers_api_healthCheck() throws Exception {
        // when && then
        mvc.perform(get("/api/health-check"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("antMatchers 등록되지 않은 API 인증 없이 실패")
    void fail_not_antMatchers_api() throws Exception {
        // when && then
        mvc.perform(get("/api/not-authorization-api"))
                .andExpect(status().isUnauthorized());
    }

}