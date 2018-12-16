package com.manager.offermanager.model;

import java.util.ArrayList;
import java.util.List;

public class OfferList {
private List<Offer> list;

public List<Offer> getList() {
	// TODO Auto-generated method stub
	return this.list;
}

public void setList(Offer offer) {
	this.list.add(offer);
}
}
