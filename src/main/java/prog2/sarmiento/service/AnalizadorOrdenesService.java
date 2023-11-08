package prog2.sarmiento.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;
import prog2.sarmiento.domain.enumeration.Modo;

@Service
@Transactional
public class AnalizadorOrdenesService {

    private final Logger log = LoggerFactory.getLogger(AnalizadorOrdenesService.class);

    List<Orden> ordenesOk = new ArrayList<>();
    List<Orden> ordenesFail = new ArrayList<>();

    @Autowired
    ApiService apiService;

    public void analizarOrdenes(List<Orden> ordenes) {
        for (Orden orden : ordenes) {
            // Perform analysis on each order
            log.info("Analyzing order: {}", orden);
        }
    }

    public Orden analizarOrden(Orden orden) {
        String estado = "OK";

        if (!analizarCliente(orden)) {
            estado = "ERROR: Cliente inexistente";
        } else if (!analizarComp(orden)) {
            estado = "ERROR: Codigo de compañia invalido";
        } else if (!analizarAccion(orden)) {
            estado = "ERROR: Una orden no puede tener un número de acciones <=0";
        } else if (orden.getModo() == Modo.AHORA && !analizarHorario(orden)) {
            estado = "ERROR: Una orden instantánea no puede ejecutarse fuera del horario de transacciones, antes de las 09:00 y después de las 18:00";
        }

        if (estado.equals("OK")) {
                orden.setEstado(Estado.OK);
                orden.setDescripcionEstado("Analisis: OK.");
        } else {
            orden.setEstado(Estado.FAIL);
            orden.setDescripcionEstado(estado);
        }
        return orden;  
    }


    

    public void registrarOrden(Orden orden) {
        // log.info("Orden analizada, estado:"+orden.getEstado());
        if (!(orden.getEstado().equals(Estado.FAIL))) {
            ordenesOk.add(orden);
        } else {
            ordenesFail.add(orden);
        }
    }




    public String mostrarResultadoAnalisis () {
        StringBuilder resultado = new StringBuilder();
        log.info("\nOrdenes OK:");
        for (Orden orden : ordenesOk) {
            log.info("OK: "+orden);
            resultado.append(orden+"\n");
        } 
        log.info("\nOrdenes FAIL:");
        for (Orden orden : ordenesFail) {
            log.info("FAIL: "+orden);
            resultado.append(orden+"\n");
        } 
        return resultado.toString();
        
    }

    public List<List<Orden>> terminarAnalisis() {
        List<List<Orden>> listas = new ArrayList<>();
        listas.add(ordenesOk);
        listas.add(ordenesFail);
        return listas;
    }

    public void limpiarAnalisis () {
        ordenesOk.clear();
        ordenesFail.clear();
    }




    public boolean analizarCliente(Orden orden) {
        List<Integer> clientes = apiService.obtenerClientesDesdeAPI();
        return clientes.contains(orden.getCliente());
    }
    

    public boolean analizarComp(Orden orden){
        String codigo = orden.getAccion();
        String accionCodigo = apiService.obtenerCompDesdeAPIconCodigo(codigo);
        return (accionCodigo.equals(codigo));

    }

    public boolean analizarAccion(Orden orden){
        return (orden.getCantidad() > 0 );
    }

    public boolean analizarHorario(Orden orden){
        LocalDateTime fecha = strToDate(orden.getFechaOperacion());
        LocalTime horaOrden = fecha.toLocalTime();
        LocalTime horaInicio = LocalTime.of(9, 0);
        LocalTime horaFin = LocalTime.of(18, 0);
        return (horaOrden.isAfter(horaInicio) && horaOrden.isBefore(horaFin));
    }  

    public LocalDateTime strToDate(String fechaStr){
        LocalDateTime fecha = LocalDateTime.parse(fechaStr, DateTimeFormatter.ISO_DATE_TIME);
        return fecha;
    }
}
