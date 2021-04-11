package com.kantox.kantoxmarket.shoppingcart.offers;

import com.kantox.kantoxmarket.shoppingcart.products.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BuyMoreXItemGetYDiscountFromEachItemPrice implements IOffer {

	private int XItem;

	@Override
	public void applyOffer(Product product) {
		int totalQuantity = product.getQuantity();
		if (totalQuantity >= XItem) {
			double price = product.getPrice();
			double priceAfterDiscount = Math.round(((price / 3 * 2) * 100.0)) / 100.0;
			product.setPrice(priceAfterDiscount);
		}

	}

}
