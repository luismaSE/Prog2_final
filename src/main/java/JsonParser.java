import com.fasterxml.jackson.databind.ObjectMapper;
import prog2.sarmiento.domain.Orden;

import java.io.IOException;

public class JsonParser {
    public static Orden mapearJsonAOrden(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, Orden.class);
    }
}


// import com.fasterxml.jackson.databind.ObjectMapper;
//     import prog2.sarmiento.domain.Orden;

//     import java.io.File;
//     import java.io.IOException;
//     import java.util.List;

// public class JsonParser {
//     public static OrdenApiResponse parseJsonFile(String filePath) throws IOException {
//         ObjectMapper objectMapper = new ObjectMapper();
//         return objectMapper.readValue(new File(filePath), OrdenApiResponse.class);
//     }
// }
