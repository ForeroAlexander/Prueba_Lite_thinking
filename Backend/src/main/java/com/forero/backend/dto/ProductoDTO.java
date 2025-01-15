package com.forero.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class ProductoDTO {
    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "Las características no pueden exceder los 500 caracteres")
    private String caracteristicas;

    @NotNull(message = "El precio en USD es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private BigDecimal precio_usd;

    @NotNull(message = "El precio en EUR es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private BigDecimal precio_eur;

    @NotNull(message = "El precio en COP es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private BigDecimal precio_cop;

    @NotBlank(message = "El NIT de la empresa es obligatorio")
    private String empresa_nit;

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public BigDecimal getPrecio_usd() {
        return precio_usd;
    }

    public BigDecimal getPrecio_eur() {
        return precio_eur;
    }

    public BigDecimal getPrecio_cop() {
        return precio_cop;
    }

    public String getEmpresa_nit() {
        return empresa_nit;
    }

    // Setters
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public void setPrecio_usd(BigDecimal precio_usd) {
        this.precio_usd = precio_usd;
    }

    public void setPrecio_eur(BigDecimal precio_eur) {
        this.precio_eur = precio_eur;
    }

    public void setPrecio_cop(BigDecimal precio_cop) {
        this.precio_cop = precio_cop;
    }

    public void setEmpresa_nit(String empresa_nit) {
        this.empresa_nit = empresa_nit;
    }
}
