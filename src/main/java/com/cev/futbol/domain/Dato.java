package com.cev.futbol.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Dato.
 */
@Entity
@Table(name = "dato")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dato")
public class Dato implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jornada")
    private Integer jornada;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "rival", length = 20, nullable = false)
    private String rival;

    @Size(min = 1, max = 10)
    @Column(name = "resultado", length = 10)
    private String resultado;

    @Column(name = "numero_de_goles")
    private Integer numeroDeGoles;

    @Column(name = "corner")
    private Integer corner;

    @Column(name = "faltas")
    private Integer faltas;

    @ManyToOne
    @JsonIgnoreProperties(value = "datoes", allowSetters = true)
    private Equipo equipo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getJornada() {
        return jornada;
    }

    public Dato jornada(Integer jornada) {
        this.jornada = jornada;
        return this;
    }

    public void setJornada(Integer jornada) {
        this.jornada = jornada;
    }

    public String getRival() {
        return rival;
    }

    public Dato rival(String rival) {
        this.rival = rival;
        return this;
    }

    public void setRival(String rival) {
        this.rival = rival;
    }

    public String getResultado() {
        return resultado;
    }

    public Dato resultado(String resultado) {
        this.resultado = resultado;
        return this;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Integer getNumeroDeGoles() {
        return numeroDeGoles;
    }

    public Dato numeroDeGoles(Integer numeroDeGoles) {
        this.numeroDeGoles = numeroDeGoles;
        return this;
    }

    public void setNumeroDeGoles(Integer numeroDeGoles) {
        this.numeroDeGoles = numeroDeGoles;
    }

    public Integer getCorner() {
        return corner;
    }

    public Dato corner(Integer corner) {
        this.corner = corner;
        return this;
    }

    public void setCorner(Integer corner) {
        this.corner = corner;
    }

    public Integer getFaltas() {
        return faltas;
    }

    public Dato faltas(Integer faltas) {
        this.faltas = faltas;
        return this;
    }

    public void setFaltas(Integer faltas) {
        this.faltas = faltas;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public Dato equipo(Equipo equipo) {
        this.equipo = equipo;
        return this;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dato)) {
            return false;
        }
        return id != null && id.equals(((Dato) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dato{" +
            "id=" + getId() +
            ", jornada=" + getJornada() +
            ", rival='" + getRival() + "'" +
            ", resultado='" + getResultado() + "'" +
            ", numeroDeGoles=" + getNumeroDeGoles() +
            ", corner=" + getCorner() +
            ", faltas=" + getFaltas() +
            "}";
    }
}
