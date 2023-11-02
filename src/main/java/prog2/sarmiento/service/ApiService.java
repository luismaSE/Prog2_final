package prog2.sarmiento.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.OrdenApiResponse;



@Service
@Transactional
public class ApiService {

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
        try {
            String API_URL = "http://192.168.194.254:8000/api/ordenes/ordenes";
            HttpResponse<String> response = getApiResponse(API_URL);
            if (response != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                OrdenApiResponse ordenApiResponse = objectMapper.readValue(response.body(), OrdenApiResponse.class);
                return ordenApiResponse.getOrdenes();
            } else {
                // Manejar caso en que la respuesta sea nula
                return Collections.emptyList();
            }
        } catch (IOException e) {
            // Manejar excepción de lectura
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public List<Long> obtenerClientesDesdeAPI() {
        try {
            String API_URL = "http://192.168.194.254:8000/api/clientes/";
            HttpResponse<String> response = getApiResponse(API_URL);
            List<Long> clienteIds = new ArrayList<>();
    
            if (response != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode clientesNode = rootNode.get("clientes");
    
                if (clientesNode.isArray()) {
                    for (JsonNode clienteNode : clientesNode) {
                        long clienteId = clienteNode.get("id").asLong();
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
    

    

    // public List<Orden> obtenerOrdenesDesdeAPI() {
    //     String API_URL = "http://192.168.194.254:8000/api/ordenes/ordenes";
    //     HttpResponse<String> response = getApiResponse(API_URL);
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     OrdenApiResponse ordenApiResponse = objectMapper.readValue(response.body(), OrdenApiResponse.class);
    //     return ordenApiResponse.getOrdenes();
    // }

    // public List<Long> obtenerClientesDesdeAPI() {
    //     String API_URL = "http://192.168.194.254:8000/api/clientes/";
    //     HttpResponse<String> response = getApiResponse(API_URL);
    //     List<Long> clienteIds = new ArrayList<>();

    //     if (response != null) {
    //         try {
    //             ObjectMapper objectMapper = new ObjectMapper();
    //             JsonNode rootNode = objectMapper.readTree(response.body());
    //             JsonNode clientesNode = rootNode.get("clientes");

    //             if (clientesNode.isArray()) {
    //                 for (JsonNode clienteNode : clientesNode) {
    //                     long clienteId = clienteNode.get("id").asLong();
    //                     clienteIds.add(clienteId);
    //                 }
    //             }
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     }

    //     return clienteIds;
    // }

    // public String obetenerCompDesdeAPIconCodigo(String codigo) {
    //     String API_URL = "http://192.168.194.254:8000/api/acciones/buscar?codigo="+codigo;
    //     HttpResponse<String> response = getApiResponse(API_URL);
    //     if (response != null) {
    //         try {
    //             ObjectMapper objectMapper = new ObjectMapper();
    //             JsonNode rootNode = objectMapper.readTree(response.body());
    //             JsonNode accionesNode = rootNode.get("acciones");

    //             if (accionesNode.isArray() && accionesNode.size() == 1) {
    //                 JsonNode accionNode = accionesNode.get(0);
    //                 return accionNode.get("codigo").asText();
    //             } else {
    //                 return "null";
    //             }

    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     }

    // }

}

    // public HttpResponse<String> getApiResponse(String API_URL) {
    //     HttpClient client = HttpClient.newHttpClient();
    //     HttpRequest request = HttpRequest
    //             .newBuilder().uri(URI.create(API_URL))
    //             .header("Authorization", "Bearer " + JWT_TOKEN)
    //             .build();
    //     try {
    //         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    //         return response;
    //     } catch (IOException | InterruptedException e) {
    //         e.printStackTrace();
    //     }
    // }

    // public List<Orden> obtenerOrdenesDesdeAPI() {
    //     String API_URL = "http://192.168.194.254:8000/api/ordenes/ordenes";
    //     HttpResponse<String> response = getApiResponse(API_URL);
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     OrdenApiResponse ordenApiResponse = objectMapper.readValue(response.body(), OrdenApiResponse.class);
    //     return ordenApiResponse.getOrdenes();
        
    // }


    // public List<Integer> obetenerClientesDesdeAPI() {
    //     String API_URL = "http://192.168.194.254:8000/api/clientes/";
    //     HttpResponse<String> response = getApiResponse(API_URL);


    // }

    
