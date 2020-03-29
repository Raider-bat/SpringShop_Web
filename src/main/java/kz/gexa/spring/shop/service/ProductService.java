package kz.gexa.spring.shop.service;

import kz.gexa.spring.shop.entity.product.Category;
import kz.gexa.spring.shop.entity.product.Product;
import kz.gexa.spring.shop.repository.CategoryRepo;
import kz.gexa.spring.shop.repository.ProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    final
    ProductRepo productRepo;
    final
    CategoryRepo categoryRepo;

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }


    public Page<Product> getAllProducts(Pageable pageable){

       return productRepo.findAll(pageable);
    }

    public List<Category> getAllCategory(){
        return categoryRepo.findAll();
    }


    public List<Product> getProductByCategoryIdAndOrderByPriceAscOrDesc(Long categoryId, String price){

        if (price.equals("top")) {

            log.info("СОРТИРОВКА ПО УБЫВАНИЮ ЦЕНЫ");
            return productRepo.
                            findByCategory_IdOrderByPriceDesc(categoryId);
        } else if (price.equals("bottom")) {

            log.info("СОРТИРОВКА ПО ВОЗРАСТАНИЮ ЦЕНЫ");
            return productRepo.
                            findByCategory_IdOrderByPriceAsc(categoryId);
        }
        return productRepo.
                findByCategory_IdOrderByPriceDesc(categoryId);
    }
}
