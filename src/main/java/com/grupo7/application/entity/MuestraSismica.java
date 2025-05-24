package com.grupo7.application.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "muestra_sismica")
public class MuestraSismica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MUESTRA")
    private Long id;

    @Column(name = "FECHA_HORA_MUESTRA")
    private LocalDateTime fechaHoraMuestra;

    @ManyToOne
    @JoinColumn(name = "ID_DETALLE_MUESTRA")
    private DetalleMuestraSismica detalleMuestraSismica;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHoraMuestra() { return fechaHoraMuestra; }
    public void setFechaHoraMuestra(LocalDateTime fechaHoraMuestra) { this.fechaHoraMuestra = fechaHoraMuestra; }

    public DetalleMuestraSismica getDetalleMuestraSismica() { return detalleMuestraSismica; }
    public void setDetalleMuestraSismica(DetalleMuestraSismica detalleMuestraSismica) { this.detalleMuestraSismica = detalleMuestraSismica; }
}
