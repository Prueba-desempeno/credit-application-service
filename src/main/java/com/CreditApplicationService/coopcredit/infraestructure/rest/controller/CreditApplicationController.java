package com.CreditApplicationService.coopcredit.infraestructure.rest.controller;

import com.CreditApplicationService.coopcredit.domain.port.inbound.RegisterCreditApplicationUseCase;
import com.CreditApplicationService.coopcredit.domain.port.inbound.EvaluateCreditApplicationUseCase;
import com.CreditApplicationService.coopcredit.infraestructure.rest.dto.CreditApplicationRequest;
import com.CreditApplicationService.coopcredit.infraestructure.rest.dto.CreditApplicationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credit-applications")
public class CreditApplicationController {

    private final RegisterCreditApplicationUseCase registerCreditApplicationUseCase;
    private final EvaluateCreditApplicationUseCase evaluateCreditApplicationUseCase;

    public CreditApplicationController(
            RegisterCreditApplicationUseCase registerCreditApplicationUseCase,
            EvaluateCreditApplicationUseCase evaluateCreditApplicationUseCase
    ) {
        this.registerCreditApplicationUseCase = registerCreditApplicationUseCase;
        this.evaluateCreditApplicationUseCase = evaluateCreditApplicationUseCase;
    }

    @PostMapping
    public ResponseEntity<CreditApplicationResponse> create(@RequestBody CreditApplicationRequest request) {
        var app = registerCreditApplicationUseCase.create(request.toDomain());
        return ResponseEntity.ok(CreditApplicationResponse.fromDomain(app));
    }

    @PostMapping("/{id}/evaluate")
    public ResponseEntity<String> evaluate(@PathVariable Long id) {
        evaluateCreditApplicationUseCase.evaluate(id);
        return ResponseEntity.ok("Solicitud evaluada correctamente.");
    }
}

