package com.yousef.social_media_api.services.files;

import com.yousef.social_media_api.exceptions.files.FailedToSaveFile;
import com.yousef.social_media_api.exceptions.files.MaximumFilesReached;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LocalFileService implements FilesService {

    @Value("${uploads.folder}")
    private String uploadsFolder;

    @Value("${uploads.max-count}")
    private Integer maxCount;

    @Override
    public String saveFile(MultipartFile file, AppFileType fileType, String folderName) {
        try {
            final Path folder = Paths.get(uploadsFolder, fileType.getName());

            Files.createDirectories(folder);

            if (fileType == AppFileType.ProfileImage) {
                final Path userFolder = Paths.get(folder.toString(), folderName);
                final Long imageCount = getFilesCount(userFolder);

                if (imageCount >= maxCount) {
                    throw new MaximumFilesReached("You have reached maximum allowed profile images, " +
                            "please delete an image before uploading a new one");
                }
            }

            final Path filePath = Paths.get(folder.toString(), folderName, file.getOriginalFilename());

            file.transferTo(filePath);

            return filePath.toString();
        } catch (IOException e) {
            throw new FailedToSaveFile("Could not save the file: " + file.getOriginalFilename());
        }
    }

    private Long getFilesCount(Path userFolder) {
        try (Stream<Path> files = Files.list(userFolder)) {
            return files.filter(Files::isRegularFile).count();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
