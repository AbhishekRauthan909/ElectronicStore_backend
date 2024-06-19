package com.electronic.store.ElectronicStore.services.implementation;
import com.electronic.store.ElectronicStore.dtos.*;
import com.electronic.store.ElectronicStore.entities.*;
import com.electronic.store.ElectronicStore.exceptions.BadApiRequest;
import com.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.electronic.store.ElectronicStore.repositories.CartItemRepository;
import com.electronic.store.ElectronicStore.repositories.CartRepository;
import com.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.electronic.store.ElectronicStore.repositories.UserRepository;
import com.electronic.store.ElectronicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService
{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request)
    {
        Products product=productRepository.findById(request.getProductId()).orElseThrow(()->new ResourceNotFoundException("Product of the given id not found"));
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User of the given id not found"));
        //If the cart is not available for that particular user we will create the cart
        //else we will use that cart only
        if(request.getQuantity()<=0)
        {
            throw new BadApiRequest("Please enter valid quantity");
        }
        Cart cart=null;
        try
        {
            cart=cartRepository.findByUser(user).get();
        }catch(NoSuchElementException e)
        {
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatesAt(new Date());
        }
        AtomicBoolean flag= new AtomicBoolean(true);
        List<CartItem> items=cart.getItems();
        items=items.stream().map(item->
        {
            if(item.getProduct().getProductId().equals(request.getProductId()))
            {
                flag.set(false);
                item.setQuantity(request.getQuantity());
                item.setTotalPrice(request.getQuantity()*product.getDiscountedPrice());
            }
            return item;
        }).collect(Collectors.toList());
        if(flag.get())
        {
            CartItem newItem=CartItem.builder()
                    .quantity(request.getQuantity())
                    .totalPrice(request.getQuantity()*product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(newItem);
        }
        cart.setUser(user);
        Cart updateCart=cartRepository.save(cart);
        return mapCartToDto(updateCart);
    }

    private CartDto mapCartToDto(Cart updateCart)
    {
        CartDto cartDto=new CartDto();
        cartDto.setUserDto(mapUserToDto(updateCart.getUser()));
        cartDto.setCreatesAt(updateCart.getCreatesAt());
        cartDto.setCartId(updateCart.getCartId());
        cartDto.setItems(updateCart.getItems().stream().map(this::mapCartItemToDto).collect(Collectors.toList()));
        return cartDto;
    }

    private CartItemDto mapCartItemToDto(CartItem cartItem)
    {
        CartItemDto cartItemDto=new CartItemDto();
        cartItemDto.setCartItemId(cartItem.getCartItemId());
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setTotalPrice(cartItem.getTotalPrice());
        cartItemDto.setProductDto(mapProductToDto(cartItem.getProduct()));
        return cartItemDto;
    }

    private ProductDto mapProductToDto(Products product)
    {
        ProductDto productDto=new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setLive(product.isLive());
        productDto.setAddedDate(product.getAddedDate());
        productDto.setCategory(mapCategoryToDto(product.getCategory()));
        productDto.setQuantity(product.getQuantity());
        productDto.setDescription(product.getDescription());
        productDto.setStock(product.isStock());
        productDto.setDiscountedPrice(product.getDiscountedPrice());
        productDto.setTitle(product.getTitle());
        productDto.setPrice(product.getPrice());
        productDto.setImageName(product.getImageName());
        return productDto;
    }
    private CategoryDto mapCategoryToDto(Category category)
    {
        CategoryDto categoryDto=new CategoryDto();
        if(category==null)
        {
            return null;
        }
        categoryDto.setCategoryId(category.getCategoryId());
        categoryDto.setCoverImage(category.getCoverImage());
        categoryDto.setTitle(category.getTitle());
        categoryDto.setDescription(category.getDescription());
        return categoryDto;
    }
    private UserDto mapUserToDto(User user)
    {
        UserDto userDto=new UserDto();
        userDto.setPassword(user.getPassword());
        userDto.setUserId(user.getUserId());
        userDto.setImageName(user.getImageName());
        userDto.setEmail(user.getEmail());
        userDto.setAbout(user.getAbout());
        userDto.setEmail(user.getEmail());
        userDto.setGender(user.getGender());
        userDto.setName(user.getName());
        return userDto;
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem)
    {
        CartItem cartItems=cartItemRepository.findById(cartItem).orElseThrow(()->new ResourceNotFoundException("Cart item not found!!"));
        cartItemRepository.delete(cartItems);
    }

    @Override
    public void clearCart(String userId)
    {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User of the given id not found!"));
        Cart cart=cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Given user cart not found!!"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId)
    {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User of the given id not found!"));
        Cart cart=cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Given user cart not found!!"));
        return mapCartToDto(cart);
    }
}
