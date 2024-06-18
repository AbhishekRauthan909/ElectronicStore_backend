package com.electronic.store.ElectronicStore.services;

import com.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.electronic.store.ElectronicStore.dtos.ProductDto;
import com.electronic.store.ElectronicStore.entities.Category;
import com.electronic.store.ElectronicStore.entities.Products;

import java.util.List;

public interface ProductService
{
    ProductDto create(ProductDto productDto);
    ProductDto update(ProductDto productDto,String productId);
    void delete(String productId);
    ProductDto getSingleProduct(String productId);
    PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);
    PageableResponse<ProductDto> getAlllIVE(int pageNumber,int pageSize,String sortBy,String sortDir);
    PageableResponse<ProductDto> searchByTitle(String title,int pageNumber,int pageSize,String sortBy,String sortDir);
    ProductDto createWithCategory(ProductDto productDto,String categoryId);
    ProductDto updateCategory(String productId,String categoryId);
    PageableResponse<ProductDto> getCategoryProduct(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);
}

