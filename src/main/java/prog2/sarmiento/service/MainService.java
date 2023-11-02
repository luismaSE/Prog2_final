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
import prog2.sarmiento.domain.enumeration.EstadoOrden;
import prog2.sarmiento.domain.enumeration.ModoOrden;
import prog2.sarmiento.repository.OrdenRepository;

@Service
// @Transactional
public class MainService {

    // @Autowired
    // private OrdenRepository ordenRepository;

    public List<Orden> ordenesAhora = new ArrayList<>();
    
    private Queue<Orden> ordenesPendientes = new LinkedList<>();
    public Queue<Orden> ordenesFinDia = new LinkedList<>();
    public Queue<Orden> ordenesPrincipioDia = new LinkedList<>();

    ApiService apiService = new ApiService();
    AnalizadorOrdenesService analizadorOrdenes = new AnalizadorOrdenesService(apiService);
    ProcesadorOrdenesService procesadorOrdenes = new ProcesadorOrdenesService();

    private final LocalTime HORA_PRINCIPIO_DIA = LocalTime.of(9, 0);
    private final LocalTime HORA_FIN_DIA = LocalTime.of(18, 0);


    Long i = (long) 0;

    private final Logger log = LoggerFactory.getLogger(MainService.class);

    public void Serve() {
    System.out.println("Iniciando...");
    System.out.println("Obteniendo nuevas Ordenes...");
    ordenesPendientes.addAll(apiService.obtenerOrdenesDesdeAPI());
    
    
    System.out.println("Analizando Ordenes...");

    while (!ordenesPendientes.isEmpty()) {
        Orden orden = analizadorOrdenes.analizarOrden(ordenesPendientes.poll());
        orden.setId(i);;

        if (orden.getModo().equals(ModoOrden.AHORA) && orden.getEstado().equals(EstadoOrden.OK)) {
            orden = procesadorOrdenes.procesarOrden(orden);
        }
        // ordenRepository.save(orden);
        analizadorOrdenes.registrarOrden(orden);
        i++;
    }

    analizadorOrdenes.mostrarResultadoAnalisis();
    List<List<Orden>> analisis = analizadorOrdenes.terminarAnalisis();
    programarOrdenes(analisis.get(0));
    revisarOrdenesProg();

    System.out.println("\nTodas las tareas han sido completadas");

    }











    public void programarOrdenes(List<Orden> ordenes) {
        for(Orden orden : ordenes) {
            if (!orden.getModo().equals(ModoOrden.AHORA)) {
                orden.setEstado(EstadoOrden.PROG);
                orden.setDescripcionEstado("Programada para procesamiento");;
                if (orden.getModo() == ModoOrden.PRINCIPIODIA) {
                    ordenesPrincipioDia.add(orden);
                }
                if (orden.getModo() == ModoOrden.FINDIA) {
                    ordenesFinDia.add(orden);
                }
            }
        }
    }


    public void revisarOrdenesProg() {
        LocalTime horaActual = LocalTime.now();
        System.out.println("\nHora actual:"+horaActual);
        if (horaActual.equals(HORA_PRINCIPIO_DIA)) {
                while (!ordenesPrincipioDia.isEmpty()) {
                    Orden orden = procesadorOrdenes.procesarOrden(ordenesPrincipioDia.poll());
                }
        } else if (horaActual.equals(HORA_FIN_DIA)) {
            while (!ordenesFinDia.isEmpty()) {
                Orden orden = procesadorOrdenes.procesarOrden(ordenesFinDia.poll());
            }
        } else {
            System.out.println("\nNada por hacer");
        }
    }
}
