package ConflictTracker.conflict_tracker.service;

import ConflictTracker.conflict_tracker.dto.EventDTO;
import ConflictTracker.conflict_tracker.model.Conflict;
import ConflictTracker.conflict_tracker.model.Event;
import ConflictTracker.conflict_tracker.repository.ConflictRepository;
import ConflictTracker.conflict_tracker.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ConflictRepository conflictRepository;

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return convertToDTO(event);
    }

    public EventDTO createEvent(EventDTO dto) {
        Event event = new Event();
        updateEventFromDTO(event, dto);
        Event saved = eventRepository.save(event);
        return convertToDTO(saved);
    }

    public EventDTO updateEvent(Long id, EventDTO dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        updateEventFromDTO(event, dto);
        Event saved = eventRepository.save(event);
        return convertToDTO(saved);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    private void updateEventFromDTO(Event event, EventDTO dto) {
        event.setEventDate(dto.getEventDate());
        event.setLocation(dto.getLocation());
        event.setDescription(dto.getDescription());

        if (dto.getConflictId() != null) {
            Conflict conflict = conflictRepository.findById(dto.getConflictId())
                    .orElseThrow(() -> new RuntimeException("Conflict not found"));
            event.setConflict(conflict);
        }
    }

    private EventDTO convertToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setEventDate(event.getEventDate());
        dto.setLocation(event.getLocation());
        dto.setDescription(event.getDescription());

        if (event.getConflict() != null) {
            dto.setConflictId(event.getConflict().getId());
            dto.setConflictName(event.getConflict().getName());
        }

        return dto;
    }
}

