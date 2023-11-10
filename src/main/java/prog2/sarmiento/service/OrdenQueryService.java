package prog2.sarmiento.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prog2.sarmiento.domain.*; // for static metamodels
import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.repository.OrdenRepository;
import prog2.sarmiento.service.criteria.OrdenCriteria;
import prog2.sarmiento.service.dto.OrdenDTO;
import prog2.sarmiento.service.mapper.OrdenMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Orden} entities in the database.
 * The main input is a {@link OrdenCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrdenDTO} or a {@link Page} of {@link OrdenDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrdenQueryService extends QueryService<Orden> {

    private final Logger log = LoggerFactory.getLogger(OrdenQueryService.class);

    private final OrdenRepository ordenRepository;

    private final OrdenMapper ordenMapper;

    public OrdenQueryService(OrdenRepository ordenRepository, OrdenMapper ordenMapper) {
        this.ordenRepository = ordenRepository;
        this.ordenMapper = ordenMapper;
    }

    /**
     * Return a {@link List} of {@link OrdenDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrdenDTO> findByCriteria(OrdenCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Orden> specification = createSpecification(criteria);
        return ordenMapper.toDto(ordenRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrdenDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrdenDTO> findByCriteria(OrdenCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Orden> specification = createSpecification(criteria);
        return ordenRepository.findAll(specification, page).map(ordenMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrdenCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Orden> specification = createSpecification(criteria);
        return ordenRepository.count(specification);
    }

    /**
     * Function to convert {@link OrdenCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Orden> createSpecification(OrdenCriteria criteria) {
        Specification<Orden> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Orden_.id));
            }
            if (criteria.getCliente() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCliente(), Orden_.cliente));
            }
            if (criteria.getAccionId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccionId(), Orden_.accionId));
            }
            if (criteria.getAccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccion(), Orden_.accion));
            }
            if (criteria.getOperacion() != null) {
                specification = specification.and(buildSpecification(criteria.getOperacion(), Orden_.operacion));
            }
            if (criteria.getPrecio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecio(), Orden_.precio));
            }
            if (criteria.getCantidad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidad(), Orden_.cantidad));
            }
            if (criteria.getModo() != null) {
                specification = specification.and(buildSpecification(criteria.getModo(), Orden_.modo));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Orden_.estado));
            }
            if (criteria.getDescripcionEstado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcionEstado(), Orden_.descripcionEstado));
            }
            if (criteria.getFechaOperacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaOperacion(), Orden_.fechaOperacion));
            }
        }
        return specification;
    }
}
