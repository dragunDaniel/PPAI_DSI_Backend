package com.grupo7.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_de_dato")
public class TipoDeDato {

    @Id
    @Column(name = "id_tipo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(name = "DENOMINACION", nullable = false) // <-- FIX 2: Explicitly map 'denominacion'
    private String denominacion;

    @Column(name = "NOMBRE_UNIDAD_MEDIDA", nullable = false) // <-- FIX 3: Map 'nombreUnidadMedida' to 'NOMBRE_UNIDAD_MEDIDA'
    private String nombreUnidadMedida;

    @Column(name = "VALOR_UMBRAL", nullable = false) // <-- FIX 4: Map 'valorUmbral' to 'VALOR_UMBRAL' AND change data type
    private Double valorUmbral; // <-- FIX 5: Changed from int to Double to match REAL in schema

    // constructor por defecto
    public TipoDeDato() {

    }

    public TipoDeDato(String denominacion, String nombreUnidadMedida, Double valorUmbral) { // <-- FIX 6: Constructor parameter type
        this.denominacion = denominacion;
        this.nombreUnidadMedida = nombreUnidadMedida;
        this.valorUmbral = valorUmbral;
    }

    public boolean esTuDenominacion() {
        return denominacion.equals("Longitud") || denominacion.equals("Velocidad") || denominacion.equals("Frecuencia");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { // Added setter for id
        this.id = id;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) { // Added setter for denominacion
        this.denominacion = denominacion;
    }

    public String getNombreUnidadMedida() {
        return nombreUnidadMedida;
    }

    public void setNombreUnidadMedida(String nombreUnidadMedida) { // Added setter for nombreUnidadMedida
        this.nombreUnidadMedida = nombreUnidadMedida;
    }

    public Double getValorUmbral() { // <-- FIX 7: Getter return type
        return valorUmbral;
    }

    public void setValorUmbral(Double valorUmbral) { // <-- FIX 8: Setter parameter type
        this.valorUmbral = valorUmbral;
    }
}