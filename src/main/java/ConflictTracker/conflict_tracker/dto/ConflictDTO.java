package ConflictTracker.conflict_tracker.dto;

import ConflictTracker.conflict_tracker.model.ConflictStatus;
import java.time.LocalDate;
import java.util.Set;

public class ConflictDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private ConflictStatus status;
    private String description;
    private Set<Long> countryIds;
    private Set<CountryDTO> countries;

    public ConflictDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public ConflictStatus getStatus() { return status; }
    public void setStatus(ConflictStatus status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Set<Long> getCountryIds() { return countryIds; }
    public void setCountryIds(Set<Long> countryIds) { this.countryIds = countryIds; }
    public Set<CountryDTO> getCountries() { return countries; }
    public void setCountries(Set<CountryDTO> countries) { this.countries = countries; }
}

