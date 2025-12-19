package ConflictTracker.conflict_tracker.repository;

import ConflictTracker.conflict_tracker.model.Faction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactionRepository extends JpaRepository<Faction, Long> {
}

