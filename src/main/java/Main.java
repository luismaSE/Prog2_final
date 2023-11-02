import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.EstadoOrden;
import prog2.sarmiento.service.AnalizardorOrdenesService;
// import prog2.sarmiento.domain.OrdenApiResponse;
import prog2.sarmiento.service.ApiService;
import prog2.sarmiento.service.ProcesadorOrdenesService;

import java.util.ArrayList;
// import java.io.IOException;
// import java.net.http.HttpResponse;
import java.util.List;

import org.aspectj.weaver.ast.Or;

// import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando...");
        ApiService apiService = new ApiService();
        AnalizardorOrdenesService analizadorOrdenes = new AnalizardorOrdenesService(apiService);
        ProcesadorOrdenesService procesadorOrdenes = new ProcesadorOrdenesService();
        System.out.println("Servicios instanciados");
        System.out.println("Analizando...");
        List<List<Orden>> ordenesAnalisis = analizadorOrdenes.analizarOrdenes();
        System.out.println("Analisis terminado\n\nIniciando Clasificacion...");
        procesadorOrdenes.clasificarOrdenes(ordenesAnalisis.get(0));
        System.out.println("Clasificacion terminada\n\nIniciando procesamiento...");
        procesadorOrdenes.procesarOrdenesAhora();
        System.out.println("Ordenes instantaneas procesadas");
        procesadorOrdenes.procesarOrdenesProg();
        System.out.println("\nTodas las tareas han sido completadas");

        

    }
}
