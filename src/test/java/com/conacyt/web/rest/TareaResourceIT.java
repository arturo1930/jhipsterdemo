package com.conacyt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.conacyt.IntegrationTest;
import com.conacyt.domain.Tarea;
import com.conacyt.repository.EntityManager;
import com.conacyt.repository.TareaRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link TareaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TareaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVIDADES = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVIDADES = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tareas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Tarea tarea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarea createEntity(EntityManager em) {
        Tarea tarea = new Tarea().nombre(DEFAULT_NOMBRE).actividades(DEFAULT_ACTIVIDADES).fechaCreacion(DEFAULT_FECHA_CREACION);
        return tarea;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarea createUpdatedEntity(EntityManager em) {
        Tarea tarea = new Tarea().nombre(UPDATED_NOMBRE).actividades(UPDATED_ACTIVIDADES).fechaCreacion(UPDATED_FECHA_CREACION);
        return tarea;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Tarea.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        tarea = createEntity(em);
    }

    @Test
    void createTarea() throws Exception {
        int databaseSizeBeforeCreate = tareaRepository.findAll().collectList().block().size();
        // Create the Tarea
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarea))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeCreate + 1);
        Tarea testTarea = tareaList.get(tareaList.size() - 1);
        assertThat(testTarea.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTarea.getActividades()).isEqualTo(DEFAULT_ACTIVIDADES);
        assertThat(testTarea.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
    }

    @Test
    void createTareaWithExistingId() throws Exception {
        // Create the Tarea with an existing ID
        tarea.setId(1L);

        int databaseSizeBeforeCreate = tareaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarea))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tareaRepository.findAll().collectList().block().size();
        // set the field null
        tarea.setNombre(null);

        // Create the Tarea, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarea))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTareas() {
        // Initialize the database
        tareaRepository.save(tarea).block();

        // Get all the tareaList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(tarea.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].actividades")
            .value(hasItem(DEFAULT_ACTIVIDADES))
            .jsonPath("$.[*].fechaCreacion")
            .value(hasItem(DEFAULT_FECHA_CREACION.toString()));
    }

    @Test
    void getTarea() {
        // Initialize the database
        tareaRepository.save(tarea).block();

        // Get the tarea
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tarea.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tarea.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.actividades")
            .value(is(DEFAULT_ACTIVIDADES))
            .jsonPath("$.fechaCreacion")
            .value(is(DEFAULT_FECHA_CREACION.toString()));
    }

    @Test
    void getNonExistingTarea() {
        // Get the tarea
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTarea() throws Exception {
        // Initialize the database
        tareaRepository.save(tarea).block();

        int databaseSizeBeforeUpdate = tareaRepository.findAll().collectList().block().size();

        // Update the tarea
        Tarea updatedTarea = tareaRepository.findById(tarea.getId()).block();
        updatedTarea.nombre(UPDATED_NOMBRE).actividades(UPDATED_ACTIVIDADES).fechaCreacion(UPDATED_FECHA_CREACION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTarea.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedTarea))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
        Tarea testTarea = tareaList.get(tareaList.size() - 1);
        assertThat(testTarea.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTarea.getActividades()).isEqualTo(UPDATED_ACTIVIDADES);
        assertThat(testTarea.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
    }

    @Test
    void putNonExistingTarea() throws Exception {
        int databaseSizeBeforeUpdate = tareaRepository.findAll().collectList().block().size();
        tarea.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tarea.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarea))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTarea() throws Exception {
        int databaseSizeBeforeUpdate = tareaRepository.findAll().collectList().block().size();
        tarea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarea))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTarea() throws Exception {
        int databaseSizeBeforeUpdate = tareaRepository.findAll().collectList().block().size();
        tarea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarea))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTareaWithPatch() throws Exception {
        // Initialize the database
        tareaRepository.save(tarea).block();

        int databaseSizeBeforeUpdate = tareaRepository.findAll().collectList().block().size();

        // Update the tarea using partial update
        Tarea partialUpdatedTarea = new Tarea();
        partialUpdatedTarea.setId(tarea.getId());

        partialUpdatedTarea.fechaCreacion(UPDATED_FECHA_CREACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTarea.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTarea))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
        Tarea testTarea = tareaList.get(tareaList.size() - 1);
        assertThat(testTarea.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTarea.getActividades()).isEqualTo(DEFAULT_ACTIVIDADES);
        assertThat(testTarea.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
    }

    @Test
    void fullUpdateTareaWithPatch() throws Exception {
        // Initialize the database
        tareaRepository.save(tarea).block();

        int databaseSizeBeforeUpdate = tareaRepository.findAll().collectList().block().size();

        // Update the tarea using partial update
        Tarea partialUpdatedTarea = new Tarea();
        partialUpdatedTarea.setId(tarea.getId());

        partialUpdatedTarea.nombre(UPDATED_NOMBRE).actividades(UPDATED_ACTIVIDADES).fechaCreacion(UPDATED_FECHA_CREACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTarea.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTarea))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
        Tarea testTarea = tareaList.get(tareaList.size() - 1);
        assertThat(testTarea.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTarea.getActividades()).isEqualTo(UPDATED_ACTIVIDADES);
        assertThat(testTarea.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
    }

    @Test
    void patchNonExistingTarea() throws Exception {
        int databaseSizeBeforeUpdate = tareaRepository.findAll().collectList().block().size();
        tarea.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tarea.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarea))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTarea() throws Exception {
        int databaseSizeBeforeUpdate = tareaRepository.findAll().collectList().block().size();
        tarea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarea))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTarea() throws Exception {
        int databaseSizeBeforeUpdate = tareaRepository.findAll().collectList().block().size();
        tarea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarea))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTarea() {
        // Initialize the database
        tareaRepository.save(tarea).block();

        int databaseSizeBeforeDelete = tareaRepository.findAll().collectList().block().size();

        // Delete the tarea
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tarea.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Tarea> tareaList = tareaRepository.findAll().collectList().block();
        assertThat(tareaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
