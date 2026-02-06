package ConflictTracker.conflict_tracker.service;

import ConflictTracker.conflict_tracker.dto.EventCreateDTO;
import ConflictTracker.conflict_tracker.dto.EventDTO;
import ConflictTracker.conflict_tracker.model.Conflict;
import ConflictTracker.conflict_tracker.model.Event;
import ConflictTracker.conflict_tracker.repository.ConflictRepository;
import ConflictTracker.conflict_tracker.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final ConflictRepository conflictRepository;

    public EventService(EventRepository eventRepository, ConflictRepository conflictRepository) {
        this.eventRepository = eventRepository;
        this.conflictRepository = conflictRepository;
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return toDTO(event);
    }

    public List<EventDTO> getEventsByConflictId(Long conflictId) {
        return eventRepository.findByConflictIdOrderByEventDateDesc(conflictId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public EventDTO createEvent(EventCreateDTO createDTO) {
        Conflict conflict = conflictRepository.findById(createDTO.getConflictId())
                .orElseThrow(() -> new ResourceNotFoundException("Conflict not found with id: " + createDTO.getConflictId()));

        Event event = new Event();
        event.setEventDate(createDTO.getEventDate());
        event.setLocation(createDTO.getLocation());
        event.setDescription(createDTO.getDescription());
        event.setConflict(conflict);

        Event saved = eventRepository.save(event);
        return toDTO(saved);
    }

    public EventDTO updateEvent(Long id, EventCreateDTO updateDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        Conflict conflict = conflictRepository.findById(updateDTO.getConflictId())
                .orElseThrow(() -> new ResourceNotFoundException("Conflict not found with id: " + updateDTO.getConflictId()));

        event.setEventDate(updateDTO.getEventDate());
        event.setLocation(updateDTO.getLocation());
        event.setDescription(updateDTO.getDescription());
        event.setConflict(conflict);

        Event saved = eventRepository.save(event);
        return toDTO(saved);
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    private EventDTO toDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getEventDate(),
                event.getLocation(),
                event.getDescription(),
                event.getConflict().getId(),
                event.getConflict().getName()
        );
    }
}

