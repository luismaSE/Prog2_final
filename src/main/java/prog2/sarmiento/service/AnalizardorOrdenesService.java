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
public class AnalizardorOrdenesService {

    
    private ApiService apiService;

    // public AnalizarOrdenService(ApiService apiService, ProcesadorOrdenesService procesadorOrdenes) {
    public AnalizardorOrdenesService(ApiService apiService) {
        this.apiService = apiService;
    }

    private final Logger log = LoggerFactory.getLogger(AnalizardorOrdenesService.class);


    public List<List<Orden>> analizarOrdenes(ProcesadorOrdenesService procesadorOrdenes) {


        List<List<Orden>> ordenesAnalisis = new ArrayList<>();
        List<Orden> ordenes = apiService.obtenerOrdenesDesdeAPI();
        List<Orden> ordenesOk = new ArrayList<>();
        List<Orden> ordenesFail = new ArrayList<>();
        for (Orden orden : ordenes) {
            String estado = analizarOrden(orden);

            if (estado.equals("OK")) {
                orden.setEstado(EstadoOrden.OK);
                orden.setDescripcionEstado(estado);
                ordenesOk.add(orden);
            } else {
            orden.setEstado(EstadoOrden.FAIL);
            orden.setDescripcionEstado(estado);
            ordenesFail.add(orden);
            }  
        } 
        ordenesAnalisis.add(ordenesOk);
        ordenesAnalisis.add(ordenesFail);
        return ordenesAnalisis;
    }

    public String analizarOrden(Orden orden) {
        String estado = "OK";
        if (!analizarCliente(orden)) {
            estado = "ERROR: Cliente inexistente";
        }
        if (!analizarComp(orden)) {
            estado = "ERROR: Codigo de compañia invalido";
        }
        if (!analizarAccion(orden)) {
            estado = "ERROR: Una orden no puede tener un número de acciones <=0";
        }
        if (orden.getModo() == ModoOrden.AHORA) {
            if (!analizarHorario(orden)){
                estado = "ERROR: Una orden instantánea no puede ejecutarse fuera del horario de transacciones, antes de las 09:00 y después de las 18:00";
            }
        }
        return estado;
    }
 

    public boolean analizarCliente(Orden orden){
        List<Long> clientes = apiService.obtenerClientesDesdeAPI();
        for (Long id : clientes) {
            if (orden.getCliente().equals(id)) {
                return true;
            }
        }
        return false;
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
        // String fechaStr = "2023-09-25T03:00:00Z";
        LocalDateTime fecha = LocalDateTime.parse(fechaStr, DateTimeFormatter.ISO_DATE_TIME);
        System.out.println(fecha.getDayOfMonth()+"/" + fecha.getMonthValue() + "/" + fecha.getYear() + "-" + fecha.getHour() + ":" + fecha.getMinute() + ":" + fecha.getSecond());
        return fecha;
    }


}
