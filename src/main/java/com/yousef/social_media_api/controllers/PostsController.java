package com.yousef.social_media_api.controllers;

import com.yousef.social_media_api.dtos.post.CreatePostRequest;
import com.yousef.social_media_api.dtos.post.PostResponse;
import com.yousef.social_media_api.helpers.PagedResponse;
import com.yousef.social_media_api.services.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {
    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> createPost(
            @RequestPart("post") @Valid CreatePostRequest post,
            @RequestPart("image") MultipartFile image,
            Authentication auth
    ) {
        return ResponseEntity.ok(postService.createPost(post, image, auth));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<PostResponse>> getPosts(@RequestParam(name = "page") int page, @RequestParam(name = "pageSize") int pageSize) {
        return ResponseEntity.ok(postService.getAllPosts(page, pageSize));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id, Authentication auth) {
        return ResponseEntity.ok(postService.deletePost(id, auth));
    }
}
