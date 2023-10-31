package prog2.sarmiento.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import prog2.sarmiento.IntegrationTest;
import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.EstadoOrden;
import prog2.sarmiento.domain.enumeration.ModoOrden;
import prog2.sarmiento.domain.enumeration.Operacion;
import prog2.sarmiento.repository.OrdenRepository;

/**
 * Integration tests for the {@link OrdenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdenResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final Long DEFAULT_CLIENTE = 1L;
    private static final Long UPDATED_CLIENTE = 2L;

    private static final Long DEFAULT_ACCION_ID = 1L;
    private static final Long UPDATED_ACCION_ID = 2L;

    private static final String DEFAULT_ACCION = "AAAAAAAAAA";
    private static final String UPDATED_ACCION = "BBBBBBBBBB";

    private static final Operacion DEFAULT_OPERACION = Operacion.COMPRA;
    private static final Operacion UPDATED_OPERACION = Operacion.VENTA;

    private static final Long DEFAULT_PRECIO = 1L;
    private static final Long UPDATED_PRECIO = 2L;

    private static final Long DEFAULT_CANTIDAD = 1L;
    private static final Long UPDATED_CANTIDAD = 2L;

    private static final String DEFAULT_FECHA_OPERACION = "AAAAAAAAAA";
    private static final String UPDATED_FECHA_OPERACION = "BBBBBBBBBB";

    private static final ModoOrden DEFAULT_MODO = ModoOrden.AHORA;
    private static final ModoOrden UPDATED_MODO = ModoOrden.FINDIA;

    private static final EstadoOrden DEFAULT_ESTADO = EstadoOrden.PENDIENTE;
    private static final EstadoOrden UPDATED_ESTADO = EstadoOrden.EXITOSA;

    private static final String ENTITY_API_URL = "/api/ordens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdenMockMvc;

    private Orden orden;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orden createEntity(EntityManager em) {
        Orden orden = new Orden()
            .numero(DEFAULT_NUMERO)
            .cliente(DEFAULT_CLIENTE)
            .accionId(DEFAULT_ACCION_ID)
            .accion(DEFAULT_ACCION)
            .operacion(DEFAULT_OPERACION)
            .precio(DEFAULT_PRECIO)
            .cantidad(DEFAULT_CANTIDAD)
            .fechaOperacion(DEFAULT_FECHA_OPERACION)
            .modo(DEFAULT_MODO)
            .estado(DEFAULT_ESTADO);
        return orden;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orden createUpdatedEntity(EntityManager em) {
        Orden orden = new Orden()
            .numero(UPDATED_NUMERO)
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .operacion(UPDATED_OPERACION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO)
            .estado(UPDATED_ESTADO);
        return orden;
    }

    @BeforeEach
    public void initTest() {
        orden = createEntity(em);
    }

    @Test
    @Transactional
    void createOrden() throws Exception {
        int databaseSizeBeforeCreate = ordenRepository.findAll().size();
        // Create the Orden
        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isCreated());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeCreate + 1);
        Orden testOrden = ordenList.get(ordenList.size() - 1);
        assertThat(testOrden.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testOrden.getCliente()).isEqualTo(DEFAULT_CLIENTE);
        assertThat(testOrden.getAccionId()).isEqualTo(DEFAULT_ACCION_ID);
        assertThat(testOrden.getAccion()).isEqualTo(DEFAULT_ACCION);
        assertThat(testOrden.getOperacion()).isEqualTo(DEFAULT_OPERACION);
        assertThat(testOrden.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testOrden.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testOrden.getFechaOperacion()).isEqualTo(DEFAULT_FECHA_OPERACION);
        assertThat(testOrden.getModo()).isEqualTo(DEFAULT_MODO);
        assertThat(testOrden.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createOrdenWithExistingId() throws Exception {
        // Create the Orden with an existing ID
        orden.setId(1L);

        int databaseSizeBeforeCreate = ordenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setNumero(null);

        // Create the Orden, which fails.

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClienteIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setCliente(null);

        // Create the Orden, which fails.

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setAccionId(null);

        // Create the Orden, which fails.

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setAccion(null);

        // Create the Orden, which fails.

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOperacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setOperacion(null);

        // Create the Orden, which fails.

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setPrecio(null);

        // Create the Orden, which fails.

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setCantidad(null);

        // Create the Orden, which fails.

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaOperacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setFechaOperacion(null);

        // Create the Orden, which fails.

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setModo(null);

        // Create the Orden, which fails.

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setEstado(null);

        // Create the Orden, which fails.

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrdens() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList
        restOrdenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orden.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].cliente").value(hasItem(DEFAULT_CLIENTE.intValue())))
            .andExpect(jsonPath("$.[*].accionId").value(hasItem(DEFAULT_ACCION_ID.intValue())))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION)))
            .andExpect(jsonPath("$.[*].operacion").value(hasItem(DEFAULT_OPERACION.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.intValue())))
            .andExpect(jsonPath("$.[*].fechaOperacion").value(hasItem(DEFAULT_FECHA_OPERACION)))
            .andExpect(jsonPath("$.[*].modo").value(hasItem(DEFAULT_MODO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    void getOrden() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get the orden
        restOrdenMockMvc
            .perform(get(ENTITY_API_URL_ID, orden.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orden.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.cliente").value(DEFAULT_CLIENTE.intValue()))
            .andExpect(jsonPath("$.accionId").value(DEFAULT_ACCION_ID.intValue()))
            .andExpect(jsonPath("$.accion").value(DEFAULT_ACCION))
            .andExpect(jsonPath("$.operacion").value(DEFAULT_OPERACION.toString()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.intValue()))
            .andExpect(jsonPath("$.fechaOperacion").value(DEFAULT_FECHA_OPERACION))
            .andExpect(jsonPath("$.modo").value(DEFAULT_MODO.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrden() throws Exception {
        // Get the orden
        restOrdenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrden() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();

        // Update the orden
        Orden updatedOrden = ordenRepository.findById(orden.getId()).get();
        // Disconnect from session so that the updates on updatedOrden are not directly saved in db
        em.detach(updatedOrden);
        updatedOrden
            .numero(UPDATED_NUMERO)
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .operacion(UPDATED_OPERACION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO)
            .estado(UPDATED_ESTADO);

        restOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrden.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrden))
            )
            .andExpect(status().isOk());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
        Orden testOrden = ordenList.get(ordenList.size() - 1);
        assertThat(testOrden.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testOrden.getCliente()).isEqualTo(UPDATED_CLIENTE);
        assertThat(testOrden.getAccionId()).isEqualTo(UPDATED_ACCION_ID);
        assertThat(testOrden.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testOrden.getOperacion()).isEqualTo(UPDATED_OPERACION);
        assertThat(testOrden.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testOrden.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testOrden.getFechaOperacion()).isEqualTo(UPDATED_FECHA_OPERACION);
        assertThat(testOrden.getModo()).isEqualTo(UPDATED_MODO);
        assertThat(testOrden.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orden.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orden))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orden))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdenWithPatch() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();

        // Update the orden using partial update
        Orden partialUpdatedOrden = new Orden();
        partialUpdatedOrden.setId(orden.getId());

        partialUpdatedOrden
            .accion(UPDATED_ACCION)
            .operacion(UPDATED_OPERACION)
            .precio(UPDATED_PRECIO)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .estado(UPDATED_ESTADO);

        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrden.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrden))
            )
            .andExpect(status().isOk());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
        Orden testOrden = ordenList.get(ordenList.size() - 1);
        assertThat(testOrden.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testOrden.getCliente()).isEqualTo(DEFAULT_CLIENTE);
        assertThat(testOrden.getAccionId()).isEqualTo(DEFAULT_ACCION_ID);
        assertThat(testOrden.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testOrden.getOperacion()).isEqualTo(UPDATED_OPERACION);
        assertThat(testOrden.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testOrden.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testOrden.getFechaOperacion()).isEqualTo(UPDATED_FECHA_OPERACION);
        assertThat(testOrden.getModo()).isEqualTo(DEFAULT_MODO);
        assertThat(testOrden.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateOrdenWithPatch() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();

        // Update the orden using partial update
        Orden partialUpdatedOrden = new Orden();
        partialUpdatedOrden.setId(orden.getId());

        partialUpdatedOrden
            .numero(UPDATED_NUMERO)
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .operacion(UPDATED_OPERACION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO)
            .estado(UPDATED_ESTADO);

        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrden.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrden))
            )
            .andExpect(status().isOk());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
        Orden testOrden = ordenList.get(ordenList.size() - 1);
        assertThat(testOrden.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testOrden.getCliente()).isEqualTo(UPDATED_CLIENTE);
        assertThat(testOrden.getAccionId()).isEqualTo(UPDATED_ACCION_ID);
        assertThat(testOrden.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testOrden.getOperacion()).isEqualTo(UPDATED_OPERACION);
        assertThat(testOrden.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testOrden.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testOrden.getFechaOperacion()).isEqualTo(UPDATED_FECHA_OPERACION);
        assertThat(testOrden.getModo()).isEqualTo(UPDATED_MODO);
        assertThat(testOrden.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orden.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orden))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orden))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orden)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrden() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        int databaseSizeBeforeDelete = ordenRepository.findAll().size();

        // Delete the orden
        restOrdenMockMvc
            .perform(delete(ENTITY_API_URL_ID, orden.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
