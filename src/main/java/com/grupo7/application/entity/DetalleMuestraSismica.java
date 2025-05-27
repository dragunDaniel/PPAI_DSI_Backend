package com.grupo7.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_muestra_sismica")
public class DetalleMuestraSismica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_MUESTRA_SISMICA")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO")
    private TipoDeDato tipoDeDato;

    @Column(name = "VALOR")
    private Double valor;

    // Many DetalleMuestraSismica to one MuestraSismica
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MUESTRA_SISMICA") // Matches the foreign key in schema.sql
    private MuestraSismica muestraSismica;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoDeDato getTipoDeDato() { return tipoDeDato; }
    public void setTipoDeDato(TipoDeDato tipoDeDato) { this.tipoDeDato = tipoDeDato; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public MuestraSismica getMuestraSismica() {
        return muestraSismica;
    }

    public void setMuestraSismica(MuestraSismica muestraSismica) {
        this.muestraSismica = muestraSismica;
    }
}
