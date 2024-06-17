package com.electronic.store.ElectronicStore.services;

import com.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.electronic.store.ElectronicStore.entities.Category;

import java.util.List;

public interface CategoryService
{
    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto,String categoryId);

    //delete
    void deleteCategory(String categoryId);

    //get all
    PageableResponse<CategoryDto> getAllCategory(int pageNumber,int pageSize,String sortBy,String sortDir);


    //get single
    CategoryDto getCategoryById(String categoryId);

    //search
    List<CategoryDto> searchCategory(String keyword);


}
