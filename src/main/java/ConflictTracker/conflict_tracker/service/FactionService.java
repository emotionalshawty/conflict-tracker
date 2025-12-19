package ConflictTracker.conflict_tracker.service;

import ConflictTracker.conflict_tracker.dto.CountryDTO;
import ConflictTracker.conflict_tracker.dto.FactionDTO;
import ConflictTracker.conflict_tracker.model.Conflict;
import ConflictTracker.conflict_tracker.model.Country;
import ConflictTracker.conflict_tracker.model.Faction;
import ConflictTracker.conflict_tracker.repository.ConflictRepository;
import ConflictTracker.conflict_tracker.repository.CountryRepository;
import ConflictTracker.conflict_tracker.repository.FactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FactionService {

    @Autowired
    private FactionRepository factionRepository;

    @Autowired
    private ConflictRepository conflictRepository;

    @Autowired
    private CountryRepository countryRepository;

    public List<FactionDTO> getAllFactions() {
        return factionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FactionDTO getFactionById(Long id) {
        Faction faction = factionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faction not found"));
        return convertToDTO(faction);
    }

    public FactionDTO createFaction(FactionDTO dto) {
        Faction faction = new Faction();
        updateFactionFromDTO(faction, dto);
        Faction saved = factionRepository.save(faction);
        return convertToDTO(saved);
    }

    public FactionDTO updateFaction(Long id, FactionDTO dto) {
        Faction faction = factionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faction not found"));
        updateFactionFromDTO(faction, dto);
        Faction saved = factionRepository.save(faction);
        return convertToDTO(saved);
    }

    public void deleteFaction(Long id) {
        factionRepository.deleteById(id);
    }

    private void updateFactionFromDTO(Faction faction, FactionDTO dto) {
        faction.setName(dto.getName());

        if (dto.getConflictId() != null) {
            Conflict conflict = conflictRepository.findById(dto.getConflictId())
                    .orElseThrow(() -> new RuntimeException("Conflict not found"));
            faction.setConflict(conflict);
        }

        if (dto.getSupportingCountryIds() != null) {
            Set<Country> countries = new HashSet<>(countryRepository.findAllById(dto.getSupportingCountryIds()));
            faction.setSupportingCountries(countries);
        }
    }

    private FactionDTO convertToDTO(Faction faction) {
        FactionDTO dto = new FactionDTO();
        dto.setId(faction.getId());
        dto.setName(faction.getName());

        if (faction.getConflict() != null) {
            dto.setConflictId(faction.getConflict().getId());
            dto.setConflictName(faction.getConflict().getName());
        }

        Set<CountryDTO> countryDTOs = faction.getSupportingCountries().stream()
                .map(c -> new CountryDTO(c.getId(), c.getName(), c.getCode()))
                .collect(Collectors.toSet());
        dto.setSupportingCountries(countryDTOs);

        return dto;
    }
}

