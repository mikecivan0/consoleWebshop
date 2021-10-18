package hr.mikec.webstore;

import hr.mikec.webstore.controller.*;
import hr.mikec.webstore.entity.*;
import hr.mikec.webstore.util.BaseException;
import hr.mikec.webstore.util.Tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

	private ProductController productController;
	private ProductCommandController productCommandController;
	private ShoppingCartCommandController shoppingCartCommandController;
	private ShoppingCartController shoppingCartController;
	private List<Product> inventory;
	private List<ShoppingCart> shoppingCart;

	public Main() throws BaseException {
		productController = new ProductController();
		productCommandController = new ProductCommandController();
		shoppingCartController = new ShoppingCartController();
		shoppingCartCommandController = new ShoppingCartCommandController();
		Tools.scanner = new Scanner(System.in);
		descriptionAndRules();
	}

	private void descriptionAndRules() {
		System.out.println();
		System.out.println("---------CONSOLE SHOP APP BY IVAN MIKEC---------");
		Tools.printHeading("Adding products to inventory");
		System.out.print("Add products to inventory by typing ");
		System.out.println("command ADD followed by SKU, name of product, quantity and price");
		System.out.println("Example: ADD 1 T-shirt 1 5.00");
		System.out.println("Name of the product cannot contain spaces");
		System.out.println("Command, SKU, quantity, name of product and price must be in described format and order");
		System.out.println("You can add as much product as you want by repeating described way for adding products");
		System.out.println("When you are done with products adding, type END to move on adding products to cart");
		addProductToInventory();
	}

	private void addProductToInventory() {
		inventory = productController.read();
		showInventory();
		Tools.printHeading("Add products to inventory");
		boolean proceed = true;
		try{
			String commandStr = Tools.parseString("\nAdd new product: ", "Command cannot be empty");
			List<String> command = productCommandController.productCommand(commandStr);
			if(command.get(0).equalsIgnoreCase("ADD")){
				Product product = new Product();
				try{
					product.setSku(Integer.parseInt(command.get(1)));
					product.setQuantity(Integer.parseInt(command.get(3)));
					product.setPrice(new BigDecimal(command.get(4)));
				} catch (Exception e){
					System.out.println("SKU and quantity must be a whole numbers, price must be in numerical format");
					proceed = false;
				}
				if(proceed){
					product.setName(command.get(2));
					productController.setEntity(product);
					productController.create();
				}
				addProductToInventory();
			}else if(command.get(0).equalsIgnoreCase("END")){
				shoppingCartDescriptionAndRules();
			}
		} catch (BaseException e){
			parseException(e.getMessage());
			addProductToInventory();
		}
	}

	private void showInventory() {
		System.out.println("\nInventory status");
		if(inventory.size()!=0){
			for(Product p : inventory){
				System.out.println(p);
			}
		}else{
			System.out.println("Inventory is empty");
		}
	}

	private void parseException(String s){
		if(s.equals("Exists")){
			String product = productController.getEntity().getName();
			Integer quantity =  productController.getEntity().getQuantity();
			System.out.println("Product with same SKU already exists." +
					"\nDo you want increase quantity of " + product + " by " + quantity + "?");
			if(Tools.parseYesNo("Enter yes od no: ", "You can enter only yes or no")){
				try {
					Product p = productController.findBySku();
					p.setQuantity(p.getQuantity()+quantity);
					productController.setEntity(p);
					productController.update();
				} catch (BaseException e) {
					System.out.println(e.getMessage());
				}
			}
		}else{
			System.out.println(s);
		}
	}

	private void shoppingCartDescriptionAndRules() {
		Tools.printHeading("Shopping cart");
		System.out.print("Add products to cart by typing ");
		System.out.println("command ADD followed by SKU and quantity");
		System.out.println("Example: ADD 1 1");
		System.out.print("\nRemove products from cart by typing ");
		System.out.println("command REMOVE followed by SKU and quantity");
		System.out.println("Example: REMOVE 1 1");
		System.out.print("\nShow cart details and empty cart by typing ");
		System.out.println("command CHECKOUT");
		System.out.println("\nFor exit application type END");
		editShoppingCart();
	}

	private void editShoppingCart() {
		shoppingCart = shoppingCartController.read();
		System.out.println("Current in inventory:");
		showInventory();
		System.out.println("\nCurrent in shopping cart:");
		showShoppingCart();
		Tools.printHeading("Edit shopping cart");
		boolean proceed = true;

	}

	private void showShoppingCart() {
		if(shoppingCart.size()!=0){
			System.out.println("\nShopping cart status");
			for(ShoppingCart sc : shoppingCart){
				System.out.println(sc);
			}
		}else{
			System.out.println("Shopping cart is empty");
		}
	}


	public static void main(String[] args){
		try {
			new Main();
		} catch (BaseException e){
			System.out.println(e.getMessage());
		}
	}

}
