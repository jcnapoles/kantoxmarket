package com.kantox.kantoxmarket.shoppingcart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.kantox.kantoxmarket.shoppingcart.offers.IOffer;
import com.kantox.kantoxmarket.shoppingcart.products.Product;

import lombok.Builder;

@Builder
public class ShoppingCart {

	@Builder.Default
	private List<Product> productList = new ArrayList<Product>();

	@Builder.Default
	private Map<String, IOffer> offerList = new HashMap<String, IOffer>();

	private double totalCartValue;

	public int getProductCount() {
		return productList.size();
	}

	public void addProduct(Product product) {

		Product exists = getProductByCode(product.getProductCode());
		if (null == exists) {
			product.setOfferApplied(false);
			productList.add(product);
		} else {
			exists.setQuantity(exists.getQuantity() + product.getQuantity());
			exists.setPrice(product.getPrice() * exists.getQuantity());
			removeItemFromCart(product);
			exists.setOfferApplied(false);
			productList.add(exists);
		}

		applyDiscounts();
	}

	public double checkout() {
		productList.forEach(product -> totalCartValue = totalCartValue + product.getPrice());

		return totalCartValue;
	}

	public Product getProductByCode(String code) {
		Optional<Product> productOpt = productList.stream()
				.filter(product -> product.getProductCode().equalsIgnoreCase(code)).findFirst();

		return productOpt.orElse(null);
	}

	public void removeItemFromCart(Product product) {
		productList.remove(product);
	}

	public void addOffer(IOffer offerProd, String productCode) {
		this.offerList.put(productCode, offerProd);
	}

	public void applyDiscounts() {
		offerList.forEach((productCode, offer) -> {
			Product product = getProductByCode(productCode);
			if (null != product) {
				if (!product.isOfferApplied()) {
					if (null != offer) {
						removeItemFromCart(product);
						offer.applyOffer(product);
						product.setOfferApplied(true);
						productList.add(product);
					}
				}
			}
		});
	}

}
