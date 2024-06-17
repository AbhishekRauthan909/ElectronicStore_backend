package com.electronic.store.ElectronicStore.controllers;
import com.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.electronic.store.ElectronicStore.dtos.UserDto;
import com.electronic.store.ElectronicStore.payload.ApiResponseMessage;
import com.electronic.store.ElectronicStore.payload.ImageResponse;
import com.electronic.store.ElectronicStore.services.FileService;
import com.electronic.store.ElectronicStore.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController
{

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    //create api
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
    {
        UserDto user=userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    //update api
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable("userId") String userId,@RequestBody UserDto userDto)
    {
       UserDto updatedUser=userService.updateUser(userDto,userId);
       return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }


    //delete api
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) throws IOException {
        userService.deleteUser(userId);
        ApiResponseMessage message=ApiResponseMessage.builder()
                .message("User is deleted Successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value="pageNumber",defaultValue ="0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "ASC",required = false) String sortDir
            )
    {
        PageableResponse<UserDto> userDtos=userService.getAllUser(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(userDtos,HttpStatus.OK);
    }

    //get single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId)
    {
        return new ResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
    }

    //get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
    {
        return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
    }

    //search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords)
    {
        return new ResponseEntity<>(userService.searchUser(keywords),HttpStatus.OK);
    }

    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage") MultipartFile image,
            @PathVariable String userId
            ) throws IOException {
        String imageName=fileService.uploadImage(image,imageUploadPath);
        UserDto user=userService.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto=userService.updateUser(user,userId);
        ImageResponse imageResponse=ImageResponse.
                builder().
                imageName(imageName).
                success(true).
                status(HttpStatus.CREATED).
                build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }


    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userService.getUserById(userId);
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        //sets the Content-Type header of an HTTP response to
        // indicate that the response body contains a JPEG image.
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
