package cmq.core.notification.consumer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmq.core.notification.Notification;

public class NotificationConsumer {
	private List<NotificationListener> consumers = new ArrayList<NotificationListener>();

	private Log logger = LogFactory.getLog(this.getClass().getName());

	public NotificationConsumer(List<NotificationListener> consumers) {
		this.consumers = consumers;
	}

	public void add(NotificationListener consumer) {
		this.consumers.add(consumer);
	}

	private void before() {
		logger.info("开启事物");
	}

	private void after() {
		logger.info("提交事物");

	}

	public void consumeNotification(Notification notification) {
		before();
		doConsume(notification);
		after();
	}

	private void doConsume(Notification notification) {
		for (NotificationListener cons : this.consumers) {
			cons.handle(notification);
		}
	}
}
