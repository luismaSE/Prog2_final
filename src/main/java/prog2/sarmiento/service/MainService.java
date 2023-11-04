package prog2.sarmiento.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.repository.OrdenRepository;

@Service
@Transactional
public class MainService {

    private final Logger log = LoggerFactory.getLogger(MainService.class);

    @Autowired
    ApiService apiService;
    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    AnalizadorOrdenesService analizadorOrdenes;
    @Autowired
    ProcesadorOrdenesService procesadorOrdenes;

    public List<Orden> ordenesAhora = new ArrayList<>();
    
    private Queue<Orden> ordenesPendientes = new LinkedList<>();
    public Queue<Orden> ordenesFinDia = new LinkedList<>();
    public Queue<Orden> ordenesPrincipioDia = new LinkedList<>();

    private final LocalTime HORA_PRINCIPIO_DIA = LocalTime.of(13, 0);
    private final LocalTime HORA_FIN_DIA = LocalTime.of(18, 0);


    public String Serve() {
    log.info("Iniciando Procesamiento de Ordenes...");
    log.info("Obteniendo nuevas Ordenes...");
    ordenesPendientes.addAll(apiService.obtenerOrdenesDesdeAPI());
    
    
    log.info("Analizando Ordenes...");

    while (!ordenesPendientes.isEmpty()) {
        Orden orden = analizadorOrdenes.analizarOrden(ordenesPendientes.poll());
        log.info("Orden Analizada: "+orden);

        if (orden.getModo().equals(Modo.AHORA) && orden.getEstado().equals(Estado.OK)) {
            orden = procesadorOrdenes.procesarOrden(orden);
            log.info("Orden Inmediata Procesada: "+orden);
        }
        analizadorOrdenes.registrarOrden(orden);
        orden = ordenRepository.save(orden);
        log.info("Orden Guardada en Base de Datos: "+orden);


    }

    String estado = analizadorOrdenes.mostrarResultadoAnalisis();
    List<List<Orden>> analisis = analizadorOrdenes.terminarAnalisis();
    programarOrdenes(analisis.get(0));
    revisarOrdenesProg();


    return (estado);

    }


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


    public void revisarOrdenesProg() {
        LocalTime horaActual = LocalTime.now();
        log.info("\nHora actual:"+horaActual);
        if (horaActual.getHour() == (HORA_PRINCIPIO_DIA.getHour())) {
            log.info("Iniciando Procesamiento de Ordenes Programadas para PRINCIPIODIA");
                while (!ordenesPrincipioDia.isEmpty()) {
                    Orden orden = procesadorOrdenes.procesarOrden(ordenesPrincipioDia.poll());
                    ordenRepository.save(orden);
                }
        } else if (horaActual.getHour() == (HORA_FIN_DIA.getHour())) {

            log.info("Iniciando Procesamiento de Ordenes Programadas para FINDIA");
            while (!ordenesFinDia.isEmpty()) {
                Orden orden = procesadorOrdenes.procesarOrden(ordenesFinDia.poll());
                ordenRepository.save(orden);
            }
        } else {
            log.info("\nNada por hacer, terminando ejecucion");
        }
    } 
}
