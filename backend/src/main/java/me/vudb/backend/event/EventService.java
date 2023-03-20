package me.vudb.backend.event;

import me.vudb.backend.event.repository.EventRepository;
import me.vudb.backend.event.repository.PrivateEventRepository;
import me.vudb.backend.event.repository.PublicEventRepository;
import me.vudb.backend.event.repository.RsoEventRepository;
import org.springframework.stereotype.Service;

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


}
