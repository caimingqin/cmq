package cmq.core.notification.producer;

import cmq.core.notification.Notification;

public interface NotificationBuffer {

	boolean  put(Notification domainEvent);
	Notification take();
	int size();
}
