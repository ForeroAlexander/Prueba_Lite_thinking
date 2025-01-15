package com.forero.backend.repository;

import com.forero.backend.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface OrdenRepository extends JpaRepository<Orden, UUID> {
    List<Orden> findByClienteId(UUID clienteId);
}