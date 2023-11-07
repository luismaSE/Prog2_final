// package prog2.sarmiento.service;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;

// import java.util.ArrayList;
// import java.util.List;

// import org.junit.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import prog2.sarmiento.domain.Orden;

// @SpringBootTest
// public class MainServiceTest {

//     @Autowired
//     MainService mainService;
//     @Autowired
//     ApiService apiService;
//     @Autowired
//     AnalizadorOrdenesService analizadorOrdenes;
//     @Autowired
//     ProcesadorOrdenesService procesadorOrdenes;
//     @Autowired
//     ProgramadorOrdenesService programadorOrdenes;

//     @Test
//     public void testServe() {

//         // Simular la respuesta de la API para obtener órdenes
//         when(apiService.obtenerOrdenesDesdeAPI()).thenReturn(new ArrayList<>()); // Puedes proporcionar órdenes de ejemplo

//         // Simular el análisis de órdenes
//         Orden orden = new Orden(); // Crea una orden de ejemplo
//         when(analizadorOrdenes.analizarOrden(orden)).thenReturn(orden);

//         // Simular el procesamiento de órdenes
//         when(procesadorOrdenes.procesarOrden(orden)).thenReturn(orden);

//         // Ejecutar el servicio
//         String resultado = mainService.Serve();

//         // Verificar que el resultado sea correcto
//         assertEquals("OK", resultado); // Ajusta según el resultado esperado
//     }

//     @Test
//     public void testProgramarOrdenes() {
    
//         // Crear una lista de órdenes para programar
//         List<Orden> ordenes = new ArrayList<>(); // Agrega órdenes de ejemplo
    
//         // Ejecutar el método de programación de órdenes
//         mainService.programadorOrdenes.programarOrdenes(ordenes);
    
//         // Verificar que las órdenes se hayan programado correctamente
//         assertEquals(ordenes.size(), mainService.programadorOrdenes.ordenesPrincipioDia.size()); // Ajusta según tus necesidades
//     }


// }
