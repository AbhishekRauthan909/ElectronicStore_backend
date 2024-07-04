package com.electronic.store.ElectronicStore.dtos;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse
{
    private String token;
    UserDto user;
    private String refreshToken;
}
