package com.kantox.kantoxmarket.shoppingcart.service;

import com.kantox.kantoxmarket.shoppingcart.products.Product;

public interface Inventory {

	boolean isRegistered(String productCode);

	Product add(Product product);

	Product find(String productCode);

	void remove(String productCode);

	Product productRegister(Product product);

}
