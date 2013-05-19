package cmq.core.command;

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
import cmq.core.domain.DomainEvent;
import cmq.core.domain.DomainEventBuffer;
import cmq.core.inject.BeanInjector;
import cmq.utils.JSON;

public class CommandServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3218877381912237143L;
	private Log logger=LogFactory.getLog(this.getClass().getName());
    private BeanInjector beanInjector;
    private CommandProducer commandProducer;
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();
		this.beanInjector=(BeanInjector) servletContext.getAttribute(BeanInjector.class.getName());
		CommandVisitor commandVisitor = this.beanInjector.getBean(CommandVisitor.class);
		commandProducer=new CommandProducer(commandVisitor.getCommandMap());
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
		}
	}

}
