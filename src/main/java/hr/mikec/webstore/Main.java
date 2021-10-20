package hr.mikec.webstore;

import hr.mikec.webstore.controller.*;
import hr.mikec.webstore.entity.*;
import hr.mikec.webstore.util.BaseException;
import hr.mikec.webstore.util.Tools;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

	private ProductController productController;
	private ProductCommandController productCommandController;
	private ShoppingCartCommandController shoppingCartCommandController;
	private ShoppingCartController shoppingCartController;
	private List<Product> inventoryList;
	private List<ShoppingCart> shoppingCartList;
	private ShoppingCart shoppingCart;
	private Product product;
	private int sku = 0, quantity = 0;


	public Main() throws BaseException {
		productController = new ProductController();
		productCommandController = new ProductCommandController();
		shoppingCartController = new ShoppingCartController();
		shoppingCartCommandController = new ShoppingCartCommandController();
		shoppingCart = new ShoppingCart();
		product = new Product();
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
		inventoryList = productController.read();
		showInventory();
		Tools.printHeading("Add products to inventory");
		boolean proceed = true;
		try{
			String commandStr = Tools.parseString("\nAdd new product: ", "Command cannot be empty");
			List<String> command = productCommandController.productCommand(commandStr);
			processProductCommand(command);
		} catch (BaseException e){
			System.out.println(e.getMessage());
		}
	}

	private void processProductCommand(List<String> command){
		switch(command.get(0).toUpperCase()){
			case "ADD" -> productAdd(command);
			case "END" -> productEnd();
		}
	}

	private void productAdd(List<String> command){
		boolean proceed = true;
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
			try {
				productController.create();
			} catch (BaseException e) {
				parseProductException(e.getMessage());
			}
		}
		addProductToInventory();
	}

	private void productEnd(){
		shoppingCartDescriptionAndRules();
	}

	private void showInventory() {
		System.out.println("\nInventory status");
		if(inventoryList.size()!=0){
			for(Product p : inventoryList){
				System.out.println(p);
			}
		}else{
			System.out.println("Inventory is empty");
		}
	}

	private void parseProductException(String s){
		if(s.equals("Exists")){
			String product = productController.getEntity().getName();
			Integer quantity =  productController.getEntity().getQuantity();
			System.out.println("Product with same SKU already exists." +
					"\nDo you want increase quantity of " + product + " by " + quantity + "?");
			if(Tools.parseYesNo("Enter yes od no: ", "You can enter only yes or no")){
				try {
					Product p = productController.findBySku(productController.getEntity().getSku());
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
		System.out.print("Remove products from cart by typing ");
		System.out.println("command REMOVE followed by SKU and quantity");
		System.out.println("Example: REMOVE 1 1");
		System.out.print("Show cart details and empty cart by typing ");
		System.out.println("command CHECKOUT");
		System.out.println("For exit application type END");
		editShoppingCart();
	}

	private void editShoppingCart() {
		shoppingCartList = shoppingCartController.read();
		showInventory();
		showShoppingCart();
		try{
			String commandStr = Tools.parseString("\nEdit shopping cart: ", "Command cannot be empty");
			List<String> command = shoppingCartCommandController.shoppingCartCommand(commandStr);
			processShoppingCartCommand(command);
		} catch (BaseException e){
			parseShoppingCartException(e.getMessage());
			editShoppingCart();
		}
	}

	private void processShoppingCartCommand(List<String> command){
		switch(command.get(0).toUpperCase()){
			case "ADD" -> shoppingCartAdd(command);
			case "REMOVE" -> shoppingCartRemove(command);
			case "CHECKOUT" -> shoppingCartCheckout();
			case "END" -> shoppingCartEnd();
		}
	}

	private void parseShoppingCartException(String s){
		if(s.equals("Exists")){
			String product = shoppingCartController.getEntity().getProduct().getName();
			Integer quantity =  shoppingCartController.getEntity().getQuantity();
			System.out.println("Product with same SKU already exists in your shopping cart." +
					"\nDo you want increase quantity of " + product + " by " + quantity + "?");
			if(Tools.parseYesNo("Enter yes od no: ", "You can enter only yes or no")){
				try {
					ShoppingCart sc = shoppingCartController.findBySku(shoppingCartController.getEntity().getProduct().getSku());
					sc.setQuantity(sc.getQuantity()+quantity);
					shoppingCartController.setEntity(sc);
					shoppingCartController.update();
					updateProductReduce();
				} catch (BaseException e) {
					System.out.println(e.getMessage());
				}
			}
		}else{
			System.out.println(s);
		}
	}

	private void shoppingCartAdd(List<String> command){
		boolean proceed = parseShoppingCartAddCommand(command);
		if(proceed){
			shoppingCartController.setEntity(shoppingCart);
			try {
				shoppingCartController.create();
				updateProductReduce();
			}catch (BaseException e){
				System.out.println(e.getMessage());
				updateCart();
			}
		}
		editShoppingCart();
	}

	private void updateCart() {
		if(Tools.parseYesNo("Increase quantity of product " + product.getName() + " in cart by " + quantity + "?: " ,
				"Allowed answers are YES and NO")){
			ShoppingCart sc = new ShoppingCart();
			try {
				sc = shoppingCartController.findByProduct(product);
			} catch (BaseException ex) {
				System.out.println(ex.getMessage());
			}
			sc.setQuantity(sc.getQuantity()+quantity);
			shoppingCartController.setEntity(sc);
			try {
				shoppingCartController.update();
				updateProductReduce();
			} catch (BaseException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	private void updateProductReduce() {
		product.setQuantity(product.getQuantity()-quantity);
		productController.setEntity(product);
		try {
			productController.update();
		} catch (BaseException e) {
			System.out.println(e.getMessage());
		}
	}

	private void updateProductAdd() {
		product.setQuantity(product.getQuantity()+quantity);
		productController.setEntity(product);
		try {
			productController.update();
		} catch (BaseException e) {
			System.out.println(e.getMessage());
		}
	}

	private void shoppingCartRemove(List<String> command) {
		boolean proceed = parseRemoveCommand(command);
		if(proceed){
			shoppingCartController.setEntity(shoppingCart);
			if(shoppingCart.getQuantity()-quantity==0){
				removeShoppingCartItem();
			}else{
				updateShoppingCartReduce();
			}
			updateProductAdd();
		}
		editShoppingCart();
	}

	private void updateShoppingCartReduce() {
		ShoppingCart sc = new ShoppingCart();
		sc = shoppingCartController.getEntity();
		sc.setQuantity(sc.getQuantity()-quantity);
		shoppingCartController.setEntity(sc);
		try {
			shoppingCartController.update();
		} catch (BaseException e) {
			System.out.println(e.getMessage());
		}
	}

	private void removeShoppingCartItem() {
		try {
			shoppingCartController.delete();
		}catch (BaseException e){
			System.out.println(e.getMessage());
			updateProductAdd();
		}
	}

	private void shoppingCartCheckout() {
	}
	private void shoppingCartEnd() {
	}

	private void showShoppingCart() {
		if(shoppingCartList.size()!=0){
			System.out.println("\nShopping cart status");
			for(ShoppingCart sc : shoppingCartList){
				System.out.println(sc);
			}
		}else{
			System.out.println("\nShopping cart is empty");
		}
	}

	private boolean parseShoppingCartAddCommand(List<String> command ){
		boolean proceed = true;
		try {
			sku = Integer.parseInt(command.get(1));
			quantity = Integer.parseInt(command.get(2));
		} catch (Exception e){
			System.out.println("SKU and quantity must be a whole numbers");
			proceed = false;
		}
		if(proceed){
			try {
				product = productController.findBySku(sku);
			} catch (Exception e) {
				System.out.println("There is no product related with that SKU");
				proceed = false;
			}
		}
		if(proceed){
			if(product.getQuantity()<quantity){
				System.out.println("There is no enough items on stock");
				proceed = false;
			}
		}
		if(proceed){
			shoppingCart = new ShoppingCart(product, quantity);
		}
		return proceed;
	}

    private boolean parseRemoveCommand(List<String> command ){
        boolean proceed = true;
        try {
            sku = Integer.parseInt(command.get(1));
            quantity = Integer.parseInt(command.get(2));
        } catch (Exception e){
            System.out.println("SKU and quantity must be a whole numbers");
            proceed = false;
        }
        if(proceed){
			shoppingCart = shoppingCartController.findBySku(sku);
			if(shoppingCart.getId()==null) {
				System.out.println("There is no product in your shopping cart related with that SKU");
				proceed = false;
			}
        }
        if(proceed){
            if(shoppingCart.getQuantity()<quantity){
                System.out.println("You trying to remove more products than you have in shopping cart");
                proceed = false;
            }
        }
        return proceed;
    }


	public static void main(String[] args){
		try {
			new Main();
		} catch (BaseException e){
			System.out.println(e.getMessage());
		}
	}

}
