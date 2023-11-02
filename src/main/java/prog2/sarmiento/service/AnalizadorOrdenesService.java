package prog2.sarmiento.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.EstadoOrden;
import prog2.sarmiento.domain.enumeration.ModoOrden;

@Service
@Transactional
public class AnalizadorOrdenesService {
    
    List<Orden> ordenesOk = new ArrayList<>();
    List<Orden> ordenesFail = new ArrayList<>();

    private final Logger log = LoggerFactory.getLogger(AnalizadorOrdenesService.class);
    private ApiService apiService;


    public AnalizadorOrdenesService(ApiService apiService) {
        this.apiService = apiService;
	}

    public Orden analizarOrden(Orden orden) {
        String estado = "OK";

        if (!analizarCliente(orden)) {
            estado = "ERROR: Cliente inexistente";
        } else if (!analizarComp(orden)) {
            estado = "ERROR: Codigo de compañia invalido";
        } else if (!analizarAccion(orden)) {
            estado = "ERROR: Una orden no puede tener un número de acciones <=0";
        } else if (orden.getModo() == ModoOrden.AHORA && !analizarHorario(orden)) {
            estado = "ERROR: Una orden instantánea no puede ejecutarse fuera del horario de transacciones, antes de las 09:00 y después de las 18:00";
        }

        if (estado.equals("OK")) {
                orden.setEstado(EstadoOrden.OK);
                orden.setDescripcionEstado("Analisis: OK.");
        } else {
            orden.setEstado(EstadoOrden.FAIL);
            orden.setDescripcionEstado(estado);
        }
        return orden;  
    }


    

    public void registrarOrden(Orden orden) {
        // System.out.println("Orden analizada, estado:"+orden.getEstado());
        if (!(orden.getEstado().equals(EstadoOrden.FAIL))) {
            ordenesOk.add(orden);
        } else {
            ordenesFail.add(orden);
        }
    }




    public void mostrarResultadoAnalisis () {
        System.out.println("\nOrdenes OK:");
        for (Orden orden : ordenesOk) {
            System.out.println(orden);
        } 
        System.out.println("\nOrdenes FAIL:");
        for (Orden orden : ordenesFail) {
            System.out.println(orden);
        } 
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
        List<Long> clientes = apiService.obtenerClientesDesdeAPI();
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
        // System.out.println(fecha.getDayOfMonth()+"/" + fecha.getMonthValue() + "/" + fecha.getYear() + "-" + fecha.getHour() + ":" + fecha.getMinute() + ":" + fecha.getSecond());
        return fecha;
    }


}
