package dev.cesarzavaleta.pruebas_unitarias_s11.controllers;

import dev.cesarzavaleta.pruebas_unitarias_s11.dtos.HistorialMedicoDTO;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.Cita;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.Cliente;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.HistorialMedico;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.CitaService;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.HistorialMedicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historial_medico")
public class HistorialMedicoController {

    private final HistorialMedicoService historialMedicoService;
    private final CitaService citaService;
    public HistorialMedicoController(HistorialMedicoService historialMedicoService, CitaService citaService) {
        this.historialMedicoService = historialMedicoService;
        this.citaService = citaService;
    }

    @GetMapping
    public ResponseEntity<List<HistorialMedico>> getAll() {
        return ResponseEntity.ok(historialMedicoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialMedico> getById(@PathVariable Long id) {
        return ResponseEntity.ok(historialMedicoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<HistorialMedico> create(@RequestBody HistorialMedicoDTO historialMedicoDTO) {
        Cita cita = citaService.findById(historialMedicoDTO.getCita());
        HistorialMedico historialMedico = new HistorialMedico();
        historialMedico.setDiagnostico(historialMedicoDTO.getDiagnostico());
        historialMedico.setTratamiento(historialMedicoDTO.getTratamiento());
        historialMedico.setObservaciones(historialMedicoDTO.getObservaciones());
        historialMedico.setCita(cita);
        return ResponseEntity.ok(historialMedicoService.save(historialMedico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistorialMedico> update(@PathVariable Long id, @RequestBody HistorialMedicoDTO historialMedicoDTO) {
        HistorialMedico existingHistorialMedico = historialMedicoService.findById(id);
        if (existingHistorialMedico != null) {
            Cita cita = citaService.findById(historialMedicoDTO.getCita());
            existingHistorialMedico.setDiagnostico(historialMedicoDTO.getDiagnostico());
            existingHistorialMedico.setTratamiento(historialMedicoDTO.getTratamiento());
            existingHistorialMedico.setObservaciones(historialMedicoDTO.getObservaciones());
            existingHistorialMedico.setCita(cita);
            // update other fields as necessary
            return ResponseEntity.ok(historialMedicoService.save(existingHistorialMedico));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        historialMedicoService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

