package com.conacyt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.conacyt.IntegrationTest;
import com.conacyt.domain.HistoriaUsuario;
import com.conacyt.repository.EntityManager;
import com.conacyt.repository.HistoriaUsuarioRepository;
import java.time.Duration;
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
 * Integration tests for the {@link HistoriaUsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HistoriaUsuarioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CRITERIOS_ACEPTACION = 1;
    private static final Integer UPDATED_CRITERIOS_ACEPTACION = 2;

    private static final String ENTITY_API_URL = "/api/historia-usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoriaUsuarioRepository historiaUsuarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HistoriaUsuario historiaUsuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriaUsuario createEntity(EntityManager em) {
        HistoriaUsuario historiaUsuario = new HistoriaUsuario()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .criteriosAceptacion(DEFAULT_CRITERIOS_ACEPTACION);
        return historiaUsuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriaUsuario createUpdatedEntity(EntityManager em) {
        HistoriaUsuario historiaUsuario = new HistoriaUsuario()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .criteriosAceptacion(UPDATED_CRITERIOS_ACEPTACION);
        return historiaUsuario;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(HistoriaUsuario.class).block();
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
        historiaUsuario = createEntity(em);
    }

    @Test
    void createHistoriaUsuario() throws Exception {
        int databaseSizeBeforeCreate = historiaUsuarioRepository.findAll().collectList().block().size();
        // Create the HistoriaUsuario
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiaUsuario))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the HistoriaUsuario in the database
        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeCreate + 1);
        HistoriaUsuario testHistoriaUsuario = historiaUsuarioList.get(historiaUsuarioList.size() - 1);
        assertThat(testHistoriaUsuario.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testHistoriaUsuario.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testHistoriaUsuario.getCriteriosAceptacion()).isEqualTo(DEFAULT_CRITERIOS_ACEPTACION);
    }

    @Test
    void createHistoriaUsuarioWithExistingId() throws Exception {
        // Create the HistoriaUsuario with an existing ID
        historiaUsuario.setId(1L);

        int databaseSizeBeforeCreate = historiaUsuarioRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiaUsuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistoriaUsuario in the database
        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiaUsuarioRepository.findAll().collectList().block().size();
        // set the field null
        historiaUsuario.setNombre(null);

        // Create the HistoriaUsuario, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiaUsuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiaUsuarioRepository.findAll().collectList().block().size();
        // set the field null
        historiaUsuario.setDescripcion(null);

        // Create the HistoriaUsuario, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiaUsuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCriteriosAceptacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiaUsuarioRepository.findAll().collectList().block().size();
        // set the field null
        historiaUsuario.setCriteriosAceptacion(null);

        // Create the HistoriaUsuario, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiaUsuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllHistoriaUsuarios() {
        // Initialize the database
        historiaUsuarioRepository.save(historiaUsuario).block();

        // Get all the historiaUsuarioList
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
            .value(hasItem(historiaUsuario.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].descripcion")
            .value(hasItem(DEFAULT_DESCRIPCION))
            .jsonPath("$.[*].criteriosAceptacion")
            .value(hasItem(DEFAULT_CRITERIOS_ACEPTACION));
    }

    @Test
    void getHistoriaUsuario() {
        // Initialize the database
        historiaUsuarioRepository.save(historiaUsuario).block();

        // Get the historiaUsuario
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, historiaUsuario.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(historiaUsuario.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.descripcion")
            .value(is(DEFAULT_DESCRIPCION))
            .jsonPath("$.criteriosAceptacion")
            .value(is(DEFAULT_CRITERIOS_ACEPTACION));
    }

    @Test
    void getNonExistingHistoriaUsuario() {
        // Get the historiaUsuario
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHistoriaUsuario() throws Exception {
        // Initialize the database
        historiaUsuarioRepository.save(historiaUsuario).block();

        int databaseSizeBeforeUpdate = historiaUsuarioRepository.findAll().collectList().block().size();

        // Update the historiaUsuario
        HistoriaUsuario updatedHistoriaUsuario = historiaUsuarioRepository.findById(historiaUsuario.getId()).block();
        updatedHistoriaUsuario.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).criteriosAceptacion(UPDATED_CRITERIOS_ACEPTACION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedHistoriaUsuario.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedHistoriaUsuario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HistoriaUsuario in the database
        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeUpdate);
        HistoriaUsuario testHistoriaUsuario = historiaUsuarioList.get(historiaUsuarioList.size() - 1);
        assertThat(testHistoriaUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testHistoriaUsuario.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testHistoriaUsuario.getCriteriosAceptacion()).isEqualTo(UPDATED_CRITERIOS_ACEPTACION);
    }

    @Test
    void putNonExistingHistoriaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = historiaUsuarioRepository.findAll().collectList().block().size();
        historiaUsuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, historiaUsuario.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiaUsuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistoriaUsuario in the database
        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHistoriaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = historiaUsuarioRepository.findAll().collectList().block().size();
        historiaUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiaUsuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistoriaUsuario in the database
        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHistoriaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = historiaUsuarioRepository.findAll().collectList().block().size();
        historiaUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiaUsuario))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HistoriaUsuario in the database
        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHistoriaUsuarioWithPatch() throws Exception {
        // Initialize the database
        historiaUsuarioRepository.save(historiaUsuario).block();

        int databaseSizeBeforeUpdate = historiaUsuarioRepository.findAll().collectList().block().size();

        // Update the historiaUsuario using partial update
        HistoriaUsuario partialUpdatedHistoriaUsuario = new HistoriaUsuario();
        partialUpdatedHistoriaUsuario.setId(historiaUsuario.getId());

        partialUpdatedHistoriaUsuario.nombre(UPDATED_NOMBRE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHistoriaUsuario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoriaUsuario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HistoriaUsuario in the database
        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeUpdate);
        HistoriaUsuario testHistoriaUsuario = historiaUsuarioList.get(historiaUsuarioList.size() - 1);
        assertThat(testHistoriaUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testHistoriaUsuario.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testHistoriaUsuario.getCriteriosAceptacion()).isEqualTo(DEFAULT_CRITERIOS_ACEPTACION);
    }

    @Test
    void fullUpdateHistoriaUsuarioWithPatch() throws Exception {
        // Initialize the database
        historiaUsuarioRepository.save(historiaUsuario).block();

        int databaseSizeBeforeUpdate = historiaUsuarioRepository.findAll().collectList().block().size();

        // Update the historiaUsuario using partial update
        HistoriaUsuario partialUpdatedHistoriaUsuario = new HistoriaUsuario();
        partialUpdatedHistoriaUsuario.setId(historiaUsuario.getId());

        partialUpdatedHistoriaUsuario
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .criteriosAceptacion(UPDATED_CRITERIOS_ACEPTACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHistoriaUsuario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoriaUsuario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HistoriaUsuario in the database
        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeUpdate);
        HistoriaUsuario testHistoriaUsuario = historiaUsuarioList.get(historiaUsuarioList.size() - 1);
        assertThat(testHistoriaUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testHistoriaUsuario.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testHistoriaUsuario.getCriteriosAceptacion()).isEqualTo(UPDATED_CRITERIOS_ACEPTACION);
    }

    @Test
    void patchNonExistingHistoriaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = historiaUsuarioRepository.findAll().collectList().block().size();
        historiaUsuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, historiaUsuario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiaUsuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistoriaUsuario in the database
        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHistoriaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = historiaUsuarioRepository.findAll().collectList().block().size();
        historiaUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiaUsuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistoriaUsuario in the database
        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHistoriaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = historiaUsuarioRepository.findAll().collectList().block().size();
        historiaUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiaUsuario))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HistoriaUsuario in the database
        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHistoriaUsuario() {
        // Initialize the database
        historiaUsuarioRepository.save(historiaUsuario).block();

        int databaseSizeBeforeDelete = historiaUsuarioRepository.findAll().collectList().block().size();

        // Delete the historiaUsuario
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, historiaUsuario.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<HistoriaUsuario> historiaUsuarioList = historiaUsuarioRepository.findAll().collectList().block();
        assertThat(historiaUsuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
