package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetImageCommand;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class GetImageCommandImpl implements GetImageCommand {

    @Value("${storage.image-path}")
    private String locationImage;

    @SneakyThrows(FileNotFoundException.class)
    @Override
    public Mono<ResponseEntity<byte[]>> execute(String fileName) {
        return Mono.just(getFileToByte(fileName))
                .map(data -> {
                    HttpHeaders headers = new HttpHeaders();

                    headers.setContentType(MediaType.IMAGE_PNG);

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.IMAGE_PNG))
                            .body(data);
                });

    }

    private byte[] getFileToByte(String fileName) throws FileNotFoundException {
        File file = new File(this.locationImage + "/" + fileName);
        
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);

            return bytes;
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }
}
