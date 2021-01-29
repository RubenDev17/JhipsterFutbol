package com.cev.futbol.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Partido.
 */
@Entity
@Table(name = "partido")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "partido")
public class Partido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jornada")
    private Integer jornada;

    @Column(name = "fecha")
    private Instant fecha;

    @Size(min = 3, max = 20)
    @Column(name = "rival", length = 20)
    private String rival;

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

    public Partido jornada(Integer jornada) {
        this.jornada = jornada;
        return this;
    }

    public void setJornada(Integer jornada) {
        this.jornada = jornada;
    }

    public Instant getFecha() {
        return fecha;
    }

    public Partido fecha(Instant fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getRival() {
        return rival;
    }

    public Partido rival(String rival) {
        this.rival = rival;
        return this;
    }

    public void setRival(String rival) {
        this.rival = rival;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Partido)) {
            return false;
        }
        return id != null && id.equals(((Partido) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Partido{" +
            "id=" + getId() +
            ", jornada=" + getJornada() +
            ", fecha='" + getFecha() + "'" +
            ", rival='" + getRival() + "'" +
            "}";
    }
}
