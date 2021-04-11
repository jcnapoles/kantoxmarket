package com.kantox.kantoxmarket.shoppingcart.exceptions;

public class ProductNotInInventoryException extends RuntimeException {

	private static final long serialVersionUID = 1952491292203877872L;

	private static final String SORRY_NOT_REGISTERED = "Sorry, '%s' not registered!!";

	public ProductNotInInventoryException(String productCode) {
		super(String.format(SORRY_NOT_REGISTERED, productCode));
	}
}
