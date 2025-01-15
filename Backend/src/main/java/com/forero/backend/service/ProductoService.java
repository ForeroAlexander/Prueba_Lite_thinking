package com.forero.backend.service;

import com.forero.backend.dto.ProductoDTO;
import com.forero.backend.dto.ProductoResponseDTO;
import com.forero.backend.model.Producto;
import com.forero.backend.model.Empresa;
import com.forero.backend.repository.ProductoRepository;
import com.forero.backend.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final EmpresaRepository empresaRepository;
    private final EmpresaService empresaService;

    public ProductoService(ProductoRepository productoRepository,
                           EmpresaRepository empresaRepository,
                           EmpresaService empresaService) {
        this.productoRepository = productoRepository;
        this.empresaRepository = empresaRepository;
        this.empresaService = empresaService;
    }

    public List<ProductoResponseDTO> obtenerTodosLosProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProductoResponseDTO obtenerProductoPorCodigo(String codigo) {
        Producto producto = productoRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con código: " + codigo));
        return convertirADTO(producto);
    }

    @Transactional
    public ProductoResponseDTO crearProducto(ProductoDTO productoDTO) {
        Empresa empresa = empresaRepository.findById(productoDTO.getEmpresa_nit())
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada con NIT: " + productoDTO.getEmpresa_nit()));

        Producto producto = new Producto();
        copiarDTOaProducto(productoDTO, producto, empresa);

        Producto productoGuardado = productoRepository.save(producto);
        return convertirADTO(productoGuardado);
    }

    @Transactional
    public ProductoResponseDTO actualizarProducto(String codigo, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con código: " + codigo));

        Empresa empresa = empresaRepository.findById(productoDTO.getEmpresa_nit())
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada con NIT: " + productoDTO.getEmpresa_nit()));

        copiarDTOaProducto(productoDTO, producto, empresa);

        Producto productoActualizado = productoRepository.save(producto);
        return convertirADTO(productoActualizado);
    }

    @Transactional
    public void eliminarProducto(String codigo) {
        if (!productoRepository.existsById(codigo)) {
            throw new EntityNotFoundException("Producto no encontrado con código: " + codigo);
        }
        productoRepository.deleteById(codigo);
    }

    private ProductoResponseDTO convertirADTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setCodigo(producto.getCodigo());
        dto.setNombre(producto.getNombre());
        dto.setCaracteristicas(producto.getCaracteristicas());
        dto.setPrecio_usd(producto.getPrecio_usd());
        dto.setPrecio_eur(producto.getPrecio_eur());
        dto.setPrecio_cop(producto.getPrecio_cop());
        dto.setEmpresa(empresaService.convertirADTO(producto.getEmpresa()));
        return dto;
    }

    private void copiarDTOaProducto(ProductoDTO dto, Producto producto, Empresa empresa) {
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setCaracteristicas(dto.getCaracteristicas());
        producto.setPrecio_usd(dto.getPrecio_usd());
        producto.setPrecio_eur(dto.getPrecio_eur());
        producto.setPrecio_cop(dto.getPrecio_cop());
        producto.setEmpresa(empresa);
    }
}
