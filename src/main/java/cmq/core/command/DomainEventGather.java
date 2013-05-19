package cmq.core.command;

import cmq.core.domain.DomainEvent;

public class DomainEventGather {

	private DomainEvent domainEvent;

	public void set(DomainEvent domainEvento) {
		this.domainEvent=domainEvento;

	}

	public DomainEvent get() {
		return this.domainEvent;
	}
}
