package prog2.sarmiento.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;

@Service
@Transactional
public class ProcesadorOrdenesService {

    @Autowired
    ExternalService servicioExterno;

    private final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesService.class);

    List<Orden> ordenesAhora = new ArrayList<>();
    
    public Orden procesarOrden(Orden orden) {
        Boolean response = false;
        switch (orden.getOperacion()) {
            case COMPRA:
                response = servicioExterno.ordenCompra(orden);
            case VENTA:
                response = servicioExterno.ordenVenta(orden);
            default: {};
            if (response) {
                orden.setEstado(Estado.COMPLETE);
                orden.setDescripcionEstado("Orden COMPLETADA");;
                log.info("Orden de (" + orden.getOperacion() + ") procesada correctamente:" + orden);
            }
            return orden;
        }
    }
}
