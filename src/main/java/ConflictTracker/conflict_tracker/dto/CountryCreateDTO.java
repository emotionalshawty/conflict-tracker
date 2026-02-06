package ConflictTracker.conflict_tracker.dto;

import jakarta.validation.constraints.NotBlank;

public class CountryCreateDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Code is required")
    private String code;

    // Constructors
    public CountryCreateDTO() {}

    public CountryCreateDTO(String name, String code) {
        this.name = name;
        this.code = code;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}

