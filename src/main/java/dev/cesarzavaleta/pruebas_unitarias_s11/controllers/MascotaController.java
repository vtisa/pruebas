package dev.cesarzavaleta.pruebas_unitarias_s11.controllers;

import dev.cesarzavaleta.pruebas_unitarias_s11.dtos.MascotaDTO;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.Cliente;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.Mascota;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.ClienteService;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;
    private final ClienteService clienteService;

    public MascotaController(MascotaService mascotaService, ClienteService clienteService) {
        this.mascotaService = mascotaService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Mascota> getAllMascotas() {
        return mascotaService.findAll();
    }

    @GetMapping("/{id}")
    public Mascota getMascotaById(@PathVariable Long id) {
        return mascotaService.findById(id);
    }

    @PostMapping
    public Mascota createMascota(@RequestBody MascotaDTO mascota) {
        Cliente cliente = clienteService.findById(mascota.getCliente());
        Mascota mascotaEntity = new Mascota();
        mascotaEntity.setNombre(mascota.getNombre());
        mascotaEntity.setEspecie(mascota.getEspecie());
        mascotaEntity.setFechaNacimiento(mascota.getFechaNacimiento());
        mascotaEntity.setRaza(mascota.getRaza());
        mascotaEntity.setSexo(mascota.getSexo());
        mascotaEntity.setCliente(cliente);
        return mascotaService.save(mascotaEntity);
    }

    @PutMapping("/{id}")
    public Mascota updateMascota(@PathVariable Long id, @RequestBody MascotaDTO mascota) {
        Mascota existingMascota = mascotaService.findById(id);
        if (existingMascota != null) {
            Cliente cliente = clienteService.findById(mascota.getCliente());
            existingMascota.setNombre(mascota.getNombre());
            existingMascota.setEspecie(mascota.getEspecie());
            existingMascota.setFechaNacimiento(mascota.getFechaNacimiento());
            existingMascota.setRaza(mascota.getRaza());
            existingMascota.setSexo(mascota.getSexo());
            existingMascota.setFechaNacimiento(mascota.getFechaNacimiento());
            existingMascota.setCliente(cliente);
            // update other fields as necessary
            return mascotaService.save(existingMascota);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteMascota(@PathVariable Long id) {
        mascotaService.deleteById(id);
    }
}