package ConflictTracker.conflict_tracker.controller;

import ConflictTracker.conflict_tracker.dto.ConflictCreateDTO;
import ConflictTracker.conflict_tracker.dto.ConflictDTO;
import ConflictTracker.conflict_tracker.model.ConflictStatus;
import ConflictTracker.conflict_tracker.service.ConflictService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/web/conflicts")
public class WebConflictController {

    private final ConflictService conflictService;

    public WebConflictController(ConflictService conflictService) {
        this.conflictService = conflictService;
    }

    @GetMapping
    public String listConflicts(Model model) {
        model.addAttribute("conflicts", conflictService.getAllConflicts());
        return "conflicts";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("conflict", new ConflictCreateDTO());
        model.addAttribute("statuses", ConflictStatus.values());
        return "conflict-form";
    }

    @PostMapping
    public String saveConflict(@Valid @ModelAttribute("conflict") ConflictCreateDTO dto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", ConflictStatus.values());
            return "conflict-form";
        }
        conflictService.createConflict(dto);
        return "redirect:/web/conflicts";
    }

    @GetMapping("/delete/{id}")
    public String deleteConflict(@PathVariable Long id) {
        conflictService.deleteConflict(id);
        return "redirect:/web/conflicts";
    }
}
