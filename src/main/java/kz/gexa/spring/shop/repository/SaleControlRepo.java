package kz.gexa.spring.shop.repository;

import kz.gexa.spring.shop.entity.product.SaleControl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleControlRepo extends JpaRepository<SaleControl, Long> {
}
