package hr.mikec.webstore.controller;

import hr.mikec.webstore.entity.ShopingCart;
import hr.mikec.webstore.util.BaseException;

import java.lang.reflect.Method;
import java.util.List;

public class ShopingCartController extends BaseController<ShopingCart>{

    public ShopingCartController() throws BaseException {
        super();
    }

    @Override
    public List<ShopingCart> read() {
        return session.createQuery("FROM ShopingCart").list();
    }

    @Override
    protected void createControl() throws BaseException {
        notNullControl("Product");
        notEmptyControl("Quantity");
        createExistsControl();
    }

    @Override
    protected void updateControl() throws BaseException {
        notNullControl("Product");
        notEmptyControl("Quantity");
        updateExistsControl();
    }

    @Override
    protected void deleteControl() throws BaseException {

    }

    private void notEmptyControl(String variable) throws BaseException{
        if(getVariable(variable)==null || getVariable(variable).trim().length()==0){
            throw new BaseException("Input '" + variable + "' cannot be empty");
        }
    }

    private void notNullControl(String variable) throws BaseException{
        if(getVariable(variable)==null){
            throw new BaseException(variable + "' cannot be empty");
        }
    }

    private String getVariable(String variable){
        String text = "";
        try {
            Method method = ShopingCart.class.getDeclaredMethod("get" + variable, null);
            text = (String) method.invoke(entity, null);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return text;
    }

    private void createExistsControl() throws BaseException{
        Long productSkuExists = (Long) session.createQuery(
                        "SELECT COUNT(id) FROM ShopingCart WHERE "
                                + "product=:product")
                .setParameter("product", entity.getProduct())
                .uniqueResult();
        if(productSkuExists!=0){
            throw new BaseException("Product with same sku already exists in database");
        }
    }

    private void updateExistsControl() throws BaseException{
        Long productExists = (Long) session.createQuery(
                        "SELECT COUNT(id) FROM ShopingCart WHERE "
                                + "product=:product "
                                + "AND id!=:id")
                .setParameter("product", entity.getProduct())
                .setParameter("id", entity.getId())
                .uniqueResult();
        if(productExists!=0){
            throw new BaseException("Product with same sku already exists ina database");
        }
    }
}

