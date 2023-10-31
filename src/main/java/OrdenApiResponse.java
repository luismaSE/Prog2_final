
import prog2.sarmiento.domain.Orden;

import java.util.List;

public class OrdenApiResponse {
    private List<Orden> ordenes;

    public List<Orden> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<Orden> ordenes) {
        this.ordenes = ordenes;
    }
}
