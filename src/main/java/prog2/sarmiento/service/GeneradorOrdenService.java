package prog2.sarmiento.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.AbstractMap.SimpleEntry;

import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.noneDSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.domain.enumeration.Operacion;

@Service
@Transactional
public class GeneradorOrdenService {

    private final Logger log = LoggerFactory.getLogger(GeneradorOrdenService.class);
    private Random random = new Random();

    private List<Integer> clientes;
    private List<String> acciones = new ArrayList<>();

    public void defClientes(){
        for (int i = 51101; i <= 51112; i++) {
            clientes.add(i);
        }
    }

    public void defAcciones(){
    String[] codigos = {"AAPL", "GOOGL", "INTC", "KO", "MSFT", "YPF", "GGAL", "ALUA", "HAVA", "DIS", "MELI", "GLOB", "PAM"};

    for (String codigo : codigos) {
        acciones.add(codigo);
}
    }

    public List<Orden> generarOrdenes(int cantidad) {
        defClientes();
        defAcciones();
        List<Orden> ordenes = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            Orden orden = new Orden();
            double probabilidad = random.nextDouble();

            if (probabilidad < 0.8) { // Órdenes  válidas
                Integer cliente = clientes.get(random.nextInt(clientes.size()));
                String accion = acciones.get(random.nextInt(acciones.size()));
                Integer accionId = acciones.indexOf(accion)+1;


                orden.setCliente(cliente);
                orden.setAccionId(accionId);
                orden.setAccion(accion);
                orden.setModo(Modo.values()[random.nextInt(Modo.values().length)]);
                orden.setOperacion(Operacion.values()[random.nextInt(Operacion.values().length)]);
                orden.setCantidad(random.nextInt(100));
                orden.setPrecio(random.nextInt() * 500); // Precio al azar
                orden.setFechaOperacion(ZonedDateTime.now().minusDays(random.nextInt(365)));
            } else { // Órdenes inválidas
                orden.setCliente(-1); // Cliente inválido
                orden.setAccionId(-1); // Acción inválida
                orden.setAccion("Invalid"); // Acción inválida
                orden.setModo(Modo.values()[random.nextInt(Modo.values().length)]);
                orden.setOperacion(Operacion.values()[random.nextInt(Operacion.values().length)]);
                orden.setCantidad(-1); // Cantidad inválida
                orden.setPrecio(-1); // Precio inválido
                orden.setFechaOperacion(null); // Fecha inválida
            }
            ordenes.add(orden);
        }
        return ordenes;
    }

}
