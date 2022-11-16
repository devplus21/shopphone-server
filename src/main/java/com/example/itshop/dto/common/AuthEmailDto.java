package com.example.itshop.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthEmailDto {
    private String toEmail;
    private String link;
}
