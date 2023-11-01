import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.service.OrdenService;

import java.net.http.HttpResponse;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Crea una instancia del servicio OrdenService
        OrdenService ordenService = new OrdenService();
        System.out.println(ordenService);
        // Llama al método para obtener las órdenes desde la API
        HttpResponse <String> response = ordenService.obtenerOrdenesDesdeAPI();
        // System.out.println(response.body());
    //     // Procesa las órdenes obtenidas
    //     for (Orden orden : ordenes) {
    //         System.out.println(orden);
    //     }
    }
}
