package uz.md.shopapp.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.md.shopapp.exceptions.BadRequestException;
import uz.md.shopapp.exceptions.NotAllowedException;
import uz.md.shopapp.service.contract.FilesStorageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static uz.md.shopapp.utils.MessageConstants.ERROR_IN_REQUEST_RU;
import static uz.md.shopapp.utils.MessageConstants.ERROR_IN_REQUEST_UZ;

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
            throw NotAllowedException.builder()
                    .messageUz("Faylni yuklab bo'lmadi")
                    .messageRu("")
                    .build();
        }
    }

    @Override
    public void save(MultipartFile file, Path path) {

        if (file == null || path == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();

        try {
            Files.copy(file.getInputStream(), path.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                return;
            }
            throw NotAllowedException.builder()
                    .messageUz(e.getMessage())
                    .messageRu("")
                    .build();
        }
    }

    @Override
    public Resource load(String filename, Path path) {

        if (filename == null || path == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();

        try {
            Path file = path.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw NotAllowedException.builder()
                        .messageUz("Could not read the file!")
                        .messageRu("")
                        .build();
            }
        } catch (MalformedURLException e) {
            throw NotAllowedException.builder()
                    .messageUz("Error: " + e.getMessage())
                    .messageRu("")
                    .build();
        }
    }

    @Override
    public boolean delete(String filename, Path path) {
        try {
            Path file = path.resolve(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw NotAllowedException.builder()
                    .messageUz("Error: " + e.getMessage())
                    .messageRu("")
                    .build();
        }
    }

    @Override
    public void deleteAll(Path path) {
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    @Override
    public Stream<Path> loadAll(Path path) {
        try {
            List<Path> files = new ArrayList<>();
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    files.add(path.relativize(file));
                    return FileVisitResult.CONTINUE;
                }
            });
            return files.stream();
        } catch (IOException e) {
            throw new NotAllowedException("Could not load the files!", e.getMessage());
        }
    }


}
