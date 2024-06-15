package dev.cesarzavaleta.pruebas_unitarias_s11.dtos;

import dev.cesarzavaleta.pruebas_unitarias_s11.model.Cita;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistorialMedicoDTO implements Serializable {

    private Long idHistorial;
    private Long cita;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;

}
