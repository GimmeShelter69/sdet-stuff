package org.wjihle.cars.tests.testbase;

import java.io.FileNotFoundException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.wjihle.cars.tests.CarRentalsBusinessLogic;
import org.wjihle.cars.tests.TestConstants;
import org.wjihle.cars.tests.TestController;

public class TestBase {
	
	String modelResponse;
	Logger logger;
	CarRentalsBusinessLogic logic = new CarRentalsBusinessLogic();
	
	public String getResponseFromServer() throws FileNotFoundException {
		return logic.getResource(TestConstants.DATASTRING);			
	}
	
	public Logger getLogger() {
		/*
		 * Configure the Logger
		 */
		logger = Logger.getLogger(TestController.class);
	    Logger root = Logger.getRootLogger();
	    root.addAppender(new ConsoleAppender(
	           new PatternLayout(PatternLayout.DEFAULT_CONVERSION_PATTERN)));
	    return logger;
	}
}
