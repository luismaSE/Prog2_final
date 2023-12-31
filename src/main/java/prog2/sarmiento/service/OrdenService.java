package prog2.sarmiento.service;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mapstruct.ap.shaded.freemarker.core.ReturnInstruction.Return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.domain.enumeration.Operacion;
import prog2.sarmiento.repository.OrdenRepository;
import prog2.sarmiento.service.dto.OrdenDTO;
import prog2.sarmiento.service.mapper.OrdenMapper;

/**
 * Service Implementation for managing {@link Orden}.
 */
@Service
@Transactional
public class OrdenService {

    private final Logger log = LoggerFactory.getLogger(OrdenService.class);

    private final OrdenRepository ordenRepository;

    private final OrdenMapper ordenMapper;

    public OrdenService(OrdenRepository ordenRepository, OrdenMapper ordenMapper) {
        this.ordenRepository = ordenRepository;
        this.ordenMapper = ordenMapper;
    }

    /**
     * Save a orden.
     *
     * @param ordenDTO the entity to save.
     * @return the persisted entity.
     */
    public OrdenDTO save(OrdenDTO ordenDTO) {
        log.debug("Request to save Orden : {}", ordenDTO);
        Orden orden = ordenMapper.toEntity(ordenDTO);
        orden = ordenRepository.save(orden);
        return ordenMapper.toDto(orden);
    }

    /**
     * Update a orden.
     *
     * @param ordenDTO the entity to save.
     * @return the persisted entity.
     */
    public OrdenDTO update(OrdenDTO ordenDTO) {
        log.debug("Request to update Orden : {}", ordenDTO);
        Orden orden = ordenMapper.toEntity(ordenDTO);
        orden = ordenRepository.save(orden);
        return ordenMapper.toDto(orden);
    }

    /**
     * Partially update a orden.
     *
     * @param ordenDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrdenDTO> partialUpdate(OrdenDTO ordenDTO) {
        log.debug("Request to partially update Orden : {}", ordenDTO);

        return ordenRepository
            .findById(ordenDTO.getId())
            .map(existingOrden -> {
                ordenMapper.partialUpdate(existingOrden, ordenDTO);

                return existingOrden;
            })
            .map(ordenRepository::save)
            .map(ordenMapper::toDto);
    }

    /**
     * Get all the ordens.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdenDTO> findAll() {
        log.debug("Request to get all Ordens");
        return ordenRepository.findAll().stream().map(ordenMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one orden by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrdenDTO> findOne(Long id) {
        log.debug("Request to get Orden : {}", id);
        return ordenRepository.findById(id).map(ordenMapper::toDto);
    }

    /**
     * Delete the orden by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Orden : {}", id);
        ordenRepository.deleteById(id);
    }

    // public List<Orden> findOrdenes(Integer cliente, Integer accionId, String accion, Instant fechaInicio,
    //     Instant fechaFin, Operacion operacion, Modo modo, Estado estado) {
    //     List<Orden> ordenes = ordenRepository.findOrdenes(cliente, accionId, accion, fechaInicio, fechaFin, operacion, modo, estado);
    //     return ordenes;
    // }

    public List<Orden> findOrdenes(Integer cliente, Integer accionId, String accion, Instant fechaInicio,
        Instant fechaFin, Operacion operacion, Modo modo, Estado estado) {

        Specification<Orden> clienteSpec = (root, query, cb) -> cliente == null ? cb.conjunction() : cb.equal(root.get("cliente"), cliente);
        Specification<Orden> accionIdSpec = (root, query, cb) -> accionId == null ? cb.conjunction() : cb.equal(root.get("accionId"), accionId);
        Specification<Orden> accionSpec = (root, query, cb) -> accion == null ? cb.conjunction() : cb.equal(root.get("accion"), accion);
        Specification<Orden> fechaInicioSpec = (root, query, cb) -> fechaInicio == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("fechaOperacion"), fechaInicio);
        Specification<Orden> fechaFinSpec = (root, query, cb) -> fechaFin == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("fechaOperacion"), fechaFin);
        Specification<Orden> operacionSpec = (root, query, cb) -> operacion == null ? cb.conjunction() : cb.equal(root.get("operacion"), operacion);
        Specification<Orden> modoSpec = (root, query, cb) -> modo == null ? cb.conjunction() : cb.equal(root.get("modo"), modo);
        Specification<Orden> estadoSpec = (root, query, cb) -> estado == null ? cb.conjunction() : cb.equal(root.get("estado"), estado);

        Specification<Orden> combinedSpec = Specification.where(clienteSpec)
            .and(clienteSpec)
            .and(accionIdSpec)
            .and(fechaInicioSpec)
            .and(fechaFinSpec)
            .and(operacionSpec)
            .and(modoSpec)
            .and(estadoSpec);

        List<Orden> ordenes = ordenRepository.findAll(combinedSpec);

        return ordenes;
    }

    public List<Orden> findOrdenesPend() {

        Specification<Orden> spec = (root, query, cb) -> {
            return cb.and(
                cb.notEqual(root.get("estado"), Estado.COMPLETE),
                cb.notEqual(root.get("estado"), Estado.FAIL)
                );
            };
            List<Orden> ordenes = ordenRepository.findAll(spec);
            return ordenes;
        }

    
}
