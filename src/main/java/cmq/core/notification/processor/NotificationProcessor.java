package cmq.core.notification.processor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmq.core.notification.Notification;
import cmq.core.notification.consumer.NotificationConsumer;
import cmq.core.notification.producer.NotificationBuffer;

public class NotificationProcessor implements Runnable {

	private Log logger = LogFactory.getLog(this.getClass().getName());
	private final AtomicBoolean running = new AtomicBoolean(false);
	private NotificationBuffer buffer;
	private NotificationConsumer consumer;
	private Executor executor = Executors.newFixedThreadPool(1);

	public NotificationProcessor(NotificationBuffer buffer, NotificationConsumer consumer) {
		this.buffer = buffer;
		this.consumer = consumer;
	}

	public void start() {
		if (running.compareAndSet(false, true)) {
			logger.info("start NotificationProcessor success =====================>>>");
			this.executor.execute(this);
		} else {
			logger.error("start NotificationProcessor failure =====================>>>");
		}
	}

	public void stop() {
		if (running.compareAndSet(true, false)) {
			logger.info("stop NotificationProcessor success =====================>>>");
		} else {
			logger.error("stop NotificationProcessor failure =====================>>>");
		}

	}

	@Override
	public void run() {
		while (running.get()) {
			synchronized (this) {
				try {
					if (buffer.size() > 0) {
						Notification Notification = buffer.take();
						this.consumer.consumeNotification(Notification);
					} else {
						wait(2 * 1000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

}