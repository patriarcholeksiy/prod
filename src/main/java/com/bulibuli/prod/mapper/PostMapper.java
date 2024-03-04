package com.bulibuli.prod.mapper;

import com.bulibuli.prod.dto.PostDTO;
import com.bulibuli.prod.entity.PostEntity;
import com.bulibuli.prod.entity.TagEntity;
import com.bulibuli.prod.service.PostService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostMapper {

    private PostService postService;

    public PostMapper(PostService postService) {
        this.postService = postService;
    }

    public PostDTO toPostDTO(PostEntity post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(post.getId());
        postDTO.setPostContent(post.getContent());
        List<String> tags = new ArrayList<>();
        for (TagEntity tag: post.getTags())
            tags.add(tag.getTag());
        postDTO.setPostTags(tags);
        postDTO.setUserLogin(post.getCreator().getLogin());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setLikesCount(post.getLikes());
        postDTO.setDislikesCount(post.getDislikes());
        return postDTO;
    }

}
