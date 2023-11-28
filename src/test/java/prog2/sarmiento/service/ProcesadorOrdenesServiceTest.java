package prog2.sarmiento.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.domain.enumeration.Operacion;
import prog2.sarmiento.repository.OrdenRepository;

class ProcesadorOrdenesServiceTest {

    @Mock
    private ApiService apiService;

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private ExternalService servicioExterno;

    @Mock
    private ReportarOrdenesService reportarOrdenes;

    @InjectMocks
    private ProcesadorOrdenesService procesadorOrdenesService;

    private Queue<Orden> ordenesPrincipioDia;
    private Queue<Orden> ordenesFinDia;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ordenesPrincipioDia = new ConcurrentLinkedQueue<>();
        ordenesFinDia = new ConcurrentLinkedQueue<>();
        procesadorOrdenesService.ordenesPrincipioDia = ordenesPrincipioDia;
        procesadorOrdenesService.ordenesFinDia = ordenesFinDia;
    }

    @Test
    void testAddOrden() {
        Orden ordenPrincipioDia = new Orden();
        ordenPrincipioDia.setModo(Modo.PRINCIPIODIA);
        procesadorOrdenesService.addOrden(ordenPrincipioDia);
        assertEquals(1, ordenesPrincipioDia.size());

        Orden ordenFinDia = new Orden();
        ordenFinDia.setModo(Modo.FINDIA);
        procesadorOrdenesService.addOrden(ordenFinDia);
        assertEquals(1, ordenesFinDia.size());
    }

    @Test
    void testProcesarOrdenCompra() {
        Orden orden = new Orden();
        orden.setOperacion(Operacion.COMPRA);
        when(servicioExterno.ordenCompra(any(Orden.class))).thenReturn(true);

        orden = procesadorOrdenesService.procesarOrden(orden);

        assertEquals(Estado.COMPLETE, orden.getEstado());
        assertEquals("Orden COMPLETADA", orden.getDescripcionEstado());
        verify(reportarOrdenes, times(1)).addOrden(orden);
    }

    @Test
    void testProcesarOrdenVenta() {
        Orden orden = new Orden();
        orden.setOperacion(Operacion.VENTA);
        when(servicioExterno.ordenVenta(any(Orden.class))).thenReturn(true);

        orden = procesadorOrdenesService.procesarOrden(orden);

        assertEquals(Estado.COMPLETE, orden.getEstado());
        assertEquals("Orden COMPLETADA", orden.getDescripcionEstado());
        verify(reportarOrdenes, times(1)).addOrden(orden);
    }

    @Test
    void testProcesarOrdenCompraFail() {
        Orden orden = new Orden();
        orden.setOperacion(Operacion.COMPRA);
        when(servicioExterno.ordenCompra(any(Orden.class))).thenReturn(false);

        orden = procesadorOrdenesService.procesarOrden(orden);

        assertEquals(Estado.FAIL, orden.getEstado());
        verify(reportarOrdenes, times(1)).addOrden(orden);
    }

    @Test
    void testProcesarOrdenesPrincipioDia() {
        Orden orden = new Orden();
        orden.setModo(Modo.PRINCIPIODIA);
        orden.setOperacion(Operacion.COMPRA);
        ordenesPrincipioDia.add(orden);
        when(apiService.obtenerUltimoValor(anyString())).thenReturn(100.0);
        when(servicioExterno.ordenCompra(any(Orden.class))).thenReturn(true);

        procesadorOrdenesService.procOrdenesInicioDia();

        verify(reportarOrdenes, times(1)).addOrden(orden);
        verify(ordenRepository, times(1)).save(orden);
    }

    @Test
    void testProcesarOrdenesFinDia() {
        Orden orden = new Orden();
        orden.setModo(Modo.FINDIA);
        orden.setOperacion(Operacion.VENTA);
        ordenesFinDia.add(orden);
        when(apiService.obtenerUltimoValor(anyString())).thenReturn(100.0);
        when(servicioExterno.ordenCompra(any(Orden.class))).thenReturn(true);

        procesadorOrdenesService.procOrdenesFinDia();

        verify(reportarOrdenes, times(1)).addOrden(orden);
        verify(ordenRepository, times(1)).save(orden);
    }

}
