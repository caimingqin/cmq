package cmq.core.domain;

public interface DomainEventBuffer {

	boolean  put(DomainEvent domainEvent);
	DomainEvent take();
	int size();
}
