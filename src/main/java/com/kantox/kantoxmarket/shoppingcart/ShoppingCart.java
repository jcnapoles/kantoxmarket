package com.kantox.kantoxmarket.shoppingcart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.kantox.kantoxmarket.shoppingcart.exceptions.ProductNotInInventoryException;
import com.kantox.kantoxmarket.shoppingcart.offers.IOffer;
import com.kantox.kantoxmarket.shoppingcart.products.Product;
import com.kantox.kantoxmarket.shoppingcart.service.InMemoryInventory;
import com.kantox.kantoxmarket.shoppingcart.service.Inventory;

import lombok.Builder;

@Builder
public class ShoppingCart {

	@Builder.Default
	private List<Product> productList = new ArrayList<Product>();

	@Builder.Default
	private Map<String, IOffer> offerList = new HashMap<String, IOffer>();

	@Builder.Default
	private Inventory inventory = new InMemoryInventory();

	private double totalCartValue;

	public int getProductCount() {
		return productList.size();
	}

	public void addProduct(Product product) {

		if (null != product.getProductCode()) {

			if (!inventory.isRegistered(product.getProductCode())) {
				product = inventory.productRegister(product);
			}
			if (0 == product.getQuantity() && 0 == product.getPrice()) {
				product = inventory.find(product.getProductCode());
			}
			Product exists = getProductByCode(product.getProductCode());
			if (null == exists) {
				exists = Product.builder().build().product(product);
				exists.setOfferApplied(false);
				productList.add(exists);
			} else {
				exists.setQuantity(exists.getQuantity() + product.getQuantity());
				exists.setPrice(product.getPrice() * exists.getQuantity());
				removeProductFromCart(product);
				exists.setOfferApplied(false);
				productList.add(exists);
			}

			applyDiscounts();

		} else {
			throw new ProductNotInInventoryException(null);
		}

	}

	public double checkout() {
		totalCartValue = 0;
		productList.forEach(product -> totalCartValue = totalCartValue + product.getPrice());

		return Math.round(totalCartValue * 100.0) / 100.0;
	}

	public Product getProductByCode(String code) {
		Optional<Product> productOpt = productList.stream()
				.filter(product -> product.getProductCode().equalsIgnoreCase(code)).findFirst();

		return productOpt.orElse(null);
	}

	public void removeProductFromCart(Product product) {
		productList.remove(product);
	}

	public void addOffer(IOffer offerProd, String productCode) {
		this.offerList.put(productCode, offerProd);
	}

	public void applyDiscounts() {
		offerList.forEach((productCode, offer) -> {
			Product product = getProductByCode(productCode);
			if (null != product) {
				if (!product.getOfferApplied()) {
					if (null != offer) {
						removeProductFromCart(product);
						offer.applyOffer(product);
						product.setOfferApplied(true);
						productList.add(product);
					}
				}
			}
		});
	}

	public void addProductByCode(String productCode) throws ProductNotInInventoryException {

		if (inventory.isRegistered(productCode)) {
			Product product = inventory.find(productCode);
			if (null != product) {
				Product exists = getProductByCode(productCode);
				if (null == exists) {
					exists = Product.builder().build().product(product);
					exists.setOfferApplied(false);
					productList.add(exists);
				} else {
					exists.setQuantity(exists.getQuantity() + product.getQuantity());
					exists.setPrice(product.getPrice() * exists.getQuantity());
					removeProductFromCart(product);
					exists.setOfferApplied(false);
					productList.add(exists);
				}
				applyDiscounts();
			} else {

			}
		} else {
			throw new ProductNotInInventoryException(productCode);
		}

	}

	public void addProductByCodeAndQuantity(String productCode, int quantity) {
		while (quantity > 0) {
			addProductByCode(productCode);
			quantity--;
		}

	}

	public void addProductsByArrayCode(String[] basket) {
		Arrays.asList(basket).forEach(productCode ->{
			addProductByCode(productCode);
		});

	}

}
