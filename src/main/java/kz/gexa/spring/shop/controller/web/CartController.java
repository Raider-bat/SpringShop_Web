package kz.gexa.spring.shop.controller.web;


import kz.gexa.spring.shop.entity.Cart.Cart;
import kz.gexa.spring.shop.entity.product.SaleControl;
import kz.gexa.spring.shop.repository.CartRepo;
import kz.gexa.spring.shop.repository.SaleControlRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@Slf4j
public class CartController {

    private final
    CartRepo cartRepo;
    private final
    HttpServletRequest httpServletRequest;
    private final
    SaleControlRepo saleControlRepo;

    public CartController(CartRepo cartRepo, HttpServletRequest httpServletRequest, SaleControlRepo saleControlRepo) {
        this.cartRepo = cartRepo;
        this.httpServletRequest = httpServletRequest;
        this.saleControlRepo = saleControlRepo;
    }

    @GetMapping("/cart")
    public String cart(Model model){
        String username = httpServletRequest.getRemoteUser();
        AtomicReference<Integer> summaryPrice = new AtomicReference<>(0);
        List<Cart> cartList = cartRepo.findAllByUserUsername(username);
        Optional<SaleControl> saleControl = saleControlRepo.findById(1L);
        boolean saleProducts = saleControl.get().isSaleProducts();
        if (!cartList.isEmpty()){
            cartList.forEach(cartItem ->{
                summaryPrice.updateAndGet(v -> v + cartItem.getProduct().getPrice());
            });
            log.info(cartList.toString());
        }
        model.addAttribute("saleProducts", saleProducts);
        model.addAttribute("productInCartList", cartList);
        model.addAttribute("summaryPrice", summaryPrice.get());
        return "cart";
    }

    @PostMapping("/deleteProductInCart")
    public String deleteProductInCart(@RequestParam Long cartId){

        cartRepo.deleteById(cartId);
        return "redirect:/cart";
    }

    @PostMapping("/buyProducts")
    @Transactional
    public String buyProducts(@RequestParam Long summaryPrice, @RequestParam Long userId){
        cartRepo.deleteAllByUser_Id(userId);
        return "successfulpayment";
    }
}
