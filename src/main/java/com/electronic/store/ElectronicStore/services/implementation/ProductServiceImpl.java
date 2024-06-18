package com.electronic.store.ElectronicStore.services.implementation;

import com.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.electronic.store.ElectronicStore.dtos.ProductDto;
import com.electronic.store.ElectronicStore.entities.Category;
import com.electronic.store.ElectronicStore.entities.Products;
import com.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.electronic.store.ElectronicStore.helper.Helper;
import com.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.electronic.store.ElectronicStore.services.ProductService;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService
{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${product.profile.image.path}")
    private String imagePath;
    @Override
    public ProductDto create(ProductDto productDto)
    {
        String productId=UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Products product=mapper.map(productDto,Products.class);
        Products savedProduct=productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Products product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found of given id"));
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setTitle(productDto.getTitle());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setImageName(productDto.getImageName());
        Products updatedProduct=productRepository.save(product);
        return mapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public void delete(String productId)
    {
        Products product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product of the given id is not found"));
        String fullPath=imagePath+product.getImageName();
        try
        {
            Path path= Paths.get(fullPath);
            Files.delete(path);
        } catch(NoSuchFileException ex)
        {
            ex.printStackTrace();
        } catch(IOException io)
        {
            io.printStackTrace();
        }
        productRepository.delete(product);
    }

    @Override
    public ProductDto getSingleProduct(String productId)
    {
        Products product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product of the given id not found"));
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Products> page=productRepository.findAll(pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAlllIVE(int pageNumber,int pageSize,String sortBy,String sortDir)
    {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Products> page=productRepository.findByLiveTrue(pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String title,int pageNumber,int pageSize,String sortBy,String sortDir)
    {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Products> page=productRepository.findByTitleContaining(title,pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId)
    {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category of the given id is not found"));
        String productId=UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Products product=mapper.map(productDto,Products.class);
        product.setCategory(category);
        Products savedProduct=productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId)
    {
        Products product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product of the given id is not found"));
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category of the given id is not found"));
        product.setCategory(category);
        Products updatedProduct=productRepository.save(product);//saved products
        return mapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getCategoryProduct(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category not found with given id"));
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Products> page=productRepository.findByCategory(category,pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }
}
