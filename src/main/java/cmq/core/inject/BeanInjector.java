package cmq.core.inject;

import java.util.Map;

public interface BeanInjector {

	<T> T getBean(Class<T> type);

	Object getBean(String name);

	void registBean(String name, Object bean);

	void registClass(String key, Class<?> obs);

	void registClass(String name, Class<?> clazz, Map<String, Object> omap);

	void destroyAll();

	void inject(Object pt);

	<T> T getBeanOnSafe(Class<T> type);

	String[] getBeanNames();

	String[] getBeanNamesForType(Class<?> class1);


}
