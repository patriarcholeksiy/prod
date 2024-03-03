package com.bulibuli.prod.controller;

import com.bulibuli.prod.dto.RegisterDTO;
import com.bulibuli.prod.dto.SignInDTO;
import com.bulibuli.prod.entity.UserEntity;
import com.bulibuli.prod.mapper.UserMapper;
import com.bulibuli.prod.service.CountryService;
import com.bulibuli.prod.service.JwtService;
import com.bulibuli.prod.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

    private UserMapper userMapper;

    private UserService userService;

    private JwtService jwtService;

    private CountryService countryService;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    public UserController(UserMapper userMapper, UserService userService, JwtService jwtService, CountryService countryService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.jwtService = jwtService;
        this.countryService = countryService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        if (countryService.existsByAlpha2(registerDTO.getAlpha2())) {
            if (userService.checkValidRegistration(registerDTO)) {
                UserEntity user = new UserEntity();
                user.setLogin(registerDTO.getLogin());
                user.setEmail(registerDTO.getEmail());
                user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
                user.setAlpha2(registerDTO.getAlpha2());
                user.setPublic(registerDTO.isPublic());
                user.setPhoneNumber(registerDTO.getPhoneNumber());
                user.setImage(registerDTO.getImage());
                user = userService.save(user);
                return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserDTO(user));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/sign-in")
    private ResponseEntity<?> signIn(@RequestBody SignInDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getLogin(),
                request.getPassword()
        ));
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(request.getLogin());
        if (userDetails != null) {
            return ResponseEntity.status(HttpStatus.OK).body(jwtService.generateToken(userDetails));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь с указанным логином и паролем не найден");
    }



}
