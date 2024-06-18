package com.electronic.store.ElectronicStore.controllers;

import com.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.electronic.store.ElectronicStore.dtos.ProductDto;
import com.electronic.store.ElectronicStore.dtos.UserDto;
import com.electronic.store.ElectronicStore.payload.ApiResponseMessage;
import com.electronic.store.ElectronicStore.payload.ImageResponse;
import com.electronic.store.ElectronicStore.services.FileService;
import com.electronic.store.ElectronicStore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController
{

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.profile.image.path}")
    private String imageUploadPath;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto)
    {
        ProductDto productDto1=productService.create(productDto);
        return new ResponseEntity<>(productDto1,HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @PathVariable String productId,@RequestBody ProductDto productDto)
    {
        ProductDto updatedProduct=productService.update(productDto,productId);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) throws IOException {
        productService.delete(productId);
        ApiResponseMessage message=ApiResponseMessage.builder()
                .message("Product is deleted Successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value="pageNumber",defaultValue ="0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "ASC",required = false) String sortDir
    )
    {
        PageableResponse<ProductDto> productDtos=productService.getAll(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId)
    {
        return new ResponseEntity<>(productService.getSingleProduct(productId),HttpStatus.OK);
    }

    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(
            @RequestParam(value="pageNumber",defaultValue ="0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "ASC",required = false) String sortDir
    )
    {
        PageableResponse<ProductDto> productDtos=productService.getAlllIVE(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProducts(
            @PathVariable String keyword,
            @RequestParam(value="pageNumber",defaultValue ="0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "ASC",required = false) String sortDir
    )
    {
        PageableResponse<ProductDto> productDtos=productService.searchByTitle(keyword,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @RequestParam("productImage") MultipartFile image,
            @PathVariable String productId
    ) throws IOException {
        String imageName=fileService.uploadImage(image,imageUploadPath);
        ProductDto product=productService.getSingleProduct(productId);
        product.setImageName(imageName);
        ProductDto productDto=productService.update(product,productId);
        ImageResponse imageResponse=ImageResponse.
                builder().
                imageName(imageName).
                success(true).
                status(HttpStatus.CREATED).
                build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    @GetMapping("/image/{productId}")
    public void serveProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto product = productService.getSingleProduct(productId);
        InputStream resource = fileService.getResource(imageUploadPath, product.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
