package hr.mikec.webstore.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class ShoppingCart {

    public ShoppingCart() {

    }

    public ShoppingCart(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Product product;

    @Column(nullable = false)
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        final String s = "SKU: " + product.getSku() + ", product: " +
                product.getName() + ", piece price: " +
                product.getPrice().setScale(2, RoundingMode.CEILING) + ", quantity: " +
                quantity + ", " +
                "total amount for this product: " +
                product.getPrice().multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.CEILING);
        return s;
    }
}
