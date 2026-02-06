package ConflictTracker.conflict_tracker.service;

import ConflictTracker.conflict_tracker.dto.FactionCreateDTO;
import ConflictTracker.conflict_tracker.dto.FactionDTO;
import ConflictTracker.conflict_tracker.model.Conflict;
import ConflictTracker.conflict_tracker.model.Country;
import ConflictTracker.conflict_tracker.model.Faction;
import ConflictTracker.conflict_tracker.repository.ConflictRepository;
import ConflictTracker.conflict_tracker.repository.CountryRepository;
import ConflictTracker.conflict_tracker.repository.FactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class FactionService {

    private final FactionRepository factionRepository;
    private final ConflictRepository conflictRepository;
    private final CountryRepository countryRepository;

    public FactionService(FactionRepository factionRepository,
                          ConflictRepository conflictRepository,
                          CountryRepository countryRepository) {
        this.factionRepository = factionRepository;
        this.conflictRepository = conflictRepository;
        this.countryRepository = countryRepository;
    }

    public List<FactionDTO> getAllFactions() {
        return factionRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public FactionDTO getFactionById(Long id) {
        Faction faction = factionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faction not found with id: " + id));
        return toDTO(faction);
    }

    public List<FactionDTO> getFactionsByConflictId(Long conflictId) {
        return factionRepository.findByConflictId(conflictId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public FactionDTO createFaction(FactionCreateDTO createDTO) {
        Conflict conflict = conflictRepository.findById(createDTO.getConflictId())
                .orElseThrow(() -> new ResourceNotFoundException("Conflict not found with id: " + createDTO.getConflictId()));

        Faction faction = new Faction();
        faction.setName(createDTO.getName());
        faction.setConflict(conflict);

        if (createDTO.getSupportingCountryCodes() != null && !createDTO.getSupportingCountryCodes().isEmpty()) {
            Set<Country> countries = new HashSet<>();
            for (String code : createDTO.getSupportingCountryCodes()) {
                Country country = countryRepository.findByCode(code)
                        .orElseThrow(() -> new ResourceNotFoundException("Country not found with code: " + code));
                countries.add(country);
            }
            faction.setSupportingCountries(countries);
        }

        Faction saved = factionRepository.save(faction);
        return toDTO(saved);
    }

    public FactionDTO updateFaction(Long id, FactionCreateDTO updateDTO) {
        Faction faction = factionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faction not found with id: " + id));

        Conflict conflict = conflictRepository.findById(updateDTO.getConflictId())
                .orElseThrow(() -> new ResourceNotFoundException("Conflict not found with id: " + updateDTO.getConflictId()));

        faction.setName(updateDTO.getName());
        faction.setConflict(conflict);

        if (updateDTO.getSupportingCountryCodes() != null) {
            Set<Country> countries = new HashSet<>();
            for (String code : updateDTO.getSupportingCountryCodes()) {
                Country country = countryRepository.findByCode(code)
                        .orElseThrow(() -> new ResourceNotFoundException("Country not found with code: " + code));
                countries.add(country);
            }
            faction.setSupportingCountries(countries);
        }

        Faction saved = factionRepository.save(faction);
        return toDTO(saved);
    }

    public void deleteFaction(Long id) {
        if (!factionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Faction not found with id: " + id);
        }
        factionRepository.deleteById(id);
    }

    private FactionDTO toDTO(Faction faction) {
        Set<String> countryCodes = faction.getSupportingCountries().stream()
                .map(Country::getCode)
                .collect(Collectors.toSet());

        return new FactionDTO(
                faction.getId(),
                faction.getName(),
                faction.getConflict().getId(),
                faction.getConflict().getName(),
                countryCodes
        );
    }
}

