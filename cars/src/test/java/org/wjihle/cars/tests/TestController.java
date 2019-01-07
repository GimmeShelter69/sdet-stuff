package org.wjihle.cars.tests;

import java.io.FileNotFoundException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.wjihle.cars.tests.TestConstants;
import org.wjihle.cars.tests.testbase.TestBase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class TestController extends TestBase {

	CarRentalsBusinessLogic rentalsBusinessLogic = new CarRentalsBusinessLogic();
	String modelResponse;
	Logger logger;

	@BeforeSuite
	public void setUp() {
		logger=getLogger();
	}

	@BeforeTest
	public void init() throws FileNotFoundException {
		modelResponse = getResponseFromServer();
	}

	@Test
	@Parameters({ "expNumberOfTeslas", "expColorOfTesla" })
	public void testForBlueTeslas(int numberOfTeslasExpected, String expectedColorOfTesla) throws JSONException {

		JSONArray blueTeslas = rentalsBusinessLogic.getBlueTeslas(modelResponse);
		JSONObject car=null;
		JSONObject metadata = null;

		/*
		 * Validate that only two cars were returned
		 */
		Assert.assertEquals(blueTeslas.length(), numberOfTeslasExpected, "Incorrect number of cars returned:");

		/*
		 * Print all the blue Teslas received in the web service response. Also
		 * print the notes
		 */

		logger.info("Print the Teslas (and notes):\n");

		for (int i = 0; i < blueTeslas.length(); i++) {
			car = blueTeslas.getJSONObject(i);
			metadata = car.getJSONObject(TestConstants.METADATA);
			Assert.assertEquals(car.get(TestConstants.MAKE), TestConstants.TESLA,
					"Incorrect make/model of car returned:");
			Assert.assertEquals(metadata.get(TestConstants.COLOR), expectedColorOfTesla, "Incorrect color returned:");
			logger.info(car.get(TestConstants.MAKE) + ", " + car.get(TestConstants.MODEL) + ", " + metadata.get("Color")
					+ ", " + car.get(TestConstants.VIN) + ", " + metadata.get("Notes"));
		}
	}

	@Test
	@Parameters({ "expLowestPricedCar" })
	public void testForLowestPerDayRentalCostCar(String expLowestPricedCar)
			throws FileNotFoundException, JSONException {
		JSONObject lowestPerDayRentalCostCar = rentalsBusinessLogic.getLowestPerDayRentalCostCar(modelResponse);

		String actualLowestPricedCar = lowestPerDayRentalCostCar.get(TestConstants.MAKE) + " "
				+ lowestPerDayRentalCostCar.get(TestConstants.MODEL);
		/*
		 * Validate make and model of car
		 */		
		Assert.assertEquals(actualLowestPricedCar, expLowestPricedCar, "Incorrect make/model of car returned:");
	}

	@Test
	@Parameters({ "expLowestPricedCarAfterDiscount" })
	public void testForLowestPerDayRentalCostCarAfterDiscount(String expLowestPricedCarAfterDiscount)
			throws JSONException {
		JSONObject lowestPerDayRentalCostCarAfterDiscount = rentalsBusinessLogic
				.getLowestPerDayRentalCostCarAfterDiscount(modelResponse);
		String actualLowestPricedCarAfterDiscount = lowestPerDayRentalCostCarAfterDiscount.get(TestConstants.MAKE) + " "
				+ lowestPerDayRentalCostCarAfterDiscount.get(TestConstants.MODEL);
		/*
		 * Validate make and model of car
		 */
		Assert.assertEquals(actualLowestPricedCarAfterDiscount, expLowestPricedCarAfterDiscount,
				"Incorrect make/model of car returned:");
	}

	@Test()
	@Parameters({ "expHighestProfitCar" })
	public void testForHighestProfitCarForYear(String expHighestProfitCar) throws JSONException {
		JSONObject highestProfitCarForYear = rentalsBusinessLogic.getHighestProfitCarForYear(modelResponse);
		String mostProfitable = highestProfitCarForYear.get(TestConstants.MAKE) + " "
				+ highestProfitCarForYear.get(TestConstants.MODEL);
		/*
		 * Validate make and model of car
		 */
		Assert.assertEquals(mostProfitable, expHighestProfitCar, "Incorrect make/model of car returned:");
	}
}
