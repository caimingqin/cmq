package cmq.core.command;

public abstract class AbstractDomainEventcommand implements Command {
	
	public Object execute() {
		DomainEventGather gather = new DomainEventGather();
		execute(gather);
		return gather;
	}

	public abstract Object execute(DomainEventGather gather);
}
