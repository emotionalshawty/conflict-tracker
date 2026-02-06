package ConflictTracker.conflict_tracker.repository;

import ConflictTracker.conflict_tracker.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByCode(String code);

    Optional<Country> findByNameIgnoreCase(String name);

    boolean existsByCode(String code);
}

