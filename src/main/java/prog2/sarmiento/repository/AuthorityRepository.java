package prog2.sarmiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prog2.sarmiento.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
