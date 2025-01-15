package com.forero.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventarioDTO {
    @NotBlank
    @Email
    private String emailDestino;
    private String formatoReporte;
}