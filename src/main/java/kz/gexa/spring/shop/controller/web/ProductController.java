package kz.gexa.spring.shop.controller.web;

import kz.gexa.spring.shop.entity.product.Product;
import kz.gexa.spring.shop.entity.product.SaleControl;
import kz.gexa.spring.shop.repository.SaleControlRepo;
import kz.gexa.spring.shop.service.CartService;
import kz.gexa.spring.shop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/main")
public class ProductController {

    final HttpServletRequest httpServletRequest;
    final SaleControlRepo saleControlRepo;
    final ProductService productService;
    final CartService cartService;

    public ProductController(HttpServletRequest httpServletRequest, SaleControlRepo saleControlRepo, ProductService productService, CartService cartService) {
        this.httpServletRequest = httpServletRequest;
        this.saleControlRepo = saleControlRepo;
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("{categoryId}")
    public ModelAndView orderByPrice(@PathVariable Long categoryId, @RequestParam(required = false, defaultValue = "top") String price) {
        ModelAndView modelAndView = new ModelAndView("product");
        String userName = httpServletRequest.getRemoteUser();
        Optional<SaleControl> saleControl = saleControlRepo.findById(1L);
        boolean saleProducts = saleControl.get().isSaleProducts();
        List<Product> sortedProductList = productService.
                getProductByCategoryIdAndOrderByPriceAscOrDesc(categoryId, price);

        modelAndView.addObject("productList", sortedProductList);
        modelAndView.addObject("price", price);
        modelAndView.addObject("saleProducts", saleProducts);
        modelAndView.addObject("user", userName);
        return modelAndView;
    }

    @PostMapping("/buyProduct")
    public String buyProduct(
            @RequestParam Long categoryId,
            @RequestParam(value = "productId")
                    Product product,
            @RequestParam
                    (required = false, defaultValue = "top")
                    String price
    ) {
        cartService.addProductInCart(product);
        return "redirect:/main/"+categoryId+"?price="+price;
    }
}
