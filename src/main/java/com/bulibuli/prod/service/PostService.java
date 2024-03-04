package com.bulibuli.prod.service;

import com.bulibuli.prod.entity.PostEmotionsEntity;
import com.bulibuli.prod.entity.PostEntity;
import com.bulibuli.prod.entity.TagEntity;
import com.bulibuli.prod.entity.UsersPostsEntity;
import com.bulibuli.prod.repository.PostEmotionsRepository;
import com.bulibuli.prod.repository.PostRepository;
import com.bulibuli.prod.repository.TagsRepository;
import com.bulibuli.prod.repository.UsersPostsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private TagsRepository tagsRepository;

    private PostRepository postRepository;

    private UsersPostsRepository usersPostsRepository;

    private PostEmotionsRepository postEmotionsRepository;

    public PostService(TagsRepository tagsRepository,
                       PostRepository postRepository,
                       UsersPostsRepository usersPostsRepository,
                       PostEmotionsRepository postEmotionsRepository) {
        this.tagsRepository = tagsRepository;
        this.postRepository = postRepository;
        this.usersPostsRepository = usersPostsRepository;
        this.postEmotionsRepository = postEmotionsRepository;
    }

    public PostEntity save(PostEntity post) {
        return postRepository.save(post);
    }

    public TagEntity save(TagEntity tag) {
        return tagsRepository.save(tag);
    }

    public UsersPostsEntity save(UsersPostsEntity usersPosts) {
        return usersPostsRepository.save(usersPosts);
    }

    public PostEmotionsEntity save(PostEmotionsEntity postEmotions) {
        return postEmotionsRepository.save(postEmotions);
    }

    public PostEntity getById(Long id) {
        return postRepository.getById(id);
    }

    public PostEmotionsEntity getPostEmotionByUserIdAndPostId(Long userId, Long postId) {
        return postEmotionsRepository.getByUser_IdAndPost_Id(userId, postId).orElse(new PostEmotionsEntity());
    }

    public List<PostEntity> gelAllByCreatorId(Long creatorId, int pageNum, int pageSize) {
        return postRepository.getAllByCreator_IdOrderByCreatedAtDesc(creatorId, PageRequest.of(pageNum, pageSize));
    }

    public int countOfLikesOnPost(Long postId) {
        return postEmotionsRepository.countAllByPost_IdAndLikes(postId, true);
    }

    public int countOfDislikesOnPost(Long postId) {
        return postEmotionsRepository.countAllByPost_IdAndDislikes(postId, true);
    }

    public boolean existById(Long id) {
        return postRepository.existsById(id);
    }

}
