package com.creditapplicationservice.coopcredit.riskcentral.controller;

import com.creditapplicationservice.coopcredit.riskcentral.dto.RiskRequest;
import com.creditapplicationservice.coopcredit.riskcentral.dto.RiskResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class RiskController {

    @PostMapping("/risk-evaluation")
    public RiskResponse evaluate(@RequestBody RiskRequest request) {
        String documento = request.getDocumento();
        int seed = Math.abs(documento != null ? documento.hashCode() : 0) % 1000;
        int score = 300 + (int) ((950 - 300) * (seed / 999.0));
        String nivel;
        String detalle;
        if (score <= 500) {
            nivel = "ALTO";
            detalle = "Historial crediticio alto riesgo.";
        } else if (score <= 700) {
            nivel = "MEDIO";
            detalle = "Historial crediticio moderado.";
        } else {
            nivel = "BAJO";
            detalle = "Historial crediticio bajo riesgo.";
        }
        RiskResponse resp = new RiskResponse();
        resp.setDocumento(documento);
        resp.setScore(score);
        resp.setNivelRiesgo(nivel);
        resp.setDetalle(detalle);
        return resp;
    }
}
