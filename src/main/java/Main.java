// import org.springframework.boot.autoconfigure.SpringBootApplication;

import prog2.sarmiento.service.MainService;

// import com.fasterxml.jackson.databind.ObjectMapper;
// @SpringBootApplication
public class Main {

    public static void main(String[] args) {
        MainService mainService = new MainService();
        mainService.Serve();        

    }
}
