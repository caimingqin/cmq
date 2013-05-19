package cmq.core.bootstrap.resource.surpport.visitor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;

import cmq.core.annotation.AutoCommand;
import cmq.core.bootstrap.resource.surpport.ClassMetadata;
import cmq.core.bootstrap.resource.surpport.ClassVisitor;
import cmq.core.command.Command;

public class CommandVisitor implements ClassVisitor {

	private Log logger = LogFactory.getLog(this.getClass().getName());
	private Map<String, Class<? extends Command>> cMap = new HashMap<String, Class<? extends Command>>();

	@SuppressWarnings("unchecked")
	@Override
	public void visit(ClassMetadata paramClassMetadata) {
		String[] annotations = paramClassMetadata.getAnnotations();
		if (annotations != null && annotations.length > 0) {

		}
		for (String an : annotations) {
			if (AutoCommand.class.getName().equals(an)) {
				String className = paramClassMetadata.getClassName();
				try {
					Class<?> clazz = ClassUtils.forName(className, null);
					if (!Command.class.isAssignableFrom(clazz)) {
						throw new IllegalArgumentException("The class["
								+ paramClassMetadata.getClassName()
								+ "] not implements Command interface");
					}
					logger.info("add ============>>>command [" + clazz + "]");
					AutoCommand command = clazz
							.getAnnotation(AutoCommand.class);
					cMap.put(command.name(), (Class<? extends Command>) clazz);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LinkageError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public Map<String, Class<? extends Command>> getCommandMap() {
		return this.cMap;

	}

}
