package prog2.sarmiento.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Operacion;


@SpringBootTest
public class ProcesadorOrdenesServiceTest {

    @Mock
    private ExternalService externalService;

    @Autowired
    private ProcesadorOrdenesService procesadorOrdenes;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        procesadorOrdenes.servicioExterno = externalService;
    }

    @Test
    public void testProcesarOrdenCompraCompleta() {
        // Configuración de prueba
        Orden orden = new Orden();
        orden.setOperacion(Operacion.COMPRA);
        Mockito.when(externalService.ordenCompra(orden)).thenReturn(true);

        // Ejecutar la prueba
        Orden resultado = procesadorOrdenes.procesarOrden(orden);

        // Verificar que la orden esté completa
        assertEquals(Estado.COMPLETE, resultado.getEstado());
        assertEquals("Orden COMPLETADA", resultado.getDescripcionEstado());
    }

    @Test
    public void testProcesarOrdenVentaCompleta() {
        // Configuración de prueba
        Orden orden = new Orden();
        orden.setOperacion(Operacion.VENTA);
        Mockito.when(externalService.ordenVenta(orden)).thenReturn(true);

        // Ejecutar la prueba
        Orden resultado = procesadorOrdenes.procesarOrden(orden);

        // Verificar que la orden esté completa
        assertEquals(Estado.COMPLETE, resultado.getEstado());
        assertEquals("Orden COMPLETADA", resultado.getDescripcionEstado());
    }

    @Test
    public void testProcesarOrdenFallida() {
        // Configuración de prueba
        Orden orden = new Orden();
        orden.setOperacion(Operacion.COMPRA);
        Mockito.when(externalService.ordenCompra(orden)).thenReturn(false);

        // Ejecutar la prueba
        Orden resultado = procesadorOrdenes.procesarOrden(orden);

        // Verificar que la orden no esté completa
        assertEquals(Estado.PEND, resultado.getEstado());
    }
}
