package com.cake.easytrade.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path rootLocation = Paths.get("uploads");

    public String storeFile(MultipartFile file, String directory) {
        try {
            Path uploadPath = rootLocation.resolve(directory).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return directory + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Error: " + e.getMessage(), e);
        }
    }
}
