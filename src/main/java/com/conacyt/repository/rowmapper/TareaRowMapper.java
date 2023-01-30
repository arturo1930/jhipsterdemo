package com.conacyt.repository.rowmapper;

import com.conacyt.domain.Tarea;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Tarea}, with proper type conversions.
 */
@Service
public class TareaRowMapper implements BiFunction<Row, String, Tarea> {

    private final ColumnConverter converter;

    public TareaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Tarea} stored in the database.
     */
    @Override
    public Tarea apply(Row row, String prefix) {
        Tarea entity = new Tarea();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setActividades(converter.fromRow(row, prefix + "_actividades", String.class));
        entity.setFechaCreacion(converter.fromRow(row, prefix + "_fecha_creacion", Instant.class));
        entity.setHistoriaUsuarioId(converter.fromRow(row, prefix + "_historia_usuario_id", Long.class));
        return entity;
    }
}
