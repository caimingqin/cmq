package cmq.core.notification;

import java.io.Serializable;

public class Notification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6539406456663363348L;
	private Type type;
	private String name;
	private Object body;

	public Notification(Type type, Object body) {
		this(type,null,body);
	}

	public Notification(Type type, String name) {
		this(type, name, null);
	}

	public Notification(Type type, String name, Object body) {
		this.type = type;
		this.name = name;
		this.body = body;
	}

	public boolean isDomainEvent() {
		if (this.type.equals(Type.DOMAIN_EVENT)) {
			return true;
		}
		return false;
	}

	public boolean isBroadcast() {
		if (this.type.equals(Type.BROADCAST)) {
			return true;
		}
		return false;
	}

	public boolean isTask() {
		if (this.type.equals(Type.TASK)) {
			return true;
		}
		return false;
	}

	public Object getBody() {
		return body;
	}

	public String getName() {
		return name;
	}

	public enum Type {
		BROADCAST, TASK, DOMAIN_EVENT
	}

}
