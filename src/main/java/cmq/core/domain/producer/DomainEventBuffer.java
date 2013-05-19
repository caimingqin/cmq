package cmq.core.domain.producer;

import cmq.core.domain.DomainEvent;

public interface DomainEventBuffer {

	boolean  put(DomainEvent domainEvent);
	DomainEvent take();
	int size();
}
