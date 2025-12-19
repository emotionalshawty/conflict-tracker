package ConflictTracker.conflict_tracker.service;

import ConflictTracker.conflict_tracker.dto.ConflictDTO;
import ConflictTracker.conflict_tracker.dto.CountryDTO;
import ConflictTracker.conflict_tracker.model.Conflict;
import ConflictTracker.conflict_tracker.model.ConflictStatus;
import ConflictTracker.conflict_tracker.model.Country;
import ConflictTracker.conflict_tracker.repository.ConflictRepository;
import ConflictTracker.conflict_tracker.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConflictService {

    @Autowired
    private ConflictRepository conflictRepository;

    @Autowired
    private CountryRepository countryRepository;

    public List<ConflictDTO> getAllConflicts() {
        return conflictRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ConflictDTO getConflictById(Long id) {
        Conflict conflict = conflictRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conflict not found"));
        return convertToDTO(conflict);
    }

    public List<ConflictDTO> getConflictsByStatus(ConflictStatus status) {
        return conflictRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ConflictDTO> getConflictsByCountryCode(String code) {
        return conflictRepository.findByCountries_Code(code).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ConflictDTO createConflict(ConflictDTO dto) {
        Conflict conflict = new Conflict();
        updateConflictFromDTO(conflict, dto);
        Conflict saved = conflictRepository.save(conflict);
        return convertToDTO(saved);
    }

    public ConflictDTO updateConflict(Long id, ConflictDTO dto) {
        Conflict conflict = conflictRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conflict not found"));
        updateConflictFromDTO(conflict, dto);
        Conflict saved = conflictRepository.save(conflict);
        return convertToDTO(saved);
    }

    public void deleteConflict(Long id) {
        conflictRepository.deleteById(id);
    }

    private void updateConflictFromDTO(Conflict conflict, ConflictDTO dto) {
        conflict.setName(dto.getName());
        conflict.setStartDate(dto.getStartDate());
        conflict.setStatus(dto.getStatus());
        conflict.setDescription(dto.getDescription());

        if (dto.getCountryIds() != null) {
            Set<Country> countries = new HashSet<>(countryRepository.findAllById(dto.getCountryIds()));
            conflict.setCountries(countries);
        }
    }

    private ConflictDTO convertToDTO(Conflict conflict) {
        ConflictDTO dto = new ConflictDTO();
        dto.setId(conflict.getId());
        dto.setName(conflict.getName());
        dto.setStartDate(conflict.getStartDate());
        dto.setStatus(conflict.getStatus());
        dto.setDescription(conflict.getDescription());

        Set<CountryDTO> countryDTOs = conflict.getCountries().stream()
                .map(c -> new CountryDTO(c.getId(), c.getName(), c.getCode()))
                .collect(Collectors.toSet());
        dto.setCountries(countryDTOs);

        return dto;
    }
}

