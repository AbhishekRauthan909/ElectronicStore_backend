package com.electronic.store.ElectronicStore.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User
{
    @Id
    private String userId;
    @Column(name="user_name")
    private String name;
   @Column(name="user_password",length = 10)
    private String password;
    @Column(name="user_email",unique = true)
    private String email;
    private String gender;
    @Column(length = 100)
    private String about;
    @Column(name="user_image_name")
    private String imageName;
}
