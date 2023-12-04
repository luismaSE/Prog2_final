package prog2.sarmiento.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.service.dto.OrdenJsonWrapper;

@Service
@Transactional
public class ApiService {

    @Value("${apiCatedra.jwt}") private String JWT_TOKEN;
    @Value("${apiCatedra.url-api}") private String URL_API;
    @Value("${apiCatedra.url-ordenes}") private String URL_ORDENES;
    @Value("${apiCatedra.url-espejo}") private String URL_ESPEJO;
    @Value("${apiCatedra.url-reportar}") private String URL_REPORTAR;
    @Value("${apiCatedra.url-clientes}") private String URL_CLIENTES;
    @Value("${apiCatedra.url-acciones}") private String URL_ACCIONES;



    private final Logger log = LoggerFactory.getLogger(ApiService.class);
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private OrdenJsonWrapper ordenApiResponse;
    private final HttpClient client;

    public ApiService() {
        this.client = HttpClient.newHttpClient();
        log.info("Token extraido correctamente");
    }

    public HttpResponse<String> sendRequest(String url, HttpRequest.BodyPublisher bodyPublisher, String method) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .header("Content-Type", "application/json")
                .method(method, bodyPublisher)
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error("Error llamando a la API", e);
            return null;
        }
    }

    public HttpResponse<String> getApiMethod(String API_URL) {
        log.info("Estableciendo conexión con la API");
        return sendRequest(API_URL, HttpRequest.BodyPublishers.noBody(), "GET");
    }

    public HttpResponse<String> postApiMethod(String API_URL, String jsonOrdenes) {
        log.info("Estableciendo conexión con la API");
        return sendRequest(API_URL, HttpRequest.BodyPublishers.ofString(jsonOrdenes), "POST");
    }


    public List<Orden> obtenerOrdenesDesdeAPI() {
        List<Orden> ordenes = new ArrayList<>();
        String API_URL = URL_API + URL_ORDENES;
        log.info("Obteniendo listado de ordenes por Procesar...");
        HttpResponse<String> response = getApiMethod(API_URL);
        if (response.statusCode() == 200) {
            ordenes = mapOrdenes(response.body());
        }
        return ordenes;
    }
    
    public String postEspejo(String jsonString) throws IOException, InterruptedException {
        String API_URL = URL_API + URL_ESPEJO;
    
        log.info("Enviando ordenes a espejo...");
        HttpResponse<String> response = postApiMethod(API_URL, jsonString);
    
        if (response.statusCode() == 200) {
            log.info("Ordenes enviadas a espejo correctamente");
            return response.body();
        } else {
            log.error("Error al enviar ordenes a espejo: " + response.body());
            throw new RuntimeException("Error al enviar ordenes a espejo, código de estado: " + response.statusCode());
        }
    }

    public String postReportar (String jsonString) throws IOException, InterruptedException {
        String API_URL = URL_API + URL_REPORTAR;
        log.info("Enviando ordenes para reportar...");
        HttpResponse<String> response = postApiMethod(API_URL, jsonString);
        if (response.statusCode() == 200) {
            log.info("Ordenes enviadas para reportar correctamente");
            return response.body();
        } else {
            log.error("Error al enviar ordenes para reportar: " + response.body());
            throw new RuntimeException("Error al enviar ordenes para reportar, código de estado: " + response.statusCode());
        }
    }

 
    public List<Integer> obtenerClientesDesdeAPI() {
        String API_URL = URL_API + URL_CLIENTES;
        log.info("Obteniendo listado de Clientes...");
        HttpResponse<String> response = getApiMethod(API_URL);
        List<Integer> clienteIds = new ArrayList<>();
        if (response.statusCode() == 200) {
            String responseBody = response.body();
            JsonNode rootNode;
            try {
                rootNode = objectMapper.readTree(responseBody);
                JsonNode lista = rootNode.get("clientes");
                for (JsonNode nodo : lista) {
                    clienteIds.add(nodo.get("id").asInt());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return clienteIds;
    } 
    public String obtenerCompDesdeAPIconCodigo(String codigo) {
        String API_URL = URL_API + URL_ACCIONES + "/buscar?codigo=" + codigo;
        log.info("Obteniendo Codigo de Acción...");
        HttpResponse<String> response = getApiMethod(API_URL);
        if (response.statusCode() != 200) {
            return "null";
        }
        String responseBody = response.body();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            if (rootNode.size() != 1) {
                return "null";
            }
            JsonNode node = rootNode.get("acciones");
            return node.get(0).get("codigo").asText();
        } catch (IOException e) {
            log.error("Error: "+ e.getMessage());
            return "null";
        }
    }


    public Integer obtenerCantidadDesdeAPI(Integer clienteId,Integer accionId) {
        String API_URL = URL_API + URL_ACCIONES + "/buscar?clienteId=" + clienteId + "&accionId=" + accionId;
        log.info("Obteniendo cantidad de Acción...");
        HttpResponse<String> response = getApiMethod(API_URL);
        if (response.statusCode() != 200) {
            return null;
        }
        String responseBody = response.body();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode cantidadActualNode = rootNode.get("cantidadActual");
            if (cantidadActualNode == null) {
                return 0;
            }
            return cantidadActualNode.asInt();
        } catch (IOException e) {
            log.error("Error: "+ e.getMessage());
            return null;
        }
    }
    
    
    
    public Double obtenerUltimoValor (String codigo) {
        String API_URL = URL_API + URL_ACCIONES + "/ultimovalor/" + codigo;
        log.info("Obteniendo Ultimo Valor de Acción...");
        try {
            HttpResponse<String> response = getApiMethod(API_URL);
            if (response.statusCode() == 200) {
                log.info("Ultimo Valor obtenido");
                String responseBody = response.body();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode node = rootNode.get("ultimoValor");
                Double ultimoValor = node.get("valor").asDouble();
                return ultimoValor;
            } else {
                throw new IOException("No se pudo obtener el ultimo valor");
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Orden> mapOrdenes (String jsonOrdenes) {
        List<Orden> ordenes = new ArrayList<>();
        try {
            ordenApiResponse = objectMapper.readValue(jsonOrdenes, OrdenJsonWrapper.class);
            ordenes = ordenApiResponse.getOrdenes();
        } catch (Exception e) {
            log.error("Error al mapear ordenes: {}", e.getMessage());
        }
        return ordenes;
    }

}
 
