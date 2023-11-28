package prog2.sarmiento.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.domain.enumeration.Operacion;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class AnalizadorOrdenesServiceTest {

    @Mock
    private ApiService apiService;

    @InjectMocks
    private AnalizadorOrdenesService analizadorOrdenesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testAnalizarOrdenOk() {
        Orden orden = new Orden();
        orden.setCliente(51101);
        orden.setAccion("AAPL");
        orden.setOperacion(Operacion.COMPRA);
        orden.setCantidad(10);
        orden.setModo(Modo.FINDIA);
        orden.setFechaOperacion(Instant.now());

        when(apiService.obtenerClientesDesdeAPI()).thenReturn(List.of(51101));
        when(apiService.obtenerCompDesdeAPIconCodigo("AAPL")).thenReturn("AAPL");
        when(apiService.obtenerCantidadDesdeAPI(anyInt(), anyInt())).thenReturn(20);

        analizadorOrdenesService.analizarOrden(orden);

        assertEquals(Estado.OK, orden.getEstado());
        assertEquals("Analisis: OK.", orden.getDescripcionEstado());
    }

    @Test
    void testAnalizarOrdenFail() {
        Orden orden = new Orden();
        orden.setCliente(51101);
        orden.setAccion("AAPL");
        orden.setOperacion(Operacion.VENTA);
        orden.setCantidad(30);
        orden.setModo(Modo.AHORA);
        orden.setFechaOperacion(Instant.now());

        when(apiService.obtenerClientesDesdeAPI()).thenReturn(List.of(51101));
        when(apiService.obtenerCompDesdeAPIconCodigo("AAPL")).thenReturn("AAPL");
        when(apiService.obtenerCantidadDesdeAPI(anyInt(), anyInt())).thenReturn(20); // no tiene sufiicientes acciones para vender

        analizadorOrdenesService.analizarOrden(orden);

        assertEquals(Estado.FAIL, orden.getEstado());
        assertNotEquals("Analisis: OK.", orden.getDescripcionEstado());
    }

    @Test
    void testRegistrarOrdenOk() {
        Orden ordenOk = new Orden();
        ordenOk.setEstado(Estado.OK);

        analizadorOrdenesService.registrarOrden(ordenOk);

        assertEquals(1, analizadorOrdenesService.ordenesOk.size());
        assertTrue(analizadorOrdenesService.ordenesOk.contains(ordenOk));
        assertEquals(0, analizadorOrdenesService.ordenesFail.size());
    }

    @Test
    void testRegistrarOrdenFail() {
        Orden ordenFail = new Orden();
        ordenFail.setEstado(Estado.FAIL);

        analizadorOrdenesService.registrarOrden(ordenFail);

        assertEquals(1, analizadorOrdenesService.ordenesFail.size());
        assertTrue(analizadorOrdenesService.ordenesFail.contains(ordenFail));
        assertEquals(0, analizadorOrdenesService.ordenesOk.size());
    }

    @Test
    void testAnalizarCliente() {
        // Configuración del mock
        List<Integer> clientesDesdeApi = Arrays.asList(123, 456, 789);
        when(apiService.obtenerClientesDesdeAPI()).thenReturn(clientesDesdeApi);

        // Prueba
        Orden orden = new Orden();
        orden.setCliente(456); // Cliente existente en la lista
        assertTrue(analizadorOrdenesService.analizarCliente(orden));

        orden.setCliente(999); // Cliente no existente en la lista
        assertFalse(analizadorOrdenesService.analizarCliente(orden));

        // Verificación del método llamado
        verify(apiService, times(2)).obtenerClientesDesdeAPI();
    }

    @Test
    void testAnalizarComp() {
        String codigoAccion = "AAPL";
        when(apiService.obtenerCompDesdeAPIconCodigo(anyString())).thenReturn(codigoAccion);

        // Prueba
        Orden orden = new Orden();
        orden.setAccion(codigoAccion);
        assertTrue(analizadorOrdenesService.analizarComp(orden));

        orden.setAccion("GOOGL"); // Código diferente al obtenido desde la API
        assertFalse(analizadorOrdenesService.analizarComp(orden));

        // Verificación del método llamado
        verify(apiService, times(2)).obtenerCompDesdeAPIconCodigo(anyString());
    }

    @Test
    void testAnalizarAccion() {
        Orden orden = new Orden();
        orden.setCantidad(5);
        assertTrue(analizadorOrdenesService.analizarAccion(orden));

        orden.setCantidad(0);
        assertFalse(analizadorOrdenesService.analizarAccion(orden));
    }

    @Test
    void testAnalizarCantidad() {
        // Configuración del mock
        when(apiService.obtenerCantidadDesdeAPI(anyInt(), anyInt())).thenReturn(10);

        // Prueba
        Orden orden = new Orden();
        orden.setCliente(123);
        orden.setAccionId(1);
        orden.setCantidad(5);
        assertTrue(analizadorOrdenesService.analizarCantidad(orden));

        orden.setCantidad(15); // Cantidad mayor a la disponible según la API
        assertFalse(analizadorOrdenesService.analizarCantidad(orden));

        // Verificación del método llamado
        verify(apiService, times(2)).obtenerCantidadDesdeAPI(anyInt(), anyInt());
    }

    @Test
    void testAnalizarHorario() {
        // Prueba
        Orden orden = new Orden();
        Instant fechaOperacion = Instant.parse("2023-01-01T12:00:00Z");
        orden.setFechaOperacion(fechaOperacion);
        assertTrue(analizadorOrdenesService.analizarHorario(orden));

        orden.setFechaOperacion(Instant.parse("2023-01-01T20:00:00Z")); // Fuera del horario permitido
        assertFalse(analizadorOrdenesService.analizarHorario(orden));
    }
}


