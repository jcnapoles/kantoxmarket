package com.kantox.kantoxmarket.shoppingcart.products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {

	private String productCode;

	private String productName;

	private int quantity;

	private double price;

	private Boolean offerApplied;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (productCode == null) {
			if (other.productCode != null)
				return false;
		} else if (!productCode.equals(other.productCode))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productCode == null) ? 0 : productCode.hashCode());
		return result;
	}

	public Product product(Product product) {
		return Product.builder().productCode(product.getProductCode()).productName(product.getProductName())
				.quantity(product.getQuantity()).price(product.getPrice()).offerApplied(product.getOfferApplied())
				.build();
	}

}
