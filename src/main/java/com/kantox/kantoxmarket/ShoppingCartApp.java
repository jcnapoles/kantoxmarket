package com.kantox.kantoxmarket;

import java.util.Scanner;

import com.kantox.kantoxmarket.shoppingcart.ShoppingCart;
import com.kantox.kantoxmarket.shoppingcart.offers.BuyMoreXItemGetYDiscountFromEachItemPrice;
import com.kantox.kantoxmarket.shoppingcart.offers.BuyMoreXItemGetYPercentDiscountEachItemPrice;
import com.kantox.kantoxmarket.shoppingcart.offers.BuyXItemGetYItemFreeOffer;
import com.kantox.kantoxmarket.shoppingcart.offers.IOffer;
import com.kantox.kantoxmarket.shoppingcart.products.Product;
import com.kantox.kantoxmarket.shoppingcart.service.InMemoryInventory;
import com.kantox.kantoxmarket.shoppingcart.service.Inventory;

public class ShoppingCartApp {
	
	private static ShoppingCart basket;
	
	public static void main(String[] args) {		
		initInventory();
		printWelcome();		
		Scanner keyboard = new Scanner(System.in);	
		System.out.println("Do you want to register a new product?: (Y/N)");		
		String answerRegister = keyboard.nextLine(); 
		while(answerRegister.equalsIgnoreCase("Y")) {
			registerNewProduct();
			System.out.println("Do you want to register a new product?: (Y/N)");
			answerRegister = keyboard.nextLine(); 
		}
		System.out.println("You can start scanning products");
		String productCode = keyboard.nextLine();	
		double total = checkout(productCode);		
		System.out.println("*******The total price of the basket is: " + "£" + total + "  *****");
		System.out.println("Do you want to scan another product?: (Y/N)");
		String answerNewProduct = keyboard.nextLine();
		while(answerNewProduct.equalsIgnoreCase("Y")) {
			productCode = keyboard.nextLine();			
			total = checkout(productCode);			
			System.out.println("*******The total price of the basket is: " + "£" + total + "  *****");
			System.out.println("Do you want to scan another product?: (Y/N)");
			answerNewProduct = keyboard.nextLine();
		}
		System.out.println("*******The total price of the basket is: " + "£" + total + "  *****");
		keyboard.close();
		System.exit(0);
	}

	private static double checkout(String productCode) {		
		basket.addProductByCode(productCode);		
		return basket.checkout();
		
	}

	private static void registerNewProduct() {
		/*To implement*/
		System.out.println("Function not implemented");
	}

	private static void printWelcome() {
		System.out.println("Welcome to KANTOXMARKET");
		
	}

	private static void initInventory() {
		IOffer offerGR1 = BuyXItemGetYItemFreeOffer.builder().XItem(2).YItem(1).build();
		IOffer offerSR1 = BuyMoreXItemGetYPercentDiscountEachItemPrice.builder().XItem(3).Y(10.0).build();
		IOffer offerCF1 = BuyMoreXItemGetYDiscountFromEachItemPrice.builder().XItem(3).build();
		Inventory inventory = InMemoryInventory.builder().build();
		Product GR1 = Product.builder().productCode("GR1").productName("Green tea").price(3.11).quantity(1)
				.offerApplied(null).build();
		Product SR1 = Product.builder().productCode("SR1").productName("Strawberries").price(5.00).quantity(1)
				.offerApplied(null).build();
		Product CF1 = Product.builder().productCode("CF1").productName("Coffee").price(11.23).quantity(1)
				.offerApplied(null).build();		
		basket = ShoppingCart.builder().inventory(inventory).build();
		inventory.add(GR1);
		inventory.add(SR1);
		inventory.add(CF1);
		basket.addOffer(offerGR1, "GR1");
		basket.addOffer(offerSR1, "SR1");
		basket.addOffer(offerCF1, "CF1");
		
	}

	
}
