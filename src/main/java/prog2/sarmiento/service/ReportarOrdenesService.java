package prog2.sarmiento.service;

import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.Estado;


@Service
@Transactional
public class ReportarOrdenesService {
 
    //este servicio debe recibir ordenes procesadas y guardarlas en una lista
    
    private final Logger log = LoggerFactory.getLogger(ReportarOrdenesService.class);

    List<Orden> ordenesReport = new ArrayList<>();
    static ObjectMapper objectMapper = new ObjectMapper();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    
    public void addOrden(Orden orden) {
        ordenesReport.add(orden);
    }
 
    
    public String reporte() {
        log.info("Generando Reporte de Ordenes...");
        ObjectNode ordenesJson = new ObjectMapper().createObjectNode();
        ArrayNode ordenesArray = JsonNodeFactory.instance.arrayNode();
        for (Orden orden : ordenesReport) {
            ordenesArray.add(convertirFormato(orden));
        }
        ordenesJson.set("ordenes", ordenesArray);
        ordenesReport.clear();

        try {
            String jsonOrdenesReport =  objectMapper.writeValueAsString(ordenesJson);
            return jsonOrdenesReport;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }
 

    public static ObjectNode convertirFormato(Orden ordenOriginal) {

        try {
            // Crear un nuevo ObjectNode con el formato deseado
            ObjectNode nodoNuevo = objectMapper.createObjectNode()
                .put("cliente", ordenOriginal.getCliente())
                .put("accionId", ordenOriginal.getAccionId())
                .put("accion", ordenOriginal.getAccion())
                .put("modo", ordenOriginal.getModo().toString())
                .put("cantidad", ordenOriginal.getCantidad())
                .put("precio", ordenOriginal.getPrecio())
                // .put("fechaOperacion", ZonedDateTime.ofInstant(ordenOriginal.getFechaOperacion(), ZoneId.systemDefault()))
                .put("fechaOperacion", ordenOriginal.getFechaOperacion().toString())
                .put("operacion", ordenOriginal.getOperacion().toString())
                .put("operacionObservaciones", ordenOriginal.getDescripcionEstado());
            if (ordenOriginal.getEstado() == Estado.COMPLETE) {
                nodoNuevo.put("operacionExitosa", true);
            } else {
                nodoNuevo.put("operacionExitosa", false);
            }
            return nodoNuevo;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}