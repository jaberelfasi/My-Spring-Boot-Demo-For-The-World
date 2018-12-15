package com.manager.offermanager.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.offermanager.exception.ResourceNotFoundException;
import com.manager.offermanager.model.Offer;
import com.manager.offermanager.repository.OfferRepository;

@RestController
@RequestMapping("/api")
public class OfferController {

	@Autowired
	OfferRepository offerRepository;

	// create offer
	@PostMapping("/offer")
	public Offer createOffer(@Valid @RequestBody Offer offer) {
		return offerRepository.save(offer);
	}

	// cancel offer
	@PutMapping("/offer/{id}")
	public Offer updateOffer(@PathVariable(value = "id") Long offerId, @Valid @RequestBody Offer offerDetails) {

		Offer offer = offerRepository.findById(offerId)
				.orElseThrow(() -> new ResourceNotFoundException("Offer", "id", offerId));

		offer.setCanceled(offerDetails.getCanceled());

		Offer updatedOffer = offerRepository.save(offer);
		return updatedOffer;
	}

	// query offers: get all offers
	@GetMapping("/offer")
	public List<Offer> getAllOffers() {
		return offerRepository.findAll();
	}

	// query offers: get offers with a specific name
	@GetMapping("/offer/{offername}")
	public List<Offer> getOfferByOffername(@PathVariable(value = "offername") String offername) {
		List<Offer> offerlist = offerRepository.findAll();
		List<Offer> foundOffers = new ArrayList<Offer>();
		for (int i = 0; i < offerlist.size(); i++) {
			if (offerlist.get(i).getOffername().equals(offername))
				foundOffers.add(offerlist.get(i));
		}
		return foundOffers;
	}

	// query offers: check if an offer is valid and has not been canceled
	@GetMapping("/checkoffer/{id}")
	public String getNoteById(@PathVariable(value = "id") Long offerId) {
		Offer offer = offerRepository.findById(offerId)
				.orElseThrow(() -> new ResourceNotFoundException("Offer", "id", offerId));
		String offerStatus = "Not Specified Yet";
		if (!offer.getCanceled()) {

			float offerperiod = offer.getOfferperiod();
			String perdiodUnit = offer.getPeriodunit();
			Date initialOfferDate = offer.getCreatedAt();
			Date now = new Date();

			Calendar cal1 = new GregorianCalendar();
			Calendar cal2 = new GregorianCalendar();
			cal1.setTime(initialOfferDate);
			cal2.setTime(now);
			float offerAge = 24 * daysBetween(cal1.getTime(), cal2.getTime());

			if (perdiodUnit.equals("Month"))
				offerperiod = offerperiod * 30 * 24;

			if (perdiodUnit.equals("Day"))
				offerperiod = offerperiod * 24;
			System.out.println("the diffirence between " + initialOfferDate.toString() + " and " + now.toString()
					+ " is " + offerAge);
			System.out
					.println("the age of the offer is: " + offerAge + " Hours and the offer period is " + offerperiod);

			if ((offerAge - offerperiod) >= 0)
				offerStatus = "Offer has expired";
			else
				offerStatus = "Offer Still Valid ("+(offerperiod-offerAge)+") hours left";

		} else {
			offerStatus = "Offer has been canceled";
		}

		return offerStatus;
	}

	public int daysBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}

}
