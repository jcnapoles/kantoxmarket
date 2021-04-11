package com.kantox.kantoxmarket.shoppingcart.offers;

import com.kantox.kantoxmarket.shoppingcart.products.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BuyMoreXItemGetYPercentDiscountEachProduct implements IOffer {

	private double Y;

	private int XItem;

	@Override
	public void applyOffer(Product product) {
		int totalQuantity = product.getQuantity();
		double unitPrice = product.getPrice() / product.getQuantity();
		while (totalQuantity >= XItem) {
			double price = product.getPrice();
			product.setPrice(price - totalQuantity * (unitPrice / (100 / Y)));
			totalQuantity = totalQuantity - 2;
		}

	}

}
