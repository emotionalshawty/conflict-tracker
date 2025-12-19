package ConflictTracker.conflict_tracker.controller;

import ConflictTracker.conflict_tracker.dto.FactionDTO;
import ConflictTracker.conflict_tracker.service.FactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/factions")
public class FactionController {

    @Autowired
    private FactionService factionService;

    @GetMapping
    public ResponseEntity<List<FactionDTO>> getAllFactions() {
        return ResponseEntity.ok(factionService.getAllFactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactionDTO> getFactionById(@PathVariable Long id) {
        return ResponseEntity.ok(factionService.getFactionById(id));
    }

    @PostMapping
    public ResponseEntity<FactionDTO> createFaction(@RequestBody FactionDTO factionDTO) {
        return new ResponseEntity<>(factionService.createFaction(factionDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FactionDTO> updateFaction(@PathVariable Long id, @RequestBody FactionDTO factionDTO) {
        return ResponseEntity.ok(factionService.updateFaction(id, factionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaction(@PathVariable Long id) {
        factionService.deleteFaction(id);
        return ResponseEntity.noContent().build();
    }
}

