package ConflictTracker.conflict_tracker.dto;

import ConflictTracker.conflict_tracker.model.ConflictStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public class ConflictCreateDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private LocalDate startDate;

    @NotNull(message = "Status is required")
    private ConflictStatus status;

    private String description;

    private Set<String> countryCodes;

    // Constructors
    public ConflictCreateDTO() {}

    public ConflictCreateDTO(String name, LocalDate startDate, ConflictStatus status,
                             String description, Set<String> countryCodes) {
        this.name = name;
        this.startDate = startDate;
        this.status = status;
        this.description = description;
        this.countryCodes = countryCodes;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public ConflictStatus getStatus() { return status; }
    public void setStatus(ConflictStatus status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<String> getCountryCodes() { return countryCodes; }
    public void setCountryCodes(Set<String> countryCodes) { this.countryCodes = countryCodes; }
}

