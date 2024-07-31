package com.ilogistic.delivery_admin_backend.test

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
@RequiredArgsConstructor
class PostService(
        private val postRepository: PostRepository
){

    fun createPost(postDto: PostDto) : Post {
        return postRepository.save(postDto.toEntity())
    }

    fun getPostList() : List<PostDto>{
        return postRepository.findAll().stream().map(PostDto::of).collect(Collectors.toList())
    }
}
