package kz.gexa.spring.shop.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SaleControl {
    @Id
    private Long id = 1L;
    @Column(name = "sale_products")
    private boolean saleProducts;

    public boolean isSaleProducts() {
        return saleProducts;
    }

    public void setSaleProducts(boolean saleProducts) {
        this.saleProducts = saleProducts;
    }

}
