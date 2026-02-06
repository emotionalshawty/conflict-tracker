package ConflictTracker.conflict_tracker.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class EventCreateDTO {

    private LocalDate eventDate;

    private String location;

    private String description;

    @NotNull(message = "Conflict ID is required")
    private Long conflictId;

    // Constructors
    public EventCreateDTO() {}

    public EventCreateDTO(LocalDate eventDate, String location, String description, Long conflictId) {
        this.eventDate = eventDate;
        this.location = location;
        this.description = description;
        this.conflictId = conflictId;
    }

    // Getters and Setters
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getConflictId() { return conflictId; }
    public void setConflictId(Long conflictId) { this.conflictId = conflictId; }
}

