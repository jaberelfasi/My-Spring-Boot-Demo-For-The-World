package com.manager.offermanager;

import static org.assertj.core.api.Assertions.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
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

	Utilities util = new Utilities();
	@Value("8088")
	private int localServerPort;

	@Before
	public void prepData() throws ParseException{
		util.prepTestDataInDB("validity");
		util.prepTestDataInDB("searchByName");
		util.prepTestDataInDB("extra");
		util.updateOfferToBeExpired();
	}

	@Test
	public void contextLoads() {
	}

	// Please run the Spring Boot API before you start testing
	@Test
	public void testExpiredOffer() {
	    //before you expect this to pass, you need to run this query
		//update offermanager.offer set created_at = '2018-10-10 08:02:05' where id = 1;
		//this is to go back in time to simulate the passed period.
		int idOfExpriedOffer = 1;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(
				"http://localhost:8088/api/checkoffer/" + idOfExpriedOffer, String.class, localServerPort);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Offer has expired");
	}

	@Test
	public void testValidOffer() {
		int idOfValidOffer = 2;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate
				.getForEntity("http://localhost:8088/api/checkoffer/" + idOfValidOffer, String.class, localServerPort);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Offer Still Valid");
	}

	@Test
	public void testCanceledOffer() {
		int idOfCanceledOffer = 3;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(
				"http://localhost:8088/api/checkoffer/" + idOfCanceledOffer, String.class, localServerPort);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Offer has been canceled");
	}

	@Test
	public void testCreateOffer() {
		Offer offer = util.mokOffer();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Offer> response = restTemplate.postForEntity("http://localhost:8088/api/offer", offer,
				Offer.class);

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
		ResponseEntity<Offer[]> response = restTemplate.getForEntity("http://localhost:8088/api/offer/" + keyWord,
				Offer[].class, localServerPort);

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
		ResponseEntity<Offer[]> response = restTemplate.getForEntity("http://localhost:8088/api/offer", Offer[].class,
				localServerPort);

		List<Offer> ofList = Arrays.asList(response.getBody());

		Offer of = ofList.get(7);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		of.setCanceled(true);
		String url = "http://localhost:8088/api/offer/" + of.getId();
		restTemplate.put(url, of);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		response = restTemplate.getForEntity("http://localhost:8088/api/offer", Offer[].class, localServerPort);

		ofList = Arrays.asList(response.getBody());
		of = ofList.get(7);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(of.getCanceled()).isEqualTo(true);
	}

	

}
