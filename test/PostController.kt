package com.ilogistic.delivery_admin_backend.test

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
        private val postService: PostService
) {

    @PostMapping("/api/v1/post")
    fun createPost(@RequestBody postDto: PostDto) : ResponseEntity<Post>{
        return ResponseEntity.ok(postService.createPost(postDto))
    }

    @GetMapping("/api/v1/post")
    fun getPostList() : ResponseEntity<List<PostDto>>{
        return ResponseEntity.ok(postService.getPostList())
    }
}
