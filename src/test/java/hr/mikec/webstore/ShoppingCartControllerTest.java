package hr.mikec.webstore;

import com.github.javafaker.Faker;
import hr.mikec.webstore.controller.ProductController;
import hr.mikec.webstore.controller.ShoppingCartController;
import hr.mikec.webstore.entity.Product;
import hr.mikec.webstore.entity.ShoppingCart;
import hr.mikec.webstore.util.BaseException;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.Assert.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShoppingCartControllerTest {

    private static Product product;
    private static ShoppingCart shoppingCart;
    private static Faker faker;
    private static Random random;
    private static ProductController productController;
    private static ShoppingCartController shoppingCartController;

    @BeforeClass
    public static void prepareData(){
        try {
            productController = new ProductController();
        } catch (BaseException e) {
            System.out.println(e.getMessage());
        }
        try {
            shoppingCartController = new ShoppingCartController();
        } catch (BaseException e) {
            System.out.println(e.getMessage());
        }
        random = new Random();
        product = new Product();
        shoppingCart = new ShoppingCart();
        faker = new Faker();
        int upperbound = 50;
        int int_random = random.nextInt(upperbound)*10;
        int quantity_random = random.nextInt(upperbound)*10;
        int shoppingCartQuantity_random = random.nextInt(upperbound)*10;
        float price_random = random.nextFloat();
        product.setSku(int_random);
        product.setName(faker.funnyName().name());
        product.setQuantity(quantity_random);
        product.setPrice(new BigDecimal(price_random));
        productController.setEntity(product);
        try {
            shoppingCart.setProduct(productController.create());
        } catch (BaseException e) {
            e.getMessage();
        }
        shoppingCart.setQuantity(shoppingCartQuantity_random);
        shoppingCartController.setEntity(shoppingCart);
    }

    @Test
    public void test1_createTest(){
        System.out.println(product);
        try {
            ShoppingCart s = shoppingCartController.create();
            assertFalse("Product is not added to database", s.getId().equals(Long.valueOf(0)));
        } catch (BaseException e) {
            fail(e.getMessage());
        }catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void test2_ReadTest(){
        for(ShoppingCart s: shoppingCartController.read()){
            if(s.getProduct().getName().equals(shoppingCart.getProduct().getName())){
                assertTrue(true);
                return;
            }
        }
        assertFalse("Cannot read shopping cart items from database",true);
    }


    @Test
    public void test3_UpdateTest(){
        int upperbound = 3;
        int newQuantity_random = random.nextInt(upperbound)*10;
        shoppingCartController.getEntity().setQuantity(newQuantity_random);
        try {
            shoppingCartController.update();
            assertTrue("Quantity is altered", true);
        } catch (BaseException e) {
            fail(e.getMessage());
        }catch(Exception e){
            fail(e.getMessage());
        }
    }


    @Test
    public void test4_DeleteTest(){
        try {
            shoppingCartController.delete();
            assertTrue("Product is deleted from shopping cart", true);
        } catch (BaseException e) {
            fail(e.getMessage());
        }catch(Exception e){
            fail(e.getMessage());
        }
    }


}
