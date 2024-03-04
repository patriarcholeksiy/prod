package com.bulibuli.prod.mapper;

import com.bulibuli.prod.dto.UserDTO;
import com.bulibuli.prod.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapper {

    public UserDTO toUserDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLogin(user.getLogin());
        userDTO.setEmail(user.getEmail());
        userDTO.setAlpha2(user.getAlpha2());
        userDTO.setPublic(user.isPublic());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setImage(user.getImage());
        return userDTO;
    }

}
