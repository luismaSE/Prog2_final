package prog2.sarmiento.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.domain.enumeration.Operacion;
import prog2.sarmiento.repository.OrdenRepository;

@Service
@Transactional
public class ProcesadorOrdenesService {

    @Autowired ApiService apiService;
    @Autowired OrdenRepository ordenRepository;
    @Autowired ExternalService servicioExterno;
    @Autowired ReportarOrdenesService reportarOrdenes;


    private final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesService.class);
    
    public Queue<Orden> ordenesFinDia = new LinkedList<>();
    public Queue<Orden> ordenesPrincipioDia = new LinkedList<>();


    public void addOrden(Orden orden) {
        if (orden.getModo().equals(Modo.PRINCIPIODIA)) {
            ordenesPrincipioDia.add(orden);
        }
        if (orden.getModo().equals(Modo.FINDIA)) {
            ordenesFinDia.add(orden);
        }
    }

    public Orden procesarOrden(Orden orden) {
        Boolean response = false;
        if (orden.getOperacion().equals(Operacion.COMPRA)) {
            response = servicioExterno.ordenCompra(orden);
        } else if (orden.getOperacion().equals(Operacion.VENTA)) {
            response = servicioExterno.ordenVenta(orden);
        }
            if (response) {
                orden.setEstado(Estado.COMPLETE);
                orden.setDescripcionEstado("Orden COMPLETADA");;
                log.info("Orden de (" + orden.getOperacion() + ") procesada correctamente:" + orden);
            } else {
                orden.setEstado(Estado.FAIL);
                orden.setDescripcionEstado("Orden FALLIDA");
                log.info("Orden de (" + orden.getOperacion() + ") procesada con error:" + orden);
            }
        log.info("Orden Procesada: "+orden);
        reportarOrdenes.addOrden(orden);
        return orden;
    }


    public void programarOrdenes(List<Orden> ordenes) {
        for(Orden orden : ordenes) {
            if (!orden.getModo().equals(Modo.AHORA)) {
                orden.setEstado(Estado.PROG);
                orden.setDescripcionEstado("Programada para procesamiento");
                addOrden(orden);
                log.info("Orden Programada para su Procesamiento: "+orden);
                ordenRepository.save(orden);
                log.info("Orden ACtualizada en Base de datos: "+orden);
            }   

            }
        }

    @Scheduled(cron = "0 0 9 * * ?")
    public List<Orden> procOrdenesInicioDia() {
        List<Orden> ordenes = new ArrayList<>();
        log.info("procesando ordenes PRINCIPIODIA");
        while (!ordenesPrincipioDia.isEmpty()) {
            Orden orden = ordenesPrincipioDia.poll();
            Double ultimoValor = apiService.obtenerUltimoValor(orden.getAccion());
            orden.setPrecio(ultimoValor);
            orden = procesarOrden(orden);
            orden = ordenRepository.save(orden);
            ordenes.add(orden);

        }
        reportarProcesadas();
        return ordenes;
    }

    @Scheduled(cron = "0 0 18 * * ?")
    public List<Orden> procOrdenesFinDia() {
        List<Orden> ordenes = new ArrayList<>();
        log.info("procesando ordenes FINDIA");
        while (!ordenesFinDia.isEmpty()) {
            Orden orden = ordenesFinDia.poll();
            Double ultimoValor = apiService.obtenerUltimoValor(orden.getAccion());
            orden.setPrecio(ultimoValor);
            orden = procesarOrden(orden);
            orden = ordenRepository.save(orden);
            ordenes.add(orden);
        }
        reportarProcesadas();
        return ordenes;
    } 


    public void reportarProcesadas(){
        String reporte = reportarOrdenes.reporte();
        try {
            apiService.postReportar(reporte);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
