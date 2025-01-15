package com.forero.backend.dto;

import java.math.BigDecimal;

public class ProductoResponseDTO {
    private String codigo;
    private String nombre;
    private String caracteristicas;
    private BigDecimal precio_usd;
    private BigDecimal precio_eur;
    private BigDecimal precio_cop;
    private EmpresaResponseDTO empresa;

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

    public EmpresaResponseDTO getEmpresa() {
        return empresa;
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

    public void setEmpresa(EmpresaResponseDTO empresa) {
        this.empresa = empresa;
    }
}
