import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prog2.sarmiento.domain.Orden;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrdenController {
    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @GetMapping("/ordenes")
    public List<Orden> obtenerOrdenes() {
        return ordenService.obtenerOrdenesDesdeAPI();
    }
}
