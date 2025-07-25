package com.grupo7.application.dto;

public class EstadoDTO {

    private Long id;
    private String ambito;
    private String nombreEstado;

    public EstadoDTO() {
    }

    public EstadoDTO(Long id, String ambito, String nombreEstado) {
        this.id = id;
        this.ambito = ambito;
        this.nombreEstado = nombreEstado;
    }

    public boolean sosBloqueadoEnRevision() {
        return this.nombreEstado.equals("BloqueadoEnRevision");
    }

    public boolean sosPendienteDeRevision() {
        return this.nombreEstado.equals("PendienteDeRevision");
    }

    public boolean sosAutoDetectado() {
        return this.nombreEstado.equals("AutoDetectado");
    }

    public boolean sosRevisionManual() {
        return this.nombreEstado.equals("RevisionManual");
    }

    public boolean sosRevisionAutomatica() {
        return this.nombreEstado.equals("RevisionAutomatica");
    }

    public boolean sosRevisionFinalizada() {
        return this.nombreEstado.equals("RevisionFinalizada");
    }

    public boolean sosRevisionCancelada() {
        return this.nombreEstado.equals("RevisionCancelada");
    }

    public boolean sosDerivadoAExperto() {
        return this.nombreEstado.equals("DerivadoAExperto");
    }

    public boolean sosRechazado() {
        return this.nombreEstado.equals("Rechazado");
    }

    public boolean sosConfirmado() {
        return this.nombreEstado.equals("Confirmado");
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
}
