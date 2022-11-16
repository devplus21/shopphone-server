package com.example.itshop.dto.client.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
public class UpdateUserClientReqDto implements Serializable {
    @Email
    private String email;

    @Size(min = 6)
    private String password;

    private String name;
}
