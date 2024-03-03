package com.bulibuli.prod.service;

import com.bulibuli.prod.dto.RegisterDTO;
import com.bulibuli.prod.entity.UserEntity;
import com.bulibuli.prod.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    private CountryService countryService;

    public UserService(UserRepository userRepository, CountryService countryService) {
        this.userRepository = userRepository;
        this.countryService = countryService;
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public UserEntity getByUsername(String username) {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public boolean checkValidRegistration(RegisterDTO registerDTO) {
        return !userRepository.existsByLoginOrEmailOrPhoneNumber(registerDTO.getLogin(),
                registerDTO.getEmail(), registerDTO.getPhoneNumber());
    }

}
