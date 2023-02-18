package uz.md.shopapp.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.md.shopapp.service.contract.FilesStorageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    @Value("${app.images.institutions.root.path}")
    public String institutionsPath;

    @Value("${app.images.products.root.path}")
    public String productsPath;

    private Path productsImagesRoot;
    private Path institutionsImagesRoot;

    @PostConstruct
    public void initial() {
        productsImagesRoot = Path.of(productsPath);
        institutionsImagesRoot = Path.of(institutionsPath);
    }


    @Override
    public void init() {
        try {
            Files.createDirectories(institutionsImagesRoot);
            Files.createDirectories(productsImagesRoot);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file, Path path) {
        try {
            Files.copy(file.getInputStream(), path.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                return;
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename, Path path) {
        try {
            Path file = path.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String filename, Path path) {
        try {
            Path file = path.resolve(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll(Path path) {
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    @Override
    public Stream<Path> loadAll(Path path) {
        try {
            return Files.walk(path, 1).filter(p -> !p.equals(path)).map(path::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

}
