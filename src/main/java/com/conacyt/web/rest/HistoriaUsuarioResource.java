package com.conacyt.web.rest;

import com.conacyt.domain.HistoriaUsuario;
import com.conacyt.repository.HistoriaUsuarioRepository;
import com.conacyt.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.conacyt.domain.HistoriaUsuario}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HistoriaUsuarioResource {

    private final Logger log = LoggerFactory.getLogger(HistoriaUsuarioResource.class);

    private static final String ENTITY_NAME = "historiaUsuario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriaUsuarioRepository historiaUsuarioRepository;

    public HistoriaUsuarioResource(HistoriaUsuarioRepository historiaUsuarioRepository) {
        this.historiaUsuarioRepository = historiaUsuarioRepository;
    }

    /**
     * {@code POST  /historia-usuarios} : Create a new historiaUsuario.
     *
     * @param historiaUsuario the historiaUsuario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiaUsuario, or with status {@code 400 (Bad Request)} if the historiaUsuario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/historia-usuarios")
    public Mono<ResponseEntity<HistoriaUsuario>> createHistoriaUsuario(@Valid @RequestBody HistoriaUsuario historiaUsuario)
        throws URISyntaxException {
        log.debug("REST request to save HistoriaUsuario : {}", historiaUsuario);
        if (historiaUsuario.getId() != null) {
            throw new BadRequestAlertException("A new historiaUsuario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return historiaUsuarioRepository
            .save(historiaUsuario)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/historia-usuarios/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /historia-usuarios/:id} : Updates an existing historiaUsuario.
     *
     * @param id the id of the historiaUsuario to save.
     * @param historiaUsuario the historiaUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiaUsuario,
     * or with status {@code 400 (Bad Request)} if the historiaUsuario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiaUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/historia-usuarios/{id}")
    public Mono<ResponseEntity<HistoriaUsuario>> updateHistoriaUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistoriaUsuario historiaUsuario
    ) throws URISyntaxException {
        log.debug("REST request to update HistoriaUsuario : {}, {}", id, historiaUsuario);
        if (historiaUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiaUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return historiaUsuarioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return historiaUsuarioRepository
                    .save(historiaUsuario)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /historia-usuarios/:id} : Partial updates given fields of an existing historiaUsuario, field will ignore if it is null
     *
     * @param id the id of the historiaUsuario to save.
     * @param historiaUsuario the historiaUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiaUsuario,
     * or with status {@code 400 (Bad Request)} if the historiaUsuario is not valid,
     * or with status {@code 404 (Not Found)} if the historiaUsuario is not found,
     * or with status {@code 500 (Internal Server Error)} if the historiaUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/historia-usuarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HistoriaUsuario>> partialUpdateHistoriaUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistoriaUsuario historiaUsuario
    ) throws URISyntaxException {
        log.debug("REST request to partial update HistoriaUsuario partially : {}, {}", id, historiaUsuario);
        if (historiaUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiaUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return historiaUsuarioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HistoriaUsuario> result = historiaUsuarioRepository
                    .findById(historiaUsuario.getId())
                    .map(existingHistoriaUsuario -> {
                        if (historiaUsuario.getNombre() != null) {
                            existingHistoriaUsuario.setNombre(historiaUsuario.getNombre());
                        }
                        if (historiaUsuario.getDescripcion() != null) {
                            existingHistoriaUsuario.setDescripcion(historiaUsuario.getDescripcion());
                        }
                        if (historiaUsuario.getCriteriosAceptacion() != null) {
                            existingHistoriaUsuario.setCriteriosAceptacion(historiaUsuario.getCriteriosAceptacion());
                        }

                        return existingHistoriaUsuario;
                    })
                    .flatMap(historiaUsuarioRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /historia-usuarios} : get all the historiaUsuarios.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiaUsuarios in body.
     */
    @GetMapping("/historia-usuarios")
    public Mono<ResponseEntity<List<HistoriaUsuario>>> getAllHistoriaUsuarios(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of HistoriaUsuarios");
        return historiaUsuarioRepository
            .count()
            .zipWith(historiaUsuarioRepository.findAllBy(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /historia-usuarios/:id} : get the "id" historiaUsuario.
     *
     * @param id the id of the historiaUsuario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiaUsuario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historia-usuarios/{id}")
    public Mono<ResponseEntity<HistoriaUsuario>> getHistoriaUsuario(@PathVariable Long id) {
        log.debug("REST request to get HistoriaUsuario : {}", id);
        Mono<HistoriaUsuario> historiaUsuario = historiaUsuarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(historiaUsuario);
    }

    /**
     * {@code DELETE  /historia-usuarios/:id} : delete the "id" historiaUsuario.
     *
     * @param id the id of the historiaUsuario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/historia-usuarios/{id}")
    public Mono<ResponseEntity<Void>> deleteHistoriaUsuario(@PathVariable Long id) {
        log.debug("REST request to delete HistoriaUsuario : {}", id);
        return historiaUsuarioRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
