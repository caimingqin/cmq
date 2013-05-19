package cmq.core.domain;

import java.util.HashMap;
import java.util.Map;

public class DomainEvent {

	private String commandName;
	private Object target;
	private Map<String, Object> map=new HashMap<String, Object>();
	
	
	public DomainEvent(String commandName) {
		this.commandName = commandName;
	}


	public DomainEvent(String commandName, Object target) {
		this.commandName = commandName;
		this.target = target;
	}
	
	public void putProperty(String key,Object object){
		map.put(key, object);
	}
	
	public Object getProperty(String key) {
		return map.get(key);
	}


	public String getCommandName() {
		return commandName;
	}


	public Object getTarget() {
		return target;
	}
	
}
