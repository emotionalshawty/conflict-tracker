package ConflictTracker.conflict_tracker.repository;

import ConflictTracker.conflict_tracker.model.Conflict;
import ConflictTracker.conflict_tracker.model.ConflictStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConflictRepository extends JpaRepository<Conflict, Long> {
    List<Conflict> findByStatus(ConflictStatus status);
    List<Conflict> findByCountries_Code(String countryCode);
}

