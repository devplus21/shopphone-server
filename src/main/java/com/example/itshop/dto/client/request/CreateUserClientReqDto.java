package com.example.itshop.dto.client.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class CreateUserClientReqDto implements Serializable {
    @Email
    private String email;

    @NotBlank
    @Length(min = 6)
    private String password;
}
