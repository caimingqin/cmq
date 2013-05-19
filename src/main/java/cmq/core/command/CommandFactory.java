package cmq.core.command;

import java.util.HashMap;
import java.util.Map;

import cmq.utils.JSON;

public class CommandFactory {

	private  Map<String, Class<? extends Command>> cMaps = new HashMap<String, Class<? extends Command>>();

	public CommandFactory(  Map<String, Class<? extends Command>> cMaps) {
		this.cMaps = cMaps;
	}


	public  Command createCommand(CommandConvertor convertor) {
		Class<? extends Command> commandType = cMaps.get(convertor.getCommand());
		Command command = JSON.decode(convertor.getBody(), commandType);
		return command;
	}
}
