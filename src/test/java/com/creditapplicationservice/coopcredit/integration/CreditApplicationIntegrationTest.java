package com.creditapplicationservice.coopcredit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.creditapplicationservice.coopcredit.infraestructure.rest.dto.AuthRegisterRequest;
import com.creditapplicationservice.coopcredit.infraestructure.rest.dto.AuthRequest;
import com.creditapplicationservice.coopcredit.infraestructure.rest.dto.CreditApplicationRequest;
import com.creditapplicationservice.coopcredit.infraestructure.rest.dto.AffiliateRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreditApplicationIntegrationTest {

    private static PostgreSQLContainer<?> postgres;
    private static boolean containerStarted = false;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        try {
            postgres = new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");
            postgres.start();
            containerStarted = true;
            registry.add("spring.datasource.url", postgres::getJdbcUrl);
            registry.add("spring.datasource.username", postgres::getUsername);
            registry.add("spring.datasource.password", postgres::getPassword);
        } catch (Exception e) {
            // Docker not available; fall back to test application.properties (H2)
            containerStarted = false;
            System.out.println("Testcontainers not available, falling back to H2 in-memory DB for integration test: " + e.getMessage());
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeAll
    static void setupAll() {
        // nothing
    }

    @AfterAll
    static void tearDown() {
        if (containerStarted && postgres != null) {
            postgres.stop();
        }
    }

    @Test
    void fullFlow_createAffiliate_createApplication_andEvaluate() throws Exception {
        // 1) register admin user
        AuthRegisterRequest reg = new AuthRegisterRequest();
        reg.setUsername("admin1");
        reg.setPassword("pass");
        reg.setRole("ROLE_ADMIN");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reg)))
                .andExpect(status().isOk());

        // 2) login to obtain token
        AuthRequest auth = new AuthRequest();
        auth.setUsername("admin1");
        auth.setPassword("pass");
        String loginResp = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(auth)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String token = mapper.readTree(loginResp).get("token").asText();

        // 3) create affiliate
        AffiliateRequest affiliate = new AffiliateRequest();
        affiliate.setDocument("555");
        affiliate.setName("Alice");
        affiliate.setSalary(5000);
        // asegurar antigüedad mínima (7 meses atrás)
        affiliate.setAffiliationDate(LocalDate.now().minusMonths(7));
        String affiliateResp = mockMvc.perform(post("/api/affiliates")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(affiliate)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        long affiliateId = mapper.readTree(affiliateResp).get("id").asLong();

        // 4) create credit application
        CreditApplicationRequest req = new CreditApplicationRequest();
        req.setAffiliateId(affiliateId);
        req.setAmount(1000);
        req.setTermMonths(12);
        req.setInterestRate(5.0);
        String appResp = mockMvc.perform(post("/api/credit-applications")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        long appId = mapper.readTree(appResp).get("id").asLong();

        // 5) evaluate
        mockMvc.perform(post("/api/credit-applications/" + appId + "/evaluate")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
