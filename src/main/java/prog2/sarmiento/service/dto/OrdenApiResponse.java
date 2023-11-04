package prog2.sarmiento.service.dto;

import java.util.List;

import prog2.sarmiento.domain.Orden;

public class OrdenApiResponse {
    private List<Orden> ordenes;

    public List<Orden> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<Orden> ordenes) {
        this.ordenes = ordenes;
    }
}


