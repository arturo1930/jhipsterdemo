package com.conacyt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Tarea.
 */
@Table("tarea")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tarea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @Column("actividades")
    private String actividades;

    @Column("fecha_creacion")
    private Instant fechaCreacion;

    @Transient
    @JsonIgnoreProperties(value = { "tareas" }, allowSetters = true)
    private HistoriaUsuario historiaUsuario;

    @Column("historia_usuario_id")
    private Long historiaUsuarioId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tarea id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Tarea nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getActividades() {
        return this.actividades;
    }

    public Tarea actividades(String actividades) {
        this.setActividades(actividades);
        return this;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Tarea fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public HistoriaUsuario getHistoriaUsuario() {
        return this.historiaUsuario;
    }

    public void setHistoriaUsuario(HistoriaUsuario historiaUsuario) {
        this.historiaUsuario = historiaUsuario;
        this.historiaUsuarioId = historiaUsuario != null ? historiaUsuario.getId() : null;
    }

    public Tarea historiaUsuario(HistoriaUsuario historiaUsuario) {
        this.setHistoriaUsuario(historiaUsuario);
        return this;
    }

    public Long getHistoriaUsuarioId() {
        return this.historiaUsuarioId;
    }

    public void setHistoriaUsuarioId(Long historiaUsuario) {
        this.historiaUsuarioId = historiaUsuario;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tarea)) {
            return false;
        }
        return id != null && id.equals(((Tarea) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tarea{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", actividades='" + getActividades() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            "}";
    }
}
