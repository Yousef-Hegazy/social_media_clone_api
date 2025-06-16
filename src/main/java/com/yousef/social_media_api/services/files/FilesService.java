package com.yousef.social_media_api.services.files;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FilesService {
    String saveFile(MultipartFile file, AppFileType fileType, String folderName);
    void deleteFolder(AppFileType fileType, String folderName);
    void deleteFile(String path);
}
