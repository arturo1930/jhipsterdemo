package com.conacyt.repository;

import com.conacyt.domain.HistoriaUsuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the HistoriaUsuario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriaUsuarioRepository extends ReactiveCrudRepository<HistoriaUsuario, Long>, HistoriaUsuarioRepositoryInternal {
    Flux<HistoriaUsuario> findAllBy(Pageable pageable);

    @Override
    <S extends HistoriaUsuario> Mono<S> save(S entity);

    @Override
    Flux<HistoriaUsuario> findAll();

    @Override
    Mono<HistoriaUsuario> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface HistoriaUsuarioRepositoryInternal {
    <S extends HistoriaUsuario> Mono<S> save(S entity);

    Flux<HistoriaUsuario> findAllBy(Pageable pageable);

    Flux<HistoriaUsuario> findAll();

    Mono<HistoriaUsuario> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<HistoriaUsuario> findAllBy(Pageable pageable, Criteria criteria);

}
