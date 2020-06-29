package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.AddImageCommandImpl;
import com.oofoodie.backend.helper.StoreImageHelper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class AddImageCommandImplTest {

    @InjectMocks
    private AddImageCommandImpl command;

    @Mock
    private StoreImageHelper storeImageHelper;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void executeTest() {
        when(storeImageHelper.store(anyString(), anyString(), any())).thenReturn(anyString());
        StepVerifier.create(command.execute(anyString()))
                .expectNext(anyString())
                .verifyComplete();
        verify(storeImageHelper).store(anyString(), anyString(), any());
    }
}
