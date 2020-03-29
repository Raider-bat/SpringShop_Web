package kz.gexa.spring.shop.controller.web;


import kz.gexa.spring.shop.entity.product.Category;
import kz.gexa.spring.shop.entity.product.Product;
import kz.gexa.spring.shop.entity.product.SaleControl;
import kz.gexa.spring.shop.repository.*;
import kz.gexa.spring.shop.service.CartService;
import kz.gexa.spring.shop.service.ProductService;
import kz.gexa.spring.shop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class MainController {

    final HttpServletRequest httpServletRequest;
    final UserService userService;
    final SaleControlRepo saleControlRepo;
    final CartService cartService;
    final ProductService productService;

    public MainController(HttpServletRequest httpServletRequest,
                          UserService userService,
                          SaleControlRepo saleControlRepo,
                          CartService cartService,
                          ProductService productService) {
        this.httpServletRequest = httpServletRequest;
        this.userService = userService;
        this.saleControlRepo = saleControlRepo;
        this.cartService = cartService;
        this.productService = productService;
    }


    @GetMapping("/")
    public String redirectMain(){
        return "redirect:main";
    }

    @GetMapping("/main")
    public String main(@PageableDefault(size = 9) Pageable pageable, Model model)
    {
        String userName = httpServletRequest.getRemoteUser();
        Page<Product> productList = productService.getAllProducts(pageable);
        List<Category> categories = productService.getAllCategory();
        Optional<SaleControl> saleControl = saleControlRepo.findById(1L);
        boolean userNotAuthorised = true;

        if (userName!= null){

            userNotAuthorised =false;
        }

        boolean saleProducts = saleControl.get().isSaleProducts();
        model.addAttribute("saleProducts", saleProducts);
        model.addAttribute("userNotAuthorised", userNotAuthorised);
        model.addAttribute("isAdmin", userService.isAdmin(userName));
        model.addAttribute("productList",productList);
        model.addAttribute("categories",categories);
        model.addAttribute("user", userName);
        log.info("ПЕРЕХОД НА MAIN");
        log.info("Пользователь в системе: "+ !userNotAuthorised);
        return "main";
    }

    @PostMapping("/main")
    public String insertProductInCart(@RequestParam("productId") Product product,
                                      @RequestParam(value = "page", required = false) String page){

        cartService.addProductInCart(product);
        return "redirect:/main?page="+page;
    }

}