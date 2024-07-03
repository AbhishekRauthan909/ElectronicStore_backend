package com.electronic.store.ElectronicStore.services.implementation;
import com.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.electronic.store.ElectronicStore.dtos.UserDto;
import com.electronic.store.ElectronicStore.entities.Role;
import com.electronic.store.ElectronicStore.entities.User;
import com.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.electronic.store.ElectronicStore.helper.Helper;
import com.electronic.store.ElectronicStore.repositories.RoleRepository;
import com.electronic.store.ElectronicStore.repositories.UserRepository;
import com.electronic.store.ElectronicStore.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagePath;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDto createUser(UserDto userDto)
    {
        //first we will generate the id
        String userId= UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //first we will convert userDto to entity
        User user=dtoToEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role=roleRepository.findByName("ROLE_USER");
        user.setRoles(List.of(role));
        User savedUser=userRepository.save(user);
        UserDto tempDto=entityToDto(savedUser);
        return tempDto;
    }

    private UserDto entityToDto(User user)
    {
        return mapper.map(user,UserDto.class);
    }

    private User dtoToEntity(UserDto userDto)
    {
        return mapper.map(userDto,User.class);
    }
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with the given Id try again with different Id"));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getPassword());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        User updatedUser=userRepository.save(user);
        UserDto tempDto=entityToDto(updatedUser);
        return tempDto;
    }

    @Override
    public void deleteUser(String userId){
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found for the given id"));
        String fullPath=imagePath+user.getImageName();
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
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir)
    {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page=userRepository.findAll(pageable);
        return Helper.getPageableResponse(page,UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId)
    {
        User user= userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found for given Id"));
        UserDto tempDto=entityToDto(user);
        return tempDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user=userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found for given email"));
        UserDto userDto=entityToDto(user);
        return userDto;
    }

    @Override
    public List<UserDto> searchUser(String keyword)
    {
        List<User> users=userRepository.findByNameContaining(keyword);
        List<UserDto> userDtos=users.stream().map(user->entityToDto(user)).collect(Collectors.toList());
        return userDtos;
    }
}