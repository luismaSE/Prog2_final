package prog2.sarmiento.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;

@SpringBootTest
public class AnalizadorOrdenesServiceTest {

    @Autowired
    AnalizadorOrdenesService analizadorOrdenesService;

    @Test
    public void testAnalizarOrdenOK() {
        Orden orden = new Orden();
        orden.setCliente(1);
        orden.setAccion("AAPL");
        orden.setCantidad(100);
        orden.setModo(Modo.AHORA);
        orden.setFechaOperacion(ZonedDateTime.parse("2023-01-01T12:00:00"));

        Orden ordenAnalizada = analizadorOrdenesService.analizarOrden(orden);

        assertEquals(Estado.OK, ordenAnalizada.getEstado());
        assertEquals("Analisis: OK.", ordenAnalizada.getDescripcionEstado());
    }

    @Test
    public void testAnalizarOrdenClienteInexistente() {
        Orden orden = new Orden();
        orden.setCliente(999); // Cliente inexistente
        // Resto de los datos válidos
    
        Orden ordenAnalizada = analizadorOrdenesService.analizarOrden(orden);
    
        assertEquals(Estado.FAIL, ordenAnalizada.getEstado());
        assertTrue(ordenAnalizada.getDescripcionEstado().contains("ERROR: Cliente inexistente"));
    }

    @Test
    public void testAnalizarOrdenCodigoCompaniaIncorrecto() {
        Orden orden = new Orden();
        orden.setAccion("INVALID"); // Código de compañía incorrecto
        // Resto de los datos válidos

        Orden ordenAnalizada = analizadorOrdenesService.analizarOrden(orden);

        assertEquals(Estado.FAIL, ordenAnalizada.getEstado());
        assertTrue(ordenAnalizada.getDescripcionEstado().contains("ERROR: Codigo de compañia invalido"));
    }


    @Test
    public void testAnalizarOrdenCantidadIncorrecta() {
        Orden orden = new Orden();
        orden.setCantidad(0); // Cantidad de acciones incorrecta
        // Resto de los datos válidos

        Orden ordenAnalizada = analizadorOrdenesService.analizarOrden(orden);

        assertEquals(Estado.FAIL, ordenAnalizada.getEstado());
        assertTrue(ordenAnalizada.getDescripcionEstado().contains("ERROR: Una orden no puede tener un número de acciones <=0"));
    }

    @Test
    public void testAnalizarOrdenFueraHorarioTransacciones() {
        Orden orden = new Orden();
        orden.setModo(Modo.AHORA);
        orden.setFechaOperacion(ZonedDateTime.parse("2023-01-01T07:00:00")); // Fuera del horario de transacciones (antes de las 09:00)
    
        Orden ordenAnalizada = analizadorOrdenesService.analizarOrden(orden);
    
        assertEquals(Estado.FAIL, ordenAnalizada.getEstado());
        assertTrue(ordenAnalizada.getDescripcionEstado().contains("ERROR: Una orden instantánea no puede ejecutarse fuera del horario de transacciones"));
    }


}
