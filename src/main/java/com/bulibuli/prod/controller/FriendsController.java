package com.bulibuli.prod.controller;

import com.bulibuli.prod.dto.FriendDTO;
import com.bulibuli.prod.dto.LoginDTO;
import com.bulibuli.prod.entity.UserEntity;
import com.bulibuli.prod.entity.UsersFriendsEntity;
import com.bulibuli.prod.entity.key.UsersFriendsKey;
import com.bulibuli.prod.mapper.UserMapper;
import com.bulibuli.prod.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/friends")
public class FriendsController {

    private UserMapper userMapper;

    private UserService userService;

    public FriendsController(UserMapper userMapper,
                             UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFriends(@RequestBody LoginDTO loginDTO, Principal principal) {
        if (principal != null && userService.existByLogin(principal.getName())) {
            if (userService.existByLogin(loginDTO.getLogin())) {
                if (!loginDTO.getLogin().equals(principal.getName())) {
                    UserEntity user = userService.getByUsername(principal.getName());
                    UserEntity friend = userService.getByUsername(loginDTO.getLogin());
                    if (!user.getFriends().contains(friend)) {
                        UsersFriendsKey key = new UsersFriendsKey();
                        key.setUserId(user.getId());
                        key.setFriendId(friend.getId());
                        UsersFriendsEntity usersFriends = new UsersFriendsEntity();
                        usersFriends.setId(key);
                        usersFriends.setUser(user);
                        usersFriends.setFriend(friend);
                        userService.save(usersFriends);
                    }
                }
                return ResponseEntity.status(HttpStatus.OK).body("ok");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(loginDTO.getLogin() + " не найден");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFriends(@RequestBody LoginDTO loginDTO, Principal principal) {
        if (principal != null && userService.existByLogin(principal.getName())) {
            if (userService.existByLogin(loginDTO.getLogin())) {
                UserEntity user = userService.getByUsername(principal.getName());
                UserEntity friend = userService.getByUsername(loginDTO.getLogin());
                user.getFriends().remove(friend);
                userService.save(user);
                return ResponseEntity.status(HttpStatus.OK).body("ok");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(loginDTO.getLogin() + " не найден");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/get")
    public ResponseEntity<?> getFriends(@RequestParam(defaultValue = "0") int paginationOffset,
                                        @RequestParam(defaultValue = "5") int paginationLimit,
                                        Principal principal) {
        if (principal != null && userService.existByLogin(principal.getName())) {
            UserEntity user = userService.getByUsername(principal.getName());
            List<UsersFriendsEntity> usersFriends = userService.getFriendsByUserId(user.getId(), paginationOffset, paginationLimit);
            List<FriendDTO> friends = new ArrayList<>();
            for (UsersFriendsEntity userFriend: usersFriends) {
                FriendDTO friendDTO = new FriendDTO();
                friendDTO.setLogin(userFriend.getFriend().getLogin());
                friendDTO.setAddedAt(userFriend.getDateTime());
                friends.add(friendDTO);
            }
            return ResponseEntity.status(HttpStatus.OK).body(friends);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
