package com.oofoodie.backend.models.request;

import com.oofoodie.backend.models.entity.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SignupRequest {

    @NotNull
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    private List<Role> roles;
}
