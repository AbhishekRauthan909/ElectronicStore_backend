package com.electronic.store.ElectronicStore.controllers;
import com.electronic.store.ElectronicStore.JWT.JwtHelper;
import com.electronic.store.ElectronicStore.dtos.JwtRequest;
import com.electronic.store.ElectronicStore.dtos.JwtResponse;
import com.electronic.store.ElectronicStore.dtos.UserDto;
import com.electronic.store.ElectronicStore.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/auth")
public class AuthenticationController
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper modelMapper;

   @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest)
    {
        this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());

        User user=(User)userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token=jwtHelper.generateToken(user);
        JwtResponse jwtResponse= JwtResponse.builder().token(token).user(modelMapper.map(user, UserDto.class)).build();
        return ResponseEntity.ok(jwtResponse);
    }

    private void doAuthenticate(String email, String password)
    {
        try
        {
            Authentication authentication=new UsernamePasswordAuthenticationToken(email,password);
            authenticationManager.authenticate(authentication);
        }
        catch(BadCredentialsException ex)
        {
            throw new BadCredentialsException("Please enter correct username or password");
        }
    }
}
