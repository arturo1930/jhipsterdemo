package com.conacyt.repository.rowmapper;

import com.conacyt.domain.HistoriaUsuario;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HistoriaUsuario}, with proper type conversions.
 */
@Service
public class HistoriaUsuarioRowMapper implements BiFunction<Row, String, HistoriaUsuario> {

    private final ColumnConverter converter;

    public HistoriaUsuarioRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HistoriaUsuario} stored in the database.
     */
    @Override
    public HistoriaUsuario apply(Row row, String prefix) {
        HistoriaUsuario entity = new HistoriaUsuario();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setDescripcion(converter.fromRow(row, prefix + "_descripcion", String.class));
        entity.setCriteriosAceptacion(converter.fromRow(row, prefix + "_criterios_aceptacion", Integer.class));
        return entity;
    }
}
