package com.yousef.social_media_api.services.files;

import com.yousef.social_media_api.exceptions.files.FailedToSaveFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalFileService implements FilesService {

    @Value("${uploads.folder}")
    private String uploadsFolder;

    @Value("${uploads.max-count}")
    private Integer maxCount;

    @Override
    public String saveFile(MultipartFile file, AppFileType fileType, String folderName) {
        final Path folder = Paths.get(uploadsFolder, fileType.getName(), folderName);

        try {

            Files.createDirectories(folder);

            if (fileType == AppFileType.ProfileImage) {

                final Long imageCount = getFilesCount(folder);

                if (imageCount >= 1) {
                    forceDeleteDirectory(folder);
                    Files.createDirectories(folder);
                }
            }

            final Path filePath = Paths.get(folder.toString(), file.getOriginalFilename());

            file.transferTo(filePath);

            return filePath.toString();
        }  catch (IOException e) {
            log.error(e.getMessage());
            throw new FailedToSaveFile("Could not save the file: " + file.getOriginalFilename());
        }
    }

    private Long getFilesCount(Path userFolder) {
        try (Stream<Path> files = Files.list(userFolder)) {
            return files.filter(Files::isRegularFile).count();
        } catch (IOException e) {
            throw new FailedToSaveFile("Could not save the file: " + e.getMessage());
        }
    }

    public static void forceDeleteDirectory(Path path) {
        if (Files.notExists(path)) return;

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new FailedToSaveFile("Could not save the file: " + e.getMessage());
        }
    }
}
