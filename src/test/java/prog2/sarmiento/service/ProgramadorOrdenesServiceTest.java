package prog2.sarmiento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Modo;

@SpringBootTest
public class ProgramadorOrdenesServiceTest {

    @Autowired
    ProgramadorOrdenesService programadorOrdenesService;

    @Test
    public void testProgramarOrdenes() {
        List<Orden> ordenes = new ArrayList<>();
        
        // Agregar órdenes a la lista
        Orden orden1 = new Orden();
        orden1.setModo(Modo.PRINCIPIODIA);
        ordenes.add(orden1);
        
        Orden orden2 = new Orden();
        orden2.setModo(Modo.FINDIA);
        ordenes.add(orden2);
        
        // Llamar al método programarOrdenes
        programadorOrdenesService.programarOrdenes(ordenes);
        
        // Verificar si las órdenes están en las colas correctas
        assertEquals(1, programadorOrdenesService.ordenesPrincipioDia.size());
        assertEquals(1, programadorOrdenesService.ordenesFinDia.size());
    }


    @Test
    public void testProcOrdenesInicioDia() {
        ProcesadorOrdenesService procesadorOrdenesService = mock(ProcesadorOrdenesService.class);
        programadorOrdenesService.procesadorOrdenes = procesadorOrdenesService;

        // Agregar una orden a la cola de órdenes al principio del día
        Orden orden = new Orden();
        programadorOrdenesService.ordenesPrincipioDia.add(orden);

        // Llamar al método de procesamiento de órdenes al inicio del día
        programadorOrdenesService.procOrdenesInicioDia();

        // Verificar si el método de procesamiento de órdenes se llamó con la orden correcta
        verify(procesadorOrdenesService, times(1)).procesarOrden(orden);
        // Verificar si la orden se eliminó de la cola
        assertTrue(programadorOrdenesService.ordenesPrincipioDia.isEmpty());
    }

    @Test
    public void testProcOrdenesFinDia() {
        ProcesadorOrdenesService procesadorOrdenesService = mock(ProcesadorOrdenesService.class);
        programadorOrdenesService.procesadorOrdenes = procesadorOrdenesService;
    
        // Agregar una orden a la cola de órdenes al final del día
        Orden orden = new Orden();
        programadorOrdenesService.ordenesFinDia.add(orden);
    
        // Llamar al método de procesamiento de órdenes al final del día
        programadorOrdenesService.procOrdenesFinDia();
    
        // Verificar si el método de procesamiento de órdenes se llamó con la orden correcta
        verify(procesadorOrdenesService, times(1)).procesarOrden(orden);
        // Verificar si la orden se eliminó de la cola
        assertTrue(programadorOrdenesService.ordenesFinDia.isEmpty());
    }

}