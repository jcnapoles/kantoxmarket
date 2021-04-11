package com.kantox.kantoxmarket;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.kantox.kantoxmarket.shoppingcart.ShoppingCart;
import com.kantox.kantoxmarket.shoppingcart.exceptions.ProductNotInInventoryException;
import com.kantox.kantoxmarket.shoppingcart.offers.BuyMoreXItemGetYDiscountFromEachItemPrice;
import com.kantox.kantoxmarket.shoppingcart.offers.BuyMoreXItemGetYPercentDiscountEachItemPrice;
import com.kantox.kantoxmarket.shoppingcart.offers.BuyXItemGetYItemFreeOffer;
import com.kantox.kantoxmarket.shoppingcart.offers.IOffer;
import com.kantox.kantoxmarket.shoppingcart.offers.NoOffer;
import com.kantox.kantoxmarket.shoppingcart.products.Product;
import com.kantox.kantoxmarket.shoppingcart.service.InMemoryInventory;
import com.kantox.kantoxmarket.shoppingcart.service.Inventory;

public class ShoppingCartTest {

	private ShoppingCart cart;	

	@Before
	public void setup() {

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
		cart = ShoppingCart.builder().inventory(inventory).build();
		inventory.add(GR1);
		inventory.add(SR1);
		inventory.add(CF1);
		cart.addOffer(offerGR1, "GR1");
		cart.addOffer(offerSR1, "SR1");
		cart.addOffer(offerCF1, "CF1");
	}

	@Test
	public void testCreateEmptyShoppingCart() {
		ShoppingCart cart = ShoppingCart.builder().build();
		Assert.assertEquals(0, cart.getProductCount());
	}

	@Test
	public void testAddSingleProductToShoppingCart() {
		ShoppingCart cart = ShoppingCart.builder().build();
		Product product = Product.builder().productCode("GR1").productName("Green tea").price(3.11).quantity(1).build();
		cart.addProduct(product);
		Assert.assertEquals(1, cart.getProductCount());
		Assert.assertEquals(3.11, cart.checkout(), 0);
	}

	@Test
	public void addDifferentProductsToShoppingCart() {
		ShoppingCart cart = ShoppingCart.builder().build();
		Product gr1 = Product.builder().productCode("GR1").productName("Green tea").price(3.11).quantity(1).build();
		Product sr1 = Product.builder().productCode("SR1").productName("Strawberries").price(5.00).quantity(1).build();
		Product cf1 = Product.builder().productCode("CF1").productName("Coffee").price(11.23).quantity(1).build();
		cart.addProduct(gr1);
		cart.addProduct(sr1);
		cart.addProduct(cf1);
		Assert.assertEquals(3, cart.getProductCount());
		Assert.assertEquals(19.34, cart.checkout(), 0);
	}

	@Test
	public void testAddMultipleQuantityOfAProductAndApplyOfferToShoppingCart() {
		IOffer offer = BuyXItemGetYItemFreeOffer.builder().XItem(2).YItem(1).build();
		ShoppingCart cart = ShoppingCart.builder().build();
		cart.addOffer(offer, "GR1");
		Product gr1 = Product.builder().productCode("GR1").productName("Green tea").price(15.55).quantity(5).build();
		cart.addProduct(gr1);
		Assert.assertEquals(1, cart.getProductCount());
		Assert.assertEquals(9.33, cart.checkout(), 0);
	}

	@Test
	public void addDifferentProductsAndAppyOfferToShoppingCart() {
		IOffer offer = BuyXItemGetYItemFreeOffer.builder().XItem(2).YItem(1).build();
		ShoppingCart cart = ShoppingCart.builder().build();
		Product gr1 = Product.builder().productCode("GR1").productName("Green tea").price(9.33).quantity(3).build();
		Product sr1 = Product.builder().productCode("SR1").productName("Strawberries").price(10.00).quantity(2).build();
		Product cf1 = Product.builder().productCode("CF1").productName("Coffee").price(11.23).quantity(1).build();
		cart.addOffer(offer, gr1.getProductCode());
		cart.addProduct(gr1);
		cart.addOffer(new NoOffer(), sr1.getProductCode());
		cart.addProduct(sr1);
		cart.addOffer(new NoOffer(), cf1.getProductCode());
		;
		cart.addProduct(cf1);
		Assert.assertEquals(3, cart.getProductCount());
		Assert.assertEquals(27.45, cart.checkout(), 0);
	}

	@Test
	public void testApplyBuyXItemGetYPercentDiscountOnEachItemPrice() {
		IOffer offer = BuyMoreXItemGetYPercentDiscountEachItemPrice.builder().XItem(3).Y(10.0).build();
		ShoppingCart cart = ShoppingCart.builder().build();
		cart.addOffer(offer, "SR1");
		Product sr1 = Product.builder().productCode("SR1").productName("Strawberries").price(15.00).quantity(3).build();
		cart.addProduct(sr1);
		Assert.assertEquals(1, cart.getProductCount());
		Assert.assertEquals(13.50, cart.getProductByCode("SR1").getPrice(), 0.001);
		Assert.assertEquals(13.50, cart.checkout(), 0);
	}

	@Test
	public void testApplyBuyMoreXItemGetYPercentDiscountOnEachProductPrice() {
		IOffer offer = BuyMoreXItemGetYPercentDiscountEachItemPrice.builder().XItem(3).Y(10.0).build();
		;
		ShoppingCart cart = ShoppingCart.builder().build();
		cart.addOffer(offer, "SR1");
		Product sr1 = Product.builder().productCode("SR1").productName("Strawberries").price(20.00).quantity(4).build();
		cart.addProduct(sr1);
		Assert.assertEquals(1, cart.getProductCount());
		Assert.assertEquals(18.00, cart.getProductByCode("SR1").getPrice(), 0.001);
		Assert.assertEquals(18.00, cart.checkout(), 0);
	}

	@Test
	public void testApplyBuyXItemGetYDiscountOnOriginalItemPrice() {
		IOffer offer = BuyMoreXItemGetYDiscountFromEachItemPrice.builder().XItem(3).build();
		ShoppingCart cart = ShoppingCart.builder().build();
		cart.addOffer(offer, "CF1");
		Product cf1 = Product.builder().productCode("CF1").productName("Coffee").price(33.69).quantity(3).build();
		cart.addProduct(cf1);
		Assert.assertEquals(1, cart.getProductCount());
		Assert.assertEquals(22.46, cart.getProductByCode("CF1").getPrice(), 0.001);
		Assert.assertEquals(22.46, cart.checkout(), 0);
	}

	@Test
	public void testApplyBuyMoreXItemGetTwoThirdsDiscountOnOriginalPrice() {
		IOffer offer = BuyMoreXItemGetYDiscountFromEachItemPrice.builder().XItem(3).build();
		ShoppingCart cart = ShoppingCart.builder().build();
		cart.addOffer(offer, "CF1");
		Product cf1 = Product.builder().productCode("CF1").productName("Coffee").price(56.15).quantity(5).build();
		cart.addProduct(cf1);
		Assert.assertEquals(1, cart.getProductCount());
		Assert.assertEquals(37.43, cart.getProductByCode("CF1").getPrice(), 0.001);
		Assert.assertEquals(37.43, cart.checkout(), 0);
	}

	@Test
	public void addDiferentProductsAndApplyEachDiferentOffers() {
		IOffer offerGR1 = BuyXItemGetYItemFreeOffer.builder().XItem(2).YItem(1).build();
		IOffer offerSR1 = BuyMoreXItemGetYPercentDiscountEachItemPrice.builder().XItem(3).Y(10.0).build();
		IOffer offerCF1 = BuyMoreXItemGetYDiscountFromEachItemPrice.builder().XItem(3).build();
		ShoppingCart cart = ShoppingCart.builder().build();
		Product gr1 = Product.builder().productCode("GR1").productName("Green tea").price(15.55).quantity(5).build();
		Product sr1 = Product.builder().productCode("SR1").productName("Strawberries").price(20.00).quantity(4).build();
		Product cf1 = Product.builder().productCode("CF1").productName("Coffee").price(56.15).quantity(5).build();
		cart.addOffer(offerGR1, gr1.getProductCode());
		cart.addProduct(gr1);
		cart.addOffer(offerSR1, sr1.getProductCode());
		cart.addProduct(sr1);
		cart.addOffer(offerCF1, cf1.getProductCode());
		cart.addProduct(cf1);
		Assert.assertEquals(3, cart.getProductCount());
		Assert.assertEquals(64.76, cart.checkout(), 0);
	}

	@Test
	public void addDiferentProductsInAnyOrderAndApplyOneOffer() {
		ShoppingCart cart = ShoppingCart.builder().build();
		IOffer offer = BuyXItemGetYItemFreeOffer.builder().XItem(2).YItem(1).build();
		Product gr1 = Product.builder().productCode("GR1").productName("Green tea").price(3.11).quantity(1).build();
		Product sr1 = Product.builder().productCode("SR1").productName("Strawberries").price(5.0).quantity(1).build();
		Product gr2 = Product.builder().productCode("GR1").productName("Green tea").price(3.11).quantity(1).build();
		Product gr3 = Product.builder().productCode("GR1").productName("Green tea").price(3.11).quantity(1).build();
		Product cf1 = Product.builder().productCode("CF1").productName("Coffee").price(11.23).quantity(1).build();
		cart.addOffer(offer, gr1.getProductCode());
		cart.addProduct(gr1);
		cart.addProduct(sr1);
		cart.addProduct(gr2);
		cart.addProduct(gr3);
		cart.addProduct(cf1);
		Assert.assertEquals(3, cart.getProductCount());
		Assert.assertEquals(22.45, cart.checkout(), 0);
	}

	@Test
	public void addDiferentProductsInAnyOrderAndApplyDiferentOffers() {
		ShoppingCart cart = ShoppingCart.builder().build();
		IOffer offerGR1 = BuyXItemGetYItemFreeOffer.builder().XItem(2).YItem(1).build();
		IOffer offerSR1 = BuyMoreXItemGetYPercentDiscountEachItemPrice.builder().XItem(3).Y(10.0).build();
		IOffer offerCF1 = BuyMoreXItemGetYDiscountFromEachItemPrice.builder().XItem(3).build();
		cart.addOffer(offerGR1, "GR1");
		cart.addOffer(offerSR1, "SR1");
		cart.addOffer(offerCF1, "CF1");

		Product gr1 = Product.builder().productCode("GR1").productName("Green tea").price(3.11).quantity(1).build();
		Product gr2 = Product.builder().productCode("GR1").productName("Green tea").price(3.11).quantity(1).build();
		Product gr3 = Product.builder().productCode("GR1").productName("Green tea").price(3.11).quantity(1).build();
		Product sr1 = Product.builder().productCode("SR1").productName("Strawberries").price(5.0).quantity(1).build();
		Product sr2 = Product.builder().productCode("SR1").productName("Strawberries").price(5.0).quantity(1).build();
		Product sr3 = Product.builder().productCode("SR1").productName("Strawberries").price(5.0).quantity(1).build();
		Product cf1 = Product.builder().productCode("CF1").productName("Coffee").price(11.23).quantity(1).build();
		Product cf2 = Product.builder().productCode("CF1").productName("Coffee").price(11.23).quantity(1).build();
		Product cf3 = Product.builder().productCode("CF1").productName("Coffee").price(11.23).quantity(1).build();

		cart.addProduct(gr1);
		cart.addProduct(sr1);
		cart.addProduct(sr2);
		cart.addProduct(cf1);
		cart.addProduct(gr2);
		cart.addProduct(cf2);
		cart.addProduct(sr3);
		cart.addProduct(cf3);
		cart.addProduct(gr3);

		Assert.assertEquals(3, cart.getProductCount());
		Assert.assertEquals(42.18, cart.checkout(), 0);
	}

	@Test
	public void testDataCase1() {
		cart.addProductByCode("GR1");
		Assert.assertEquals(3.11, cart.checkout(), 0);
		cart.addProductByCode("SR1");
		Assert.assertEquals(8.11, cart.checkout(), 0);
		cart.addProductByCodeAndQuantity("GR1", 2);
		Assert.assertEquals(11.22, cart.checkout(), 0);
		cart.addProductByCode("CF1");

		Assert.assertEquals(3, cart.getProductCount());
		Assert.assertEquals(22.45, cart.checkout(), 0);
	}

	@Test
	public void testDataCase2() {
		cart.addProductByCode("GR1");
		cart.addProductByCode("GR1");
		
		Assert.assertEquals(1, cart.getProductCount());
		Assert.assertEquals(3.11, cart.checkout(), 0);
	}

	@Test
	public void testDataCase3() throws Exception {
		cart.addProductByCodeAndQuantity("SR1", 2);
		Product product = Product.builder().productCode("GR1").build();
		cart.addProduct(product);		
		cart.addProductByCode("SR1");
		
		Assert.assertEquals(2, cart.getProductCount());
		Assert.assertEquals(16.61, cart.checkout(), 0);
	}

	@Test
	public void testDataCase4() {
		String[] basket = {"GR1","CF1","SR1","CF1","CF1"};
		cart.addProductsByArrayCode(basket);
		
		Assert.assertEquals(3, cart.getProductCount());
		Assert.assertEquals(30.57, cart.checkout(), 0);
	}

	@Test(expected = ProductNotInInventoryException.class)
	public void addingANonExistentProductByCode(){			
			cart.addProductByCode("Prueba");
	}
	
	@Test
	public void initConsole() {
		
		
	}
}
