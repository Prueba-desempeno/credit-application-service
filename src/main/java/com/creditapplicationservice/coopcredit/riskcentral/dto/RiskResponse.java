package com.creditapplicationservice.coopcredit.riskcentral.dto;

public class RiskResponse {
    private String documento;
    private int score;
    private String nivelRiesgo;
    private String detalle;

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getNivelRiesgo() { return nivelRiesgo; }
    public void setNivelRiesgo(String nivelRiesgo) { this.nivelRiesgo = nivelRiesgo; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
}

