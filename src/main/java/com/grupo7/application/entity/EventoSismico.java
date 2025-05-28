package com.grupo7.application.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "evento_sismico")
public class EventoSismico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FECHA_HORA_FIN", nullable = false)
    private LocalDateTime fechaHoraFin;

    @Column(name = "FECHA_HORA_OCURRENCIA", nullable = false)
    private LocalDateTime fechaHoraOcurrencia;

    @Column(name = "LATITUD_EPICENTRO", nullable = false)
    private Double latitudEpicentro;

    @Column(name = "LONGITUD_EPICENTRO", nullable = false)
    private Double longitudEpicentro;

    @Column(name = "LATITUD_HIPOCENTRO", nullable = false)
    private Double latitudHipocentro;

    @Column(name = "LONGITUD_HIPOCENTRO", nullable = false)
    private Double longitudHipocentro;

    @Column(name = "VALOR_MAGNITUD", nullable = false)
    private Double valorMagnitud;

    @ManyToOne
    @JoinColumn(name = "ALCANCE_SISMO_ID")
    private AlcanceSismo alcanceSismo;

    @ManyToOne
    @JoinColumn(name = "CLASIFICACION_SISMO_ID")
    private ClasificacionSismo clasificacionSismo;

    @ManyToOne
    @JoinColumn(name = "ESTADO_ACTUAL_ID")
    private Estado estadoActual;

    @ManyToOne
    @JoinColumn(name = "MAGNITUD_RITCHER_ID")
    private MagnitudRitcher magnitudRitcher;

    @ManyToOne
    @JoinColumn(name = "ORIGEN_GENERACION_ID")
    private OrigenDeGeneracion origenGeneracion;
    
    // bi-direccional hacia SerieTemporal
    @OneToMany(mappedBy = "eventoSismico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SerieTemporal> seriesTemporales = new ArrayList<>();

    // Constructor por defecto
    public EventoSismico() {
    }

    public EventoSismico(Long id, LocalDateTime fechaHoraFin, LocalDateTime fechaHoraOcurrencia, Double latitudEpicentro, Double longitudEpicentro, Double latitudHipocentro, Double longitudHipocentro, Double valorMagnitud) {
        this.id = id;
        this.fechaHoraFin = fechaHoraFin;
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
        this.latitudEpicentro = latitudEpicentro;
        this.longitudEpicentro = longitudEpicentro;
        this.latitudHipocentro = latitudHipocentro;
        this.longitudHipocentro = longitudHipocentro;
        this.valorMagnitud = valorMagnitud;
    }
    
    
    public List<SerieTemporal> getSeriesTemporales() { return seriesTemporales; }

    // método auxiliar para mantener la consistencia
    public void agregarSerieTemporal(SerieTemporal serie) {
        seriesTemporales.add(serie);
        serie.setEventoSismico(this);
        }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public LocalDateTime getFechaHoraOcurrencia() {
        return fechaHoraOcurrencia;
    }

    public void setFechaHoraOcurrencia(LocalDateTime fechaHoraOcurrencia) {
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
    }

    public Double getLatitudEpicentro() {
        return latitudEpicentro;
    }

    public void setLatitudEpicentro(Double latitudEpicentro) {
        this.latitudEpicentro = latitudEpicentro;
    }

    public Double getLongitudEpicentro() {
        return longitudEpicentro;
    }

    public void setLongitudEpicentro(Double longitudEpicentro) {
        this.longitudEpicentro = longitudEpicentro;
    }

    public Double getLatitudHipocentro() {
        return latitudHipocentro;
    }

    public void setLatitudHipocentro(Double latitudHipocentro) {
        this.latitudHipocentro = latitudHipocentro;
    }

    public Double getLongitudHipocentro() {
        return longitudHipocentro;
    }

    public void setLongitudHipocentro(Double longitudHipocentro) {
        this.longitudHipocentro = longitudHipocentro;
    }

    public Double getValorMagnitud() {
        return valorMagnitud;
    }

    public void setValorMagnitud(Double valorMagnitud) {
        this.valorMagnitud = valorMagnitud;
    }

    public AlcanceSismo getAlcanceSismo() {
        return alcanceSismo;
    }

    public void setAlcanceSismo(AlcanceSismo alcanceSismo) {
        this.alcanceSismo = alcanceSismo;
    }

    public ClasificacionSismo getClasificacionSismo() {
        return clasificacionSismo;
    }

    public void setClasificacionSismo(ClasificacionSismo clasificacionSismo) {
        this.clasificacionSismo = clasificacionSismo;
    }

    public Estado getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(Estado estadoActual) {
        this.estadoActual = estadoActual;
    }

    public MagnitudRitcher getMagnitudRitcher() {
        return magnitudRitcher;
    }

    public void setMagnitudRitcher(MagnitudRitcher magnitudRitcher) {
        this.magnitudRitcher = magnitudRitcher;
    }

    public OrigenDeGeneracion getOrigenGeneracion() {
        return origenGeneracion;
    }

    public void setOrigenGeneracion(OrigenDeGeneracion origenGeneracion) {
        this.origenGeneracion = origenGeneracion;
    }
}
