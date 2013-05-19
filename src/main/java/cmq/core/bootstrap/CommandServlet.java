package cmq.core.bootstrap;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmq.core.bootstrap.resource.surpport.visitor.CommandVisitor;
import cmq.core.command.AbstractDomainEventcommand;
import cmq.core.command.Command;
import cmq.core.command.CommandConvertor;
import cmq.core.command.CommandFactory;
import cmq.core.domain.DomainEvent;
import cmq.core.domain.DomainEventGather;
import cmq.core.domain.producer.DomainEventBuffer;
import cmq.core.inject.BeanInjector;
import cmq.core.notification.Notification;
import cmq.core.notification.Notification.Type;
import cmq.core.notification.producer.NotificationBuffer;
import cmq.utils.JSON;

public class CommandServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3218877381912237143L;
	private Log logger=LogFactory.getLog(this.getClass().getName());
    private BeanInjector beanInjector;
    private CommandFactory commandProducer;
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();
		this.beanInjector=(BeanInjector) servletContext.getAttribute(BeanInjector.class.getName());
		CommandVisitor commandVisitor = this.beanInjector.getBean(CommandVisitor.class);
		commandProducer=new CommandFactory(commandVisitor.getCommandMap());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		InputStream inputStream = req.getInputStream();
		CommandConvertor convertor = JSON.decode(inputStream,
				CommandConvertor.class);
		
		this.logger.info("convertor====>>>"+JSON.encode(convertor));
		Command command = commandProducer.createCommand(convertor);
		Object object = command.execute();
		if (AbstractDomainEventcommand.class.isAssignableFrom(command
				.getClass())) {
			DomainEventGather gather = (DomainEventGather) object;
			DomainEvent domainEvent= gather.get();
			DomainEventBuffer domainEventBuffer = this.beanInjector.getBean(DomainEventBuffer.class);
			this.logger.info("get domainEventBuffer===========>>>"+domainEventBuffer);
			this.logger.info("domainEvent==============>>>>"+domainEvent);
			domainEventBuffer.put(domainEvent);
			NotificationBuffer notificationBuffer = this.beanInjector.getBean(NotificationBuffer.class);
			Notification notification = new Notification(Type.DOMAIN_EVENT, domainEvent);
			notificationBuffer.put(notification);
		}
	}

}
