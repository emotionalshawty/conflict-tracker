package ConflictTracker.conflict_tracker.dto;

import ConflictTracker.conflict_tracker.model.ConflictStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ConflictDetailDTO {

    private Long id;
    private String name;
    private LocalDate startDate;
    private ConflictStatus status;
    private String description;
    private Set<CountryDTO> countries;
    private List<FactionDTO> factions;
    private List<EventDTO> events;

    // Constructors
    public ConflictDetailDTO() {}

    public ConflictDetailDTO(Long id, String name, LocalDate startDate, ConflictStatus status,
                             String description, Set<CountryDTO> countries,
                             List<FactionDTO> factions, List<EventDTO> events) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.status = status;
        this.description = description;
        this.countries = countries;
        this.factions = factions;
        this.events = events;
    }

    // Getters and Setters
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

    public Set<CountryDTO> getCountries() { return countries; }
    public void setCountries(Set<CountryDTO> countries) { this.countries = countries; }

    public List<FactionDTO> getFactions() { return factions; }
    public void setFactions(List<FactionDTO> factions) { this.factions = factions; }

    public List<EventDTO> getEvents() { return events; }
    public void setEvents(List<EventDTO> events) { this.events = events; }
}

