package cmq.core.bootstrap.resource.surpport.visitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;

import cmq.core.annotation.Bean;
import cmq.core.bootstrap.resource.surpport.ClassMetadata;
import cmq.core.bootstrap.resource.surpport.ClassVisitor;
import cmq.core.inject.BeanInjector;

public class BeanVisitor implements ClassVisitor{

	private Log logger=LogFactory.getLog(this.getClass().getName());
	private BeanInjector beanInjector;
	
	public BeanVisitor(BeanInjector beanInjector) {
		this.beanInjector = beanInjector;
	}

	@Override
	public void visit(ClassMetadata paramClassMetadata) {
		String[] annotations = paramClassMetadata.getAnnotations();
		if(annotations!=null&&annotations.length>0){
			
		}
		for(String an:annotations){
			if(Bean.class.getName().equals(an)){
				String className = paramClassMetadata.getClassName();
				try {
					Class<?> clazz = ClassUtils.forName(className,null);
					logger.info("regist bean ["+clazz+"]");
					Bean bean = clazz.getAnnotation(Bean.class);
					beanInjector.registClass(bean.name(), clazz);
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

}
