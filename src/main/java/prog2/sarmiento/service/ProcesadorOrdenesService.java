package prog2.sarmiento.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.EstadoOrden;

@Service
@Transactional
public class ProcesadorOrdenesService {

    List<Orden> ordenesAhora = new ArrayList<>();
    List<Orden> ordenesFinDia = new ArrayList<>();
    List<Orden> ordenesPrincipioDia = new ArrayList<>();

    private final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesService.class);

    public Orden procesarOrden(Orden orden) {
        ServicioExternoService servicioExterno = new ServicioExternoService();
        Boolean response = false;
        switch (orden.getOperacion()) {
            case COMPRA:
                response = servicioExterno.ordenCompra(orden);
            case VENTA:
                response = servicioExterno.ordenVenta(orden);
            default: {};
            if (response) {
                orden.setEstado(EstadoOrden.COMPLETE);
                orden.setDescripcionEstado("Orden COMPLETADA");;
                // System.out.println("Orden de (" + orden.getOperacion() + ") procesada:" + orden);
            }
            return orden;
        }
    }
}
