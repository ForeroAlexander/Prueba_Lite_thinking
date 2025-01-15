package com.forero.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @Column(name = "codigo")
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String caracteristicas;

    @Column(name = "precio_usd", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_usd;

    @Column(name = "precio_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_eur;

    @Column(name = "precio_cop", nullable = false, precision = 15, scale = 2)
    private BigDecimal precio_cop;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_nit", nullable = false)
    private Empresa empresa;

    @ManyToMany
    @JoinTable(
            name = "producto_categoria",
            joinColumns = @JoinColumn(name = "producto_codigo"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();

    @ManyToMany(mappedBy = "productos")
    private Set<Orden> ordenes = new HashSet<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

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

    public Empresa getEmpresa() {
        return empresa;
    }

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    public Set<Orden> getOrdenes() {
        return ordenes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }

    public void setOrdenes(Set<Orden> ordenes) {
        this.ordenes = ordenes;
    }

    // Métodos de utilidad para manejar relaciones
    public void addCategoria(Categoria categoria) {
        this.categorias.add(categoria);
        categoria.getProductos().add(this);
    }

    public void removeCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
        categoria.getProductos().remove(this);
    }

    public void addOrden(Orden orden) {
        this.ordenes.add(orden);
        orden.getProductos().add(this);
    }

    public void removeOrden(Orden orden) {
        this.ordenes.remove(orden);
        orden.getProductos().remove(this);
    }

    // equals y hashCode basados en el código del producto
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return codigo != null && codigo.equals(producto.getCodigo());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
