package ConflictTracker.conflict_tracker.controller;

import ConflictTracker.conflict_tracker.dto.ConflictCreateDTO;
import ConflictTracker.conflict_tracker.dto.ConflictDetailDTO;
import ConflictTracker.conflict_tracker.dto.ConflictDTO;
import ConflictTracker.conflict_tracker.model.ConflictStatus;
import ConflictTracker.conflict_tracker.service.ConflictService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conflicts")
public class ConflictController {

    private final ConflictService conflictService;

    public ConflictController(ConflictService conflictService) {
        this.conflictService = conflictService;
    }

    @GetMapping
    public ResponseEntity<List<ConflictDTO>> getAllConflicts(
            @RequestParam(required = false) ConflictStatus status) {
        List<ConflictDTO> conflicts;
        if (status != null) {
            conflicts = conflictService.getConflictsByStatus(status);
        } else {
            conflicts = conflictService.getAllConflicts();
        }
        return ResponseEntity.ok(conflicts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConflictDetailDTO> getConflictById(@PathVariable Long id) {
        ConflictDetailDTO conflict = conflictService.getConflictById(id);
        return ResponseEntity.ok(conflict);
    }

    @PostMapping
    public ResponseEntity<ConflictDTO> createConflict(@RequestBody ConflictCreateDTO createDTO) {
        ConflictDTO created = conflictService.createConflict(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConflictDTO> updateConflict(
            @PathVariable Long id,
            @RequestBody ConflictCreateDTO updateDTO) {
        ConflictDTO updated = conflictService.updateConflict(id, updateDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConflict(@PathVariable Long id) {
        conflictService.deleteConflict(id);
        return ResponseEntity.noContent().build();
    }
}



