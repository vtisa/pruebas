package dev.cesarzavaleta.pruebas_unitarias_s11.dtos;

import dev.cesarzavaleta.pruebas_unitarias_s11.model.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MascotaDTO implements Serializable {

    private Long idMascota;
    private String nombre;
    private String especie;
    private String raza;
    private String sexo;
    private LocalDate fechaNacimiento;
    private Long cliente;
}
