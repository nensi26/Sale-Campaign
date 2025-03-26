package com.example.sale.model;


import java.util.List;



public class PaginationDTO {

    private List<Product> product;
    private int page;
    private int pageSize;
    private int totalPages;

    public PaginationDTO(List<Product> product, int page, int pageSize, int totalPages) {
        this.product = product;
        this.page=page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
