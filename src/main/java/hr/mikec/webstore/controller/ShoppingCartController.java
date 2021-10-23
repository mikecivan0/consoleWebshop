package hr.mikec.webstore.controller;

import hr.mikec.webstore.entity.Product;
import hr.mikec.webstore.controller.ProductController;
import hr.mikec.webstore.entity.ShoppingCart;
import hr.mikec.webstore.util.BaseException;
import java.util.List;

public class ShoppingCartController extends BaseController<ShoppingCart>{

    private ProductController productController;
    public ShoppingCartController() throws BaseException {
        super();
        productController = new ProductController();
    }

    @Override
    public List<ShoppingCart> read() {
        return session.createQuery("FROM ShoppingCart").list();
    }

    @Override
    protected void createControl() throws BaseException {
        notNegativeSkuControl();
        notNegativeQuantityControl();
        notEnoughProductsOnStockControl();
        createExistsControl();
    }

    @Override
    protected void updateControl() throws BaseException {
        notNegativeSkuControl();
        notNegativeQuantityControl();
        notEnoughProductsOnStockUpdateControl();
        updateExistsControl();
    }

    @Override
    protected void deleteControl() throws BaseException {

    }

    private void createExistsControl() throws BaseException{
        Long productSkuExists = (Long) session.createQuery(
                        "SELECT COUNT(id) FROM ShoppingCart WHERE "
                                + "product=:product")
                .setParameter("product", entity.getProduct())
                .uniqueResult();
        if(productSkuExists!=0){
            throw new BaseException("Product is already in cart");
        }
    }

    private void notNegativeSkuControl() throws BaseException{
        if(entity.getProduct().getSku()<1){
            throw new BaseException("SKU must be greater than 0");
        }
    }

    private void notEnoughProductsOnStockControl() throws BaseException{
        int productsOnStock = productController.findBySku(entity.getProduct().getSku()).getQuantity();
        if(entity.getQuantity()>productsOnStock){
            throw new BaseException("Not enough product items on stock");
        }
    }

    private void notEnoughProductsOnStockUpdateControl() throws BaseException{
        int productsOnStock = productController.findBySku(entity.getProduct().getSku()).getQuantity();
        int currentInShoppingCart = findBySku(entity.getProduct().getSku()).getQuantity();
        int needToAdd = entity.getQuantity()-currentInShoppingCart;
        if(needToAdd>productsOnStock){
            throw new BaseException("Not enough product items on stock");
        }
    }

    private void notNegativeQuantityControl() throws BaseException{
        if(entity.getQuantity()<1){
            throw new BaseException("Quantity must be greater than 0");
        }
    }

    private void updateExistsControl() throws BaseException{
        Long productExists = (Long) session.createQuery(
                        "SELECT COUNT(id) FROM ShoppingCart WHERE "
                                + "product=:product "
                                + "AND id!=:id")
                .setParameter("product", entity.getProduct())
                .setParameter("id", entity.getId())
                .uniqueResult();
        if(productExists!=0){
            throw new BaseException("Product with same sku already exists ina database");
        }
    }

    public ShoppingCart findByProduct(Product product) throws BaseException{
        ShoppingCart shoppingCart = (ShoppingCart) session.createQuery(
                        "FROM ShoppingCart WHERE "
                                + "product=:product")
                .setParameter("product", product)
                .getSingleResult();
        return shoppingCart;
    }

    public ShoppingCart findBySku(int sku) {
        ShoppingCart shoppingCart = new ShoppingCart();
        for(ShoppingCart sc : read()){
            if(sc.getProduct().getSku()==sku){
                shoppingCart = sc;
            }
        }
        return shoppingCart;
    }

    public void truncate(){
        session.beginTransaction();
        session.createSQLQuery("TRUNCATE TABLE shoppingCart").executeUpdate();
        session.getTransaction().commit();
    }

}

