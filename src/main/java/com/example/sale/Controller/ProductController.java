package com.example.sale.Controller;

import com.example.sale.Service.ProductService;
import com.example.sale.model.PaginationDTO;
import com.example.sale.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")

public class ProductController {

    @Autowired
    ProductService productService;


    @PostMapping("save")
    public Product save(@RequestBody Product product)   // new product save karse and history ma add karse
    {
        return productService.save(product);
    }


    @GetMapping("pagination")
    public PaginationDTO getallProducts(@RequestParam int page, @RequestParam int pageSize)
            // paginated list product return karse
    {
        Page<Product> productsPage = productService.getallproduct(page, pageSize);
        return new PaginationDTO(productsPage.getContent(),
                page,pageSize,productsPage.getTotalPages());

    }
}
