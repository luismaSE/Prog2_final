package prog2.sarmiento.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import prog2.sarmiento.domain.Orden;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

// @SpringBootTest
@ExtendWith(MockitoExtension.class)
class ApiServiceTest {

    @Mock
    private HttpClient client;

    @Mock
    private HttpRequest request;

    @Mock
    private HttpResponse<String> fakeResponse;

    @Spy
    private ApiService apiServiceSpy;

    

    @BeforeEach
    void setUp() {
        HttpResponse<String> fakeResponse = mock(HttpResponse.class);
        
    }

    @Test
    void testObtenerOrdenes() {
        String fakeJsonResponse = "{ \"ordenes\": [ { \"cliente\": 51107, \"accionId\": 6, \"accion\": \"YPF\", \"modo\": \"FINDIA\", \"cantidad\": 13, \"precio\": 58, \"operacion\": \"COMPRA\", \"fechaOperacion\": \"2023-09-01T17:13:27Z\" }, { \"cliente\": 51107, \"accionId\": 6, \"accion\": \"YPF\", \"modo\": \"AHORA\", \"cantidad\": 81, \"precio\": 16, \"operacion\": \"COMPRA\", \"fechaOperacion\": \"2022-11-24T18:56:30Z\" } ] }"; 
        when(fakeResponse.body()).thenReturn(fakeJsonResponse);
        when(fakeResponse.statusCode()).thenReturn(200);
        // Mockeamos solo el método getApiMethod
        doReturn(fakeResponse).when(apiServiceSpy).getApiMethod(anyString());


        List<Orden> ordenes = apiServiceSpy.obtenerOrdenesDesdeAPI();

        assertEquals(2, ordenes.size());
    }
    

    @Test
    void testObtenerClientes() {
        String fakeJsonResponse = "{ \"clientes\": [ { \"id\": 51101, \"nombreApellido\": \"María Corvalán\", \"empresa\": \"Happy Soul\" }, { \"id\": 51102, \"nombreApellido\": \"Ricardo Tapia\", \"empresa\": \"Salud Zen\" }, { \"id\": 51103, \"nombreApellido\": \"Valeria Rodriguez\", \"empresa\": \"Health Co\" }, { \"id\": 51104, \"nombreApellido\": \"Liliana Suarez\", \"empresa\": \"Magic Clinic\" }, { \"id\": 51105, \"nombreApellido\": \"Víctor Romero\", \"empresa\": \"Sky Jewelry\" }, { \"id\": 51106, \"nombreApellido\": \"Raúl Flores\", \"empresa\": \"Trendy Room\" }, { \"id\": 51107, \"nombreApellido\": \"Horacio Torres\", \"empresa\": \"PulserArte\" }, { \"id\": 51108, \"nombreApellido\": \"Mónica Garcia\", \"empresa\": \"Tienda urbana\" }, { \"id\": 51109, \"nombreApellido\": \"Ana Medina\", \"empresa\": \"Tienda vintage\" }, { \"id\": 51110, \"nombreApellido\": \"Ricardo Campos\", \"empresa\": \"Moda casual\" }, { \"id\": 51111, \"nombreApellido\": \"Victoria Vazquez\", \"empresa\": \"Cool fit\" }, { \"id\": 51112, \"nombreApellido\": \"Guadalupe Prieto\", \"empresa\": \"Sabor Natural\" } ] }";
        when(fakeResponse.body()).thenReturn(fakeJsonResponse);
        when(fakeResponse.statusCode()).thenReturn(200);
        // Mockeamos solo el método getApiMethod
        doReturn(fakeResponse).when(apiServiceSpy).getApiMethod(anyString());

        List<Integer> clientes = apiServiceSpy.obtenerClientesDesdeAPI();
        List<Integer> clientesEsperados = List.of(51101, 51102, 51103, 51104, 51105, 51106, 51107, 51108, 51109, 51110, 51111, 51112);
        assertEquals(12, clientes.size());
        assertEquals(clientes, clientesEsperados);
    }


    @Test
    void testObtenerComp() {
        String fakeJsonResponse = "{ \"acciones\": [ { \"id\": 13, \"codigo\": \"PAM\", \"empresa\": \"Pampa Energia SA\" } ] }";
        when(fakeResponse.body()).thenReturn(fakeJsonResponse);
        when(fakeResponse.statusCode()).thenReturn(200);
        // Mockeamos solo el método getApiMethod
        doReturn(fakeResponse).when(apiServiceSpy).getApiMethod(anyString());

        String codigoEsperado = "PAM";
        String codigo = apiServiceSpy.obtenerCompDesdeAPIconCodigo(codigoEsperado);  
        assertEquals(codigoEsperado, codigo);
        
    }


    @Test
    void testObtenerCantidad() {
        String fakeJsonResponse = "{ \"cliente\": 51110, \"accionId\": 251159, \"accion\": \"GLOB\", \"cantidadActual\": 30, \"observaciones\": \"Acciones presentes\" }";
        when(fakeResponse.body()).thenReturn(fakeJsonResponse);
        when(fakeResponse.statusCode()).thenReturn(200);
        // Mockeamos solo el método getApiMethod
        doReturn(fakeResponse).when(apiServiceSpy).getApiMethod(anyString());

        Integer cantidadEsperada = 30;
        Integer cantidad = apiServiceSpy.obtenerCantidadDesdeAPI(51110, 251159);
        assertEquals(cantidadEsperada, cantidad);
    
    }

    @Test
    void testObtenerUltimoValor() {
        String fakeJsonResponse = "{ \"codigo\": \"GOOGL\", \"empresa\": \"Alphabet Inc. (google)\", \"ultimoValor\": { \"fechaHora\": \"19:10:30\", \"valor\": 125.42152239159692 }, \"valores\": null }";
        when(fakeResponse.body()).thenReturn(fakeJsonResponse);
        when(fakeResponse.statusCode()).thenReturn(200);
        // Mockeamos solo el método getApiMethod
        doReturn(fakeResponse).when(apiServiceSpy).getApiMethod(anyString());

        Integer ultimoValorEsperado = 125;
        Integer ultimoValor = apiServiceSpy.obtenerUltimoValor("GOOGL");
        assertEquals(ultimoValorEsperado, ultimoValor);
    }


}