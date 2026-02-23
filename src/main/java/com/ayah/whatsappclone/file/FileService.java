package com.ayah.whatsappclone.file;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
public class FileService {

    @Value("${application.file.uploads.media-output-path}")
    private String fileUploadPath;

    public String saveFile(
            MultipartFile sourceFile,
            String userId) {

        // TODO: 23/02/2026 validate file, userId are not null
        final String fileUploadedSubPath = "users" + File.separator + userId;
        return uploadFile(sourceFile, fileUploadedSubPath);

    }

    private String uploadFile(MultipartFile sourceFile, String fileUploadedSubPath) {

        final String finalUploadedPath = fileUploadPath + File.separator + fileUploadedSubPath;
        File targetFolder = new File(finalUploadedPath);

        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdir();
            if (!folderCreated) {
                log.warn("Failed to create the targetPath folder, {}", targetFolder);
                return null;
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());

        String targetFilePath = finalUploadedPath + File.separator + System.currentTimeMillis() + fileExtension;

        Path targetPath = Paths.get(targetFilePath);

        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File saved to {}", targetPath);
            return targetFilePath;
        } catch (IOException e) {
            log.error("File was not save", e);
        }
        return null;
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }

        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

}
