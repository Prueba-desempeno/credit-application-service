package com.creditapplicationservice.coopcredit.riskcentral.dto;

public class RiskRequest {
    private String documento;
    private double monto;
    private int plazo;

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public int getPlazo() { return plazo; }
    public void setPlazo(int plazo) { this.plazo = plazo; }
}

