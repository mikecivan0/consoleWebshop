package hr.mikec.webstore.controller;

import hr.mikec.webstore.entity.Product;
import hr.mikec.webstore.util.BaseException;

import java.lang.reflect.Method;
import java.util.List;

public class ProductController extends BaseController<Product>{

    public ProductController() throws BaseException {
        super();
    }

    @Override
    public List<Product> read() {
        return session.createQuery("FROM Product").list();
    }

    @Override
    protected void createControl() throws BaseException {
        notEmptyControl("Name");
        notEmptyControl("Sku");
        notEmptyControl("Quantity");
        notEmptyControl("Price");
        createExistsControl();
    }

    @Override
    protected void updateControl() throws BaseException {
    }

    @Override
    protected void deleteControl() throws BaseException {

    }

    private void notEmptyControl(String variable) throws BaseException{
        if(getVariable(variable)==null || getVariable(variable).trim().length()==0){
            throw new BaseException("Input '" + variable + "' cannot be empty");
        }
    }

    private String getVariable(String variable){
        String text = "";
        try {
            Method method = Product.class.getDeclaredMethod("get" + variable, null);
            try {
                text = (String) method.invoke(entity, null);
            }catch (Exception e){
                text = String.valueOf(method.invoke(entity, null));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return text;
    }

    private void createExistsControl() throws BaseException{
        Long productSkuExists = (Long) session.createQuery(
                        "SELECT COUNT(id) FROM Product WHERE "
                                + "sku=:sku")
                .setParameter("sku", entity.getSku())
                .uniqueResult();
        if(productSkuExists!=0){
            throw new BaseException("Exists");
        }
    }

    public Product findBySku() throws BaseException{
        Product product = (Product) session.createQuery(
                        "FROM Product WHERE "
                                + "sku=:sku")
                .setParameter("sku", entity.getSku())
                .getSingleResult();
        return product;
    }
}

