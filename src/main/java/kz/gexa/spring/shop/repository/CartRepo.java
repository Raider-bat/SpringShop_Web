package kz.gexa.spring.shop.repository;

import kz.gexa.spring.shop.entity.Cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepo  extends JpaRepository<Cart, Long> {

    List<Cart> findAllByUserUsername(String username);
    void deleteAllByProduct_Id(Long productId);
    void deleteAllByUser_Id(Long userId);
}
