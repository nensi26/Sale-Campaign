package com.example.sale.Service;

import com.example.sale.model.History;
import com.example.sale.model.Product;
import com.example.sale.repository.HistoryRepo;
import com.example.sale.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    HistoryRepo historyRepo;


    public Product save(Product product)
    {
        Product saveproduct =productRepo.save(product);

        History history=new History();
        history.setProduct(product);
        history.setDate(LocalDate.now());
        history.setPrice(product.getCurrentPrice());
        historyRepo.save(history);

        return saveproduct;

    }


    public Page<Product> getallproduct(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return productRepo.findAll(pageable);
    }




}
