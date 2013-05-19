package cmq.core.bootstrap;

import java.net.URL;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmq.core.bootstrap.resource.AnnotationBeanReader;
import cmq.core.bootstrap.resource.surpport.visitor.BeanVisitor;
import cmq.core.bootstrap.resource.surpport.visitor.CommandVisitor;
import cmq.core.bootstrap.resource.surpport.visitor.EventHandlerVisitor;
import cmq.core.domain.ArrayBlockingQueueEventBuffer;
import cmq.core.domain.DomainEventBuffer;
import cmq.core.domain.handler.DomainEventConsumer;
import cmq.core.domain.processor.DomainEventProcessor;
import cmq.core.inject.BeanInjector;
import cmq.core.inject.impl.SpringBeanInjector;
import cmq.utils.ServletContextUtils;

public class ApplicationBootstrap implements ServletContextListener {

	private Log logger = LogFactory.getLog(this.getClass().getName());
	private BeanInjector beanInjector;

	public void contextInitialized(ServletContextEvent sce) {
		logger.info("start ApplicationBootstrap===============================>>>>>>");
		ServletContext servletContext = sce.getServletContext();
		beanInjector = getBeanInjector();
		servletContext.setAttribute(BeanInjector.class.getName(), beanInjector);
		initComponent(servletContext, beanInjector);
	}

	private void initComponent(ServletContext servletContext,
			BeanInjector beanInjector) {
		URL root = ServletContextUtils.getRoot(servletContext);
		AnnotationBeanReader bean = new AnnotationBeanReader(root,
				new BeanVisitor(beanInjector));
		CommandVisitor commandVisitor = new CommandVisitor();
		this.beanInjector.registBean(CommandVisitor.class.getName(), commandVisitor);
		AnnotationBeanReader command = new AnnotationBeanReader(root,
				commandVisitor);
		
		EventHandlerVisitor eventHandlerVisitor = new EventHandlerVisitor();
		this.beanInjector.registBean(EventHandlerVisitor.class.getName(), eventHandlerVisitor);
		AnnotationBeanReader evenHandler = new AnnotationBeanReader(root,
				eventHandlerVisitor);
		bean.build();
		command.build();
		evenHandler.build();

		DomainEventBuffer domainEventBuffer = getDomainEventBuffer();
		DomainEventConsumer domainEventConsumer = getDomainEventConsumer(eventHandlerVisitor
				.getObjectHandlers());
		openProcessor(domainEventBuffer, domainEventConsumer);
	}

	private void openProcessor(DomainEventBuffer buffer,
			DomainEventConsumer consumer) {
		DomainEventProcessor domainEventProcessor = new DomainEventProcessor(
				buffer, consumer);
		domainEventProcessor.start();
	}

	private DomainEventBuffer getDomainEventBuffer() {
		DomainEventBuffer domainEventBuffer = new ArrayBlockingQueueEventBuffer();
		beanInjector.registBean(DomainEventBuffer.class.getName(),
				domainEventBuffer);
		return domainEventBuffer;
	}

	private DomainEventConsumer getDomainEventConsumer(List<Object> consumers) {
		DomainEventConsumer consumer =  new DomainEventConsumer(consumers);
			beanInjector.registBean(DomainEventConsumer.class.getName(),
					consumer);
		return consumer;
	}

	public BeanInjector getBeanInjector() {
		return new SpringBeanInjector();
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
