package prog2.sarmiento.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.ModoOrden;
import prog2.sarmiento.domain.enumeration.Operacion;

@Service
@Transactional
public class ProcesadorOrdenesService {

    List<Orden> ordenesAhora = new ArrayList<>();
    List<Orden> ordenesFinDia = new ArrayList<>();;
    List<Orden> ordenesPrincipioDia = new ArrayList<>();;

    private final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesService.class);

    public void clasificarOrdenes (List<Orden> ordenes) {
        for (Orden orden : ordenes) {
            if (orden.getModo() == ModoOrden.PRINCIPIODIA) {
                ordenesPrincipioDia.add(orden); 
            }
            if (orden.getModo() == ModoOrden.FINDIA) {
                ordenesFinDia.add(orden);
            }
            if (orden.getModo() == ModoOrden.AHORA) {
                ordenesAhora.add(orden);
            }
        }
    }

    public void procesarOrden(Orden orden) {
        ServicioExternoService servicioExterno = new ServicioExternoService();
        Boolean response = null;
        if (orden.getOperacion() == Operacion.COMPRA) {
            response = servicioExterno.ordenCompra(orden) ;
        }
        if (orden.getOperacion() == Operacion.VENTA) {
            response = servicioExterno.ordenVenta(orden);
        }
        System.out.println("Orden de ("+orden.getOperacion()+") procesada:"+orden);
        System.out.println("Resultado:"+response+"\n");


    }

    public void procesarOrdenesProg () {
        LocalTime horaActual = LocalTime.now();
        LocalTime horaPrincipioDia = LocalTime.of(9, 0);
        LocalTime horaFInDia = LocalTime.of(18, 0);
        List<Orden> ordenes = null;
        if (horaActual.equals(horaFInDia)) {
            ordenes = ordenesFinDia;
        }
        if (horaActual.equals(horaPrincipioDia)) {
            ordenes = ordenesPrincipioDia;
        } 
        if (ordenes != null) {
            for (Orden orden : ordenes) {
                procesarOrden(orden);
            }
        } else {
            System.out.println("Nada por hacer, ordenes programadas:");
            System.out.println("\nOrdenes de Principio de Día:"+ordenesPrincipioDia);
            System.out.println("\nOrdenes de Fin de Día:"+ordenesFinDia);

        }

    }

    public void procesarOrdenesAhora() {
            if (ordenesAhora != null) {
                for (Orden orden : ordenesAhora) {
                    procesarOrden(orden);
                }
            }
    }


}
