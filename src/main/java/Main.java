import prog2.sarmiento.domain.Orden;
// import prog2.sarmiento.domain.OrdenApiResponse;
import prog2.sarmiento.service.OrdenService;

// import java.io.IOException;
// import java.net.http.HttpResponse;
import java.util.List;

// import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) {

        OrdenService ordenService = new OrdenService();
        System.out.println(ordenService);
        List<Orden> response = ordenService.obtenerOrdenesDesdeAPI();
        System.out.println(response);
        for (Orden orden : response) {
            // funcion de procesamiento
        }    
    }
}