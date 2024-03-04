package com.bulibuli.prod.service;

import com.bulibuli.prod.dto.RegisterDTO;
import com.bulibuli.prod.entity.UserEntity;
import com.bulibuli.prod.entity.UsersFriendsEntity;
import com.bulibuli.prod.repository.UserRepository;
import com.bulibuli.prod.repository.UsersFriendsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;

    private CountryService countryService;

    private UsersFriendsRepository usersFriendsRepository;

    public UserService(UserRepository userRepository,
                       CountryService countryService,
                       UsersFriendsRepository usersFriendsRepository) {
        this.userRepository = userRepository;
        this.countryService = countryService;
        this.usersFriendsRepository = usersFriendsRepository;
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UsersFriendsEntity save(UsersFriendsEntity usersFriends) {
        return usersFriendsRepository.save(usersFriends);
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public UserEntity getByUsername(String username) {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public List<UsersFriendsEntity> getFriendsByUserId(Long userId, int pageNum, int pageSize) {
        return usersFriendsRepository.getAllByUser_IdOrderByDateTimeDesc(userId, PageRequest.of(pageNum, pageSize));
    }

    public boolean checkValidRegistration(RegisterDTO registerDTO) {
        return !userRepository.existsByLoginOrEmailOrPhoneNumber(registerDTO.getLogin(),
                registerDTO.getEmail(), registerDTO.getPhoneNumber());
    }

    public boolean checkValidImage(String image) {
        return image.length() <= 40;
    }

    public boolean existByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public boolean existByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

}
