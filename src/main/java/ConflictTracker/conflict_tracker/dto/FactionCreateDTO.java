package ConflictTracker.conflict_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public class FactionCreateDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Conflict ID is required")
    private Long conflictId;

    private Set<String> supportingCountryCodes;

    // Constructors
    public FactionCreateDTO() {}

    public FactionCreateDTO(String name, Long conflictId, Set<String> supportingCountryCodes) {
        this.name = name;
        this.conflictId = conflictId;
        this.supportingCountryCodes = supportingCountryCodes;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getConflictId() { return conflictId; }
    public void setConflictId(Long conflictId) { this.conflictId = conflictId; }

    public Set<String> getSupportingCountryCodes() { return supportingCountryCodes; }
    public void setSupportingCountryCodes(Set<String> supportingCountryCodes) {
        this.supportingCountryCodes = supportingCountryCodes;
    }
}

