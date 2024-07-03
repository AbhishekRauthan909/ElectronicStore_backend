package com.electronic.store.ElectronicStore.dtos;
import com.electronic.store.ElectronicStore.entities.Role;
import com.electronic.store.ElectronicStore.validate.ImageNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;

    @Size(min=3,max=35,message="Invalid name !!!")
    private String name;

    @NotBlank(message ="Please enter some valid password!!!")
    @Size(max=1000,message="Your password length exceeding!!!")
    private String password;

    @Pattern(regexp="^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",message="Invalid email !!!")
    @NotBlank(message="Email is required!!")
    private String email;

    @Size(min=4,max=6,message="Invalid gender!!!")
    private String gender;

    @NotBlank(message="Invalid about!!!")
    private String about;

    @ImageNameValid
    private String imageName;


    private List<RoleDto> roles;


}
