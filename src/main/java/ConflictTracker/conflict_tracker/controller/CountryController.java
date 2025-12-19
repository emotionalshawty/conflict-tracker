package ConflictTracker.conflict_tracker.controller;

import ConflictTracker.conflict_tracker.dto.ConflictDTO;
import ConflictTracker.conflict_tracker.service.ConflictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    @Autowired
    private ConflictService conflictService;

    @GetMapping("/{code}/conflicts")
    public ResponseEntity<List<ConflictDTO>> getConflictsByCountry(@PathVariable String code) {
        return ResponseEntity.ok(conflictService.getConflictsByCountryCode(code));
    }
}

