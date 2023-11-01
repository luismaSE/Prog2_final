package prog2.sarmiento.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProcesarOrdenService {

    private final Logger log = LoggerFactory.getLogger(ProcesarOrdenService.class);
}
