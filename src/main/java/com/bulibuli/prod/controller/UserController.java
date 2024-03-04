package com.bulibuli.prod.controller;

import com.bulibuli.prod.dto.UpdatePasswordDTO;
import com.bulibuli.prod.dto.UpdateUserDTO;
import com.bulibuli.prod.entity.UserEntity;
import com.bulibuli.prod.mapper.UserMapper;
import com.bulibuli.prod.service.CountryService;
import com.bulibuli.prod.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    private UserMapper userMapper;

    private CountryService countryService;

    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
                          UserMapper userMapper,
                          CountryService countryService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.countryService = countryService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/me/profile")
    public ResponseEntity<?> getAuthProfile(Principal principal) {
        if (principal != null) {
            try {
                UserEntity user = userService.getByUsername(principal.getName());
                return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserDTO(user));
            } catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PatchMapping("/me/profile")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UpdateUserDTO updateDTO, Principal principal) {
        if (principal != null) {
            try {
                UserEntity user = userService.getByUsername(principal.getName());
                if (updateDTO.getCountryAlpha2() != null) {
                    if (!countryService.existsByAlpha2(updateDTO.getCountryAlpha2())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Страны с кодом " +
                                updateDTO.getCountryAlpha2() + " не существует");
                    }
                    user.setAlpha2(updateDTO.getCountryAlpha2());
                }

                if (updateDTO.getUserIsPublic() != null) {
                    user.setPublic(updateDTO.getUserIsPublic());
                }

                if (updateDTO.getUserPhone() != null) {
                    if (!updateDTO.getUserPhone().equals(user.getPhoneNumber())) {
                        if (userService.existByPhoneNumber(updateDTO.getUserPhone())) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Этот номер телефона занят");
                        }
                        user.setPhoneNumber(updateDTO.getUserPhone());
                    }
                }

                if (updateDTO.getUserImage() != null) {
                    if (!userService.checkValidImage(updateDTO.getUserImage())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Длина ссылки на аватар пользователя превышает допустимый лимит");
                    }
                    user.setImage(updateDTO.getUserImage());
                }

                user = userService.save(user);
                return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserDTO(user));
            } catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/me/updatePassword")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordDTO passwordDTO, Principal principal) {
        if (principal != null && userService.existByLogin(principal.getName())) {
            UserEntity user = userService.getByUsername(principal.getName());
            if (passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
                if (passwordDTO.getNewPassword().length() >= 8) {
                    user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
                    userService.save(user);
                    SecurityContextHolder.clearContext();
                    return ResponseEntity.status(HttpStatus.OK).body("Пароль успешно обновлен и ранее выпущенные токены отозваны.");
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Новый пароль не соответствует требованиям безопасности.");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Пароль не совпадают");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/profiles/{login}")
    public ResponseEntity<?> fetchProfileByLogin(@PathVariable String login, Principal principal) {
        if (principal != null && userService.existByLogin(principal.getName())) {
            try {
                UserEntity fetchingUser = userService.getByUsername(login);
                if (!fetchingUser.isPublic() && !login.equals(principal.getName())) {
                    List<UserEntity> friends = fetchingUser.getFriends();
                    if (!friends.contains(userService.getByUsername(principal.getName()))) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("У вас нет доступа к этому профилю");
                    }
                }
                return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserDTO(fetchingUser));
            } catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
