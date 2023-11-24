package prog2.sarmiento.service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import prog2.sarmiento.domain.enumeration.Modo;
import prog2.sarmiento.domain.enumeration.Operacion;

@Service
@Transactional
public class GeneradorOrdenService {
    

    private final Logger log = LoggerFactory.getLogger(GeneradorOrdenService.class);
    private Random random = new Random();
    static ObjectMapper objectMapper = new ObjectMapper();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private List<Integer> clientes = new ArrayList<>();
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


    public String generarOrdenes() {
        ObjectNode ordenesJson = new ObjectMapper().createObjectNode();
        ArrayNode ordenesArray = JsonNodeFactory.instance.arrayNode();
        int cantidad = 5;
        defClientes();
        defAcciones();
        for (int i = 0; i < cantidad; i++) {
            ordenesArray.add(crearOrdenRandom());
        }
        ordenesJson.set("ordenes", ordenesArray);
        try {
            String stringOrdenes =  objectMapper.writeValueAsString(ordenesJson);
            return stringOrdenes;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        
    }


    public ObjectNode crearOrdenRandom() {

        try {
            String accion = acciones.get(random.nextInt(acciones.size()));
            // Crear un nuevo ObjectNode con el formato deseado
            ObjectNode nodoNuevo = objectMapper.createObjectNode()
                .put("cliente", clientes.get(random.nextInt(clientes.size())) )
                .put("accionId", acciones.indexOf(accion)+1)
                .put("accion", accion)
                .put("modo", Modo.values()[random.nextInt(Modo.values().length)].toString())
                .put("cantidad",random.nextInt(50)+1)
                .put("precio", random.nextInt(100)+50)
                .put("operacion", Operacion.values()[random.nextInt(Operacion.values().length)].toString())
                .put("fechaOperacion", ZonedDateTime.now()
                                                .minusDays(random.nextInt(365))
                                                .withHour(random.nextInt(24))
                                                .withMinute(random.nextInt(60))
                                                .withSecond(random.nextInt(60))
                                                .format(formatter));
                return nodoNuevo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
