package com.bulibuli.prod.mapper;

import com.bulibuli.prod.dto.UserDTO;
import com.bulibuli.prod.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserDTO toUserDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUserLogin(user.getLogin());
        userDTO.setUserEmail(user.getEmail());
        userDTO.setCountryAlpha2(user.getAlpha2());
        userDTO.setUserIsPublic(user.isPublic());
        userDTO.setUserPhone(user.getPhoneNumber());
        userDTO.setUserImage(user.getImage());
        return userDTO;
    }

}
