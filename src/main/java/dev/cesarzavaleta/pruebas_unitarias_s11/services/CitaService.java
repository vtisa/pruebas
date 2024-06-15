package dev.cesarzavaleta.pruebas_unitarias_s11.services;

import dev.cesarzavaleta.pruebas_unitarias_s11.model.Cita;

import java.util.List;

public interface CitaService {
    List<Cita> findAll();
    Cita findById(Long id);
    Cita save(Cita mascota);
    void deleteById(Long id);
}
