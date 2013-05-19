package cmq.core.bootstrap.resource.surpport.visitor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmq.core.annotation.AutoEventHandler;
import cmq.core.bootstrap.resource.surpport.ClassMetadata;
import cmq.core.bootstrap.resource.surpport.ClassVisitor;
import cmq.utils.WClassUtil;

public class EventHandlerVisitor implements ClassVisitor {

	private Log logger = LogFactory.getLog(this.getClass().getName());
	private List<Object> handlers = new ArrayList<Object>();

	@Override
	public void visit(ClassMetadata paramClassMetadata) {
		String[] annotations = paramClassMetadata.getAnnotations();
		if (annotations != null && annotations.length > 0) {

		}
		for (String an : annotations) {
			if (AutoEventHandler.class.getName().equals(an)) {
				Object obs = WClassUtil.getInstance(paramClassMetadata
						.getClassName());
				logger.info("add ============>>>EventHandler [" + obs + "]");
				this.handlers.add(obs);
				break;
			}
		}

	}
	
	 public List<Object> getObjectHandlers()
	   {
	     return this.handlers;
	   }
}
