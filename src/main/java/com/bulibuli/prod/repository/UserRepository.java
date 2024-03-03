package com.bulibuli.prod.repository;

import com.bulibuli.prod.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);

    boolean existsByLoginOrEmailOrPhoneNumber(@Size(min = 3, max = 30) String login, @Email String email, String phoneNumber);
}
