package alltest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.gateway.util.DateTimeUtilTest;

@RunWith(Suite.class)
@SuiteClasses({
	DateTimeUtilTest.class
})
public class AllTests {

	private static ClassPathXmlApplicationContext context;
	
	public static ClassPathXmlApplicationContext getApplicationContext() {
		if (context==null) {
			context = new ClassPathXmlApplicationContext("spring-main.xml");
		}
		return context;
	}
	
	public static void closeApplicationContext() {
		context.close();
	}
	
}
