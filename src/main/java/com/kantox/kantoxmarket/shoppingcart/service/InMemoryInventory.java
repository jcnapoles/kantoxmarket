package com.kantox.kantoxmarket.shoppingcart.service;

import java.util.HashMap;
import java.util.Map;

import com.kantox.kantoxmarket.shoppingcart.exceptions.ProductNotInInventoryException;
import com.kantox.kantoxmarket.shoppingcart.products.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InMemoryInventory implements Inventory {

	@Builder.Default
	private Map<String, Product> inventory = new HashMap<String, Product>();

	@Override
	public boolean isRegistered(String productCode) {
		return inventory.containsKey(productCode);
	}

	@Override
	public Product add(Product product) {
		Product result = null;
		if (!isRegistered(product.getProductCode())) {
			inventory.put(product.getProductCode(), product);
			result = inventory.get(product.getProductCode());
		} else {
			result = inventory.replace(product.getProductCode(), product);
		}
		return result;
	}

	@Override
	public Product find(String productCode) {
		if (!isRegistered(productCode)) {
			throw new ProductNotInInventoryException(productCode);
		}
		return inventory.get(productCode);
	}

	@Override
	public void remove(String productCode) {
		if (isRegistered(productCode)) {
			inventory.remove(productCode);
		}

	}

	@Override
	public Product productRegister(Product product) {

		if (0 == product.getQuantity() && 0 == product.getPrice()) {

		} else {
			if (product.getQuantity() > 1) {
				double price = product.getPrice();
				int quantity = product.getQuantity();
				Product newProduct = Product.builder().build().product(product);
				newProduct.setQuantity(1);
				newProduct.setPrice(price / quantity);
				inventory.put(newProduct.getProductCode(), newProduct);

			} else {
				inventory.put(product.getProductCode(), product);

			}

		}
		return product;
	}

}
