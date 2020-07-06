package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetImageCommand;
import com.oofoodie.backend.helper.GetImageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;

@Service
public class GetImageCommandImpl implements GetImageCommand {

    @Value("${storage.image-path}")
    private String locationImage;

    @Autowired
    private GetImageHelper getImageHelper;

    @Override
    public Mono<ResponseEntity<byte[]>> execute(String fileName) {
        return getImageHelper.getFileToByte(this.locationImage + "/" + fileName)
                .map(data -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.IMAGE_PNG))
                        .body(data))
                .switchIfEmpty(Mono.error(new FileNotFoundException("File Not Found")));

    }
}
