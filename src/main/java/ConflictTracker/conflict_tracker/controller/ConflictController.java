package ConflictTracker.conflict_tracker.controller;

import ConflictTracker.conflict_tracker.dto.ConflictDTO;
import ConflictTracker.conflict_tracker.model.ConflictStatus;
import ConflictTracker.conflict_tracker.service.ConflictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conflicts")
public class ConflictController {

    @Autowired
    private ConflictService conflictService;

    @GetMapping
    public ResponseEntity<List<ConflictDTO>> getAllConflicts(@RequestParam(required = false) ConflictStatus status) {
        if (status != null) {
            return ResponseEntity.ok(conflictService.getConflictsByStatus(status));
        }
        return ResponseEntity.ok(conflictService.getAllConflicts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConflictDTO> getConflictById(@PathVariable Long id) {
        return ResponseEntity.ok(conflictService.getConflictById(id));
    }

    @PostMapping
    public ResponseEntity<ConflictDTO> createConflict(@RequestBody ConflictDTO conflictDTO) {
        return new ResponseEntity<>(conflictService.createConflict(conflictDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConflictDTO> updateConflict(@PathVariable Long id, @RequestBody ConflictDTO conflictDTO) {
        return ResponseEntity.ok(conflictService.updateConflict(id, conflictDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConflict(@PathVariable Long id) {
        conflictService.deleteConflict(id);
        return ResponseEntity.noContent().build();
    }
}
