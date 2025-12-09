package com.CreditApplicationService.coopcredit.infraestructure.rest.dto;

import lombok.Data;

@Data
public class AuthRegisterRequest {
    private String username;
    private String password;
    private String role; // ROLE_AFILIADO / ROLE_ANALISTA / ROLE_ADMIN
}
