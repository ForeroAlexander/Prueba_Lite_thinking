package com.forero.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaDTO {
    @NotBlank
    private String nombre;
    private String descripcion;
}