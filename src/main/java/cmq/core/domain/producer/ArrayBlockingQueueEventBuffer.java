package cmq.core.domain.producer;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmq.core.domain.DomainEvent;

public class ArrayBlockingQueueEventBuffer implements DomainEventBuffer {

	private Log logger=LogFactory.getLog(this.getClass().getName());
	private static int defaultSize = 10000;
	private Queue<DomainEvent> queue = null;

	public ArrayBlockingQueueEventBuffer() {
		this(defaultSize);
	}

	public ArrayBlockingQueueEventBuffer(int capacity) {
		this.queue = new ArrayBlockingQueue<DomainEvent>(capacity);
	}

	public boolean put(DomainEvent domainEvent) {
		logger.info("queue.size()======================>>>>"+queue.size());
		return this.queue.add(domainEvent);
	}

	public DomainEvent take() {
		// TODO Auto-generated method stub
		return this.queue.poll();
	}

	public int size() {
		// TODO Auto-generated method stub
		return queue.size();
	}

}
