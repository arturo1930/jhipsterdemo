package com.conacyt.web.rest;

import com.conacyt.domain.Tarea;
import com.conacyt.repository.TareaRepository;
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
 * REST controller for managing {@link com.conacyt.domain.Tarea}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TareaResource {

    private final Logger log = LoggerFactory.getLogger(TareaResource.class);

    private static final String ENTITY_NAME = "tarea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TareaRepository tareaRepository;

    public TareaResource(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    /**
     * {@code POST  /tareas} : Create a new tarea.
     *
     * @param tarea the tarea to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tarea, or with status {@code 400 (Bad Request)} if the tarea has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tareas")
    public Mono<ResponseEntity<Tarea>> createTarea(@Valid @RequestBody Tarea tarea) throws URISyntaxException {
        log.debug("REST request to save Tarea : {}", tarea);
        if (tarea.getId() != null) {
            throw new BadRequestAlertException("A new tarea cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return tareaRepository
            .save(tarea)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/tareas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /tareas/:id} : Updates an existing tarea.
     *
     * @param id the id of the tarea to save.
     * @param tarea the tarea to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarea,
     * or with status {@code 400 (Bad Request)} if the tarea is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tarea couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tareas/{id}")
    public Mono<ResponseEntity<Tarea>> updateTarea(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Tarea tarea
    ) throws URISyntaxException {
        log.debug("REST request to update Tarea : {}, {}", id, tarea);
        if (tarea.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarea.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tareaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return tareaRepository
                    .save(tarea)
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
     * {@code PATCH  /tareas/:id} : Partial updates given fields of an existing tarea, field will ignore if it is null
     *
     * @param id the id of the tarea to save.
     * @param tarea the tarea to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarea,
     * or with status {@code 400 (Bad Request)} if the tarea is not valid,
     * or with status {@code 404 (Not Found)} if the tarea is not found,
     * or with status {@code 500 (Internal Server Error)} if the tarea couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tareas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Tarea>> partialUpdateTarea(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tarea tarea
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tarea partially : {}, {}", id, tarea);
        if (tarea.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarea.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tareaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Tarea> result = tareaRepository
                    .findById(tarea.getId())
                    .map(existingTarea -> {
                        if (tarea.getNombre() != null) {
                            existingTarea.setNombre(tarea.getNombre());
                        }
                        if (tarea.getActividades() != null) {
                            existingTarea.setActividades(tarea.getActividades());
                        }
                        if (tarea.getFechaCreacion() != null) {
                            existingTarea.setFechaCreacion(tarea.getFechaCreacion());
                        }

                        return existingTarea;
                    })
                    .flatMap(tareaRepository::save);

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
     * {@code GET  /tareas} : get all the tareas.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tareas in body.
     */
    @GetMapping("/tareas")
    public Mono<ResponseEntity<List<Tarea>>> getAllTareas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Tareas");
        return tareaRepository
            .count()
            .zipWith(tareaRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /tareas/:id} : get the "id" tarea.
     *
     * @param id the id of the tarea to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tarea, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tareas/{id}")
    public Mono<ResponseEntity<Tarea>> getTarea(@PathVariable Long id) {
        log.debug("REST request to get Tarea : {}", id);
        Mono<Tarea> tarea = tareaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tarea);
    }

    /**
     * {@code DELETE  /tareas/:id} : delete the "id" tarea.
     *
     * @param id the id of the tarea to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tareas/{id}")
    public Mono<ResponseEntity<Void>> deleteTarea(@PathVariable Long id) {
        log.debug("REST request to delete Tarea : {}", id);
        return tareaRepository
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
