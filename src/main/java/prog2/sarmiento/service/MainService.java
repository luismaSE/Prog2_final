package prog2.sarmiento.service;

import java.io.IOException;
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
public class MainService {

    private final Logger log = LoggerFactory.getLogger(MainService.class);

    @Autowired ApiService apiService;
    @Autowired OrdenRepository ordenRepository;
    @Autowired ReportarOrdenesService reportarOrdenes;
    @Autowired GeneradorOrdenService generadorOrdenes;
    @Autowired AnalizadorOrdenesService analizadorOrdenes;
    @Autowired ProcesadorOrdenesService procesadorOrdenes;

    private Queue<Orden> ordenesPendientes = new LinkedList<>();

    @Scheduled(cron = "0/10 * 9-17 * * ?")
    public String Serve() {
        try {
            apiService.postEspejo(generadorOrdenes.generarOrdenes());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Iniciando Procesamiento de Ordenes...");
        log.info("Obteniendo nuevas Ordenes...");
        ordenesPendientes.addAll(apiService.obtenerOrdenesDesdeAPI());
        log.info("Analizando Ordenes...");

        while (!ordenesPendientes.isEmpty()) {
            Orden orden = analizadorOrdenes.analizarOrden(ordenesPendientes.poll());
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
}
