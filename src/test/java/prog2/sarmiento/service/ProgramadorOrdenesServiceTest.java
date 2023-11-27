package prog2.sarmiento.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.repository.OrdenRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProgramadorOrdenesServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private ProcesadorOrdenesService procesadorOrdenes;

    @InjectMocks
    private ProgramadorOrdenesService programadorOrdenesService;

    @Test
    void programarOrdenes() {
        // Arrange
        Orden orden1 = new Orden();
        orden1.setId(1L);
        orden1.setModo(Modo.AHORA);

        Orden orden2 = new Orden();
        orden2.setId(2L);
        orden2.setModo(Modo.PRINCIPIODIA);

         Orden orden3 = new Orden();
        orden2.setId(3L);
        orden2.setModo(Modo.FINDIA);

        List<Orden> ordenes = Arrays.asList(orden1, orden2, orden3);

        // Mock the behavior of the dependencies
        doNothing().when(procesadorOrdenes).addOrden(any(Orden.class));
        when(ordenRepository.save(any(Orden.class))).thenReturn(orden1);

        // Act
        programadorOrdenesService.programarOrdenes(ordenes);

        // Assert
        verify(procesadorOrdenes, times(1)).addOrden(orden1);
        verify(procesadorOrdenes, never()).addOrden(orden2);
        verify(procesadorOrdenes, never()).addOrden(orden3);
        verify(ordenRepository, times(1)).save(orden1);
        verify(ordenRepository, never()).save(orden2);
        verify(ordenRepository, never()).save(orden3);

        // You can add more assertions based on your specific requirements.
    }
}
