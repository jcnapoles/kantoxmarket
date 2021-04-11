package com.kantox.kantoxmarket.shoppingcart.offers;

import com.kantox.kantoxmarket.shoppingcart.products.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class BuyXItemGetYItemFreeOffer implements IOffer {

	private int XItem;

	private int YItem;

	@Override
	public void applyOffer(Product product) {
		if (product.getQuantity() >= XItem) {
			int ineligibleCount = product.getQuantity() % XItem;
			int freeProductQty = (product.getQuantity() - ineligibleCount) / XItem;
			double unitPrice = product.getPrice() / product.getQuantity();
			double discount = unitPrice * freeProductQty;
			product.setPrice(Math.round((product.getPrice() - discount) * 100.0) / 100.0);
		}

	}

}
