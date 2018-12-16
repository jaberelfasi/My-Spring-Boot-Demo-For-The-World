package com.manager.offermanager;

import static org.assertj.core.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.manager.offermanager.model.Offer;
import com.manager.offermanager.util.Utilities;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfferManagerApplicationTests {

	// @Value("${server.port}")
	private int localServerPort = 8088;//Please make sure this value corresponds to the provided port in the application.properties file
									   //should've got this value from the property file application.properties, 
									   //but currently this functionality is returning a null value.

	Utilities util = new Utilities(localServerPort);

	@Before
	public void prepData() throws ParseException {
		util.prepTestDataInDB("validity");
		util.prepTestDataInDB("searchByName");
		util.prepTestDataInDB("extra");
		util.updateOfferToBeExpired();
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
		Date date = dateFormat.parse("Sat Nov 10 00:00:00 GMT 2018");
		util.editOfferCreateDate(1, date);

	}

	@Test
	public void contextLoads() {
	}

	// Please run the Spring Boot API before you start testing
	@Test
	public void testExpiredOffer() {
		// before you expect this to pass, this query needs to be run, this has been
		// done by the code in Util now.
		// update offermanager.offer set created_at = '2018-10-10 08:02:05' where id =
		// 1;
		// this is to go back in time to simulate the passed period.
		int idOfExpriedOffer = 1;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(
				"http://localhost:" + localServerPort + "/api/checkoffer/" + idOfExpriedOffer, String.class,
				localServerPort);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Offer has expired");
	}

	@Test
	public void testValidOffer() {
		int idOfValidOffer = 2;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(
				"http://localhost:" + localServerPort + "/api/checkoffer/" + idOfValidOffer, String.class,
				localServerPort);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Offer Still Valid");
	}

	@Test
	public void testCanceledOffer() {
		int idOfCanceledOffer = 3;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(
				"http://localhost:" + localServerPort + "/api/checkoffer/" + idOfCanceledOffer, String.class,
				localServerPort);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Offer has been canceled");
	}

	@Test
	public void testCreateOffer() {
		Offer offer = util.mokOffer();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Offer> response = restTemplate
				.postForEntity("http://localhost:" + localServerPort + "/api/offer", offer, Offer.class);

		Offer of = response.getBody();
		offer.setId(of.getId());
		offer.setCreatedAt(of.getCreatedAt());
		offer.setUpdatedAt(of.getUpdatedAt());

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(of).isEqualToComparingFieldByField(offer);
	}

	@Test
	public void testGetOffersByName() {

		List<Offer> testList = util.fillTestList("searchByName");
		String keyWord = "offerlist for test";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Offer[]> response = restTemplate.getForEntity(
				"http://localhost:" + localServerPort + "/api/offer/" + keyWord, Offer[].class, localServerPort);

		List<Offer> lo = Arrays.asList(response.getBody());

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		for (int i = 0; i < testList.size(); i++) {
			assertThat(lo.get(i)).isEqualToComparingFieldByField(testList.get(i));
		}

	}

	// test the offer cancellation capabilities
	@Test
	public void testCancelOffer() {

		int idOfOfferToBeCanceled = 1;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Offer> response = restTemplate.getForEntity(
				"http://localhost:" + localServerPort + "/api/singleoffer/" + idOfOfferToBeCanceled, Offer.class,
				localServerPort);

		Offer of = response.getBody();

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		of.setCanceled(true);
		String url = "http://localhost:" + localServerPort + "/api/offer/" + of.getId();
		restTemplate.put(url, of);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		response = restTemplate.getForEntity(
				"http://localhost:" + localServerPort + "/api/singleoffer/" + idOfOfferToBeCanceled, Offer.class,
				localServerPort);

		of = response.getBody();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(of.getCanceled()).isEqualTo(true);
	}

}
