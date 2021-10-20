package hr.mikec.webstore.controller;

import hr.mikec.webstore.entity.Product;
import hr.mikec.webstore.entity.ShoppingCart;
import hr.mikec.webstore.util.BaseException;

import java.lang.reflect.Method;
import java.util.List;

public class ShoppingCartController extends BaseController<ShoppingCart>{

    public ShoppingCartController() throws BaseException {
        super();
    }

    @Override
    public List<ShoppingCart> read() {
        return session.createQuery("FROM ShoppingCart").list();
    }

    @Override
    protected void createControl() throws BaseException {
        notNegativeSkuControll();
        notNegativeQuantityControll();
        createExistsControl();
    }

    @Override
    protected void updateControl() throws BaseException {
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

    private void notNegativeSkuControll() throws BaseException{
        if(entity.getProduct().getSku()<1){
            throw new BaseException("SKU must be greater than 0");
        }
    }

    private void notNegativeQuantityControll() throws BaseException{
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
}

