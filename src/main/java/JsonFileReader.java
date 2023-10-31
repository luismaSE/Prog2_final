import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonFileReader {
    public static String leerArchivoJson(String rutaArchivo) throws IOException {
        return new String(Files.readAllBytes(Paths.get(rutaArchivo)));
    }
}
