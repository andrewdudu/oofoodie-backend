package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetImageCommandImpl;
import com.oofoodie.backend.helper.GetImageHelper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.FileNotFoundException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class GetImageCommandImplTest {

    private byte[] imgResponse = new byte[0];

    @InjectMocks
    private GetImageCommandImpl command;

    @Mock
    private GetImageHelper getImageHelper;

    @Before
    public void setUp() {
        initMocks(this);
    }

    private ResponseEntity<byte[]> constructByteResponse() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.IMAGE_PNG))
                .body(imgResponse);
    }

    @Test
    public void executeTest() {
        when(getImageHelper.getFileToByte(anyString())).thenReturn(Mono.just(imgResponse));
        StepVerifier.create(command.execute(anyString()))
                .expectNext(constructByteResponse())
                .verifyComplete();
        verify(getImageHelper).getFileToByte(anyString());
    }

    @Test
    public void executeNotFoundTest() {
        when(getImageHelper.getFileToByte(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(command.execute(anyString()))
                .expectError(FileNotFoundException.class)
                .verify();
        verify(getImageHelper).getFileToByte(anyString());
    }
}
