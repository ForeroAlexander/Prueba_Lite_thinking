package com.forero.backend.service;

import com.forero.backend.dto.OrdenDTO;
import com.forero.backend.model.Cliente;
import com.forero.backend.model.Orden;
import com.forero.backend.model.Producto;
import com.forero.backend.repository.ClienteRepository;
import com.forero.backend.repository.OrdenRepository;
import com.forero.backend.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdenService {
    private final OrdenRepository ordenRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public List<Orden> obtenerTodasLasOrdenes() {
        return ordenRepository.findAll();
    }

    public Orden obtenerOrdenPorId(UUID id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada con ID: " + id));
    }

    public List<Orden> obtenerOrdenesPorCliente(UUID clienteId) {
        return ordenRepository.findByClienteId(clienteId);
    }

    @Transactional
    public Orden crearOrden(OrdenDTO ordenDTO) {
        Cliente cliente = clienteRepository.findById(ordenDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + ordenDTO.getClienteId()));

        Set<Producto> productos = new HashSet<>();
        for (String codigo : ordenDTO.getProductosCodigos()) {
            Producto producto = productoRepository.findById(codigo)
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con código: " + codigo));
            productos.add(producto);
        }

        Orden orden = new Orden();
        orden.setCliente(cliente);
        orden.setProductos(productos);
        orden.setFecha(LocalDateTime.now());
        orden.setTotal(ordenDTO.getTotal());

        return ordenRepository.save(orden);
    }

    @Transactional
    public void eliminarOrden(UUID id) {
        if (!ordenRepository.existsById(id)) {
            throw new EntityNotFoundException("Orden no encontrada con ID: " + id);
        }
        ordenRepository.deleteById(id);
    }
}