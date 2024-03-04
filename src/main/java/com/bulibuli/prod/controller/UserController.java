package com.bulibuli.prod.controller;

import com.bulibuli.prod.dto.UpdatePasswordDTO;
import com.bulibuli.prod.dto.UpdateUserDTO;
import com.bulibuli.prod.entity.UserEntity;
import com.bulibuli.prod.mapper.UserMapper;
import com.bulibuli.prod.service.CountryService;
import com.bulibuli.prod.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

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
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateUserDTO updateDTO, Principal principal) {
        if (principal != null) {
            try {
                UserEntity user = userService.getByUsername(principal.getName());
                if (updateDTO.getAlpha2() != null) {
                    if (!countryService.existsByAlpha2(updateDTO.getAlpha2())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Страны с кодом " +
                                updateDTO.getAlpha2() + " не существует");
                    }
                    user.setAlpha2(updateDTO.getAlpha2());
                }

                if (updateDTO.getIsPublic() != null) {
                    user.setPublic(updateDTO.getIsPublic());
                }

                if (updateDTO.getPhoneNumber() != null) {
                    if (!updateDTO.getPhoneNumber().equals(user.getPhoneNumber())) {
                        if (userService.existByPhoneNumber(updateDTO.getPhoneNumber())) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Этот номер телефона занят");
                        }
                        user.setPhoneNumber(updateDTO.getPhoneNumber());
                    }
                }

                if (updateDTO.getImage() != null) {
                    if (!userService.checkValidImage(updateDTO.getImage())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Длина ссылки на аватар пользователя превышает допустимый лимит");
                    }
                    user.setImage(updateDTO.getImage());
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
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDTO passwordDTO, Principal principal) {
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
