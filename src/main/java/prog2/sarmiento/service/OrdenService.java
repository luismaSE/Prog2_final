package prog2.sarmiento.service;

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
public class OrdenService {

    private final String API_URL = "http://192.168.194.254:8000/api/ordenes/ordenes";
    private String JWT_TOKEN;


    public OrdenService() {
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


    public List<Orden> obtenerOrdenesDesdeAPI() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest
                .newBuilder().uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .build();
        
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Deserializa la respuesta JSON en una lista de objetos Orden
            ObjectMapper objectMapper = new ObjectMapper();
            OrdenApiResponse ordenApiResponse = objectMapper.readValue(response.body(), OrdenApiResponse.class);
            return ordenApiResponse.getOrdenes();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
}
