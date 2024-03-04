package com.bulibuli.prod.controller;

import com.bulibuli.prod.dto.CreatePostDTO;
import com.bulibuli.prod.dto.PostDTO;
import com.bulibuli.prod.entity.*;
import com.bulibuli.prod.entity.key.UsersPostsKey;
import com.bulibuli.prod.mapper.PostMapper;
import com.bulibuli.prod.service.PostService;
import com.bulibuli.prod.service.UserService;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/posts")
public class PostController {

    private PostService postService;

    private PostMapper postMapper;

    private UserService userService;

    public PostController(PostService postService,
                          PostMapper postMapper,
                          UserService userService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.userService = userService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createPost(@RequestBody CreatePostDTO createPostDTO, Principal principal) {
        if (principal != null && userService.existByLogin(principal.getName())) {
            UserEntity user = userService.getByUsername(principal.getName());
            PostEntity post = new PostEntity();
            post.setContent(createPostDTO.getContent());
            Set<TagEntity> tags = new HashSet<>();
            for (String tag: createPostDTO.getTags()) {
                TagEntity tagEntity = new TagEntity();
                tagEntity.setTag(tag);
                postService.save(tagEntity);
                tags.add(tagEntity);
            }
            post.setTags(tags);
            post.setCreator(user);
            post = postService.save(post);
            UsersPostsKey key = new UsersPostsKey();
            key.setUserId(user.getId());
            key.setPostId(post.getId());
            UsersPostsEntity usersPosts = new UsersPostsEntity();
            usersPosts.setId(key);
            usersPosts.setUser(user);
            usersPosts.setPost(post);
            postService.save(usersPosts);
            return ResponseEntity.status(HttpStatus.OK).body(postMapper.toPostDTO(post));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{postId}")
    private ResponseEntity<?> getPostById(@PathVariable Long postId, Principal principal) {
        if (principal != null && userService.existByLogin(principal.getName())) {
            if (postService.existById(postId)) {
                PostEntity post = postService.getById(postId);
                UserEntity user = userService.getByUsername(principal.getName());
                if (!post.getCreator().isPublic() && !post.getCreator().getLogin().equals(principal.getName())) {
                    if (post.getCreator().getFriends().contains(user)) {
                        return ResponseEntity.status(HttpStatus.OK).body(postMapper.toPostDTO(post));
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                return ResponseEntity.status(HttpStatus.OK).body(postMapper.toPostDTO(post));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/feed/my")
    public ResponseEntity<?> getMyPosts(@RequestParam(defaultValue = "0") int paginationOffset,
                                        @RequestParam(defaultValue = "5") int paginationLimit,
                                        Principal principal) {
        if (principal != null && userService.existByLogin(principal.getName())) {
            UserEntity user = userService.getByUsername(principal.getName());
            List<PostEntity> posts = postService.gelAllByCreatorId(user.getId(), paginationOffset, paginationLimit);
            List<PostDTO> postsDTO = posts.stream().map(post -> postMapper.toPostDTO(post)).toList();
            return ResponseEntity.status(HttpStatus.OK).body(postsDTO);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/feed/{login}")
    public ResponseEntity<?> getPostsByLogin(@PathVariable String login,
                                             @RequestParam(defaultValue = "0") int paginationOffset,
                                             @RequestParam(defaultValue = "5") int paginationLimit,
                                             Principal principal) {
        if (principal != null && userService.existByLogin(principal.getName())) {
            if (userService.existByLogin(login)) {
                UserEntity user = userService.getByUsername(principal.getName());
                UserEntity creator = userService.getByUsername(login);
                if (creator.isPublic() || creator.getFriends().contains(user)){
                    List<PostEntity> posts = postService.gelAllByCreatorId(creator.getId(), paginationOffset, paginationLimit);
                    List<PostDTO> postsDTO = posts.stream().map(post -> postMapper.toPostDTO(post)).toList();
                    return ResponseEntity.status(HttpStatus.OK).body(postsDTO);
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> putLikeOnPost(@PathVariable Long postId, Principal principal) {
        return putEmotionOnPost(postId, principal, "like");
    }

    @PostMapping("/{postId}/dislike")
    public ResponseEntity<?> putDislikeOnPost(@PathVariable Long postId, Principal principal) {
        return putEmotionOnPost(postId, principal, "dislike");
    }

    private ResponseEntity<?> putEmotionOnPost(Long postId, Principal principal, String emotion) {
        if (principal != null && userService.existByLogin(principal.getName())) {
            if (postService.existById(postId)) {
                PostEntity post = postService.getById(postId);
                UserEntity user = userService.getByUsername(principal.getName());
                UserEntity creator = post.getCreator();
                if (creator.isPublic() || creator.getFriends().contains(user)){
                    PostEmotionsEntity postEmotion = postService.getPostEmotionByUserIdAndPostId(user.getId(), postId);
                    UsersPostsKey key = new UsersPostsKey();
                    key.setUserId(user.getId());
                    key.setPostId(postId);
                    postEmotion.setId(key);
                    postEmotion.setUser(user);
                    postEmotion.setPost(post);
                    if (emotion.equals("like")) {
                        postEmotion.setLikes(true);
                    } else if (emotion.equals("dislike")) {
                        postEmotion.setDislikes(true);
                    }
                    postService.save(postEmotion);
                    post.setLikes(postService.countOfLikesOnPost(postId));
                    post.setDislikes(postService.countOfDislikesOnPost(postId));
                    post = postService.save(post);
                    return ResponseEntity.status(HttpStatus.OK).body(postMapper.toPostDTO(post));
                }

            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
