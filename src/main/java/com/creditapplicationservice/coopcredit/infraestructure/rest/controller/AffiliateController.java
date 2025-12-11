package com.creditapplicationservice.coopcredit.infraestructure.rest.controller;

import com.creditapplicationservice.coopcredit.domain.port.inbound.RegisterAffiliateUseCase;
import com.creditapplicationservice.coopcredit.infraestructure.rest.dto.AffiliateRequest;
import com.creditapplicationservice.coopcredit.infraestructure.rest.dto.AffiliateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/affiliates")
public class AffiliateController {

    private final RegisterAffiliateUseCase registerAffiliateUseCase;

    public AffiliateController(RegisterAffiliateUseCase registerAffiliateUseCase) {
        this.registerAffiliateUseCase = registerAffiliateUseCase;
    }

    @PostMapping
    public ResponseEntity<AffiliateResponse> register(@RequestBody AffiliateRequest request) {
        var affiliate = registerAffiliateUseCase.register(request.toDomain());
        return ResponseEntity.ok(AffiliateResponse.fromDomain(affiliate));
    }
}

