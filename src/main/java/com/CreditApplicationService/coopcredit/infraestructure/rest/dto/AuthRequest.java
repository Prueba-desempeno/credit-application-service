package com.CreditApplicationService.coopcredit.infraestructure.rest.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}

