package com.oofoodie.backend.models.request.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeenThereCommandRequest {

    private String restoId;

    private String username;
}
