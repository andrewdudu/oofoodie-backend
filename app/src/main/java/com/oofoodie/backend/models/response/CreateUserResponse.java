package com.oofoodie.backend.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserResponse {

    private String name;

    private String email;

    private String password;

    private String username;

    private Integer likes;

    private Integer credits;
}
