package com.forero.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTO {
    @NotBlank
    private String nombre;

    @NotBlank
    @Email
    private String email;

    private String telefono;
}