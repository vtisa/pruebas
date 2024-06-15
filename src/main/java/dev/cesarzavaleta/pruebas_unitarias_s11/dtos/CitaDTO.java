package dev.cesarzavaleta.pruebas_unitarias_s11.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CitaDTO {
    private Long idCita;
    private LocalDateTime fechaHora;
    private String motivoConsulta;
    private Long mascota;
}
