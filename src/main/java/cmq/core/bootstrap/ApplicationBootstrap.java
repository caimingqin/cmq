package cmq.core.bootstrap;

import java.net.URL;
import java.util.ArrayList;
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
import cmq.core.domain.consumer.DomainEventConsumer;
import cmq.core.domain.processor.DomainEventProcessor;
import cmq.core.domain.producer.DomainEventBuffer;
import cmq.core.domain.producer.LinkedBlockingQueueEventBuffer;
import cmq.core.inject.BeanInjector;
import cmq.core.inject.impl.SpringBeanInjector;
import cmq.core.notification.consumer.NotificationConsumer;
import cmq.core.notification.consumer.NotificationListener;
import cmq.core.notification.processor.NotificationProcessor;
import cmq.core.notification.producer.ArrayBlockingQueueNotificationBuffer;
import cmq.core.notification.producer.NotificationBuffer;
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

	private void initComponent(ServletContext servletContext, BeanInjector beanInjector) {
		URL root = ServletContextUtils.getRoot(servletContext);
		AnnotationBeanReader bean = new AnnotationBeanReader(root, new BeanVisitor(beanInjector));
		CommandVisitor commandVisitor = new CommandVisitor();
		this.beanInjector.registBean(CommandVisitor.class.getName(), commandVisitor);
		AnnotationBeanReader command = new AnnotationBeanReader(root, commandVisitor);

		EventHandlerVisitor eventHandlerVisitor = new EventHandlerVisitor();
		this.beanInjector.registBean(EventHandlerVisitor.class.getName(), eventHandlerVisitor);
		AnnotationBeanReader evenHandler = new AnnotationBeanReader(root, eventHandlerVisitor);
		bean.build();
		command.build();
		evenHandler.build();

		DomainEventBuffer domainEventBuffer = getDomainEventBuffer();
		DomainEventConsumer domainEventConsumer = getDomainEventConsumer(eventHandlerVisitor.getObjectHandlers());
//		for (int i = 0; i < 10; i++) {
			startDomainEventProcessor(domainEventBuffer, domainEventConsumer);
//		}

		NotificationBuffer notificationBuffer = getNotificationBuffer();
		List<NotificationListener> listeners = buildNotificationListener();
		NotificationConsumer notificationConsumer = new NotificationConsumer(listeners);
//		for (int i = 0; i < 10; i++) {
			startNotificationListener(notificationBuffer, notificationConsumer);
//		}

	}

	private void startNotificationListener(NotificationBuffer notificationBuffer, NotificationConsumer consumer) {
		NotificationProcessor domainEventProcessor = new NotificationProcessor(notificationBuffer, consumer);
		domainEventProcessor.start();

	}

	private List<NotificationListener> buildNotificationListener() {
		List<NotificationListener> listeners = new ArrayList<NotificationListener>();
		String[] beanNames = this.beanInjector.getBeanNamesForType(NotificationListener.class);
		for (String name : beanNames) {
			NotificationListener listener = (NotificationListener) this.beanInjector.getBean(name);
			listeners.add(listener);
		}
		logger.info("buildNotificationListener===>>>size:" + listeners.size());
		return listeners;
	}

	private void startDomainEventProcessor(DomainEventBuffer buffer, DomainEventConsumer consumer) {
		DomainEventProcessor domainEventProcessor = new DomainEventProcessor(buffer, consumer);
		domainEventProcessor.start();
	}

	private DomainEventBuffer getDomainEventBuffer() {
		DomainEventBuffer domainEventBuffer = new LinkedBlockingQueueEventBuffer();
		beanInjector.registBean(DomainEventBuffer.class.getName(), domainEventBuffer);
		return domainEventBuffer;
	}

	private NotificationBuffer getNotificationBuffer() {
		NotificationBuffer nBuffer = new ArrayBlockingQueueNotificationBuffer();
		beanInjector.registBean(NotificationBuffer.class.getName(), nBuffer);
		return nBuffer;
	}

	private DomainEventConsumer getDomainEventConsumer(List<Object> consumers) {
		DomainEventConsumer consumer = new DomainEventConsumer(consumers);
		beanInjector.registBean(DomainEventConsumer.class.getName(), consumer);
		return consumer;
	}

	public BeanInjector getBeanInjector() {
		return new SpringBeanInjector();
	}

	public void contextDestroyed(ServletContextEvent sce) {
		this.beanInjector.destroyAll();
	}

}
