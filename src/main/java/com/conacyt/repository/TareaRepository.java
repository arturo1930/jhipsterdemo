package com.conacyt.repository;

import com.conacyt.domain.Tarea;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Tarea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TareaRepository extends ReactiveCrudRepository<Tarea, Long>, TareaRepositoryInternal {
    Flux<Tarea> findAllBy(Pageable pageable);

    @Query("SELECT * FROM tarea entity WHERE entity.historia_usuario_id = :id")
    Flux<Tarea> findByHistoriaUsuario(Long id);

    @Query("SELECT * FROM tarea entity WHERE entity.historia_usuario_id IS NULL")
    Flux<Tarea> findAllWhereHistoriaUsuarioIsNull();

    @Override
    <S extends Tarea> Mono<S> save(S entity);

    @Override
    Flux<Tarea> findAll();

    @Override
    Mono<Tarea> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TareaRepositoryInternal {
    <S extends Tarea> Mono<S> save(S entity);

    Flux<Tarea> findAllBy(Pageable pageable);

    Flux<Tarea> findAll();

    Mono<Tarea> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Tarea> findAllBy(Pageable pageable, Criteria criteria);

}
