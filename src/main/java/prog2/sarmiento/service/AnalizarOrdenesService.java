package prog2.sarmiento.service;

import java.util.ArrayList;
import java.util.List;

// import org.hibernate.mapping.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prog2.sarmiento.domain.Orden;
import prog2.sarmiento.domain.enumeration.EstadoOrden;

@Service
@Transactional
public class AnalizarOrdenesService {

    private ApiService apiService;

    public AnalizarOrdenesService(ApiService apiService) {
        this.apiService = apiService;
    }

    public List<List<Orden>> analizarOrdenes() {
        AnalizarOrdenService analizador = new AnalizarOrdenService(apiService);
        List<List<Orden>> ordenesAnalisis = new ArrayList<>();
        List<Orden> ordenes = apiService.obtenerOrdenesDesdeAPI();
        List<Orden> ordenesOk = new ArrayList<>();
        List<Orden> ordenesFail = new ArrayList<>();
        for (Orden orden : ordenes) {
            String estado = analizador.analizarOrden(orden);

            if (estado.equals("OK")) {
                orden.setEstado(EstadoOrden.OK);
                orden.setDescripcionEstado(estado);
                ordenesOk.add(orden);
            } else {
            orden.setEstado(EstadoOrden.FAIL);
            orden.setDescripcionEstado(estado);
            ordenesFail.add(orden);
            }  
        } 
        ordenesAnalisis.add(ordenesOk);
        ordenesAnalisis.add(ordenesFail);
        return ordenesAnalisis;
    }

    private final Logger log = LoggerFactory.getLogger(AnalizarOrdenesService.class);
}
