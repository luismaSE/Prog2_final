package prog2.sarmiento.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prog2.sarmiento.domain.Orden;

@Service
@Transactional
public class AnalizarOrdenService {

    private final Logger log = LoggerFactory.getLogger(AnalizarOrdenService.class);

    public boolean analizarOrden(Orden orden) {
        // String ahora = "AHORA";
        if (orden.getModo().equals("AHORA")) {
            if (!analizarHorario()){
                String fallo = "Una orden instantánea no puede ejecutarse fuera del horario de transacciones, antes de las 09:00 y después de las 18:00";
            }
        }
        
    }

    public boolean analizarHorario(){}

    public boolean analizarCliente(){}

    public boolean analizarComp(){}

    public boolean analizarAccion(){}

    public void hora(){
        String fechaStr = "2023-09-25T03:00:00Z";
        LocalDateTime fecha = LocalDateTime.parse(fechaStr, DateTimeFormatter.ISO_DATE_TIME);
        System.out.println(fecha.getDayOfMonth()+"/" + fecha.getMonthValue() + "/" + fecha.getYear() + "-" + fecha.getHour() + ":" + fecha.getMinute() + ":" + fecha.getSecond());
    }


}
