package kz.gexa.spring.shop.repository;

import kz.gexa.spring.shop.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    List<Product> findByCategory_Id(Long category_id );
    List<Product> findByCategory_IdOrderByPriceDesc(Long category_id);
    List<Product> findByCategory_IdOrderByPriceAsc(Long category_id);
}
