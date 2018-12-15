package com.manager.offermanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manager.offermanager.model.Offer;


@Repository
public interface OfferRepository extends JpaRepository<Offer, Long>{

}
