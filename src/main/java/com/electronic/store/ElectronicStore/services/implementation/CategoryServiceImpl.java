package com.electronic.store.ElectronicStore.services.implementation;
import com.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.electronic.store.ElectronicStore.dtos.UserDto;
import com.electronic.store.ElectronicStore.entities.Category;
import com.electronic.store.ElectronicStore.entities.User;
import com.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.electronic.store.ElectronicStore.helper.Helper;
import com.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.electronic.store.ElectronicStore.services.CategoryService;
import jakarta.validation.constraints.Size;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${category.profile.image.path}")
    private String imagePath;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto)
    {
        String categoryId= UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category=dtoToentity(categoryDto);
        Category savedCategory=categoryRepository.save(category);
        CategoryDto categoryDto1=entityToDto(savedCategory);
        return categoryDto1;
    }

    private CategoryDto entityToDto(Category savedCategory)
    {
        return mapper.map(savedCategory,CategoryDto.class);
    }

    private Category dtoToentity(CategoryDto categoryDto)
    {
        return mapper.map(categoryDto,Category.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId)
    {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category not found with given id try with some different id"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory=categoryRepository.save(category);
        return entityToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(String categoryId)
    {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found for the given id"));
        String fullPath=imagePath+category.getCoverImage();
        try
        {
            Path path=Paths.get(fullPath);
            Files.delete(path);
        } catch(NoSuchFileException ex)
        {
            ex.printStackTrace();
        } catch(IOException io)
        {
            io.printStackTrace();
        }
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page=categoryRepository.findAll(pageable);
        return Helper.getPageableResponse(page, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategoryById(String categoryId)
    {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category not found with given id"));
        return entityToDto(category);
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword)
    {
        List<Category>categories=categoryRepository.findByTitleContaining(keyword);
        List<CategoryDto> categoryDtos=categories.stream().map(category -> entityToDto(category)).collect(Collectors.toList());
        return categoryDtos;
    }


}
