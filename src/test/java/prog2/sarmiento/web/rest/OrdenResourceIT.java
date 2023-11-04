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
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.domain.enumeration.Operacion;
import prog2.sarmiento.repository.OrdenRepository;
import prog2.sarmiento.service.criteria.OrdenCriteria;
import prog2.sarmiento.service.dto.OrdenDTO;
import prog2.sarmiento.service.mapper.OrdenMapper;

/**
 * Integration tests for the {@link OrdenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdenResourceIT {

    private static final Integer DEFAULT_CLIENTE = 1;
    private static final Integer UPDATED_CLIENTE = 2;
    private static final Integer SMALLER_CLIENTE = 1 - 1;

    private static final Integer DEFAULT_ACCION_ID = 1;
    private static final Integer UPDATED_ACCION_ID = 2;
    private static final Integer SMALLER_ACCION_ID = 1 - 1;

    private static final String DEFAULT_ACCION = "AAAAAAAAAA";
    private static final String UPDATED_ACCION = "BBBBBBBBBB";

    private static final Operacion DEFAULT_OPERACION = Operacion.COMPRA;
    private static final Operacion UPDATED_OPERACION = Operacion.VENTA;

    private static final Integer DEFAULT_PRECIO = 1;
    private static final Integer UPDATED_PRECIO = 2;
    private static final Integer SMALLER_PRECIO = 1 - 1;

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final String DEFAULT_FECHA_OPERACION = "AAAAAAAAAA";
    private static final String UPDATED_FECHA_OPERACION = "BBBBBBBBBB";

    private static final Modo DEFAULT_MODO = Modo.AHORA;
    private static final Modo UPDATED_MODO = Modo.FINDIA;

    private static final Estado DEFAULT_ESTADO = Estado.OK;
    private static final Estado UPDATED_ESTADO = Estado.FAIL;

    private static final String DEFAULT_DESCRIPCION_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ordens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private OrdenMapper ordenMapper;

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
            .cliente(DEFAULT_CLIENTE)
            .accionId(DEFAULT_ACCION_ID)
            .accion(DEFAULT_ACCION)
            .operacion(DEFAULT_OPERACION)
            .precio(DEFAULT_PRECIO)
            .cantidad(DEFAULT_CANTIDAD)
            .fechaOperacion(DEFAULT_FECHA_OPERACION)
            .modo(DEFAULT_MODO)
            .estado(DEFAULT_ESTADO)
            .descripcionEstado(DEFAULT_DESCRIPCION_ESTADO);
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
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .operacion(UPDATED_OPERACION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO)
            .estado(UPDATED_ESTADO)
            .descripcionEstado(UPDATED_DESCRIPCION_ESTADO);
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
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);
        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isCreated());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeCreate + 1);
        Orden testOrden = ordenList.get(ordenList.size() - 1);
        assertThat(testOrden.getCliente()).isEqualTo(DEFAULT_CLIENTE);
        assertThat(testOrden.getAccionId()).isEqualTo(DEFAULT_ACCION_ID);
        assertThat(testOrden.getAccion()).isEqualTo(DEFAULT_ACCION);
        assertThat(testOrden.getOperacion()).isEqualTo(DEFAULT_OPERACION);
        assertThat(testOrden.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testOrden.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testOrden.getFechaOperacion()).isEqualTo(DEFAULT_FECHA_OPERACION);
        assertThat(testOrden.getModo()).isEqualTo(DEFAULT_MODO);
        assertThat(testOrden.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testOrden.getDescripcionEstado()).isEqualTo(DEFAULT_DESCRIPCION_ESTADO);
    }

    @Test
    @Transactional
    void createOrdenWithExistingId() throws Exception {
        // Create the Orden with an existing ID
        orden.setId(1L);
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        int databaseSizeBeforeCreate = ordenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClienteIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setCliente(null);

        // Create the Orden, which fails.
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
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
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
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
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
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
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
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
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
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
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
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
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
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
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
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
            .andExpect(jsonPath("$.[*].cliente").value(hasItem(DEFAULT_CLIENTE)))
            .andExpect(jsonPath("$.[*].accionId").value(hasItem(DEFAULT_ACCION_ID)))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION)))
            .andExpect(jsonPath("$.[*].operacion").value(hasItem(DEFAULT_OPERACION.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].fechaOperacion").value(hasItem(DEFAULT_FECHA_OPERACION)))
            .andExpect(jsonPath("$.[*].modo").value(hasItem(DEFAULT_MODO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].descripcionEstado").value(hasItem(DEFAULT_DESCRIPCION_ESTADO)));
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
            .andExpect(jsonPath("$.cliente").value(DEFAULT_CLIENTE))
            .andExpect(jsonPath("$.accionId").value(DEFAULT_ACCION_ID))
            .andExpect(jsonPath("$.accion").value(DEFAULT_ACCION))
            .andExpect(jsonPath("$.operacion").value(DEFAULT_OPERACION.toString()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.fechaOperacion").value(DEFAULT_FECHA_OPERACION))
            .andExpect(jsonPath("$.modo").value(DEFAULT_MODO.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.descripcionEstado").value(DEFAULT_DESCRIPCION_ESTADO));
    }

    @Test
    @Transactional
    void getOrdensByIdFiltering() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        Long id = orden.getId();

        defaultOrdenShouldBeFound("id.equals=" + id);
        defaultOrdenShouldNotBeFound("id.notEquals=" + id);

        defaultOrdenShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrdenShouldNotBeFound("id.greaterThan=" + id);

        defaultOrdenShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrdenShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrdensByClienteIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cliente equals to DEFAULT_CLIENTE
        defaultOrdenShouldBeFound("cliente.equals=" + DEFAULT_CLIENTE);

        // Get all the ordenList where cliente equals to UPDATED_CLIENTE
        defaultOrdenShouldNotBeFound("cliente.equals=" + UPDATED_CLIENTE);
    }

    @Test
    @Transactional
    void getAllOrdensByClienteIsInShouldWork() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cliente in DEFAULT_CLIENTE or UPDATED_CLIENTE
        defaultOrdenShouldBeFound("cliente.in=" + DEFAULT_CLIENTE + "," + UPDATED_CLIENTE);

        // Get all the ordenList where cliente equals to UPDATED_CLIENTE
        defaultOrdenShouldNotBeFound("cliente.in=" + UPDATED_CLIENTE);
    }

    @Test
    @Transactional
    void getAllOrdensByClienteIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cliente is not null
        defaultOrdenShouldBeFound("cliente.specified=true");

        // Get all the ordenList where cliente is null
        defaultOrdenShouldNotBeFound("cliente.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdensByClienteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cliente is greater than or equal to DEFAULT_CLIENTE
        defaultOrdenShouldBeFound("cliente.greaterThanOrEqual=" + DEFAULT_CLIENTE);

        // Get all the ordenList where cliente is greater than or equal to UPDATED_CLIENTE
        defaultOrdenShouldNotBeFound("cliente.greaterThanOrEqual=" + UPDATED_CLIENTE);
    }

    @Test
    @Transactional
    void getAllOrdensByClienteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cliente is less than or equal to DEFAULT_CLIENTE
        defaultOrdenShouldBeFound("cliente.lessThanOrEqual=" + DEFAULT_CLIENTE);

        // Get all the ordenList where cliente is less than or equal to SMALLER_CLIENTE
        defaultOrdenShouldNotBeFound("cliente.lessThanOrEqual=" + SMALLER_CLIENTE);
    }

    @Test
    @Transactional
    void getAllOrdensByClienteIsLessThanSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cliente is less than DEFAULT_CLIENTE
        defaultOrdenShouldNotBeFound("cliente.lessThan=" + DEFAULT_CLIENTE);

        // Get all the ordenList where cliente is less than UPDATED_CLIENTE
        defaultOrdenShouldBeFound("cliente.lessThan=" + UPDATED_CLIENTE);
    }

    @Test
    @Transactional
    void getAllOrdensByClienteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cliente is greater than DEFAULT_CLIENTE
        defaultOrdenShouldNotBeFound("cliente.greaterThan=" + DEFAULT_CLIENTE);

        // Get all the ordenList where cliente is greater than SMALLER_CLIENTE
        defaultOrdenShouldBeFound("cliente.greaterThan=" + SMALLER_CLIENTE);
    }

    @Test
    @Transactional
    void getAllOrdensByAccionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where accionId equals to DEFAULT_ACCION_ID
        defaultOrdenShouldBeFound("accionId.equals=" + DEFAULT_ACCION_ID);

        // Get all the ordenList where accionId equals to UPDATED_ACCION_ID
        defaultOrdenShouldNotBeFound("accionId.equals=" + UPDATED_ACCION_ID);
    }

    @Test
    @Transactional
    void getAllOrdensByAccionIdIsInShouldWork() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where accionId in DEFAULT_ACCION_ID or UPDATED_ACCION_ID
        defaultOrdenShouldBeFound("accionId.in=" + DEFAULT_ACCION_ID + "," + UPDATED_ACCION_ID);

        // Get all the ordenList where accionId equals to UPDATED_ACCION_ID
        defaultOrdenShouldNotBeFound("accionId.in=" + UPDATED_ACCION_ID);
    }

    @Test
    @Transactional
    void getAllOrdensByAccionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where accionId is not null
        defaultOrdenShouldBeFound("accionId.specified=true");

        // Get all the ordenList where accionId is null
        defaultOrdenShouldNotBeFound("accionId.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdensByAccionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where accionId is greater than or equal to DEFAULT_ACCION_ID
        defaultOrdenShouldBeFound("accionId.greaterThanOrEqual=" + DEFAULT_ACCION_ID);

        // Get all the ordenList where accionId is greater than or equal to UPDATED_ACCION_ID
        defaultOrdenShouldNotBeFound("accionId.greaterThanOrEqual=" + UPDATED_ACCION_ID);
    }

    @Test
    @Transactional
    void getAllOrdensByAccionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where accionId is less than or equal to DEFAULT_ACCION_ID
        defaultOrdenShouldBeFound("accionId.lessThanOrEqual=" + DEFAULT_ACCION_ID);

        // Get all the ordenList where accionId is less than or equal to SMALLER_ACCION_ID
        defaultOrdenShouldNotBeFound("accionId.lessThanOrEqual=" + SMALLER_ACCION_ID);
    }

    @Test
    @Transactional
    void getAllOrdensByAccionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where accionId is less than DEFAULT_ACCION_ID
        defaultOrdenShouldNotBeFound("accionId.lessThan=" + DEFAULT_ACCION_ID);

        // Get all the ordenList where accionId is less than UPDATED_ACCION_ID
        defaultOrdenShouldBeFound("accionId.lessThan=" + UPDATED_ACCION_ID);
    }

    @Test
    @Transactional
    void getAllOrdensByAccionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where accionId is greater than DEFAULT_ACCION_ID
        defaultOrdenShouldNotBeFound("accionId.greaterThan=" + DEFAULT_ACCION_ID);

        // Get all the ordenList where accionId is greater than SMALLER_ACCION_ID
        defaultOrdenShouldBeFound("accionId.greaterThan=" + SMALLER_ACCION_ID);
    }

    @Test
    @Transactional
    void getAllOrdensByAccionIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where accion equals to DEFAULT_ACCION
        defaultOrdenShouldBeFound("accion.equals=" + DEFAULT_ACCION);

        // Get all the ordenList where accion equals to UPDATED_ACCION
        defaultOrdenShouldNotBeFound("accion.equals=" + UPDATED_ACCION);
    }

    @Test
    @Transactional
    void getAllOrdensByAccionIsInShouldWork() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where accion in DEFAULT_ACCION or UPDATED_ACCION
        defaultOrdenShouldBeFound("accion.in=" + DEFAULT_ACCION + "," + UPDATED_ACCION);

        // Get all the ordenList where accion equals to UPDATED_ACCION
        defaultOrdenShouldNotBeFound("accion.in=" + UPDATED_ACCION);
    }

    @Test
    @Transactional
    void getAllOrdensByAccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where accion is not null
        defaultOrdenShouldBeFound("accion.specified=true");

        // Get all the ordenList where accion is null
        defaultOrdenShouldNotBeFound("accion.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdensByAccionContainsSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where accion contains DEFAULT_ACCION
        defaultOrdenShouldBeFound("accion.contains=" + DEFAULT_ACCION);

        // Get all the ordenList where accion contains UPDATED_ACCION
        defaultOrdenShouldNotBeFound("accion.contains=" + UPDATED_ACCION);
    }

    @Test
    @Transactional
    void getAllOrdensByAccionNotContainsSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where accion does not contain DEFAULT_ACCION
        defaultOrdenShouldNotBeFound("accion.doesNotContain=" + DEFAULT_ACCION);

        // Get all the ordenList where accion does not contain UPDATED_ACCION
        defaultOrdenShouldBeFound("accion.doesNotContain=" + UPDATED_ACCION);
    }

    @Test
    @Transactional
    void getAllOrdensByOperacionIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where operacion equals to DEFAULT_OPERACION
        defaultOrdenShouldBeFound("operacion.equals=" + DEFAULT_OPERACION);

        // Get all the ordenList where operacion equals to UPDATED_OPERACION
        defaultOrdenShouldNotBeFound("operacion.equals=" + UPDATED_OPERACION);
    }

    @Test
    @Transactional
    void getAllOrdensByOperacionIsInShouldWork() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where operacion in DEFAULT_OPERACION or UPDATED_OPERACION
        defaultOrdenShouldBeFound("operacion.in=" + DEFAULT_OPERACION + "," + UPDATED_OPERACION);

        // Get all the ordenList where operacion equals to UPDATED_OPERACION
        defaultOrdenShouldNotBeFound("operacion.in=" + UPDATED_OPERACION);
    }

    @Test
    @Transactional
    void getAllOrdensByOperacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where operacion is not null
        defaultOrdenShouldBeFound("operacion.specified=true");

        // Get all the ordenList where operacion is null
        defaultOrdenShouldNotBeFound("operacion.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdensByPrecioIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where precio equals to DEFAULT_PRECIO
        defaultOrdenShouldBeFound("precio.equals=" + DEFAULT_PRECIO);

        // Get all the ordenList where precio equals to UPDATED_PRECIO
        defaultOrdenShouldNotBeFound("precio.equals=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllOrdensByPrecioIsInShouldWork() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where precio in DEFAULT_PRECIO or UPDATED_PRECIO
        defaultOrdenShouldBeFound("precio.in=" + DEFAULT_PRECIO + "," + UPDATED_PRECIO);

        // Get all the ordenList where precio equals to UPDATED_PRECIO
        defaultOrdenShouldNotBeFound("precio.in=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllOrdensByPrecioIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where precio is not null
        defaultOrdenShouldBeFound("precio.specified=true");

        // Get all the ordenList where precio is null
        defaultOrdenShouldNotBeFound("precio.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdensByPrecioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where precio is greater than or equal to DEFAULT_PRECIO
        defaultOrdenShouldBeFound("precio.greaterThanOrEqual=" + DEFAULT_PRECIO);

        // Get all the ordenList where precio is greater than or equal to UPDATED_PRECIO
        defaultOrdenShouldNotBeFound("precio.greaterThanOrEqual=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllOrdensByPrecioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where precio is less than or equal to DEFAULT_PRECIO
        defaultOrdenShouldBeFound("precio.lessThanOrEqual=" + DEFAULT_PRECIO);

        // Get all the ordenList where precio is less than or equal to SMALLER_PRECIO
        defaultOrdenShouldNotBeFound("precio.lessThanOrEqual=" + SMALLER_PRECIO);
    }

    @Test
    @Transactional
    void getAllOrdensByPrecioIsLessThanSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where precio is less than DEFAULT_PRECIO
        defaultOrdenShouldNotBeFound("precio.lessThan=" + DEFAULT_PRECIO);

        // Get all the ordenList where precio is less than UPDATED_PRECIO
        defaultOrdenShouldBeFound("precio.lessThan=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllOrdensByPrecioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where precio is greater than DEFAULT_PRECIO
        defaultOrdenShouldNotBeFound("precio.greaterThan=" + DEFAULT_PRECIO);

        // Get all the ordenList where precio is greater than SMALLER_PRECIO
        defaultOrdenShouldBeFound("precio.greaterThan=" + SMALLER_PRECIO);
    }

    @Test
    @Transactional
    void getAllOrdensByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cantidad equals to DEFAULT_CANTIDAD
        defaultOrdenShouldBeFound("cantidad.equals=" + DEFAULT_CANTIDAD);

        // Get all the ordenList where cantidad equals to UPDATED_CANTIDAD
        defaultOrdenShouldNotBeFound("cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllOrdensByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cantidad in DEFAULT_CANTIDAD or UPDATED_CANTIDAD
        defaultOrdenShouldBeFound("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD);

        // Get all the ordenList where cantidad equals to UPDATED_CANTIDAD
        defaultOrdenShouldNotBeFound("cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllOrdensByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cantidad is not null
        defaultOrdenShouldBeFound("cantidad.specified=true");

        // Get all the ordenList where cantidad is null
        defaultOrdenShouldNotBeFound("cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdensByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cantidad is greater than or equal to DEFAULT_CANTIDAD
        defaultOrdenShouldBeFound("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the ordenList where cantidad is greater than or equal to UPDATED_CANTIDAD
        defaultOrdenShouldNotBeFound("cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllOrdensByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cantidad is less than or equal to DEFAULT_CANTIDAD
        defaultOrdenShouldBeFound("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the ordenList where cantidad is less than or equal to SMALLER_CANTIDAD
        defaultOrdenShouldNotBeFound("cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllOrdensByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cantidad is less than DEFAULT_CANTIDAD
        defaultOrdenShouldNotBeFound("cantidad.lessThan=" + DEFAULT_CANTIDAD);

        // Get all the ordenList where cantidad is less than UPDATED_CANTIDAD
        defaultOrdenShouldBeFound("cantidad.lessThan=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllOrdensByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where cantidad is greater than DEFAULT_CANTIDAD
        defaultOrdenShouldNotBeFound("cantidad.greaterThan=" + DEFAULT_CANTIDAD);

        // Get all the ordenList where cantidad is greater than SMALLER_CANTIDAD
        defaultOrdenShouldBeFound("cantidad.greaterThan=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllOrdensByFechaOperacionIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where fechaOperacion equals to DEFAULT_FECHA_OPERACION
        defaultOrdenShouldBeFound("fechaOperacion.equals=" + DEFAULT_FECHA_OPERACION);

        // Get all the ordenList where fechaOperacion equals to UPDATED_FECHA_OPERACION
        defaultOrdenShouldNotBeFound("fechaOperacion.equals=" + UPDATED_FECHA_OPERACION);
    }

    @Test
    @Transactional
    void getAllOrdensByFechaOperacionIsInShouldWork() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where fechaOperacion in DEFAULT_FECHA_OPERACION or UPDATED_FECHA_OPERACION
        defaultOrdenShouldBeFound("fechaOperacion.in=" + DEFAULT_FECHA_OPERACION + "," + UPDATED_FECHA_OPERACION);

        // Get all the ordenList where fechaOperacion equals to UPDATED_FECHA_OPERACION
        defaultOrdenShouldNotBeFound("fechaOperacion.in=" + UPDATED_FECHA_OPERACION);
    }

    @Test
    @Transactional
    void getAllOrdensByFechaOperacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where fechaOperacion is not null
        defaultOrdenShouldBeFound("fechaOperacion.specified=true");

        // Get all the ordenList where fechaOperacion is null
        defaultOrdenShouldNotBeFound("fechaOperacion.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdensByFechaOperacionContainsSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where fechaOperacion contains DEFAULT_FECHA_OPERACION
        defaultOrdenShouldBeFound("fechaOperacion.contains=" + DEFAULT_FECHA_OPERACION);

        // Get all the ordenList where fechaOperacion contains UPDATED_FECHA_OPERACION
        defaultOrdenShouldNotBeFound("fechaOperacion.contains=" + UPDATED_FECHA_OPERACION);
    }

    @Test
    @Transactional
    void getAllOrdensByFechaOperacionNotContainsSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where fechaOperacion does not contain DEFAULT_FECHA_OPERACION
        defaultOrdenShouldNotBeFound("fechaOperacion.doesNotContain=" + DEFAULT_FECHA_OPERACION);

        // Get all the ordenList where fechaOperacion does not contain UPDATED_FECHA_OPERACION
        defaultOrdenShouldBeFound("fechaOperacion.doesNotContain=" + UPDATED_FECHA_OPERACION);
    }

    @Test
    @Transactional
    void getAllOrdensByModoIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where modo equals to DEFAULT_MODO
        defaultOrdenShouldBeFound("modo.equals=" + DEFAULT_MODO);

        // Get all the ordenList where modo equals to UPDATED_MODO
        defaultOrdenShouldNotBeFound("modo.equals=" + UPDATED_MODO);
    }

    @Test
    @Transactional
    void getAllOrdensByModoIsInShouldWork() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where modo in DEFAULT_MODO or UPDATED_MODO
        defaultOrdenShouldBeFound("modo.in=" + DEFAULT_MODO + "," + UPDATED_MODO);

        // Get all the ordenList where modo equals to UPDATED_MODO
        defaultOrdenShouldNotBeFound("modo.in=" + UPDATED_MODO);
    }

    @Test
    @Transactional
    void getAllOrdensByModoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where modo is not null
        defaultOrdenShouldBeFound("modo.specified=true");

        // Get all the ordenList where modo is null
        defaultOrdenShouldNotBeFound("modo.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdensByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where estado equals to DEFAULT_ESTADO
        defaultOrdenShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the ordenList where estado equals to UPDATED_ESTADO
        defaultOrdenShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllOrdensByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultOrdenShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the ordenList where estado equals to UPDATED_ESTADO
        defaultOrdenShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllOrdensByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where estado is not null
        defaultOrdenShouldBeFound("estado.specified=true");

        // Get all the ordenList where estado is null
        defaultOrdenShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdensByDescripcionEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where descripcionEstado equals to DEFAULT_DESCRIPCION_ESTADO
        defaultOrdenShouldBeFound("descripcionEstado.equals=" + DEFAULT_DESCRIPCION_ESTADO);

        // Get all the ordenList where descripcionEstado equals to UPDATED_DESCRIPCION_ESTADO
        defaultOrdenShouldNotBeFound("descripcionEstado.equals=" + UPDATED_DESCRIPCION_ESTADO);
    }

    @Test
    @Transactional
    void getAllOrdensByDescripcionEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where descripcionEstado in DEFAULT_DESCRIPCION_ESTADO or UPDATED_DESCRIPCION_ESTADO
        defaultOrdenShouldBeFound("descripcionEstado.in=" + DEFAULT_DESCRIPCION_ESTADO + "," + UPDATED_DESCRIPCION_ESTADO);

        // Get all the ordenList where descripcionEstado equals to UPDATED_DESCRIPCION_ESTADO
        defaultOrdenShouldNotBeFound("descripcionEstado.in=" + UPDATED_DESCRIPCION_ESTADO);
    }

    @Test
    @Transactional
    void getAllOrdensByDescripcionEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where descripcionEstado is not null
        defaultOrdenShouldBeFound("descripcionEstado.specified=true");

        // Get all the ordenList where descripcionEstado is null
        defaultOrdenShouldNotBeFound("descripcionEstado.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdensByDescripcionEstadoContainsSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where descripcionEstado contains DEFAULT_DESCRIPCION_ESTADO
        defaultOrdenShouldBeFound("descripcionEstado.contains=" + DEFAULT_DESCRIPCION_ESTADO);

        // Get all the ordenList where descripcionEstado contains UPDATED_DESCRIPCION_ESTADO
        defaultOrdenShouldNotBeFound("descripcionEstado.contains=" + UPDATED_DESCRIPCION_ESTADO);
    }

    @Test
    @Transactional
    void getAllOrdensByDescripcionEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList where descripcionEstado does not contain DEFAULT_DESCRIPCION_ESTADO
        defaultOrdenShouldNotBeFound("descripcionEstado.doesNotContain=" + DEFAULT_DESCRIPCION_ESTADO);

        // Get all the ordenList where descripcionEstado does not contain UPDATED_DESCRIPCION_ESTADO
        defaultOrdenShouldBeFound("descripcionEstado.doesNotContain=" + UPDATED_DESCRIPCION_ESTADO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrdenShouldBeFound(String filter) throws Exception {
        restOrdenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orden.getId().intValue())))
            .andExpect(jsonPath("$.[*].cliente").value(hasItem(DEFAULT_CLIENTE)))
            .andExpect(jsonPath("$.[*].accionId").value(hasItem(DEFAULT_ACCION_ID)))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION)))
            .andExpect(jsonPath("$.[*].operacion").value(hasItem(DEFAULT_OPERACION.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].fechaOperacion").value(hasItem(DEFAULT_FECHA_OPERACION)))
            .andExpect(jsonPath("$.[*].modo").value(hasItem(DEFAULT_MODO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].descripcionEstado").value(hasItem(DEFAULT_DESCRIPCION_ESTADO)));

        // Check, that the count call also returns 1
        restOrdenMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrdenShouldNotBeFound(String filter) throws Exception {
        restOrdenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrdenMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .operacion(UPDATED_OPERACION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO)
            .estado(UPDATED_ESTADO)
            .descripcionEstado(UPDATED_DESCRIPCION_ESTADO);
        OrdenDTO ordenDTO = ordenMapper.toDto(updatedOrden);

        restOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordenDTO))
            )
            .andExpect(status().isOk());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
        Orden testOrden = ordenList.get(ordenList.size() - 1);
        assertThat(testOrden.getCliente()).isEqualTo(UPDATED_CLIENTE);
        assertThat(testOrden.getAccionId()).isEqualTo(UPDATED_ACCION_ID);
        assertThat(testOrden.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testOrden.getOperacion()).isEqualTo(UPDATED_OPERACION);
        assertThat(testOrden.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testOrden.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testOrden.getFechaOperacion()).isEqualTo(UPDATED_FECHA_OPERACION);
        assertThat(testOrden.getModo()).isEqualTo(UPDATED_MODO);
        assertThat(testOrden.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testOrden.getDescripcionEstado()).isEqualTo(UPDATED_DESCRIPCION_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordenDTO))
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

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordenDTO))
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

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
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
            .operacion(UPDATED_OPERACION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .modo(UPDATED_MODO)
            .descripcionEstado(UPDATED_DESCRIPCION_ESTADO);

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
        assertThat(testOrden.getCliente()).isEqualTo(DEFAULT_CLIENTE);
        assertThat(testOrden.getAccionId()).isEqualTo(DEFAULT_ACCION_ID);
        assertThat(testOrden.getAccion()).isEqualTo(DEFAULT_ACCION);
        assertThat(testOrden.getOperacion()).isEqualTo(UPDATED_OPERACION);
        assertThat(testOrden.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testOrden.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testOrden.getFechaOperacion()).isEqualTo(DEFAULT_FECHA_OPERACION);
        assertThat(testOrden.getModo()).isEqualTo(UPDATED_MODO);
        assertThat(testOrden.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testOrden.getDescripcionEstado()).isEqualTo(UPDATED_DESCRIPCION_ESTADO);
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
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .operacion(UPDATED_OPERACION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO)
            .estado(UPDATED_ESTADO)
            .descripcionEstado(UPDATED_DESCRIPCION_ESTADO);

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
        assertThat(testOrden.getCliente()).isEqualTo(UPDATED_CLIENTE);
        assertThat(testOrden.getAccionId()).isEqualTo(UPDATED_ACCION_ID);
        assertThat(testOrden.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testOrden.getOperacion()).isEqualTo(UPDATED_OPERACION);
        assertThat(testOrden.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testOrden.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testOrden.getFechaOperacion()).isEqualTo(UPDATED_FECHA_OPERACION);
        assertThat(testOrden.getModo()).isEqualTo(UPDATED_MODO);
        assertThat(testOrden.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testOrden.getDescripcionEstado()).isEqualTo(UPDATED_DESCRIPCION_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordenDTO))
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

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordenDTO))
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

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
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
