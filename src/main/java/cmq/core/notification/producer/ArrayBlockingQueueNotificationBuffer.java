package cmq.core.notification.producer;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import cmq.core.notification.Notification;

public class ArrayBlockingQueueNotificationBuffer implements NotificationBuffer {

//	private Log logger=LogFactory.getLog(this.getClass().getName());
	private static int defaultSize = 10000;
	private Queue<Notification> queue = null;

	public ArrayBlockingQueueNotificationBuffer() {
		this(defaultSize);
	}

	public ArrayBlockingQueueNotificationBuffer(int capacity) {
		this.queue = new ArrayBlockingQueue<Notification>(capacity);
	}

	public boolean put(Notification notification) {
		return this.queue.add(notification);
	}

	public Notification take() {
		return this.queue.poll();
	}

	public int size() {
		return queue.size();
	}

}
