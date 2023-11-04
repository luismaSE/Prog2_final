package prog2.sarmiento.service.mapper;

import org.mapstruct.*;
import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.service.dto.OrdenDTO;

/**
 * Mapper for the entity {@link Orden} and its DTO {@link OrdenDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrdenMapper extends EntityMapper<OrdenDTO, Orden> {}
