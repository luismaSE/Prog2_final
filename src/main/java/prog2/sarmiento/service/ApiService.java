package prog2.sarmiento.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.service.dto.OrdenApiResponse;

@Service
@Transactional
public class ApiService {

    private final Logger log = LoggerFactory.getLogger(ApiService.class);

    private String JWT_TOKEN;


    public ApiService() {
        try {
            this.JWT_TOKEN = readTokenFromFile("/home/luisma_se/Documentos/programacion_2/token.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String readTokenFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] tokenBytes = Files.readAllBytes(path);
        return new String(tokenBytes).trim();
    }


    public HttpHeaders createHeadersWithJwt(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Auth", "Bearer " + jwt);
        return headers;
    }

    public HttpResponse<String> getApiResponse(String API_URL) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null; // Agrega un retorno en caso de error
        }
    }

    public List<Orden> obtenerOrdenesDesdeAPI() {
        List<Orden> ordenes = new ArrayList<>();
        try {
            String API_URL = "http://192.168.194.254:8000/api/ordenes/ordenes";
            HttpResponse<String> response = getApiResponse(API_URL);

            if (response != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                OrdenApiResponse ordenApiResponse = objectMapper.readValue(response.body(), OrdenApiResponse.class);
                ordenes = ordenApiResponse.getOrdenes();
            }
        } catch (IOException  e) {

            e.printStackTrace();

        }
        return ordenes;
    }

    public List<Integer> obtenerClientesDesdeAPI() {
        try {
            String API_URL = "http://192.168.194.254:8000/api/clientes/";
            HttpResponse<String> response = getApiResponse(API_URL);
            List<Integer> clienteIds = new ArrayList<>();
    
            if (response != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode clientesNode = rootNode.get("clientes");
    
                if (clientesNode.isArray()) {
                    for (JsonNode clienteNode : clientesNode) {
                        Integer clienteId = clienteNode.get("id").asInt();
                        clienteIds.add(clienteId);
                    }
                }
            }
            return clienteIds;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public String obtenerCompDesdeAPIconCodigo(String codigo) {
        try {
            String API_URL = "http://192.168.194.254:8000/api/acciones/buscar?codigo=" + codigo;
            HttpResponse<String> response = getApiResponse(API_URL);
            if (response != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode accionesNode = rootNode.get("acciones");
    
                if (accionesNode.isArray() && accionesNode.size() == 1) {
                    JsonNode accionNode = accionesNode.get(0);
                    return accionNode.get("codigo").asText();
                } else {
                    return "null";
                }
            }
            return "null";
        } catch (IOException e) {
            e.printStackTrace();
            return "null";
        }
    }
}
