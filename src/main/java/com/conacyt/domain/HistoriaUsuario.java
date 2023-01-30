package com.conacyt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A HistoriaUsuario.
 */
@Table("historia_usuario")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriaUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("descripcion")
    private String descripcion;

    @NotNull(message = "must not be null")
    @Column("criterios_aceptacion")
    private Integer criteriosAceptacion;

    @Transient
    @JsonIgnoreProperties(value = { "historiaUsuario" }, allowSetters = true)
    private Set<Tarea> tareas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoriaUsuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public HistoriaUsuario nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public HistoriaUsuario descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCriteriosAceptacion() {
        return this.criteriosAceptacion;
    }

    public HistoriaUsuario criteriosAceptacion(Integer criteriosAceptacion) {
        this.setCriteriosAceptacion(criteriosAceptacion);
        return this;
    }

    public void setCriteriosAceptacion(Integer criteriosAceptacion) {
        this.criteriosAceptacion = criteriosAceptacion;
    }

    public Set<Tarea> getTareas() {
        return this.tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
        if (this.tareas != null) {
            this.tareas.forEach(i -> i.setHistoriaUsuario(null));
        }
        if (tareas != null) {
            tareas.forEach(i -> i.setHistoriaUsuario(this));
        }
        this.tareas = tareas;
    }

    public HistoriaUsuario tareas(Set<Tarea> tareas) {
        this.setTareas(tareas);
        return this;
    }

    public HistoriaUsuario addTarea(Tarea tarea) {
        this.tareas.add(tarea);
        tarea.setHistoriaUsuario(this);
        return this;
    }

    public HistoriaUsuario removeTarea(Tarea tarea) {
        this.tareas.remove(tarea);
        tarea.setHistoriaUsuario(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriaUsuario)) {
            return false;
        }
        return id != null && id.equals(((HistoriaUsuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriaUsuario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", criteriosAceptacion=" + getCriteriosAceptacion() +
            "}";
    }
}
