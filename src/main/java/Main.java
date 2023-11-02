import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.EstadoOrden;
import prog2.sarmiento.service.AnalizarOrdenService;
import prog2.sarmiento.service.AnalizarOrdenesService;
// import prog2.sarmiento.domain.OrdenApiResponse;
import prog2.sarmiento.service.ApiService;

import java.util.ArrayList;
// import java.io.IOException;
// import java.net.http.HttpResponse;
import java.util.List;

import org.aspectj.weaver.ast.Or;

// import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) {
        ApiService apiService = new ApiService();
        AnalizarOrdenesService analizadorOrdenes = new AnalizarOrdenesService(apiService);
        List<List<Orden>> ordenesAnalisis = analizadorOrdenes.analizarOrdenes();
        for (List<Orden> lista : ordenesAnalisis) {
            System.out.println(lista);
        }
    }
}