package com.forero.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class OrdenDTO {
    @NotNull
    private UUID clienteId;

    @NotEmpty
    private Set<String> productosCodigos;

    private BigDecimal total;
}