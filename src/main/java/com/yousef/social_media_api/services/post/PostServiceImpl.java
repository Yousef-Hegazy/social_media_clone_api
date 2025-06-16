package com.yousef.social_media_api.services.post;

import com.yousef.social_media_api.dtos.post.CreatePostRequest;
import com.yousef.social_media_api.dtos.post.PostResponse;
import com.yousef.social_media_api.exceptions.post.PostException;
import com.yousef.social_media_api.helpers.PagedResponse;
import com.yousef.social_media_api.models.auth.AppUser;
import com.yousef.social_media_api.models.post.Post;
import com.yousef.social_media_api.repositories.auth.AppUserRepository;
import com.yousef.social_media_api.repositories.post.PostRepository;
import com.yousef.social_media_api.services.files.AppFileType;
import com.yousef.social_media_api.services.files.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AppUserRepository userRepository;
    private final FilesService filesService;

    @Override
    public PostResponse createPost(CreatePostRequest post, MultipartFile image, Authentication auth) {
        final AppUser authUser = (AppUser) auth.getPrincipal();

        final AppUser user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("The authenticated user doesn't exist... Which is weird"));

        final Post postToAdd = new Post();
        final String imageUrl = filesService.saveFile(image, AppFileType.PostsImages, user.getId().toString());

        postToAdd.setText(post.text());
        postToAdd.setUser(user);
        postToAdd.setImageUrl(imageUrl);

        final Post createdPost = postRepository.save(postToAdd);

        return mapPostToDto(createdPost);
    }

    @Override
    public PagedResponse<PostResponse> getAllPosts(int page, int pageSize) {
        if (page > 0) page -= 1;

        final Pageable req = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        final Page<Post> posts = postRepository.findAll(req);

        final Page<PostResponse> dto = posts.map(this::mapPostToDto);

        return new PagedResponse<>(dto);
    }

    @Override
    public Void deletePost(UUID id, Authentication auth) {
        final AppUser user = (AppUser) auth.getPrincipal();

        final Post postToDelete = postRepository
                .findById(id)
                .orElseThrow(() -> new PostException("Post with the given id can't be found"));

        if (!postToDelete.getUser().getId().equals(user.getId())) {
            throw new PostException("You can't delete a post that isn't yours");
        }

        filesService.deleteFile(postToDelete.getImageUrl());

        postRepository.delete(postToDelete);

        return null;
    }

    private PostResponse mapPostToDto(Post p) {

        return PostResponse.builder()
                .id(p.getId())
                .text(p.getText())
                .imageUrl(p.getImageUrl())
                .userId(p.getUser().getId())
                .username(p.getUser().getName())
                .userProfileImageUrl(p.getUser().getProfile() == null ? "" : p.getUser().getProfile().getImageUrl())
                .timestamp(p.getCreatedAt())
                .build();
    }
}
