package ConflictTracker.conflict_tracker.controller;

import ConflictTracker.conflict_tracker.dto.ConflictDTO;
import ConflictTracker.conflict_tracker.dto.CountryCreateDTO;
import ConflictTracker.conflict_tracker.dto.CountryDTO;
import ConflictTracker.conflict_tracker.service.ConflictService;
import ConflictTracker.conflict_tracker.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;
    private final ConflictService conflictService;

    public CountryController(CountryService countryService, ConflictService conflictService) {
        this.countryService = countryService;
        this.conflictService = conflictService;
    }

    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        List<CountryDTO> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable Long id) {
        CountryDTO country = countryService.getCountryById(id);
        return ResponseEntity.ok(country);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CountryDTO> getCountryByCode(@PathVariable String code) {
        CountryDTO country = countryService.getCountryByCode(code);
        return ResponseEntity.ok(country);
    }

    @GetMapping("/{code}/conflicts")
    public ResponseEntity<List<ConflictDTO>> getConflictsByCountryCode(@PathVariable String code) {
        List<ConflictDTO> conflicts = conflictService.getConflictsByCountryCode(code);
        return ResponseEntity.ok(conflicts);
    }

    @PostMapping
    public ResponseEntity<CountryDTO> createCountry(@RequestBody CountryCreateDTO createDTO) {
        CountryDTO created = countryService.createCountry(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryDTO> updateCountry(
            @PathVariable Long id,
            @RequestBody CountryCreateDTO updateDTO) {
        CountryDTO updated = countryService.updateCountry(id, updateDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }
}

