package prog2.sarmiento.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.domain.enumeration.Operacion;

import java.time.Instant;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ReportarOrdenesServiceTest {


    @InjectMocks
    private ReportarOrdenesService reportarOrdenesService;

    @Test
    void addOrden() {
        Orden orden = new Orden();
        reportarOrdenesService.addOrden(orden);
        assertEquals(1, reportarOrdenesService.ordenesReport.size());
    }

    @Test
    void reporte() {
        // Arrange
        Orden orden1 = new Orden();
        orden1.setCliente(1);
        orden1.setAccionId(1);
        orden1.setAccion("AAPL");
        orden1.setModo(Modo.AHORA);
        orden1.setCantidad(10);
        orden1.setPrecio(100);
        orden1.setFechaOperacion(Instant.now());
        orden1.setOperacion(Operacion.COMPRA);
        orden1.setDescripcionEstado("Orden completada");
        orden1.setEstado(Estado.COMPLETE);

        Orden orden2 = new Orden();
        orden2.setCliente(2);
        orden2.setAccionId(2);
        orden2.setAccion("GOOGL");
        orden2.setModo(Modo.AHORA);
        orden2.setCantidad(20);
        orden2.setPrecio(200);
        orden2.setFechaOperacion(Instant.now());
        orden2.setOperacion(Operacion.VENTA);
        orden2.setDescripcionEstado("Orden completada");
        orden2.setEstado(Estado.COMPLETE);

        reportarOrdenesService.addOrden(orden1);
        reportarOrdenesService.addOrden(orden2);

        // when(ordenesReport.toArray()).thenReturn(new Orden[]{orden1, orden2});

        // Act
        String reporte = reportarOrdenesService.reporte();

        // Assert
        String expectedJson = "{\"ordenes\":[{\"cliente\":1,\"accionId\":1,\"accion\":\"AAPL\",\"modo\":\"AHORA\",\"cantidad\":10,\"precio\":100,\"fechaOperacion\":\"" + orden1.getFechaOperacion().toString() + "\",\"operacion\":\"COMPRA\",\"operacionObservaciones\":\"Orden completada\",\"operacionExitosa\":true},{\"cliente\":2,\"accionId\":2,\"accion\":\"GOOGL\",\"modo\":\"AHORA\",\"cantidad\":20,\"precio\":200,\"fechaOperacion\":\"" + orden2.getFechaOperacion().toString() + "\",\"operacion\":\"VENTA\",\"operacionObservaciones\":\"Orden completada\",\"operacionExitosa\":true}]}";
        assertEquals(expectedJson, reporte);
    }

    @Test
    void convertirFormato() {
        // Arrange
        Orden orden = new Orden();
        orden.setCliente(1);
        orden.setAccionId(1);
        orden.setAccion("AAPL");
        orden.setModo(Modo.AHORA);
        orden.setCantidad(10);
        orden.setPrecio(100);
        orden.setFechaOperacion(Instant.now());
        orden.setOperacion(Operacion.COMPRA);
        orden.setDescripcionEstado("Analisis: OK.");
        orden.setEstado(Estado.COMPLETE);

        // Act
        ObjectNode nodo = ReportarOrdenesService.convertirFormato(orden);

        // Assert
        assertEquals(1, nodo.get("cliente").asInt());
        assertEquals(1, nodo.get("accionId").asInt());
        assertEquals("AAPL", nodo.get("accion").asText());
        assertEquals("AHORA", nodo.get("modo").asText());
        assertEquals(10, nodo.get("cantidad").asInt());
        assertEquals(100.0, nodo.get("precio").asDouble());
        assertEquals(orden.getFechaOperacion().toString(), nodo.get("fechaOperacion").asText());
        assertEquals("COMPRA", nodo.get("operacion").asText());
        assertTrue(nodo.get("operacionExitosa").asBoolean());
        assertEquals("Analisis: OK.", nodo.get("operacionObservaciones").asText());
    }
}
