package hr.mikec.webstore.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class ShopingCart {

    public ShopingCart() {

    }

    public ShopingCart(Product product, BigDecimal quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Product product;

    @Column(nullable = false)
    private BigDecimal quantity;

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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        final String s = product.getSku() + ", " +
                product.getName() + ", " +
                product.getPrice() + ", " +
                quantity + ", " +
                "total: " + quantity.multiply(product.getPrice());
        return s;
    }
}
