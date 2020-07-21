package com.oofoodie.backend.models.request.command;

import com.oofoodie.backend.models.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginCommandRequest {

    private String username;

    private String password;

    private Role role;
}
