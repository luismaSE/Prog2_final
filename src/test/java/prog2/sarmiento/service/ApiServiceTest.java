// package prog2.sarmiento.service;

// import static org.junit.Assert.assertNull;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.net.http.HttpResponse;
// import java.util.List;

// import org.junit.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import prog2.sarmiento.domain.Orden;

// @SpringBootTest
// public class ApiServiceTest {

//     @Autowired
//     ApiService apiService;
    
//     @Test
//     public void testObtenerClientesDesdeAPI() {
//         List<Integer> clienteIds = apiService.obtenerClientesDesdeAPI();

//         assertNotNull(clienteIds);
//         assertTrue(clienteIds.size() > 0);
//     }

//     @Test
//     public void testObtenerCompDesdeAPIconCodigoValido() {
//         String codigo = "AAPL"; // Código de acción válido

//         String accionCodigo = apiService.obtenerCompDesdeAPIconCodigo(codigo);

//         assertEquals(codigo, accionCodigo);
//     }

//     @Test
//     public void testObtenerCompDesdeAPIconCodigoNoValido() {
//         String codigo = "INVALID"; // Código de acción no válido

//         String accionCodigo = apiService.obtenerCompDesdeAPIconCodigo(codigo);

//         assertEquals("null", accionCodigo);
//     }

//     // @Test
//     // public void testConsultaEspejo() {
//     //     // Supongamos que tienes un JSON de órdenes válido como cadena
//     //     String jsonOrdenes = "[{...}]"; // JSON de órdenes válido

//     //     List<Orden> ordenes = apiService.consultaEspejo(jsonOrdenes);

//     //     assertNotNull(ordenes);
//     //     assertTrue(ordenes.size() > 0);
//     // }


//     // @Test
//     // public void testGetApiResponse() {
//     //     String API_URL = "http://example.com/api/data"; // URL válida

//     //     HttpResponse<String> response = apiService.getApiResponse(API_URL);

//     //     assertNotNull(response);
//     //     assertEquals(200, response.statusCode());
//     // }


//     @Test
//     public void testGetApiResponseWithInvalidURL() {
//         String API_URL = "http://example.com/nonexistent"; // URL no válida
    
//         HttpResponse<String> response = apiService.getApiMethod(API_URL);
    
//         assertNull(response);
//     }



// }
