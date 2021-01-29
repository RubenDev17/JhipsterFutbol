package com.cev.futbol.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Presidente.
 */
@Entity
@Table(name = "presidente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "presidente")
public class Presidente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "nombre", length = 20, nullable = false)
    private String nombre;

    @Column(name = "anios_en_presidencia")
    private Integer aniosEnPresidencia;

    @OneToOne
    @JoinColumn(unique = true)
    private Equipo equipo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Presidente nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAniosEnPresidencia() {
        return aniosEnPresidencia;
    }

    public Presidente aniosEnPresidencia(Integer aniosEnPresidencia) {
        this.aniosEnPresidencia = aniosEnPresidencia;
        return this;
    }

    public void setAniosEnPresidencia(Integer aniosEnPresidencia) {
        this.aniosEnPresidencia = aniosEnPresidencia;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public Presidente equipo(Equipo equipo) {
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
        if (!(o instanceof Presidente)) {
            return false;
        }
        return id != null && id.equals(((Presidente) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Presidente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", aniosEnPresidencia=" + getAniosEnPresidencia() +
            "}";
    }
}
