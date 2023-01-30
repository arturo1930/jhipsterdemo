package com.conacyt.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.conacyt.domain.Tarea;
import com.conacyt.repository.rowmapper.HistoriaUsuarioRowMapper;
import com.conacyt.repository.rowmapper.TareaRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Tarea entity.
 */
@SuppressWarnings("unused")
class TareaRepositoryInternalImpl extends SimpleR2dbcRepository<Tarea, Long> implements TareaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final HistoriaUsuarioRowMapper historiausuarioMapper;
    private final TareaRowMapper tareaMapper;

    private static final Table entityTable = Table.aliased("tarea", EntityManager.ENTITY_ALIAS);
    private static final Table historiaUsuarioTable = Table.aliased("historia_usuario", "historiaUsuario");

    public TareaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        HistoriaUsuarioRowMapper historiausuarioMapper,
        TareaRowMapper tareaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Tarea.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.historiausuarioMapper = historiausuarioMapper;
        this.tareaMapper = tareaMapper;
    }

    @Override
    public Flux<Tarea> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Tarea> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TareaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(HistoriaUsuarioSqlHelper.getColumns(historiaUsuarioTable, "historiaUsuario"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(historiaUsuarioTable)
            .on(Column.create("historia_usuario_id", entityTable))
            .equals(Column.create("id", historiaUsuarioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Tarea.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Tarea> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Tarea> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Tarea process(Row row, RowMetadata metadata) {
        Tarea entity = tareaMapper.apply(row, "e");
        entity.setHistoriaUsuario(historiausuarioMapper.apply(row, "historiaUsuario"));
        return entity;
    }

    @Override
    public <S extends Tarea> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
