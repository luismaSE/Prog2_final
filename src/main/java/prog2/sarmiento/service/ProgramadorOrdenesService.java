package prog2.sarmiento.service;


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
import prog2.sarmiento.repository.OrdenRepository;

@Service
@Transactional
public class ProgramadorOrdenesService {

    @Autowired OrdenRepository ordenRepository;
    @Autowired ProcesadorOrdenesService procesadorOrdenes;
    @Autowired ApiService apiService;

    private final Logger log = LoggerFactory.getLogger(ProgramadorOrdenesService.class);

    public Queue<Orden> ordenesFinDia = new LinkedList<>();
    public Queue<Orden> ordenesPrincipioDia = new LinkedList<>();


    public void programarOrdenes(List<Orden> ordenes) {
        for(Orden orden : ordenes) {
            if (!orden.getModo().equals(Modo.AHORA)) {
                orden.setEstado(Estado.PROG);
                orden.setDescripcionEstado("Programada para procesamiento");
                if (orden.getModo() == Modo.PRINCIPIODIA) {
                    ordenesPrincipioDia.add(orden);
                }
                if (orden.getModo() == Modo.FINDIA) {
                    ordenesFinDia.add(orden);
                }
                log.info("Orden Programada para su Procesamiento: "+orden);
                ordenRepository.save(orden);
                log.info("Orden ACtualizada en Base de datos: "+orden);

            }
        }
    }

    @Scheduled(cron = "0 15 18 * * ?")
    public void procOrdenesInicioDia() {
        log.info("procesando ordenes PRINCIPIODIA");
        while (!ordenesPrincipioDia.isEmpty()) {
            Orden orden = ordenesPrincipioDia.poll();
            Integer ultimoValor = apiService.obtenerUltimoValor(orden.getAccion());
            orden.setPrecio(ultimoValor);
            orden = procesadorOrdenes.procesarOrden(orden);
            ordenRepository.save(orden);
        }
    }

    @Scheduled(cron = "0 0 18 * * ?")
    public void procOrdenesFinDia() {
        log.info("procesando ordenes FINDIA");
        while (!ordenesFinDia.isEmpty()) {
            Orden orden = ordenesFinDia.poll();
            Integer ultimoValor = apiService.obtenerUltimoValor(orden.getAccion());
            orden.setPrecio(ultimoValor);
            orden = procesadorOrdenes.procesarOrden(orden);
            ordenRepository.save(orden);
        }
    } 

}
