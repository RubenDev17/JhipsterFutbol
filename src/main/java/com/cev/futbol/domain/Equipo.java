package com.cev.futbol.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Equipo.
 */
@Entity
@Table(name = "equipo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "equipo")
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "nombre", length = 20, nullable = false, unique = true)
    private String nombre;

    @Column(name = "titulos")
    private Integer titulos;

    @Column(name = "fecha_de_fundacion")
    private Instant fechaDeFundacion;

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

    public Equipo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTitulos() {
        return titulos;
    }

    public Equipo titulos(Integer titulos) {
        this.titulos = titulos;
        return this;
    }

    public void setTitulos(Integer titulos) {
        this.titulos = titulos;
    }

    public Instant getFechaDeFundacion() {
        return fechaDeFundacion;
    }

    public Equipo fechaDeFundacion(Instant fechaDeFundacion) {
        this.fechaDeFundacion = fechaDeFundacion;
        return this;
    }

    public void setFechaDeFundacion(Instant fechaDeFundacion) {
        this.fechaDeFundacion = fechaDeFundacion;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Equipo)) {
            return false;
        }
        return id != null && id.equals(((Equipo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Equipo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", titulos=" + getTitulos() +
            ", fechaDeFundacion='" + getFechaDeFundacion() + "'" +
            "}";
    }
}
