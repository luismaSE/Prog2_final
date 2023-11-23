package prog2.sarmiento.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.domain.enumeration.Operacion;

/**
 * Spring Data JPA repository for the Orden entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long>, JpaSpecificationExecutor<Orden> {

    // @Query("SELECT o FROM Orden o WHERE (:cliente IS NULL OR o.cliente = :cliente) " +
    // "AND (:accionId IS NULL OR o.accionId = :accionId) " +
    // "AND (:accion IS NULL OR o.accion = :accion) " +
    // "AND (:fechaInicio IS NULL OR o.fechaOperacion >= :fechaInicio) " +
    // "AND (:fechaFin IS NULL OR o.fechaOperacion <= :fechaFin) " +
    // "AND (:operacion IS NULL OR o.operacion = :operacion) " +
    // "AND (:modo IS NULL OR o.modo = :modo) " +
    // "AND (:estado IS NULL OR o.estado = :estado)")
    // List<Orden> findOrdenes(Integer cliente, Integer accionId, String accion, Instant fechaInicio, Instant fechaFin,
    //         Operacion operacion, Modo modo, Estado estado);
        
    
    @Query("SELECT o FROM Orden o WHERE (:cliente IS NULL OR o.cliente = :cliente) " +
    "AND (:accionId IS NULL OR o.accionId = :accionId) " +
    "AND (:accion IS NULL OR o.accion = :accion) " +
    "AND (:fechaInicio IS NULL OR o.fechaOperacion >= :fechaInicio) " +
    "AND (:fechaFin IS NULL OR o.fechaOperacion <= :fechaFin) " +
    "AND (:operacion IS NULL OR o.operacion = :operacion) " +
    "AND (:modo IS NULL OR o.modo = :modo) " +
    "AND (:estado IS NULL OR o.estado = :estado)")
    List<Orden> findOrdenes(@Param("cliente") Integer cliente, 
                            @Param("accionId") Integer accionId, 
                            @Param("accion") String accion, 
                            @Param("fechaInicio") Instant fechaInicio, 
                            @Param("fechaFin") Instant fechaFin,
                            @Param("operacion") Operacion operacion, 
                            @Param("modo") Modo modo, 
                            @Param("estado") Estado estado);
        
}


