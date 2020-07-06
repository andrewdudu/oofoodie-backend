package com.oofoodie.backend.helper;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class GetImageHelper {

    public Mono<byte[]> getFileToByte(String path) {
        File file = new File(path);

        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);

            return Mono.just(bytes);
        } catch (IOException e) {
            return Mono.empty();
        }
    }
}
