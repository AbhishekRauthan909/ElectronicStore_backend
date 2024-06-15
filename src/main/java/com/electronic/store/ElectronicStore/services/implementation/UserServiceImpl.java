package com.electronic.store.ElectronicStore.services.implementation;

import com.electronic.store.ElectronicStore.dtos.UserDto;
import com.electronic.store.ElectronicStore.entities.User;
import com.electronic.store.ElectronicStore.repositories.UserRepository;
import com.electronic.store.ElectronicStore.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public UserDto createUser(UserDto userDto) {
        //first we will generate the id
        String userId= UUID.randomUUID().toString();
        userDto.setUserId(userId);
    //first we will convert userDto to entity
        User user=dtoToEntity(userDto);
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
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found with the given Id try again with different Id"));
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
    public void deleteUser(String userId)
    {
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found for the given id"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAllUser()
    {
        List<User> users=userRepository.findAll();
        List<UserDto> userDtos=users.stream().map(user->entityToDto(user)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public UserDto getUserById(String userId)
    {
       User user= userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found for given Id"));
       UserDto tempDto=entityToDto(user);
        return tempDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found for given email"));
        UserDto userDto=entityToDto(user);
        return userDto;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users=userRepository.findByNameContaining(keyword);
        List<UserDto> userDtos=users.stream().map(user->entityToDto(user)).collect(Collectors.toList());
        return userDtos;
    }
}
