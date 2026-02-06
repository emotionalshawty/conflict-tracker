package ConflictTracker.conflict_tracker.service;

import ConflictTracker.conflict_tracker.dto.CountryCreateDTO;
import ConflictTracker.conflict_tracker.dto.CountryDTO;
import ConflictTracker.conflict_tracker.model.Country;
import ConflictTracker.conflict_tracker.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryDTO> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CountryDTO getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));
        return toDTO(country);
    }

    public CountryDTO getCountryByCode(String code) {
        Country country = countryRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with code: " + code));
        return toDTO(country);
    }

    public CountryDTO createCountry(CountryCreateDTO createDTO) {
        if (countryRepository.existsByCode(createDTO.getCode())) {
            throw new IllegalArgumentException("Country with code " + createDTO.getCode() + " already exists");
        }

        Country country = new Country();
        country.setName(createDTO.getName());
        country.setCode(createDTO.getCode().toUpperCase());

        Country saved = countryRepository.save(country);
        return toDTO(saved);
    }

    public CountryDTO updateCountry(Long id, CountryCreateDTO updateDTO) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));

        country.setName(updateDTO.getName());
        country.setCode(updateDTO.getCode().toUpperCase());

        Country saved = countryRepository.save(country);
        return toDTO(saved);
    }

    public void deleteCountry(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Country not found with id: " + id);
        }
        countryRepository.deleteById(id);
    }

    private CountryDTO toDTO(Country country) {
        return new CountryDTO(country.getId(), country.getName(), country.getCode());
    }
}

