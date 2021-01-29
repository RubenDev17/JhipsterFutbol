package com.cev.futbol.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Temporada.
 */
@Entity
@Table(name = "temporada")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "temporada")
public class Temporada implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 10)
    @Column(name = "anio", length = 10, nullable = false, unique = true)
    private String anio;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnio() {
        return anio;
    }

    public Temporada anio(String anio) {
        this.anio = anio;
        return this;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Temporada)) {
            return false;
        }
        return id != null && id.equals(((Temporada) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Temporada{" +
            "id=" + getId() +
            ", anio='" + getAnio() + "'" +
            "}";
    }
}
