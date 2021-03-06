package cmq.core.domain.consumer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import cmq.core.annotation.AutoEventHandler;
import cmq.core.domain.DomainEvent;
import cmq.utils.ReflectionUtility;

public class DomainEventConsumer {

	private List<Object> consumers = new ArrayList<Object>();

	private Log logger = LogFactory.getLog(this.getClass().getName());
//	private PlatformTransactionManager platformTransactionManager;

	public DomainEventConsumer(List<Object> consumers) {
		this.consumers = consumers;
//		platformTransactionManager = new DataSourceTransactionManager(dataSource);
	}

	public void add(Object consumer) {
		this.consumers.add(consumer);
	}

	private void before() {
		logger.info("开启事物");
//		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//		TransactionStatus transaction = platformTransactionManager.getTransaction(def);
	}

	private void after() {
		logger.info("提交事物");

	}

	public void consumeDomainEvent(DomainEvent domainEvent) {
		before();
		doConsume(domainEvent);
		after();
	}

	private void doConsume(DomainEvent domainEvent) {
		// this.logger.info("consumers===========size===========>>> " +
		// consumers.size());
		for (Object cons : this.consumers) {
			Method[] methods = cons.getClass().getMethods();
			// this.logger.info("methods========size=============>>> " +
			// methods.length);
			for (Method m : methods) {
				AutoEventHandler annotation = m.getAnnotation(AutoEventHandler.class);
				if (annotation != null) {
					// this.logger.info("(annotation.name()======================>>> "+
					// annotation.name());
					if (annotation.name().equals(domainEvent.getCommandName())) {
						ReflectionUtility.invokeMethod(m, cons, new Object[] { domainEvent });
					}
				}
			}

		}
	}
}
