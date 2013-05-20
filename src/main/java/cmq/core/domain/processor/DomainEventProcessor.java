package cmq.core.domain.processor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmq.core.domain.DomainEvent;
import cmq.core.domain.consumer.DomainEventConsumer;
import cmq.core.domain.producer.DomainEventBuffer;

public class DomainEventProcessor implements Runnable {

	private Log logger = LogFactory.getLog(this.getClass().getName());
	private final AtomicBoolean running = new AtomicBoolean(false);
	private DomainEventBuffer buffer;
	private DomainEventConsumer consumer;
	private Executor executor = Executors.newFixedThreadPool(1);

	public DomainEventProcessor(DomainEventBuffer buffer, DomainEventConsumer consumer) {
		this.buffer = buffer;
		this.consumer = consumer;
	}

	public void start() {
		if (running.compareAndSet(false, true)) {
			logger.info("start DomainEventProcessor success =====================>>>");
			this.executor.execute(this);
		} else {
			logger.error("start DomainEventProcessor failure =====================>>>");
		}
	}

	public void stop() {
		if (running.compareAndSet(true, false)) {
			logger.info("stop DomainEventProcessor success =====================>>>");
		} else {
			logger.error("stop DomainEventProcessor failure =====================>>>");
		}

	}

	protected void doRun() {
		while (running.get()) {
			DomainEvent domainEvent = buffer.take();// 阻塞
			if (domainEvent != null) {
				this.consumer.consumeDomainEvent(domainEvent);
			}
			logger.info("consumeDomainEvent===>>" + domainEvent);
		}
	}

	@Override
	public void run() {
		doRun();
	}

}