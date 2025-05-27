package com.grupo7.application.dto;

import java.time.LocalDateTime;
import java.util.List; // To hold the collection of MuestraSismicaDTOs
import java.util.ArrayList; // For initializing the list

public class SerieTemporalDTO {
    private Long id;
    private String condicionAlarma;
    private LocalDateTime fechaHoraRegistros;
    private String sismografoIdentificador; // Assuming you want the identifier from the sismografo
    private List<MuestraSismicaDTO> muestrasSismicas; // This will hold the hierarchical MuestraSismica data

    public SerieTemporalDTO() {
        this.muestrasSismicas = new ArrayList<>(); // Initialize to prevent NullPointerException
    }

    // Constructor for the original simple fields (if still needed)
    public SerieTemporalDTO(Long id, String condicionAlarma, LocalDateTime fechaHoraRegistros, String sismografoIdentificador) {
        this.id = id;
        this.condicionAlarma = condicionAlarma;
        this.fechaHoraRegistros = fechaHoraRegistros;
        this.sismografoIdentificador = sismografoIdentificador;
        this.muestrasSismicas = new ArrayList<>();
    }

    // New constructor to include the nested muestrasSismicas
    public SerieTemporalDTO(Long id, String condicionAlarma, LocalDateTime fechaHoraRegistros,
                            String sismografoIdentificador, List<MuestraSismicaDTO> muestrasSismicas) {
        this.id = id;
        this.condicionAlarma = condicionAlarma;
        this.fechaHoraRegistros = fechaHoraRegistros;
        this.sismografoIdentificador = sismografoIdentificador;
        this.muestrasSismicas = muestrasSismicas != null ? muestrasSismicas : new ArrayList<>();
    }

    // Note: The DatosRegistradosDTO constructor in EventoSismicoService
    // was using a different set of parameters (String names and List<SerieTemporalDetalleDTO>).
    // If this SerieTemporalDTO is intended to be the 'SerieTemporalDetalleDTO' from EventoSismicoService,
    // you would need to rename or adjust the DTOs accordingly for consistency across services.
    // For now, I'm assuming this is the standalone SerieTemporalDTO.

    // Getters
    public Long getId() { return id; }
    public String getCondicionAlarma() { return condicionAlarma; }
    public LocalDateTime getFechaHoraRegistros() { return fechaHoraRegistros; }
    public String getSismografoIdentificador() { return sismografoIdentificador; }
    public List<MuestraSismicaDTO> getMuestrasSismicas() { return muestrasSismicas; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCondicionAlarma(String condicionAlarma) { this.condicionAlarma = condicionAlarma; }
    public void setFechaHoraRegistros(LocalDateTime fechaHoraRegistros) { this.fechaHoraRegistros = fechaHoraRegistros; }
    public void setSismografoIdentificador(String sismografoIdentificador) { this.sismografoIdentificador = sismografoIdentificador; }
    public void setMuestrasSismicas(List<MuestraSismicaDTO> muestrasSismicas) {
        this.muestrasSismicas = muestrasSismicas != null ? muestrasSismicas : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "SerieTemporalDTO{" +
               "id=" + id +
               ", condicionAlarma='" + condicionAlarma + '\'' +
               ", fechaHoraRegistros=" + fechaHoraRegistros +
               ", sismografoIdentificador='" + sismografoIdentificador + '\'' +
               ", muestrasSismicas=" + muestrasSismicas +
               '}';
    }
}
