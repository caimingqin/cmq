package cmq.core.notification.producer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import cmq.core.notification.Notification;

public class LinkedBlockingQueueEventBuffer implements NotificationBuffer {

	// private Log logger=LogFactory.getLog(this.getClass().getName());
	private static int defaultSize = 10000;
	// private Queue<Notification> queue = null;

	private BlockingQueue<Notification> queue = null;

	public LinkedBlockingQueueEventBuffer() {
		this(defaultSize);
	}

	public LinkedBlockingQueueEventBuffer(int capacity) {
		this.queue = new ArrayBlockingQueue<Notification>(capacity);
	}

	public boolean put(Notification Notification) {
		// logger.info("queue.size()======================>>>>"+queue.size());
		try {
			this.queue.put(Notification);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
		return true;
	}

	public Notification take() {
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
