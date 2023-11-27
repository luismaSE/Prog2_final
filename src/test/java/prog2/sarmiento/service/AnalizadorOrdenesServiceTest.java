package prog2.sarmiento.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.domain.enumeration.Operacion;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@SpringBootTest
class AnalizadorOrdenesServiceTest {

    @Mock
    private ApiService apiService;

    @InjectMocks
    private AnalizadorOrdenesService analizadorOrdenesService;

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

    // Agrega más pruebas según sea necesario
}
