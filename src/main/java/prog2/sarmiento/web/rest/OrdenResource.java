package prog2.sarmiento.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.domain.enumeration.Operacion;
import prog2.sarmiento.repository.OrdenRepository;
import prog2.sarmiento.service.AnalizadorOrdenesService;
import prog2.sarmiento.service.ApiService;
import prog2.sarmiento.service.GeneradorOrdenService;
import prog2.sarmiento.service.MainService;
import prog2.sarmiento.service.OrdenQueryService;
import prog2.sarmiento.service.OrdenService;
import prog2.sarmiento.service.ProcesadorOrdenesService;
import prog2.sarmiento.service.ProgramadorOrdenesService;
import prog2.sarmiento.service.ReportarOrdenesService;
import prog2.sarmiento.service.criteria.OrdenCriteria;
import prog2.sarmiento.service.dto.OrdenDTO;
import prog2.sarmiento.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link prog2.sarmiento.domain.Orden}.
 */
@RestController
@RequestMapping("/api")
public class OrdenResource {

    private final Logger log = LoggerFactory.getLogger(OrdenResource.class);

    private static final String ENTITY_NAME = "orden";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdenService ordenService;

    private final OrdenRepository ordenRepository;

    private final OrdenQueryService ordenQueryService;

    public OrdenResource(OrdenService ordenService, OrdenRepository ordenRepository, OrdenQueryService ordenQueryService) {
        this.ordenService = ordenService;
        this.ordenRepository = ordenRepository;
        this.ordenQueryService = ordenQueryService;
    }

    // METODOS PROPIOS

    @Autowired private MainService mainService;
    @Autowired private ApiService apiService;
    @Autowired private AnalizadorOrdenesService analizadorOrdenesService;
    @Autowired private ProgramadorOrdenesService programadorOrdenesService;
    @Autowired private ReportarOrdenesService reportarOrdenesService;
    @Autowired private GeneradorOrdenService generadorOrdenService;
    @Autowired private ProcesadorOrdenesService procesadorOrdenes;


    @GetMapping("/ordenes/procesar")
    public ResponseEntity<String> ejecutarMainService() {   
        try {
            String estado = mainService.Serve();
            return ResponseEntity.ok(estado);
        } catch (Exception e) {
            // Manejo de excepción en caso de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al ejecutar MainService: " + e.getMessage());
        }
    }

    @GetMapping("/ordenes/procesar/principiodia")
    public ResponseEntity<String> ejecutarPrincipioDia() {   
        try {
            procesadorOrdenes.procOrdenesInicioDia();
            return ResponseEntity.ok("Ordenes procesadas correctamente");
        } catch (Exception e) {
            // Manejo de excepción en caso de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al ejecutar MainService: " + e.getMessage());
        }
    }

    @GetMapping("/ordenes/procesar/findia")
    public ResponseEntity<String> ejecutarFinDia() {   
        try {
            procesadorOrdenes.procOrdenesFinDia();
            return ResponseEntity.ok("Ordenes procesadas correctamente");
        } catch (Exception e) {
            // Manejo de excepción en caso de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al ejecutar MainService: " + e.getMessage());
        }
    }


    @GetMapping("generar/ordenes")
    public ResponseEntity<String> crearOrdenes() {
        String ordenes = generadorOrdenService.generarOrdenes();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/generar/ordenes/espejo")
    public ResponseEntity<String> crearOrdenesEspejo() {
        String ordenes = generadorOrdenService.generarOrdenes();
        return ResponseEntity.ok(ordenes);
    }


    @GetMapping("/ordenes/buscar")
    public ResponseEntity<List<Orden>> buscarOrdenes(@RequestParam(required = false) Integer cliente,
                                                  @RequestParam(required = false) Integer accionId,
                                                  @RequestParam(required = false) String accion,
                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fechaInicio,
                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fechaFin,
                                                  @RequestParam(required = false) Operacion operacion,
                                                  @RequestParam(required = false) Modo modo,
                                                  @RequestParam(required = false) Estado estado) {
        List<Orden> ordenes = ordenService.findOrdenes(cliente, accionId, accion, fechaInicio, fechaFin, operacion, modo, estado);
        return ResponseEntity.ok(ordenes);
    }

    @PostMapping("/programar")
    public ResponseEntity<String> programarOrdenes(@RequestBody List<Orden> ordenes) {
        programadorOrdenesService.programarOrdenes(ordenes);
        return ResponseEntity.ok("Ordenes programadas correctamente");
    }

    @GetMapping("/consultar/{accion}")
    public ResponseEntity<Double> consultarUltimoValor(@PathVariable String accion) {
        Double ultimoValor = apiService.obtenerUltimoValor(accion);
        return ResponseEntity.ok(ultimoValor);
    }


    @PostMapping("/espejo")
    public ResponseEntity<String> espejo(@RequestBody String jsonOrden) {
        try {
            String response = apiService.postEspejo(jsonOrden);
            return ResponseEntity.ok(response);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    @PostMapping("/reportar")
    public ResponseEntity<String> reportarOrdenes(@RequestBody String jsonOrdenes) {
        try {
            String response = apiService.postReportar(jsonOrdenes);
            return ResponseEntity.ok(response);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud: " + e.getMessage());
        }
    }


    // METODOS DE JHIPSTER

    /**
     * {@code POST  /ordens} : Create a new orden.
     *
     * @param ordenDTO the ordenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordenDTO, or with status {@code 400 (Bad Request)} if the orden has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordens")
    public ResponseEntity<OrdenDTO> createOrden(@Valid @RequestBody OrdenDTO ordenDTO) throws URISyntaxException {
        log.debug("REST request to save Orden : {}", ordenDTO);
        if (ordenDTO.getId() != null) {
            throw new BadRequestAlertException("A new orden cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdenDTO result = ordenService.save(ordenDTO);
        return ResponseEntity
            .created(new URI("/api/ordens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordens/:id} : Updates an existing orden.
     *
     * @param id the id of the ordenDTO to save.
     * @param ordenDTO the ordenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordenDTO,
     * or with status {@code 400 (Bad Request)} if the ordenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordens/{id}")
    public ResponseEntity<OrdenDTO> updateOrden(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrdenDTO ordenDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Orden : {}, {}", id, ordenDTO);
        if (ordenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdenDTO result = ordenService.update(ordenDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordenDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ordens/:id} : Partial updates given fields of an existing orden, field will ignore if it is null
     *
     * @param id the id of the ordenDTO to save.
     * @param ordenDTO the ordenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordenDTO,
     * or with status {@code 400 (Bad Request)} if the ordenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ordenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ordens/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrdenDTO> partialUpdateOrden(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrdenDTO ordenDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Orden partially : {}, {}", id, ordenDTO);
        if (ordenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdenDTO> result = ordenService.partialUpdate(ordenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ordens} : get all the ordens.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordens in body.
     */
    @GetMapping("/ordens")
    public ResponseEntity<List<OrdenDTO>> getAllOrdens(OrdenCriteria criteria) {
        log.debug("REST request to get Ordens by criteria: {}", criteria);
        List<OrdenDTO> entityList = ordenQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /ordens/count} : count all the ordens.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ordens/count")
    public ResponseEntity<Long> countOrdens(OrdenCriteria criteria) {
        log.debug("REST request to count Ordens by criteria: {}", criteria);
        return ResponseEntity.ok().body(ordenQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ordens/:id} : get the "id" orden.
     *
     * @param id the id of the ordenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordens/{id}")
    public ResponseEntity<OrdenDTO> getOrden(@PathVariable Long id) {
        log.debug("REST request to get Orden : {}", id);
        Optional<OrdenDTO> ordenDTO = ordenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordenDTO);
    }

    /**
     * {@code DELETE  /ordens/:id} : delete the "id" orden.
     *
     * @param id the id of the ordenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordens/{id}")
    public ResponseEntity<Void> deleteOrden(@PathVariable Long id) {
        log.debug("REST request to delete Orden : {}", id);
        ordenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
