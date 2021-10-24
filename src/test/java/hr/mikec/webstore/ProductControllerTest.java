package hr.mikec.webstore;

import com.github.javafaker.Faker;
import java.util.Random;
import java.math.BigDecimal;
import static org.junit.Assert.*;
import hr.mikec.webstore.controller.ProductController;
import hr.mikec.webstore.entity.Product;
import hr.mikec.webstore.util.BaseException;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductControllerTest {

    private static Product product;
    private static Faker faker;
    private static Random random;
    private static ProductController productController;

    @BeforeClass
    public static void prepareData(){
        try {
            productController = new ProductController();
        } catch (BaseException e) {
            System.out.println(e.getMessage());
        }
        random = new Random();
        product = new Product();
        faker = new Faker();
        int upperbound = 50;
        int int_random = random.nextInt(upperbound)*10;
        int quantity_random = random.nextInt(upperbound);
        float price_random = random.nextFloat();
        product.setSku(int_random);
        product.setName(faker.funnyName().name());
        product.setQuantity(quantity_random);
        product.setPrice(new BigDecimal(price_random));
        productController.setEntity(product);
    }

    @Test
    public void test1_createTest(){
        try {
            Product p = productController.create();
            assertFalse("Product is not saved in database", p.getId().equals(0L));
        } catch (BaseException e) {
            fail(e.getMessage());
        }catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void test2_ReadTest(){
        for(Product p: productController.read()){
            if(p.getName().equals(product.getName())){
                assertTrue(true);
                return;
            }
        }
        fail("Cannot read products from database");
    }


    @Test
    public void test3_UpdateTest(){
        productController.getEntity().setName(faker.funnyName().name());
        try {
            productController.update();
            assertTrue(true);
        } catch (BaseException e) {
            fail(e.getMessage());
        }catch(Exception e){
            fail(e.getMessage());
        }
    }


    @Test
    public void test4_DeleteTest(){
        try {
            productController.delete();
            assertTrue(true);
        } catch (BaseException e) {
            fail(e.getMessage());
        }catch(Exception e){
            fail(e.getMessage());
        }
    }


}
