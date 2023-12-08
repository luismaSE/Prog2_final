package prog2.sarmiento.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.repository.OrdenRepository;



@Service
@Transactional
public class MainService {

    @Value("${procesador.dev}") private Boolean DEV;

    private final Logger log = LoggerFactory.getLogger(MainService.class);

    @Autowired ApiService apiService;
    @Autowired OrdenService ordenService;
    @Autowired OrdenRepository ordenRepository;
    @Autowired ReportarOrdenesService reportarOrdenes;
    @Autowired GeneradorOrdenService generadorOrdenes;
    @Autowired AnalizadorOrdenesService analizadorOrdenes;
    @Autowired ProcesadorOrdenesService procesadorOrdenes;

    private Queue<Orden> ordenesPendientes = new LinkedList<>();

    @Scheduled(cron = "0/10 * 9-17 * * ?")
    public String Serve() {
        if (DEV) {generadorOrdenes();}
        log.info("Iniciando Procesamiento de Ordenes...");
        log.info("Obteniendo nuevas Ordenes...");
        ordenesPendientes.addAll(apiService.obtenerOrdenesDesdeAPI());
        log.info("Analizando Ordenes...");

        while (!ordenesPendientes.isEmpty()) {
            Orden orden = ordenesPendientes.poll();
            orden = ordenRepository.save(orden);
            orden = analizadorOrdenes.analizarOrden(orden);
            log.info("Orden Analizada: "+orden);
            analizadorOrdenes.analizarOrden(orden);
            log.info("Orden Analizada: "+orden);

            if (orden.getModo().equals(Modo.AHORA) && orden.getEstado().equals(Estado.OK)) {
                orden = procesadorOrdenes.procesarOrden(orden);
            }
            analizadorOrdenes.registrarOrden(orden);
            orden = ordenRepository.save(orden);
            log.info("Orden Guardada en Base de Datos: "+orden);
        }

        String estado = analizadorOrdenes.mostrarResultadoAnalisis();
        log.info("Resultado del Analisis: "+estado);
        List<List<Orden>> analisis = analizadorOrdenes.terminarAnalisis();
        procesadorOrdenes.programarOrdenes(analisis.get(0));
        analizadorOrdenes.limpiarAnalisis();
        analisis.clear();
        procesadorOrdenes.reportarProcesadas();
        return (estado);
    }


    @PostConstruct
    public void recuperarOrdenes() {
        log.info("Buscando Ordenes previas...");
        List<Orden> ordenes = ordenService.findOrdenesPend();
        log.info("Ordenes Recuperadas: "+ordenes.size());
        for (Orden orden : ordenes) {
            if (orden.getEstado().equals(Estado.PROG)) {
                procesadorOrdenes.addOrden(orden);
            } else if (orden.getEstado().equals(Estado.OK) || orden.getEstado().equals(null))  { 
                ordenesPendientes.add(orden);
            }
        }
    }

    public void generadorOrdenes () {
        try {
            apiService.postEspejo(generadorOrdenes.generarOrdenes());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    
}
