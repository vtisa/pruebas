package dev.cesarzavaleta.pruebas_unitarias_s11.controllers;

import dev.cesarzavaleta.pruebas_unitarias_s11.dtos.CitaDTO;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.Cita;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.Mascota;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.CitaService;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.MascotaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;
    private final MascotaService mascotaService;

    public CitaController(CitaService citaService, MascotaService mascotaService) {
        this.citaService = citaService;
        this.mascotaService = mascotaService;
    }

    @GetMapping
    public List<Cita> getAllCitas() {
        return citaService.findAll();
    }

    @GetMapping("/{id}")
    public Cita getCitaById(@PathVariable Long id) {
        return citaService.findById(id);
    }

    @PostMapping
    public Cita createCita(@RequestBody CitaDTO cita) {
        Mascota mascota = mascotaService.findById(cita.getMascota());
        Cita newCita = new Cita();
        newCita.setFechaHora(cita.getFechaHora());
        newCita.setMotivoConsulta(cita.getMotivoConsulta());
        newCita.setMascota(mascota);
        return citaService.save(newCita);
    }

    @PutMapping("/{id}")
    public Cita updateCita(@PathVariable Long id, @RequestBody CitaDTO cita) {
        Cita existingCita = citaService.findById(id);
        if (existingCita != null) {
            Mascota mascota = mascotaService.findById(cita.getMascota());
            existingCita.setFechaHora(cita.getFechaHora());
            existingCita.setMotivoConsulta(cita.getMotivoConsulta());
            existingCita.setMascota(mascota);
            return citaService.save(existingCita);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCita(@PathVariable Long id) {
        citaService.deleteById(id);
    }
}

