package prog2.sarmiento.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prog2.sarmiento.domain.Orden;

@Service
@Transactional
public class ServicioExternoService {

    private final Logger log = LoggerFactory.getLogger(ServicioExternoService.class);

    public boolean ordenCompra(Orden orden) {
        return true;
    }

    public boolean ordenVenta(Orden orden) {
        return true;
    }



}
