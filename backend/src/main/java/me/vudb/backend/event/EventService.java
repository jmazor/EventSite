package me.vudb.backend.event;

import me.vudb.backend.event.models.Event;
import me.vudb.backend.event.models.PrivateEvent;
import me.vudb.backend.event.models.PublicEvent;
import me.vudb.backend.event.models.RsoEvent;
import me.vudb.backend.event.repository.EventRepository;
import me.vudb.backend.event.repository.PrivateEventRepository;
import me.vudb.backend.event.repository.PublicEventRepository;
import me.vudb.backend.event.repository.RsoEventRepository;
import me.vudb.backend.rso.Rso;
import me.vudb.backend.university.University;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final PrivateEventRepository privateEventRepository;
    private final RsoEventRepository rsoEventRepository;
    private final PublicEventRepository publicEventRepository;

    public EventService(EventRepository eventRepository, PrivateEventRepository privateEventRepository, RsoEventRepository rsoEventRepository, PublicEventRepository publicEventRepository) {
        this.eventRepository = eventRepository;
        this.privateEventRepository = privateEventRepository;
        this.rsoEventRepository = rsoEventRepository;
        this.publicEventRepository = publicEventRepository;
    }

    public Optional<Event> findById(String id) {
        return eventRepository.findById(id);
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public PrivateEvent save(PrivateEvent privateEvent) {
        return privateEventRepository.save(privateEvent);
    }

    public PublicEvent save(PublicEvent publicEvent) {
        return publicEventRepository.save(publicEvent);
    }

    public RsoEvent save(RsoEvent rsoEvent) {
        return rsoEventRepository.save(rsoEvent);
    }

    public List<PublicEvent> findAllPublicEvent() {
        return publicEventRepository.findAll();
    }

    public List<PrivateEvent> findAllPrivateEvent(University university) {
        return privateEventRepository.findByUniversity(university);
    }

    public List<RsoEvent> findByRso(Rso rso) {
        return rsoEventRepository.findByRso(rso);
    }

    public List<PublicEvent> findAllApprovedPublicEvents() {
        return publicEventRepository.findByApprovalTrue();
    }

    public List<PublicEvent> findAllUnapprovedPublicEvents() {
        return publicEventRepository.findByApprovalFalse();
    }

    public void savePrivate(PrivateEvent privateEvent) {
        privateEventRepository.save(privateEvent);
    }

    public List<PublicEvent> findAllPublicEventsNeedingApproval() {
        return publicEventRepository.findByApprovalFalse();
    }

    public void savePublic(PublicEvent publicEvent) {
        publicEventRepository.save(publicEvent);
    }
}
