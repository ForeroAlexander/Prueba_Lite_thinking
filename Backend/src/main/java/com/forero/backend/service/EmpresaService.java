package com.forero.backend.service;

import com.forero.backend.dto.EmpresaDTO;
import com.forero.backend.dto.EmpresaResponseDTO;
import com.forero.backend.model.Empresa;
import com.forero.backend.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class EmpresaService {
    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public List<EmpresaResponseDTO> obtenerTodasLasEmpresas() {
        return empresaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public EmpresaResponseDTO obtenerEmpresaPorNit(String nit) {
        Empresa empresa = empresaRepository.findById(nit)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada con NIT: " + nit));
        return convertirADTO(empresa);
    }

    @Transactional
    public EmpresaResponseDTO crearEmpresa(EmpresaDTO empresaDTO) {
        Empresa empresa = new Empresa();
        copiarDTOaEmpresa(empresaDTO, empresa);
        Empresa empresaGuardada = empresaRepository.save(empresa);
        return convertirADTO(empresaGuardada);
    }

    @Transactional
    public EmpresaResponseDTO actualizarEmpresa(String nit, EmpresaDTO empresaDTO) {
        Empresa empresa = empresaRepository.findById(nit)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada con NIT: " + nit));
        copiarDTOaEmpresa(empresaDTO, empresa);
        Empresa empresaActualizada = empresaRepository.save(empresa);
        return convertirADTO(empresaActualizada);
    }

    @Transactional
    public void eliminarEmpresa(String nit) {
        if (!empresaRepository.existsById(nit)) {
            throw new EntityNotFoundException("Empresa no encontrada con NIT: " + nit);
        }
        empresaRepository.deleteById(nit);
    }

    public EmpresaResponseDTO convertirADTO(Empresa empresa) {
        EmpresaResponseDTO dto = new EmpresaResponseDTO();
        dto.setNit(empresa.getNit());
        dto.setNombre(empresa.getNombre());
        dto.setDireccion(empresa.getDireccion());
        dto.setTelefono(empresa.getTelefono());
        return dto;
    }

    private void copiarDTOaEmpresa(EmpresaDTO dto, Empresa empresa) {
        empresa.setNit(dto.getNit());
        empresa.setNombre(dto.getNombre());
        empresa.setDireccion(dto.getDireccion());
        empresa.setTelefono(dto.getTelefono());
    }
}
