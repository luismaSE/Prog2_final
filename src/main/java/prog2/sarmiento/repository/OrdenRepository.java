package prog2.sarmiento.repository;

import java.time.ZonedDateTime;
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

    //metodo propio
    @Query("SELECT o FROM Orden o WHERE (:cliente IS NULL OR o.cliente = :cliente) " +
    "AND (:accionId IS NULL OR o.accionId = :accionId) " +
    "AND (:accion IS NULL OR o.accion = :accion) " +
    "AND (:fechaInicio IS NULL OR o.fechaOperacion >= :fechaInicio) " +
    "AND (:fechaFin IS NULL OR o.fechaOperacion <= :fechaFin) " +
    "AND (:operacion IS NULL OR o.operacion = :operacion) " +
    "AND (:modo IS NULL OR o.modo = :modo) " +
    "AND (:estado IS NULL OR o.estado = :estado)")
    List<Orden> findOrdenes(@Param("cliente") Integer cliente, @Param("accionId") Integer accionId, 
                     @Param("accion") String accion, @Param("fechaInicio") ZonedDateTime fechaInicio, 
                     @Param("fechaFin") ZonedDateTime fechaFin, @Param("operacion") Operacion operacion, 
                     @Param("modo") Modo modo, @Param("estado") Estado estado);



    // List<Orden> findOrdenes(Integer cliente, Integer accionId, String accion,
    //                         ZonedDateTime fechaInicio, ZonedDateTime fechaFin,
    //                         Operacion operacion, Modo modo, Estado estado);


    // List<Orden> findOrdenes(Integer cliente, Integer accionId,  String accion, ZonedDateTime fechaInicio, ZonedDateTime fechaFin, Operacion operacion, Modo modo, Estado estado);
    
    // @Query("SELECT o FROM Orden o WHERE (:cliente is null or o.cliente = :cliente) and (:accionId is null or o.accionId = :accionId) and (:accion is null or o.accion = :accion) and (:fechaInicio is null or o.fecha >= :fechaInicio) and (:fechaFin is null or o.fecha <= :fechaFin) and (:operacion is null or o.operacion = :operacion) and (:modo is null or o.modo = :modo) and (:estado is null or o.estado = :estado)")
    // List<Orden> findOrdenes(@Param("cliente") Integer cliente, @Param("accionId") Integer accionId, @Param("accion") String accion, @Param("fechaInicio") ZonedDateTime fechaInicio, @Param("fechaFin") ZonedDateTime fechaFin, @Param("operacion") Operacion operacion, @Param("modo") Modo modo, @Param("estado") Estado estado);



}   

