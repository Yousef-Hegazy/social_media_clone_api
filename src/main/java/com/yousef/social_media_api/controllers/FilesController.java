package com.yousef.social_media_api.controllers;

import com.yousef.social_media_api.services.files.AppFileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FilesController {
    @Value("${uploads.folder}")
    private String uploadsFolder;

    @GetMapping("/{type}/{folder}/{filename:.+}")
    public ResponseEntity<?> getFile(@PathVariable String type, @PathVariable String folder, @PathVariable String filename) throws IOException {
        final List<String> availableTypes = Arrays.stream(AppFileType.values()).map(AppFileType::getName).toList();

        if (type == null || !availableTypes.contains(type) || folder == null || filename == null) {
            return ResponseEntity.badRequest().build();
        }

        final Path filePath = Paths.get(uploadsFolder, type, folder, filename);

        if (Files.notExists(filePath)) {
            return ResponseEntity.badRequest().build();
        }

        String contentType = Files.probeContentType(filePath);

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(new UrlResource(filePath.toUri()));
    }

}
