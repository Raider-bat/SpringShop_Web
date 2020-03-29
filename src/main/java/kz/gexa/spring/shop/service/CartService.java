package kz.gexa.spring.shop.service;

import kz.gexa.spring.shop.controller.web.CartController;
import kz.gexa.spring.shop.entity.Cart.Cart;
import kz.gexa.spring.shop.entity.product.Product;
import kz.gexa.spring.shop.entity.user.User;
import kz.gexa.spring.shop.repository.CartRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class CartService {

    @Autowired
    CartRepo cartRepo;

    public void addProductInCart(Product product){
        Cart cart = new Cart();

        log.info("Продукт: "+ product.toString());
        try {
            User user = (User) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();

            cart.setUser(user);
            cart.setProduct(product);
            log.info("ДОБАВЛЕНИЕ В КОРЗИНУ: "+ cart.toString());
            cartRepo.save(cart);
        }catch (Exception e){
            log.warn(Arrays.toString(e.getStackTrace()));
        }
    }
}
