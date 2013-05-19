package cmq.core.notification.consumer;

import cmq.core.notification.Notification;

public interface NotificationListener {

	public void handle(Notification notification);
}
