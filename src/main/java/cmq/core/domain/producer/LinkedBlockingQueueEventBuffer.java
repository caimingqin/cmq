package cmq.core.domain.producer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import cmq.core.domain.DomainEvent;

public class LinkedBlockingQueueEventBuffer implements DomainEventBuffer {

	// private Log logger=LogFactory.getLog(this.getClass().getName());
	private static int defaultSize = 10000;
	// private Queue<DomainEvent> queue = null;

	private BlockingQueue<DomainEvent> queue = null;

	public LinkedBlockingQueueEventBuffer() {
		this(defaultSize);
	}

	public LinkedBlockingQueueEventBuffer(int capacity) {
		this.queue = new ArrayBlockingQueue<DomainEvent>(capacity);
	}

	public boolean put(DomainEvent domainEvent) {
		// logger.info("queue.size()======================>>>>"+queue.size());
		try {
			this.queue.put(domainEvent);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
		return true;
	}

	public DomainEvent take() {
		try {
			return this.queue.take();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return null;
	}

	public int size() {
		// TODO Auto-generated method stub
		return queue.size();
	}

}
