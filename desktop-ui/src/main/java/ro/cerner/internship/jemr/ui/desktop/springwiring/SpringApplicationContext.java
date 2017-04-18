package ro.cerner.internship.jemr.ui.desktop.springwiring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class SpringApplicationContext {
	private static ApplicationContext applicationContext;

	private SpringApplicationContext() {
	}

	public static ApplicationContext instance() {
		if (null == applicationContext){
			applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		}

		return applicationContext;
	}
}
