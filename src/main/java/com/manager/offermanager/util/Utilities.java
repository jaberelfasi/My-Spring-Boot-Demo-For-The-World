package com.manager.offermanager.util;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.offermanager.model.Offer;

public class Utilities {

	private int localServerPort;
	
	public Utilities(int localServerPort) {
		// TODO Auto-generated constructor stub
		this.localServerPort = localServerPort;
	}

		// Utilities
		public void prepTestDataInDB(String stringType) throws ParseException {
			// TODO Auto-generated method stub
			/*SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = dateFormat.parse("10/11/2018");*/

			RestTemplate restTemplate = new RestTemplate();
			List<Offer> ol = fillTestList(stringType);
			for (int i = 0; i < ol.size(); i++) {
				
				Offer of = ol.get(i);
				
				try {
					ResponseEntity<Offer> response = restTemplate.postForEntity("http://localhost:"+localServerPort+"/api/offer",
							of, Offer.class);
				} catch (Exception e) {
					//System.out.println(e);
					String url = "http://localhost:"+localServerPort+"/api/offer/" + of.getId();
					restTemplate.put(url, of);
				}
			}

		}

		public Offer mokOffer() {
			// TODO Auto-generated method stub
			Offer offer = new Offer();
			offer.setCanceled(false);
			offer.setOffercode("from junit");
			offer.setOffername("from junit");
			offer.setOfferperiod(1);
			offer.setPeriodunit("Month");
			return offer;
		}

		public List<Offer> fillTestList(String stringType) {
			// TODO Auto-generated method stub
			String jsonString = "";
			String jsonStringForSearchByName = "[\n" + "    {\n" + "		\"id\": 4,\n"
					+ "        \"offername\": \"offerlist for test\",\n" + "        \"offercode\": \"hollyD15\",\n"
					+ "        \"offerperiod\": 30,\n" + "        \"periodunit\": \"Day\",\n"
					+ "        \"createdAt\": \"2018-12-16T01:20:54.000+0000\",\n"
					+ "        \"updatedAt\": \"2018-12-16T01:20:54.000+0000\",\n" + "        \"canceled\": true\n"
					+ "    },\n" + "    {\n" + "		\"id\": 5,\n" + "        \"offername\": \"offerlist for test\",\n"
					+ "        \"offercode\": \"hollyD15\",\n" + "        \"offerperiod\": 30,\n"
					+ "        \"periodunit\": \"Day\",\n" + "        \"createdAt\": \"2018-12-16T01:20:55.000+0000\",\n"
					+ "        \"updatedAt\": \"2018-12-16T01:20:55.000+0000\",\n" + "        \"canceled\": true\n"
					+ "    },\n" + "    {\n" + "		\"id\": 6,\n" + "        \"offername\": \"offerlist for test\",\n"
					+ "        \"offercode\": \"hollyD15\",\n" + "        \"offerperiod\": 30,\n"
					+ "        \"periodunit\": \"Day\",\n" + "        \"createdAt\": \"2018-12-16T01:20:56.000+0000\",\n"
					+ "        \"updatedAt\": \"2018-12-16T01:20:56.000+0000\",\n" + "        \"canceled\": true\n"
					+ "    }\n" + "]";

			String jsonStringForValidity = "[\n" + "    {\n" + "        \"id\": 1,\n"
					+ "        \"offername\": \"buy 1 get 1 free\",\n" + "        \"offercode\": \"b1g2\",\n"
					+ "        \"offerperiod\": 1,\n" + "        \"periodunit\": \"Month\",\n"
					+ "        \"createdAt\": \"2018-11-10T22:11:07.000+0000\",\n"
					+ "        \"updatedAt\": \"2018-12-15T22:11:07.000+0000\",\n" + "        \"canceled\": false\n"
					+ "    },\n" + "    {\n" + "        \"id\": 2,\n" + "        \"offername\": \"the holly days\",\n"
					+ "        \"offercode\": \"hollyD15\",\n" + "        \"offerperiod\": 15,\n"
					+ "        \"periodunit\": \"Day\",\n" + "        \"createdAt\": \"2018-12-01T22:11:07.000+0000\",\n"
					+ "        \"updatedAt\": \"2018-12-15T22:18:27.000+0000\",\n" + "        \"canceled\": false\n"
					+ "    },\n" + "    {\n" + "        \"id\": 3,\n" + "        \"offername\": \"the holly days\",\n"
					+ "        \"offercode\": \"hollyD15\",\n" + "        \"offerperiod\": 30,\n"
					+ "        \"periodunit\": \"Day\",\n" + "        \"createdAt\": \"2018-12-16T00:57:38.000+0000\",\n"
					+ "        \"updatedAt\": \"2018-12-16T00:57:38.000+0000\",\n" + "        \"canceled\": true\n"
					+ "    }\n" + "]";

			String extraEnteries = "[\n" + "    {\n" + "        \"id\": 7,\n"
					+ "        \"offername\": \"one two three\",\n" + "        \"offercode\": \"bddd1g2\",\n"
					+ "        \"offerperiod\": 1,\n" + "        \"periodunit\": \"Month\",\n"
					+ "        \"createdAt\": \"2018-11-10T22:11:07.000+0000\",\n"
					+ "        \"updatedAt\": \"2018-12-15T22:11:07.000+0000\",\n" + "        \"canceled\": false\n"
					+ "    },\n" + "    {\n" + "        \"id\": 8,\n" + "        \"offername\": \"more things\",\n"
					+ "        \"offercode\": \"fffffddd\",\n" + "        \"offerperiod\": 15,\n"
					+ "        \"periodunit\": \"Day\",\n" + "        \"createdAt\": \"2018-12-01T22:11:07.000+0000\",\n"
					+ "        \"updatedAt\": \"2018-12-15T22:18:27.000+0000\",\n" + "        \"canceled\": false\n"
					+ "    },\n" + "    {\n" + "        \"id\": 9,\n" + "        \"offername\": \"extra stuff\",\n"
					+ "        \"offercode\": \"ssssgggg\",\n" + "        \"offerperiod\": 30,\n"
					+ "        \"periodunit\": \"Day\",\n" + "        \"createdAt\": \"2018-12-16T00:57:38.000+0000\",\n"
					+ "        \"updatedAt\": \"2018-12-16T00:57:38.000+0000\",\n" + "        \"canceled\": true\n"
					+ "    }\n" + "]";

			switch (stringType) {
			case "validity":
				jsonString = jsonStringForValidity;
				break;
			case "searchByName":
				jsonString = jsonStringForSearchByName;
				break;
			case "extra":
				jsonString = extraEnteries;
				break;
			default:
			}

			ObjectMapper mapper = new ObjectMapper();
			List<Offer> testList = new ArrayList<Offer>();
			try {
				testList = mapper.readValue(jsonString, new TypeReference<List<Offer>>() {
				});
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return testList;
		}
		
		public void updateOfferToBeExpired() throws ParseException {
			// TODO Auto-generated method stub
			Offer offer = mokOffer();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = dateFormat.parse("10/11/2018");
			
			offer.setCreatedAt(date);
			
			RestTemplate restTemplate = new RestTemplate();
			String url = "http://localhost:"+localServerPort+"/api/offer/1";
			restTemplate.put(url, offer);
			
		}
		
		public void editOfferCreateDate(long offerId, Date date) {
			
			try
		    {
		      // create a java mysql database connection
		      String myDriver = "com.mysql.cj.jdbc.Driver";
		      String myUrl = "jdbc:mysql://localhost:3306/offermanager";
		      Class.forName(myDriver);
		      Connection conn = DriverManager.getConnection(myUrl, "manageruser", "managerpass");
		    
		      // create the java mysql update preparedstatement
		      String query = "update offermanager.offer set created_at = ? where id = ?";
		      PreparedStatement preparedStmt = conn.prepareStatement(query);
		      preparedStmt.setString   (1, "2018-10-10 08:02:05");
		      preparedStmt.setLong(2, offerId);

		      // execute the java preparedstatement
		      preparedStmt.executeUpdate();
		      
		      conn.close();
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		    }
		}
		
}
