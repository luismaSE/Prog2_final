import com.fasterxml.jackson.databind.ObjectMapper;

import prog2.sarmiento.domain.Orden;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Inicializa el ObjectMapper de Jackson
            ObjectMapper objectMapper = new ObjectMapper();

            // Lee el JSON desde un archivo (por ejemplo, "orden.json")
            OrdenApiResponse ordenApiResponse = objectMapper.readValue(new File("/home/luisma_se/Documentos/programacion_2/ordenes.json"), OrdenApiResponse.class);

            // Ahora tienes el objeto Java que representa el JSON
            List<Orden> ordenes = ordenApiResponse.getOrdenes();
            for (Orden orden : ordenes) {
                // Haz lo que necesites con las órdenes
                System.out.println(orden);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// import prog2.sarmiento.domain.Orden;
//     import java.io.IOException;
//     import java.util.List;

// public class Main {
//     public static void main(String[] args) {
//         try {
//             String filePath = "src/main/resources/ordenes.json";
//             OrdenApiResponse ordenApiResponse = JsonParser.parseJsonFile(filePath);

//             List<Orden> ordenes = ordenApiResponse.getOrdenes();

//             // Realiza otras operaciones con las órdenes si es necesario
//             for (Orden orden : ordenes) {
//                 System.out.println(orden);
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }

