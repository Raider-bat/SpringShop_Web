package kz.gexa.spring.shop.controller.web;

import kz.gexa.spring.shop.entity.product.Brand;
import kz.gexa.spring.shop.entity.product.Category;
import kz.gexa.spring.shop.entity.product.Product;
import kz.gexa.spring.shop.entity.product.SaleControl;
import kz.gexa.spring.shop.entity.user.Role;
import kz.gexa.spring.shop.entity.user.User;
import kz.gexa.spring.shop.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class DashboardController {

    private final
    BrandRepo brandRepo;
    private final
    CategoryRepo categoryRepo;
    private final
    ProductRepo productRepo;
    private final
    UserRepo userRepo;
    private final
    CartRepo cartRepo;
    private final
    SaleControlRepo saleControlRepo;
    private final
    PasswordEncoder passwordEncoder;

    public DashboardController(
            BrandRepo brandRepo,
            CategoryRepo categoryRepo,
            ProductRepo productRepo,
            UserRepo userRepo,
            CartRepo cartRepo,
            SaleControlRepo saleControlRepo,
            PasswordEncoder passwordEncoder
    ) {
        this.brandRepo = brandRepo;
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
        this.saleControlRepo = saleControlRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        List<Brand> brandList = brandRepo.findAll();
        List<Category> categoryList = categoryRepo.findAll();
        List<Product> productList = productRepo.findAll();
        List<User> userList = userRepo.findAll();
        Optional<SaleControl> saleControl = saleControlRepo.findById(1L);
        boolean saleProducts = saleControl.get().isSaleProducts();

        model.addAttribute("roles", Role.values());
        model.addAttribute("saleProducts",saleProducts);
        model.addAttribute("userList",userList);
        model.addAttribute("brandList",brandList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("productList",productList);
        return "dashboard";
    }

    @PostMapping("/saleControl")
    public String saleControl(@RequestParam boolean saleProductsValue){
        SaleControl saleControl = new SaleControl();
        saleControl.setSaleProducts(saleProductsValue);
        log.info("СОСТОЯНИЕ ПРОДАЖ "+ saleProductsValue);
        saleControlRepo.save(saleControl);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard")
    public String saveProductInDb(Product product,
                                  @RequestParam Long category_id,
                                  @RequestParam Long brand_id
    )  {
        product.setBrand(brandRepo.findById(brand_id).get());
        product.setCategory(categoryRepo.findById(category_id).get());
        productRepo.save(product);

        log.info("Добавленный продукт:  "+product.toString() );
        return "redirect:/dashboard";
    }

    @PostMapping("/deleteProduct")
    @Transactional
    public String deleteProduct(@RequestParam Long productId){
        cartRepo.deleteAllByProduct_Id(productId);
        productRepo.deleteById(productId);
        log.info("ПРОДУКТ С ID "+productId+" УДАЛЁН");
        return "redirect:/dashboard";
    }

    @PostMapping("/addUser")
    public String addUser(User user){
        User userExist = userRepo.findByUsername(user.getUsername());
        if (userExist!=null){

            return "redirect:/dashboard";
        }
        if (
                user.getUsername().length() >3 &&
                        user.getUsername().length() <20 &&
                        user.getPassword().length() > 5 &&
                        user.getPassword().length() <50
        ){
            user.setActive(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            log.info("Пользователь добавлен Админом: "+ user.toString());
            userRepo.save(user);
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long userId) {
        userRepo.deleteById(userId);
        log.info("USER С ID " + userId + " УДАЛЁН");
        return "redirect:/dashboard";
    }
}
