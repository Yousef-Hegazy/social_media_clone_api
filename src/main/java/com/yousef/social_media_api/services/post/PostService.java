package com.yousef.social_media_api.services.post;

import com.yousef.social_media_api.dtos.post.CreatePostRequest;
import com.yousef.social_media_api.dtos.post.PostResponse;
import com.yousef.social_media_api.helpers.PagedResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    PostResponse createPost(CreatePostRequest post, MultipartFile image, Authentication auth);

    PagedResponse<PostResponse> getAllPosts(int page, int pageSize);
}
