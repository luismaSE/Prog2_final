import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import prog2.sarmiento.domain.Orden;

import java.util.List;

@Service
public class OrdenService {
    private final String API_URL = "/api/ordenes/ordenes";

    private final RestTemplate restTemplate;

    public OrdenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Orden> obtenerOrdenesDesdeAPI() {
        // Realiza una solicitud HTTP a la API
        ResponseEntity<OrdenApiResponse> response = restTemplate.getForEntity(API_URL, OrdenApiResponse.class);

        // Extrae las órdenes del cuerpo de la respuesta
        List<Orden> ordenes = response.getBody().getOrdenes();

        return ordenes;
    }
}
