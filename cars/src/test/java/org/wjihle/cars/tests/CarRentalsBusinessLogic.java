package org.wjihle.cars.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class CarRentalsBusinessLogic {

	public  JSONObject getHighestProfitCarForYear(String modelResponse)
			throws JSONException {
		JSONArray jsonArray = getListOfAllCars(modelResponse);
		JSONObject mostProfitableCar=null,car=null, perDayRent=null, metrics=null, rentalCount=null;
		int price;
		double priceAfterDiscount,totalRevenuePerCar,totalExpensePerCar,totalProfit,highestProfit=0;
		
		for (int i = 0; i < jsonArray.length(); i++) {
			car = (JSONObject) jsonArray.get(i);
			perDayRent = (JSONObject) car.get("perdayrent");
			metrics = (JSONObject) car.get("metrics");
			rentalCount = (JSONObject) metrics.get("rentalcount");
			price=perDayRent.getInt("Price");
			priceAfterDiscount = price - (price * (perDayRent.getDouble("Discount")/100));
			/*
			 * Calc total cost and total expense per car
			 */
			
			totalRevenuePerCar = priceAfterDiscount * rentalCount.getInt("yeartodate");
			totalExpensePerCar = Math.round(metrics.getDouble("yoymaintenancecost") + metrics.getDouble("depreciation"));
			totalProfit = Math.round(totalRevenuePerCar - totalExpensePerCar);
			
			if (totalProfit > highestProfit) {
				highestProfit = totalProfit;
				mostProfitableCar=car;
			}
			
		}		
		return mostProfitableCar;
	}


	public JSONObject getLowestPerDayRentalCostCarAfterDiscount(String modelResponse)
			throws JSONException {
		JSONArray jsonArray = getListOfAllCars(modelResponse);
		JSONObject lowestPricedCar=null,car=null, perdayrent=null;
		int price;
		double priceAfterDiscount,lowestPrice=10000;
		
		for (int i = 0; i < jsonArray.length(); i++) {
			car = (JSONObject) jsonArray.get(i);
			perdayrent = (JSONObject) car.get("perdayrent");
			price=perdayrent.getInt("Price");
			priceAfterDiscount = price - (price * (perdayrent.getDouble("Discount")/100));
			if (priceAfterDiscount < lowestPrice) {
				lowestPrice = priceAfterDiscount;
				lowestPricedCar=car;
			}
			
		}		
		return lowestPricedCar;
	}

	public JSONObject getLowestPerDayRentalCostCar(String modelResponse)
			throws JSONException {
		JSONArray jsonArray = getListOfAllCars(modelResponse);
		JSONObject lowestPricedCar=null,car=null, perdayrent=null;
		int lowestPrice=10000;
		
		for (int i = 0; i < jsonArray.length(); i++) {
			car = (JSONObject) jsonArray.get(i);
			perdayrent = (JSONObject) car.get("perdayrent");
			if (perdayrent.getInt("Price") < lowestPrice) {
				lowestPrice = perdayrent.getInt("Price");
				lowestPricedCar=car;
			}
			
		}		
		return lowestPricedCar;
	}

	public JSONArray getBlueTeslas(String modelResponse) throws JSONException {
		JSONArray listOfCars = getListOfAllCars(modelResponse);
		JSONArray allBlueTeslas = new JSONArray();
		JSONObject car, metadata;

		for (int i = 0; i < listOfCars.length(); i++) {
			car = (JSONObject) listOfCars.get(i);
			metadata = (JSONObject) car.get("metadata");
			if (car.get("make").equals("Tesla")
					&& metadata.get("Color").equals("Blue")) {
				allBlueTeslas.put(car);
			}
		}
		return allBlueTeslas;
	}
		
	public JSONArray getListOfAllCars(String modelResponse)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(modelResponse);
		JSONArray jsonArray = jsonObject.getJSONArray("Cars");
		return jsonArray;
	}
	
	public String getResource(String fname) throws FileNotFoundException {
		/*
		 * Get the inputstream to the resource
		 */
		File resource = new File("cars.json");
		StringBuilder sb = new StringBuilder();
		InputStream inputStream=getClass().getClassLoader().getResourceAsStream(fname);
		Scanner scanner=new Scanner(inputStream);
		
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}
		
		scanner.close();
		return sb.toString();
	}
}
