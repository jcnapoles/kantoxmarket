package com.kantox.kantoxmarket.shoppingcart.offers;

import com.kantox.kantoxmarket.shoppingcart.products.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class NoOffer implements IOffer {

	@Override
	public void applyOffer(Product product) {
		// make business strategies

	}

}
