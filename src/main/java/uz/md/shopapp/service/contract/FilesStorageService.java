package uz.md.shopapp.service.contract;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    void init();

    void save(MultipartFile file, Path path);

    Resource load(String filename, Path path);

    boolean delete(String filename, Path path);

    void deleteAll(Path path);

    Stream<Path> loadAll(Path path);
}
