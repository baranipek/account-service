package com.payment.cmd.infrastructure;

import com.cqrs.core.domain.AggregateRoot;
import com.cqrs.core.events.BaseEvent;
import com.cqrs.core.handlers.EventSourcingHandler;
import com.cqrs.core.infra.EventStore;
import com.cqrs.core.producers.EventProducer;
import com.payment.cmd.domain.aggregate.PaymentAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class PaymentEventSourcingHandler implements EventSourcingHandler<PaymentAggregate> {

    @Autowired
    private EventStore eventStore;

    @Autowired
    private EventProducer eventProducer;

    @Override
    public void save(AggregateRoot aggregate)  {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
        aggregate.markChangesAsCommitted();
    }

    @Override
    public PaymentAggregate getById(String id) {
        var aggregate = new PaymentAggregate();
        var events = eventStore.getEvents(id);
        if (events != null && !events.isEmpty()) {
            aggregate.replayEvent(events);
            var latestVersion = events.stream()
                    .map(BaseEvent::getVersion)
                    .max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }
        return aggregate;
    }

    @Override
    public void republishEvents() {
        var aggregateIds = eventStore.getAggregateIds();
        for (var aggregateId : aggregateIds) {
            var aggregate = getById(aggregateId);
            if (aggregate == null)
                continue;
            var events = eventStore.getEvents(aggregateId);
            for (var event : events) {
                eventProducer.produce(event.getClass().getSimpleName(), event);
                System.out.println(event.getClass().getSimpleName());
            }
        }
    }
}
