package com.yousef.social_media_api.services.files;

import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    String saveFile(MultipartFile file, AppFileType fileType, String folderName);
}
