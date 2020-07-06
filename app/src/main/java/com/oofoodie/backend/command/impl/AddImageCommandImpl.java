package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.AddImageCommand;
import com.oofoodie.backend.helper.StoreImageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AddImageCommandImpl implements AddImageCommand {

    @Value("${storage.image-path}")
    private String locationImage;

    @Autowired
    private StoreImageHelper storeImageHelper;

    @Override
    public Mono<String> execute(String fileStr) {
        return Mono.fromCallable(() -> storeImageHelper.store(fileStr, UUID.randomUUID().toString() + ".png", this.locationImage));
    }
}
