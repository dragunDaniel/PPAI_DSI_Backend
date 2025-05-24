package com.grupo7.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_muestra_sismica")
public class DetalleMuestraSismica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_MUESTRA_SISMICA")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_TIPO")
    private TipoDeDato tipoDeDato;

    @Column(name = "VALOR")
    private Double valor;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoDeDato getTipoDeDato() { return tipoDeDato; }
    public void setTipoDeDato(TipoDeDato tipoDeDato) { this.tipoDeDato = tipoDeDato; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
}
