package ConflictTracker.conflict_tracker.service;

import ConflictTracker.conflict_tracker.dto.CountryDTO;
import ConflictTracker.conflict_tracker.model.Country;
import ConflictTracker.conflict_tracker.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    public List<CountryDTO> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(this::convertirToDTO)
                .collect(Collectors.toList());
    }

    public CountryDTO getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found"));
        return convertirToDTO(country);
    }

    public CountryDTO createCountry(CountryDTO dto) {
        Country country = new Country();
        country.setName(dto.getName());
        country.setCode(dto.getCode());
        Country saved = countryRepository.save(country);
        return convertirToDTO(saved);
    }

    public CountryDTO updateCountry(Long id, CountryDTO dto) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found"));
        country.setName(dto.getName());
        country.setCode(dto.getCode());
        Country saved = countryRepository.save(country);
        return convertirToDTO(saved);
    }

    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
    }

    public CountryDTO getCountryByCode(String code) {
        Country country = countryRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Country not found"));
        return convertirToDTO(country);
    }

    private CountryDTO convertirToDTO(Country country) {
        return new CountryDTO(country.getId(), country.getName(), country.getCode());
    }
}

