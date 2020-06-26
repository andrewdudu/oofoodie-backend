package com.oofoodie.backend.helper;

import com.oofoodie.backend.storage.Base64DecodedMultipartFile;
import com.oofoodie.backend.storage.StorageException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

@Component
public class StoreImageHelper {

    public String store(String fileStr, String fileName, Path path) {
        Base64DecodedMultipartFile file;

        byte[] decodedString = Base64.getDecoder().decode(fileStr);
        file = new Base64DecodedMultipartFile(decodedString);
        String filename = StringUtils.cleanPath(fileName);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }

        return filename;
    }
}
